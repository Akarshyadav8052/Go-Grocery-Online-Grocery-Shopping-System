package com.Akarsh.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.Akarsh.entities.Category;
import com.Akarsh.entities.Product;
import com.Akarsh.entities.SubCategory;

import jakarta.persistence.criteria.Join;

public class ProductSpecification 
{
 public static Specification<Product> hasSubCategoryName(String subCategoryName)
 { 
	 return (root,query,criteriaBuilder) ->
	 {
		 if(subCategoryName==null || subCategoryName.isBlank())
		 {
			 return null;
		 }
		 
		 //root.get("name)----> pointing towords name column of root(Product)
		 //productSubCategoryJoin.get("name") --> pointing towords name column of subCategory
		 
		 Join<Product, SubCategory> productSubCategoryJoin=root.join("subCategory");
		 return criteriaBuilder.equal(criteriaBuilder.lower(productSubCategoryJoin.get("name")), subCategoryName.toLowerCase());
	 };
	 
 }
 
 public static Specification<Product> hasCategoryName(String categoryName)
 { 
	 return (root,query,criteriaBuilder) ->
	 {
		 if(categoryName==null || categoryName.isBlank())
		 {
			 return null;
		 }
		 //root.get("name)----> pointing towords name column of root(Product)
		 //productSubCategoryCategoryJoin.get("name") --> pointing towords name column of Category
		 
		 Join<Product, SubCategory> productSubCategoryJoin=root.join("subCategory");
		 Join<SubCategory, Category>productSubCategoryCategoryJoin=productSubCategoryJoin.join("category");
		 return criteriaBuilder.equal(criteriaBuilder.lower(productSubCategoryCategoryJoin.get("name")), categoryName.toLowerCase());
	 };
	 
 }
 public static Specification<Product> shortByPrice(String sortDirection)
 { 
	 return (root,query,criteriaBuilder) ->
	 {
		 if(sortDirection==null || sortDirection.isBlank())
		 {
			 return null;
		 }
		 
		 if(sortDirection.equalsIgnoreCase("asc"))
		 {
			 query.orderBy(criteriaBuilder.asc(root.get("price")));
		 }
		 else
		 {
			 query.orderBy(criteriaBuilder.desc(root.get("price")));
		 }
		 return null;
		
	 };
 } 
 
 public static Specification<Product> hasProductName(String productName)
 { 
	 return (root,query,criteriaBuilder) ->
	 {
		 if(productName==null || productName.isBlank())
		 {
			 return null;
		 }
		 
		 return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%"+productName.toLowerCase()+"%") ;
		
	 };
 } 
}
