package com.invento.cart.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.invento.cart.amqp.dto.OrderMQDto;
import com.invento.cart.dto.CartItemDto;
import com.invento.cart.model.CartItem;

@Component
public class ItemMapper {

	public CartItem dtoToItem(CartItemDto dto) {

		CartItem order = new CartItem();
		order.setId(dto.getId());
		order.setProductId(dto.getProductId());
		order.setCustomerId(dto.getCustomerId());
		order.setQuantity(dto.getQuantity());
		return order;
	}
	
	public OrderMQDto itemToOrderMQDto(CartItem item) {
		
		OrderMQDto dto = new OrderMQDto();
		dto.setId(UUID.randomUUID().toString());
		dto.setCustomerId(item.getCustomerId());
		dto.setProductId(item.getProductId());
		dto.setQuantity(item.getQuantity());
		return dto;
	}
}
