package com.ClothStore.service;

import com.ClothStore.domain.ShoppingCart;

public interface ShoppingCartService {
	
	// define contract of updateShoppingCart
	ShoppingCart updateShoppingCart(ShoppingCart shoppingCart);
	
	// define contract of ShoppingCart
	void clearShoppingCart(ShoppingCart shoppingCart);

}
