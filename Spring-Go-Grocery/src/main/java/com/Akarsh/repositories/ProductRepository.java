package com.Akarsh.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.web.bind.annotation.RestController;

import com.Akarsh.entities.Product;

@RestController
public interface ProductRepository extends JpaRepository<Product, Long>,JpaSpecificationExecutor<Product>
{
	List<Product> findAllByVendorId(long vendorId); //to get product for perticuler vendor
	
	Optional<Product> findByVendorIdAndId(long vendorId,long productId); //for update/delete product

}
