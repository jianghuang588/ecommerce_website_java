package com.ClothStore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ClothStore.domain.Shirt;
import com.ClothStore.repository.ShirtRepository;
import com.ClothStore.service.ShirtService;

@Service
public class ShirtServiceImpl implements ShirtService {
	@Autowired

	private ShirtRepository clothingRepository;

	public List<Shirt> findAll() {
		
		// call the repository method findAll()  
		List<Shirt> clothingList = (List<Shirt>) clothingRepository.findAll();
		
		List<Shirt> availableShirtList = new ArrayList<>();
		
		// only retrive the item that is active 
		for (Shirt cloth : clothingList) {
			if (cloth.isActive()) {
				availableShirtList.add(cloth);
			}
		}

		return availableShirtList;
	}
	

	public Shirt findOne(Long identity) {
		// call the repository method findById  
		return clothingRepository.findById(identity).orElse(null);
	}
	
	
	public List<Shirt> findByCategory(String kind) {
		
		// retrieve all the data with same category
		List<Shirt> shirtSelection = clothingRepository.findByCategory(kind);
	
		// create empty list 
		List<Shirt> availableShirts = new ArrayList<>();
		
		// when shirt is active add the all same category into the list
		for (Shirt cloth : shirtSelection) {
			if (cloth.isActive()) {
				availableShirts.add(cloth);
			}
		}

		return availableShirts;
	}
	
	public List<Shirt> blurrySearch(String header) {
		
		List<Shirt> shirtKind = clothingRepository.findByTitleContaining(header);
		List<Shirt> availableShirtCollection = new ArrayList<>();

		for (Shirt shirt : shirtKind) {
			if (shirt.isActive()) {
				availableShirtCollection.add(shirt);
			}
		}

		return availableShirtCollection;
	}
	

}
