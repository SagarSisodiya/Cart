package com.invento.cart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import com.invento.cart.dto.CartItemDto;
import com.invento.cart.dto.CheckoutDto;
import com.invento.cart.model.CartItem;

public interface CartService {

	public List<CartItem> getItemList(int pageNumber, int pageSize,
			String field, Sort.Direction sortDirection, String status);

	public Optional<CartItem> getItemById(String id);

	public Optional<CartItem> addToCart(CartItemDto dto);

	public boolean updateItemCountById(String id, int count);

	public boolean checkout(CheckoutDto dto);
}
