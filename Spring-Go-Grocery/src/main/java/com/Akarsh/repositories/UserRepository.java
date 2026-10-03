package com.Akarsh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Akarsh.entities.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> 
{

	Optional<User> findByUsername(String username); //if username present then it will give object
	boolean existsByUsername(String username);
}
