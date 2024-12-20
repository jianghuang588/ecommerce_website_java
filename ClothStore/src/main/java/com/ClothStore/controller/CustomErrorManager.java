package com.ClothStore.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


// when user receive the crash it will return badRequestPage 
@Controller
public class CustomErrorManager implements ErrorController {

	@RequestMapping("/error")
	public String processError(HttpServletRequest query) {
		
		// the error will store RequestDispatcher.ERROR_STATUS_CODE
		Object condition = query.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (condition != null) {
			
			// convert error to object 
			Integer resultCode = Integer.valueOf(condition.toString());
			
			// if error is 404(page not find) return to the html that i design 
			if (resultCode == HttpStatus.NOT_FOUND.value()) {
				return "badRequestPage";
			}
		}

		return ""; 
	}
	
}
