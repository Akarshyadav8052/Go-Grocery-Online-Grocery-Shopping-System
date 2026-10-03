package com.Akarsh.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Akarsh.entities.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
	
	boolean existsByPhone(String phone);
	boolean existsByemail(String email);
	
	Optional<Vendor> findByUserId(long userId);

}
