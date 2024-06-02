package com.expensemanagement.category;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;


public interface CategoryRepository extends CrudRepository<Category, Integer> {
	
	public Optional<Category> findBycategoryName(String categoryName);
}
