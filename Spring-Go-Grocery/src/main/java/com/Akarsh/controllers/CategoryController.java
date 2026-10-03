package com.Akarsh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Akarsh.custom_response.CustomResponse;
import com.Akarsh.entities.Category;
import com.Akarsh.services.CategoryService;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
	@Autowired
	CategoryService categoryService;
	
	@PostMapping("/admin/categories")
	public ResponseEntity<?>addCategory(@RequestBody Category category)
	{
		return categoryService.addCategory(category);
	}
	
	
	@GetMapping("/get/categories")
	public ResponseEntity<CustomResponse> getAllCategories()
	{
		return categoryService.getAllCategories();
	}

}
