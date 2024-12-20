package com.ClothStore.service;

import com.ClothStore.domain.BillingAddress;
import com.ClothStore.domain.Order;
import com.ClothStore.domain.Payment;
import com.ClothStore.domain.ShippingAddress;
import com.ClothStore.domain.ShoppingCart;
import com.ClothStore.domain.User;

public interface OrderService {
	// define contract to createOrder
	Order createOrder(ShoppingCart shoppingCart,
			ShippingAddress shippingAddress,
			BillingAddress billingAddress,
			Payment payment,
			String shippingMethod,
			User user);
	
	// define contract to find the id for the order information
	Order findOne(Long id);
	
}
