package com.expensemanagement.category;

import java.util.ArrayList;

import java.util.List;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;



@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public void addCategory(Category category) {
		categoryRepository.save(category);
	}

	public List<Category> getAllCategories() {
		List<Category> categories = new ArrayList<>();
		categoryRepository.findAll().forEach(categories::add);

		return categories;
	}

	public Category getCategoryByName(String name) {

		Category category;
		Optional<Category> categoryDatabase = categoryRepository.findBycategoryName(name);

		if (categoryDatabase.isPresent()) {
			category = categoryDatabase.get();
			return category;

		} else {
			return null;
		}

	}

	public void deleteCategoryByName(String name) {
		Optional<Category> categoryOptional = categoryRepository.findBycategoryName(name);
		categoryOptional.ifPresent(category -> categoryRepository.delete(category));
	}

	public boolean findCategoryByName(String name) {
		Optional<Category> categoryOptional = categoryRepository.findBycategoryName(name);
		return categoryOptional.isPresent();

	}

}
