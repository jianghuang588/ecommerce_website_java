package com.ClothStore.repository;

import org.springframework.data.repository.CrudRepository;

import com.ClothStore.domain.User;



// CrudRepository is spring data jpa(interface) that provide create, read, update, and delete

public interface UserRepository extends CrudRepository<User, Long>{
	
	// jpa provide the findByUsername method to find the user name exit on the database
	User findByUsername(String username);
	
	// jpa provide the findByEmail method  to find the user email exit on the database
	User findByEmail(String email); 
	
	
}

