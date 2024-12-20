package com.Admin.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.Admin.service.ShirtService;

@RestController
public class ResourceController {
	@Autowired
	private ShirtService shirtSystem;
	
	// retrive the path from javascript
	@RequestMapping(value="/shirt/removeList", method=RequestMethod.POST)
	public String removeList(
			@RequestBody ArrayList<String> shirtShelf
			){
		// iterate and remove the iterm from shirtlist 
		for (String identity : shirtShelf) {
			
			// to skip oneShirt- from the prefix 
			String shirtIdentity =identity.substring(8);
			
			// remove from specific id 
			shirtSystem.removeOne(Long.parseLong(shirtIdentity));
		}
		
		return "delete success";
	}
}

