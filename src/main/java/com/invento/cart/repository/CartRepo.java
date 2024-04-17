package com.invento.cart.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import com.invento.cart.model.CartItem;

@Repository
public interface CartRepo extends MongoRepository<CartItem, String>{

	@Query("{'id':?0}")
	@Update("{'$set': {'status':?1}}")
	int updateItemStatus(String id, String status);

	List<CartItem> findAllByStatus(String status, Pageable pageRequest);
}
