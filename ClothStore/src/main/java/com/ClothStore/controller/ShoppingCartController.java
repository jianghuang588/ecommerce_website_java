package com.ClothStore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ClothStore.domain.CartItem;
import com.ClothStore.domain.Shirt;
import com.ClothStore.domain.ShoppingCart;
import com.ClothStore.domain.User;
import com.ClothStore.service.CartItemService;
import com.ClothStore.service.ShirtService;
import com.ClothStore.service.ShoppingCartService;
import com.ClothStore.service.UserService;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
	
	@Autowired
	private UserService customerSupportService;
	
	@Autowired
	private CartItemService cartContentService;
	
	@Autowired
	private ShoppingCartService cartManagementService;
	
	@Autowired
	private ShirtService shirtService;
	
	
	@RequestMapping("/cart")
	public String shoppingCart(Model entity, Principal mainUser) {
		
		// retrive the current account username 
		User client = customerSupportService.findByUsername(mainUser.getName());
		
		// create shopping cart for current user 
		ShoppingCart cart = client.getShoppingCart();
		
		// retrive all the item user save on shopping cart
		List<CartItem> listOfItem = cartContentService.findByShoppingCart(cart);
		
		// calculate the (total) on the shopping cart 
		cartManagementService.updateShoppingCart(cart);
		
		
		entity.addAttribute("listItem", listOfItem);
		
		entity.addAttribute("shoppingCart", cart);
		
		return "shoppingCart";
	}
	
	
	@RequestMapping("/addItem")
	public String insertItem(
			@ModelAttribute("shirt") Shirt newShirt,
			@ModelAttribute("qty") String amount,
			Model entity, Principal mainUser
			) {
		
		// retrive the user 
		User client = customerSupportService.findByUsername(mainUser.getName());
		
		// retrive the id of the shirt 
		newShirt = shirtService.findOne(newShirt.getId());
		
		// quality > inStock quiality 
		if (Integer.parseInt(amount) > newShirt.getInStockNumber()) {
			entity.addAttribute("notEnoughStock", true);
			return "forward:/shirtDetail?id="+newShirt.getId();
		}
		
		// calculate the (total) when user add the item to shopping cart 
		CartItem cartItem = cartContentService.addShirtToCartItem(newShirt, client, Integer.parseInt(amount));
		entity.addAttribute("addshirtSuccess", true);
		
		
		return "forward:/shirtDetail?id="+newShirt.getId();
	}
	
	
	@RequestMapping("/updateCartItem")
	public String editShoppingCart(
			@ModelAttribute("id") Long identity,
			@ModelAttribute("qty") int amount
			) {
		// retrive the id of the shirt item 
		CartItem shoppingCartList = cartContentService.findById(identity);
		
		// change the shopping cart and update the item
		shoppingCartList.setQuality(amount);
		cartContentService.updateCartItem(shoppingCartList);
		
		return "forward:/shoppingCart/cart";
	}
	
	
	
	@RequestMapping("/removeItem")
	public String deleteItem(@RequestParam("id") Long identity) {
		
		// remove specific item from shopping cart by retrive id 
		cartContentService.removeCartItem(cartContentService.findById(identity));
		
		return "forward:/shoppingCart/cart";
	}
	
	
	
}
