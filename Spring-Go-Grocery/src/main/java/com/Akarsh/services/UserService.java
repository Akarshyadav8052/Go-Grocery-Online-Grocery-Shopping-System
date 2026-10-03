package com.Akarsh.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Akarsh.custom_response.JWTResponse;
import com.Akarsh.custom_response.Response;
import com.Akarsh.dtos.UserLoginDTO;
import com.Akarsh.entities.Customer;
import com.Akarsh.entities.User;
import com.Akarsh.entities.Vendor;
import com.Akarsh.jwt.JWTTokenGenerator;
import com.Akarsh.repositories.CustomerRepository;
import com.Akarsh.repositories.UserRepository;
import com.Akarsh.repositories.VendorRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	VendorRepository vendorRepository;
	@Autowired
	Response response;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public ResponseEntity<?> registor(User user)
	{
		if(userRepository.existsByUsername(user.getUsername())) {
			return response.send("User already exists!", null, HttpStatus.FOUND) ;
		}
		//encoding password
		String encodedPassword=passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		//saving user data 
		User savedUser=userRepository.save(user); //id ,username ,encoded password,role
		 
		//adding user id into customer or vendor table according to role
		if(user.getRole().name().equals("CUSTOMER"))
		{
			Customer customer=new Customer();
			customer.setUser(savedUser);  //id from savedUser will be added into user_id column of customer table 
			Customer savedCustomer=customerRepository.save(customer);
			return response.send("User register as a customer", savedCustomer, HttpStatus.OK);
		}
		else 
		{ 
			Vendor vendor =new Vendor();
			vendor.setUser(savedUser); //id from savedVendor will be added into user_id column of vendor table 
			Vendor savedVendor=vendorRepository.save(vendor);
			return response.send("User register as a Vendor", savedVendor, HttpStatus.OK);
			
		}
	}

	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JWTTokenGenerator jwtTokenGenerator;
	@Autowired
	MyUserDetailsService myUserDetailsService;
	@Autowired
	JWTResponse jwtResponse;
	
	public ResponseEntity<?> login(UserLoginDTO userLoginDTO)
	{
		try 
		{
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							userLoginDTO.getUsername(), userLoginDTO.getPassword()));
		}
		catch (AuthenticationException e) {
			return response.send("bad credential!", null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//genrate jwt Token
		UserDetails userDetails=myUserDetailsService.loadUserByUsername(userLoginDTO.getUsername());
		User existingUser=userRepository.findByUsername(userLoginDTO.getUsername()).get();
		String jwtToken=jwtTokenGenerator.generateToken(userDetails, existingUser.getRole().name());
		
		//new code 9 sept
		String role=existingUser.getRole().name();
		if(role.equals("VENDOR")) {
			//vendorRepository.findByUserId(existingUser.getId()).get() --->yahan tak hme vendor object mila by using userid
			//vendorRepository.findByUserId(existingUser.getId()).get().getId();here vendor object se vendor id mila
			jwtResponse.setId(vendorRepository.findByUserId(existingUser.getId()).get().getId());
		}
		else if(role.equals("ADMIN"))  
		{
			jwtResponse.setId(userRepository.findById(existingUser.getId()).get().getId());
		}
		else {
			
			jwtResponse.setId(customerRepository.findByUserId(existingUser.getId()).get().getId());
		}
		 //old code that i changed
		//jwtResponse.setId(existingUser.getId());
		jwtResponse.setUsername(existingUser.getUsername());
		jwtResponse.setRole(existingUser.getRole().name());
		jwtResponse.setJwtToken(jwtToken);
		
		return response.send("login success", jwtResponse, HttpStatus.OK);
		 
	}
}
