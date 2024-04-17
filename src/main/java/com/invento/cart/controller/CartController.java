package com.invento.cart.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.invento.cart.dto.CartItemDto;
import com.invento.cart.dto.CheckoutDto;
import com.invento.cart.model.CartItem;
import com.invento.cart.service.CartService;
import com.invento.cart.util.Constants;

@RestController
@RequestMapping("/cart")
public class CartController {

	private CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@GetMapping("/getItemList")
	public ResponseEntity<List<CartItem>> getItemList(
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER_VALUE) int pageNumber,
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE_VALUE) int pageSize,
			@RequestParam(defaultValue = Constants.DEFAULT_UPDATE_DATE) String field,
      @RequestParam(defaultValue = Constants.DESC) Sort.Direction sortDirection,
      @RequestParam(defaultValue = Constants.IN_CART) String status) {

		List<CartItem> items = cartService.getItemList(pageNumber, pageSize, field, sortDirection, status);
		return (items.size() > 0) 
				? ResponseEntity.status(HttpStatus.OK).body(items)
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(items);
	}

	@GetMapping("/getItemById")
	public ResponseEntity<CartItem> getItemById(
			@RequestParam String id) {

		Optional<CartItem> itemOp = cartService.getItemById(id);
		return (itemOp.isPresent()) 
				? ResponseEntity.status(HttpStatus.OK).body(itemOp.get())
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CartItem());
	}

	@PostMapping("/addToCart")
	public ResponseEntity<CartItem> addToCart(@RequestBody CartItemDto dto) {
		
		Optional<CartItem> itemOp = cartService.addToCart(dto); 
		return itemOp.isPresent()
				? ResponseEntity.status(HttpStatus.CREATED).body(itemOp.get())
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CartItem());
	}

	@PutMapping("/updateItemCount")
	public ResponseEntity<String> updateItem(@RequestParam String id, @RequestParam(defaultValue="1") int itemCount) {

		return cartService.updateItemCountById(id, itemCount)
				? ResponseEntity.status(HttpStatus.OK).body("Item count updated.")
				: ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Failed to update item count.");
	}
	
	@PostMapping("/checkout")
	public ResponseEntity<String> checkout(@RequestBody CheckoutDto dto) {
		
		return cartService.checkout(dto)
				? ResponseEntity.status(HttpStatus.OK).body("Checked out successful.")
				: ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Failed to checkout.");
		
	}
}
