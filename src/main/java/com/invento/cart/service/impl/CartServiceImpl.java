package com.invento.cart.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.invento.cart.amqp.Publisher;
import com.invento.cart.amqp.dto.OrderMQDto;
import com.invento.cart.dto.CartItemDto;
import com.invento.cart.dto.CheckoutDto;
import com.invento.cart.enums.ItemStatusValue;
import com.invento.cart.mapper.ItemMapper;
import com.invento.cart.model.CartItem;
import com.invento.cart.repository.CartRepo;
import com.invento.cart.service.CartService;
import com.invento.cart.util.RMQUtils;

import io.micrometer.common.util.StringUtils;

@Service
public class CartServiceImpl implements CartService {

	private CartRepo cartRepo;

	private ItemMapper itemMapper;
	
	private Publisher publisher;
	
	private RMQUtils rmqUtils;
	
	@Value("${producer.cart.destination}")
	private String cartTopic;

	private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

	public CartServiceImpl(CartRepo cartRep, ItemMapper itemMapper, Publisher publisher, RMQUtils rmqUtils) {
		this.cartRepo = cartRep;
		this.itemMapper = itemMapper;
		this.publisher = publisher;
		this.rmqUtils = rmqUtils;
	}

	@Override
	public List<CartItem> getItemList(int pageNumber, int pageSize,
			String field, Sort.Direction sortDirection, String status) {
		List<CartItem> items = new ArrayList<>();
		try {
			Pageable pageable = PageRequest.of(pageNumber, pageSize, sortDirection, field);
			items = cartRepo.findAllByStatus(status, pageable);
		} catch (Exception e) {
			log.error("Could not found items.");
		}
		return items;
	}

	@Override
	public Optional<CartItem> getItemById(String id) {
		Optional<CartItem> itemOp = Optional.empty();
		try {
			itemOp = cartRepo.findById(id);
			if (itemOp.isEmpty()) {
				throw new Exception("Item not found in cart with id: " + id);
			}
		} catch (Exception e) {
			log.error("Error: {}", e.getMessage());
		}
		return itemOp;
	}

	@Override
	@Transactional
	public Optional<CartItem> addToCart(CartItemDto dto) {
		Optional<CartItem> itemOp = Optional.empty();
		try {
			if(Objects.isNull(dto)) {
				throw new Exception("CartItemDto is empty.");
			}
			CartItem item = itemMapper.dtoToItem(dto);
			item.setStatus(ItemStatusValue.IN_CART.toString());
			item = cartRepo.save(item);
			if (StringUtils.isBlank(item.getId())) {
				throw new Exception("Failed to save item.");
			}
			itemOp = Optional.of(item);
			log.info("Item created.");
		} catch (Exception e) {
			log.error("Error adding item to cart: {}", e.getMessage());
		}
		return itemOp;
	}

	@Override
	@Transactional
	public boolean updateItemCountById(String id, int count) {
		boolean updated = false;
		try {
			if(count < 0) {
				throw new Exception("Invalid item count");
			}
			Optional<CartItem> itemOp = cartRepo.findById(id);
			if (itemOp.isEmpty()) {
				throw new Exception("Item not found with id: " + id);
			}
			CartItem item = itemOp.get();
			if(count == 0) {
				cartRepo.delete(item);
			} else {
				item.setQuantity(count);
				cartRepo.save(item);
			}
			updated = true;
		} catch (Exception e) {
			log.error("Error updating item count. Error: {}", e.getMessage());
		}
		return updated;
	}

	@Override
	@Transactional
	public boolean checkout(CheckoutDto dto) {
		
		boolean checkedOut = false;
		List<OrderMQDto> mqDtos = new ArrayList<>();
		try {
			if(Objects.isNull(dto)) {
				throw new Exception("Request Dto is null.");
			}
			if(CollectionUtils.isEmpty(dto.getIds())) {
				throw new Exception("Id not found in the request.");
			}
			dto.getIds().stream().forEach(id  -> {
				Optional<CartItem> itemOp = cartRepo.findById(id);
				if(itemOp.isEmpty()) {
					log.error("Item not found with id: {}", id);
				} else {
					OrderMQDto mqDto = itemMapper.itemToOrderMQDto(itemOp.get());
					mqDtos.add(mqDto);
					int updated = cartRepo.updateItemStatus(id, ItemStatusValue.CHECKED_OUT.toString());
					log.error("Updated value: {}", updated);
				}
			});
			checkedOut = true;
			publishOrder(mqDtos);
		} catch (Exception e) {
			log.error("Checkout failed. Error: {}", e.getMessage());
		}
		return checkedOut;
	}
	
	private void publishOrder(List<OrderMQDto> orderMsgs) {
		try {
			if(Objects.isNull(orderMsgs)) {
				throw new Exception("Order Message is null.");
			}
			Message<?> message = rmqUtils.createRMQMsg(orderMsgs);
			log.info("Publishing message: {} to topic: {}", message, cartTopic);
			publisher.sendMessage(cartTopic, message);
		} catch(Exception e) {
			log.error("Failed to publish Order. Error: {}", e.getMessage());
		}
	}
}
