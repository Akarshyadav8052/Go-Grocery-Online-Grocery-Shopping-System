package com.Akarsh.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Akarsh.custom_response.Response;
import com.Akarsh.entities.Product;
import com.Akarsh.entities.SubCategory;
import com.Akarsh.entities.Vendor;
import com.Akarsh.repositories.ProductRepository;
import com.Akarsh.repositories.SubCategoryRepository;
import com.Akarsh.repositories.VendorRepository;
import com.Akarsh.specifications.ProductSpecification;

import tools.jackson.databind.ObjectMapper;

@Service
public class ProductService 
{
	@Autowired
	ProductRepository productRepository;
	@Autowired
	SubCategoryRepository subCategoryRepository;
	@Autowired
	VendorRepository vendorRepository;
	@Autowired
	Response response ;
	
	final private String IMAGE_UPLOAD_DIR=System.getProperty("user.dir")+"/uploads/images";
	
	public ResponseEntity<?>addProduct(String productObject,MultipartFile image) throws IOException
	{
	
		//convert String product object to real product object
		ObjectMapper objectMapper=new ObjectMapper();
		Product product=objectMapper.readValue(productObject, Product.class);
		
		//set fk for subcategory
		long subCategoryId=product.getSubCategory().getId();
		SubCategory existingSubcategory=subCategoryRepository.findById(subCategoryId).get();
		product.setSubCategory(existingSubcategory);
		
		//set fk for vendor
		long vendorId=product.getVendor().getId();
		Vendor existingVendor=vendorRepository.findById(vendorId).get();
		product.setVendor(existingVendor);
		
		//setting image name
		String imageName=image.getOriginalFilename();
		product.setImageName(imageName);
		
		//writing image into uploads/images folder
		
		Path completeImagePath=Paths.get(IMAGE_UPLOAD_DIR, imageName);
		Files.write(completeImagePath, image.getBytes());
		
		Product savedProduct=productRepository.save(product);
		return response.send("Product added", savedProduct, HttpStatus.OK);
	}
	
	//getting all products for particular vendor
	public ResponseEntity<?> getProductsByVendorId(long vendorId)
	{
		List<Product> products=productRepository.findAllByVendorId(vendorId);
		if(products.size()>0)
		{
			return response.send("Following products found", products, HttpStatus.OK);
		}
		else
		{
			return response.send("There are no products, Please add some!", null, HttpStatus.NOT_FOUND);
		}
	}
	
	//update product by id
	public ResponseEntity<?>updateProduct(String productObject,MultipartFile image,long productId) throws IOException
	{
		//collecting product data based on productId
		Product existingProduct=productRepository.findById(productId).get();
		
		//convert String product object to real product object
		ObjectMapper objectMapper=new ObjectMapper();
		Product productToUpdate=objectMapper.readValue(productObject, Product.class);
		
		//attaching id of exixstingProduct to product update
		productToUpdate.setId(productId);
		
		//attaching createdAt of exixstingProduct to productUpdate
		productToUpdate.setCreatedAt(existingProduct.getCreatedAt());
		
		//set fk for subcategory
		long subCategoryId=productToUpdate.getSubCategory().getId();
		SubCategory existingSubcategory=subCategoryRepository.findById(subCategoryId).get();
		productToUpdate.setSubCategory(existingSubcategory); //setting value of subCategory in subCategory_id column
		
		//set fk for vendor
		
		productToUpdate.setVendor(existingProduct.getVendor());
		
		//setting image name
		String imageName=image.getOriginalFilename();
		productToUpdate.setImageName(imageName);
		
		//writing image into uploads/images folder
		
		Path completeImagePath=Paths.get(IMAGE_UPLOAD_DIR, imageName);
		Files.write(completeImagePath, image.getBytes());
		
		Product savedProduct=productRepository.save(productToUpdate);
		return response.send("Product added", savedProduct, HttpStatus.OK);
	}
	
	public ResponseEntity<?>getProductById(long productId){
		Product existingProduct=productRepository.findById(productId).get();
		return response.send("Following product found.", existingProduct, HttpStatus.FOUND);
	} 

	public ResponseEntity<?>deleteProductById(long productId){
		try {
			productRepository.deleteById(productId);
			return response.send("product deleted!", null, HttpStatus.OK);
		}
		catch(Exception e) {
			return response.send("product not deleted", null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	public ResponseEntity<?>getAllProducts(){
		List<Product> products=productRepository.findAll();
		return response.send("following product found", products,HttpStatus.OK);
	}
	
	public ResponseEntity<?>getAllFilteredProducts(String subCategoryName,String categoryName,String sortDirection,String productName)
	{
		Specification<Product> customFilter=Specification.where(ProductSpecification.hasSubCategoryName(subCategoryName)
				.and(ProductSpecification.hasCategoryName(categoryName))
				.and(ProductSpecification.shortByPrice(sortDirection))
				.and(ProductSpecification.hasProductName(productName)));
		List<Product> filteredProducts=productRepository.findAll(customFilter);
		return response.send("following product found", filteredProducts,HttpStatus.OK);
	}
}

