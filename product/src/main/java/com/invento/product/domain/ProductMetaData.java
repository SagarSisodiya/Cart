package com.invento.product.domain;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "productMetaData")
public class ProductMetaData {

	private String resolution;
	
	private String color;
	
	private String size;
	
	private String Memory;

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getMemory() {
		return Memory;
	}

	public void setMemory(String memory) {
		Memory = memory;
	}
}
