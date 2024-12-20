package com.ClothStore.service.impl;

import org.springframework.stereotype.Service;

import com.ClothStore.domain.BillingAddress;
import com.ClothStore.domain.UserBilling;
import com.ClothStore.service.BillingAddressService;



@Service
public class BillingAddressServiceImpl implements BillingAddressService{
												  
	public BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress) {
		
		// set the name to billing address   
		billingAddress.setBillingRecipientName(userBilling.getNameOfUserBilling());
		
		// set the street1 to billing address
 		billingAddress.setBillingStreetAddress1(userBilling.getStreetOneUserBilling());
 		
 		// set the street2 to billing address
		billingAddress.setBillingStreetAddress2(userBilling.getStreetTwoUserBilling());
		
		// set the city of billing address
		billingAddress.setBillingCityName(userBilling.getBillingCityOfUser());
		
		// set the state for  billing address 
		billingAddress.setBillingStateCode(userBilling.getBillingStateOfUser());
		
		// set the country for billing address
		billingAddress.setBillingCountryName(userBilling.getBillingCountryForUser());
		
		// set the zipcode for billing address
		billingAddress.setBillingPostalCode(userBilling.getBillingZipcodeForUser());
		
		return billingAddress;
	}
}
