package com.ClothStore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ClothStore.domain.UserPayment;
import com.ClothStore.repository.UserPaymentRepository;
import com.ClothStore.service.UserPaymentService;


@Service
public class UserPaymentServiceImpl implements UserPaymentService{	
	@Autowired
	private UserPaymentRepository customerPaymentRepository;
	
	
	public UserPayment findById(Long identittty) {
		// call the repository method findById 
		return customerPaymentRepository.findById(identittty).orElse(null);
	}
	
	public void removeById(Long identittty) {
		// call the repository method deleteById 
		customerPaymentRepository.deleteById(identittty);	
	}	
}
