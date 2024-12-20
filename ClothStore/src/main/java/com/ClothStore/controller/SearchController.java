package com.ClothStore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ClothStore.domain.Shirt;
import com.ClothStore.domain.User;
import com.ClothStore.service.ShirtService;
import com.ClothStore.service.UserService;


@Controller
public class SearchController {
	
	@Autowired
	private UserService customerService;
	
	@Autowired
	private ShirtService clothingService;

	
	@RequestMapping("/searchByCategory")
	public String browseByCategory(
			@RequestParam("category") String type,
			Model entity, Principal mainUser
			){
		// find the current username 
		if(mainUser!=null) {
			String username = mainUser.getName();
			User client = customerService.findByUsername(username);
			entity.addAttribute("user", client);
		}
		
		// when user select the category it become available 
		String currentCategoryClass = "active"+type;
		// remove white space 
		currentCategoryClass = currentCategoryClass.replaceAll("\\s+", "");
		// remove all & 
		currentCategoryClass = currentCategoryClass.replaceAll("&", "");
		//  make the category become avaible to view 
		entity.addAttribute(currentCategoryClass, true);
		
		// to show the category 
		List<Shirt> shirtList = clothingService.findByCategory(type);
		
		if (shirtList.isEmpty()) {
			entity.addAttribute("emptyList", true);
			return "shirtShelf";
		}
		
		// display the all shirtList  
		entity.addAttribute("shirtList", shirtList);
		
		return "shirtShelf";
	}
	
	
	@RequestMapping("/searchShirt")
	public String searchShirt(
			@ModelAttribute("keyword") String identifier,
			Principal mainUser, Model entity
			) {
		
		// display username 
		if(mainUser!=null) {
			String username = mainUser.getName();
			User userID = customerService.findByUsername(username);
			entity.addAttribute("user", userID);
		}
		
		// object contains category of each shirt 
		List<Shirt> shirtList = clothingService.blurrySearch(identifier);
		
		if (shirtList.isEmpty()) {
			entity.addAttribute("emptyList", true);
			return "shirtShelf";
		}
		
		// show the shirtlist 
		entity.addAttribute("shirtList", shirtList);
		
		return "shirtShelf";
	}
	
	
	
}
