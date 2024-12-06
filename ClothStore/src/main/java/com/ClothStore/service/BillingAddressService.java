package com.ClothStore.service;

import com.ClothStore.domain.BillingAddress;
import com.ClothStore.domain.UserBilling;

public interface BillingAddressService {
	
	// define contract for setByUserBilling
	BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);

}
