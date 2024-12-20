package com.ClothStore.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ClothStore.domain.BillingAddress;
import com.ClothStore.domain.CartItem;
import com.ClothStore.domain.Order;
import com.ClothStore.domain.Payment;
import com.ClothStore.domain.ShippingAddress;
import com.ClothStore.domain.Shirt;
import com.ClothStore.domain.ShoppingCart;
import com.ClothStore.domain.User;
import com.ClothStore.repository.OrderRepository;
import com.ClothStore.service.CartItemService;
import com.ClothStore.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository purchaseRepository;
	
	@Autowired
	private CartItemService itemInCartService;
	
	public synchronized Order createOrder(ShoppingCart cart,
			ShippingAddress deliveryAddress,
			BillingAddress invoiceAddress,
			Payment transaction,
			String shipmentOption,
			User client) {
		
		// add all the information to the order 
		Order sale = new Order();
		
		// have the billing information add
		sale.setBillingAddress(invoiceAddress);
		
		sale.setStatusOfOrder("created");
		
		// create the payment  
		sale.setPayment(transaction);
		
		// create shipping address receipt 
		sale.setShippingAddress(deliveryAddress);
		
		// create shipping address receipt 
		sale.setMethodOfShipping(shipmentOption);
		
		// retrive the shopping cart information  
		List<CartItem> cartEntries = itemInCartService.findByShoppingCart(cart);
		
		
		// iterate over the shopping cart, reduce the quality from backend beacuase admin has set the limiation of the product 
		for(CartItem basket : cartEntries) {
			Shirt shirt = basket.getShirt();
			basket.setOrder(sale);
			shirt.setInStockNumber(shirt.getInStockNumber() - basket.getQuality());
		}
		
		// mark the product on shopping cart as final list
		sale.setCartItemList(cartEntries);
		
		// set order arrive date
		sale.setDateOfOrder(Calendar.getInstance().getTime());
		
		// retrive the total of the order 
		sale.setTotalOfOrder(cart.getTotal());
		
		// deliver address link to the final product 
		deliveryAddress.setOrder(sale);
		
		// billing address link to the final product 
		invoiceAddress.setPurchaseRequest(sale);
		
		// payment is link to the final product 
		transaction.setOrder(sale);
		
		// product is link to the user 
		sale.setUser(client);
		
		// save the product on database as finish shopping list  
		sale = purchaseRepository.save(sale);
		
		return sale;
	}
	
	
	public Order findOne(Long id) {
		// call the repository method findById 
		return purchaseRepository.findById(id).orElse(null);
	}
}
