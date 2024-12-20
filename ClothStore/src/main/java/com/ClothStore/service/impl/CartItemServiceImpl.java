package com.ClothStore.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ClothStore.domain.CartItem;
import com.ClothStore.domain.Order;
import com.ClothStore.domain.Shirt;
import com.ClothStore.domain.ShirtToCartItem;
import com.ClothStore.domain.ShoppingCart;
import com.ClothStore.domain.User;
import com.ClothStore.repository.CartItemRepository;
import com.ClothStore.repository.ShirtToCartItemRepository;
import com.ClothStore.service.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService {
	
	@Autowired
	private CartItemRepository shoppingCartRepository;
	
	@Autowired
	private ShirtToCartItemRepository shirtItemCartRepository;
	
	public List<CartItem> findByShoppingCart(ShoppingCart cart) {
		// call the repository method to findByShoppingCart
		// to save the item that user place in the shopping cart before
		return shoppingCartRepository.findByShoppingCart(cart);
	}
	
	
	
	
	public CartItem updateCartItem(CartItem basketItem) {
		
		// retrive the current amount with it quality 
		BigDecimal decimalValue = new BigDecimal(basketItem.getShirt().getOurPrice()).multiply(new BigDecimal(basketItem.getQuality()));		
		decimalValue = decimalValue.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		// pass that pretotal to mysql
		basketItem.setPretotal(decimalValue);
		// save that amount 
		shoppingCartRepository.save(basketItem);
		
		return basketItem;
	}
	

	
	public CartItem addShirtToCartItem(Shirt top, User client, int quantity) {
		
		// retrive the shopping cart that usr select 
		List<CartItem> cartContents = findByShoppingCart(client.getShoppingCart());
		
		// iterate all the item add the shirt, quality and calculate the total price 
		for (CartItem orderItem : cartContents) {
			if(top.getId() == orderItem.getShirt().getId()) {
				orderItem.setQuality(orderItem.getQuality()+quantity);
				orderItem.setPretotal(new BigDecimal(top.getOurPrice()).multiply(new BigDecimal(quantity)));
				shoppingCartRepository.save(orderItem);
				return orderItem;
			}
		}
		// pass the data to mysql 
		CartItem cartLineItem = new CartItem();
		cartLineItem.setShoppingCart(client.getShoppingCart());
		cartLineItem.setShirt(top);
		
		cartLineItem.setQuality(quantity);
		cartLineItem.setPretotal(new BigDecimal(top.getOurPrice()).multiply(new BigDecimal(quantity)));
		cartLineItem = shoppingCartRepository.save(cartLineItem);
		
		ShirtToCartItem shirtToCartItem = new ShirtToCartItem();
		shirtToCartItem.setShirt(top);
		shirtToCartItem.setCartItem(cartLineItem);
		shirtItemCartRepository.save(shirtToCartItem);
		
		return cartLineItem;
	}
	
	
	public CartItem findById(Long identity) {
		// call the repository method to findById
		return shoppingCartRepository.findById(identity).orElse(null);
	}

	public void removeCartItem(CartItem basketItem) {
		// call the repository method to deleteByCartItem, delete 
		// remove item on database
		// remove item from parent on the database 
		shirtItemCartRepository.deleteByCartItem(basketItem);
		shoppingCartRepository.delete(basketItem);
	}
	
	public CartItem save(CartItem basketItem) {
		// call the repository method save
		return shoppingCartRepository.save(basketItem);
	}
	
	public List<CartItem> findByOrder(Order transaction) {
		return shoppingCartRepository.findByOrder(transaction);
	}
	
}

