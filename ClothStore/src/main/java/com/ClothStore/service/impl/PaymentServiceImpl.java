package com.ClothStore.service.impl;

import org.springframework.stereotype.Service;

import com.ClothStore.domain.Payment;
import com.ClothStore.domain.UserPayment;
import com.ClothStore.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService{

	public Payment setByUserPayment(UserPayment userTransaction, Payment transaction) {
		
		// payment is set to be visa type 
		transaction.setType(userTransaction.getType());
		
		// payment is set to card holder name 
		transaction.setHolderName(userTransaction.getHolderName());
		
		// payment is set to card number
		transaction.setCardNumber(userTransaction.getCardNumber());
		
		// payment is set to card expire month
		transaction.setExpiryMonth(userTransaction.getExpiryMonth());
		
		// payment is set to card  expire year 
		transaction.setExpiryYear(userTransaction.getExpiryYear());
		
		// payment is set to card cvc number
		transaction.setCvc(userTransaction.getCvc());
		return transaction;
	}
	
}
