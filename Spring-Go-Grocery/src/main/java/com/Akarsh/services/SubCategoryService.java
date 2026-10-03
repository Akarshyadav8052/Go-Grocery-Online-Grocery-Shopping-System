package com.Akarsh.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Akarsh.custom_response.Response;
import com.Akarsh.entities.Category;
import com.Akarsh.entities.SubCategory;
import com.Akarsh.entities.User;
import com.Akarsh.repositories.CategoryRepository;
import com.Akarsh.repositories.SubCategoryRepository;
import com.Akarsh.repositories.UserRepository;

@Service
public class SubCategoryService {
	@Autowired
	SubCategoryRepository subCategoryRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Response response;
	
	public ResponseEntity<?> addSubCategory(SubCategory subCategory)
	{
		if(subCategoryRepository.existsByName(subCategory.getName()))
		{
			return response.send("SubCategory already exists", null, HttpStatus.FOUND);
		}
		else {
			
			Category exixtingCategory=categoryRepository.findById(subCategory.getCategory().getId()).get();
			subCategory.setCategory(exixtingCategory); //In sub category table FK of category will be added in category_id column
			
			User existingUser=userRepository.findById(subCategory.getUser().getId()).get();
			subCategory.setUser(existingUser); ////In sub category table FK of user will be added in user_id column
			
			SubCategory savedSubCategory=subCategoryRepository.save(subCategory);
			return response.send("SubCategory already exists", savedSubCategory, HttpStatus.OK);
		}
	}

}
