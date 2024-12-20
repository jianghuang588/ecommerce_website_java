package com.ClothStore.service;

import java.util.List;

import com.ClothStore.domain.Shirt;


public interface ShirtService {
	
	// define contract to retrive all information from shirt 
	List<Shirt> findAll();
	
	// define contract to retrive specific id from shirt 
	Shirt findOne(Long id);
	
	// define contract to set up category for shirt 
	List<Shirt> findByCategory(String category);
	
	// define contract to search for shirt 
	List<Shirt> blurrySearch(String title);

}

