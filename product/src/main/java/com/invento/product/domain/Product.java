package com.invento.product.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.invento.product.enums.Brand;
import com.invento.product.enums.ProductCategory;



@Document(indexName="stock")
public class Product {

	@Id
	private String id;
	
	private ProductCategory category;
	
	private Brand brand;
	
	@Field(type = FieldType.Nested, includeInParent=true)
	private ProductMetaData productMetaData;

	public Product(ProductCategory category, Brand brand) {
		
		this.category = category;
		this.brand = brand;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProductMetaData getProductMetaData() {
		return productMetaData;
	}

	public void setProductMetaData(ProductMetaData productMetaData) {
		this.productMetaData = productMetaData;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", category=" + category + ", brand=" + brand + "]";
	}
}
