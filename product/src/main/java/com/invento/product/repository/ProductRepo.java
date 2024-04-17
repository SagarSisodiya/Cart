package com.invento.product.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.invento.product.domain.Product;

@Repository
public interface ProductRepo extends ElasticsearchRepository<Product, String>{

	List<Product> findByCategory(String name, Pageable pageable);

}
