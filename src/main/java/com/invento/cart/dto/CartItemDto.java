package com.invento.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartItemDto{

	private String id;
	
	private String productId;
	
	private long customerId;
	
	private int quantity;
}
