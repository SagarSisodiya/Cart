package com.invento.cart.amqp.dto;

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
public class OrderMQDto {
	
	private String id;

	private long customerId;
	
	private String productId;
	
	private int quantity;
}
