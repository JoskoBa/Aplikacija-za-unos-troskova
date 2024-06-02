package com.expensemanagement.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@RequestMapping(method = RequestMethod.POST, value = "/category")
	public void addCategory(@RequestBody Category category) {
		categoryService.addCategory(category);
	}

	@GetMapping("/category/all")
	public List<Category> getAllCategories() {
		return categoryService.getAllCategories();
	}

	@GetMapping("/category/name/{name}")
	public Category getCategoryByName(@PathVariable String name) {
		return categoryService.getCategoryByName(name);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/category/delete/{name}")
	public void deleteCategoryByName(@PathVariable String name) {
		categoryService.deleteCategoryByName(name);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/category/{name}")
	public boolean findCategoryByName(@PathVariable String name) {
		return categoryService.findCategoryByName(name);
	}

}
