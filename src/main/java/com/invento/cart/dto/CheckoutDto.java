package com.invento.cart.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutDto {

	private List<String> ids = new ArrayList<>();
}
