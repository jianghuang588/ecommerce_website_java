package com.Admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Admin.domain.Shirt;
import com.Admin.repository.ShirtRepository;
import com.Admin.service.ShirtService;
  
@Service
public class ShirtServiceImpl implements ShirtService{
	@Autowired
	private ShirtRepository shirtRepository;
	
	
	public Shirt save(Shirt shirt) {
		
		// call the repository method of save
		// insert into shirt (name, ....) VALUES ('blue shirt');
		// update shirt set name = ('blue shirt') WHERE (id = 6);
		return shirtRepository.save(shirt);
	
	}
	
	public List<Shirt> findAll() {
		
		// call the repository method of findAll
		return (List<Shirt> )shirtRepository.findAll();
	}
	
	public Shirt findOne(Long id) {
		
		// call the repository method of findById
		return shirtRepository.findById(id).orElse(null); 

	}

	public void removeOne(Long id) {
		
		// call the repository method of removeOne
		shirtRepository.deleteById(id);
		
	}

	
}
