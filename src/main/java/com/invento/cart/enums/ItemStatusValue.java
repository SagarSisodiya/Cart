package com.invento.cart.enums;

public enum ItemStatusValue {

	IN_CART("In-Cart"),
	CHECKED_OUT("Checked-Out");
	
	private String value;
	
	ItemStatusValue(String value){
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
