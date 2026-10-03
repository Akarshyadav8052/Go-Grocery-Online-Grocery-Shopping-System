package com.Akarsh.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Akarsh.custom_response.CustomResponse;
import com.Akarsh.custom_response.Response;
import com.Akarsh.entities.Category;
import com.Akarsh.repositories.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	Response response;
	
	public ResponseEntity<?>addCategory(Category category)
	{
		if(categoryRepository.existsByName(category.getName())) 
		{
			return response.send("category already exists!", null, HttpStatus.OK);
		}
		else {
		Category savedCategory=categoryRepository.save(category);
		return response.send("category added!", savedCategory, HttpStatus.OK);
		}
	}
	
	public ResponseEntity<CustomResponse> getAllCategories()
	{
		List<Category> categories=categoryRepository.findAll();
		return response.send("Following Category found", categories, HttpStatus.FOUND);
	}

}
