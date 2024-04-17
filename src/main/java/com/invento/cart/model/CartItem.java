package com.invento.cart.model;

import org.springframework.data.mongodb.core.mapping.Document;

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
@Document("cart_item")
public class CartItem extends AuditLogging {
	
	private String id;
	
	private String productId;
	
	private long customerId;
	
	private int quantity;
	
	private String status;
}
