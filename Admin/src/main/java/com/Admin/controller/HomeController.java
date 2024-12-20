package com.Admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Admin.domain.User;
import com.Admin.service.UserService;


@Controller
public class HomeController {

	@Autowired
	private UserService Client;
	
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping("/")
	public String index() {
		return "redirect:/home";
	}

	@RequestMapping("/home")
	public String home() {
		return "home";
	}
	
	
	@GetMapping("/home")
	public String home(Model entity) {
		
		// retrive all user information
		List<User> endUser = Client.getAllUsers();
		// pass back to the frontend 
		entity.addAttribute("userList", endUser);
		return "home";
	}
	
	// @PathVariable retrive id is from javascript 
	@PutMapping("/lockUser/{id}")
	public ResponseEntity<?> lockUser(@PathVariable Long id) {
		try {
			// lock by specific id user click 
			Client.lockUserById(id);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			// store the error on (HttpStatus.INTERNAL_SERVER_ERROR) and pring the error message on web developer tool  
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something is wrong with lock user");
		}
	}

	
	@PutMapping("/unlockUser/{id}")
	public ResponseEntity<?> unlockUser(@PathVariable Long id) {
		try {
			// unlock specific id user user click 
			Client.unlockUserById(id); 
			return ResponseEntity.ok("Unlocked user process");
		} catch (Exception e) {
			
			// store the error on (HttpStatus.INTERNAL_SERVER_ERROR) and pring the error message on web developer tool  
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something wrong with unlock user");
		}
	}

	
	@PutMapping("/lockUsers")
	public ResponseEntity<?> lockUsers(@RequestBody Map<String, List<Long>> payload) {
		
		//  lock all id user has click
		List<Long> identity = payload.get("userIds");
		Client.lockUsersByIds(identity); 
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/unlockUsers")
	public ResponseEntity<?> unlockUsers(@RequestBody Map<String, List<Long>> payload) {
		
		//  unlock all id user has click
		List<Long> identity = payload.get("userIds");
		Client.unlockUsersByIds(identity);
		return ResponseEntity.ok().build();
	}



}
