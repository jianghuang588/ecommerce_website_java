package com.Admin.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Admin.domain.Shirt;
import com.Admin.service.ShirtService;

@Controller
@RequestMapping("/shirt")
public class ShirtController {

	@Autowired
	private ShirtService shirtSystem;

	// retrieve shirt information and pop back to the front end screen
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addBook(Model entity) {
		Shirt shirt = new Shirt();
		entity.addAttribute("shirt", shirt);
		return "addShirt";
	}
	
	
	// add image
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addBookPost(@ModelAttribute("shirt") Shirt shirt, HttpServletRequest request) {
		
		// save the shirt information user input that user just add
		shirtSystem.save(shirt);
		
		// retrieve the image 
		MultipartFile shirtImage = shirt.getShirtImage();

		try {
			
			// convert file content(png, jpg) to byte
			byte[] bit = shirtImage.getBytes();

			// retrieve the path of image local (1.png)
			String image = shirt.getId() + ".png";

			// now we know where the final image path are located
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					new FileOutputStream(new File("src/main/resources/static/image/shirt/" + image)));

			// create the image and end
			bufferedOutputStream.write(bit);

			bufferedOutputStream.close();
		} catch (Exception exception) {

			exception.printStackTrace();
		}

		return "redirect:shirtList";
	}

	@RequestMapping("/shirtInfo")
	public String shirtInfo(@RequestParam("id") Long id, Model entity) {

		// retrive the shirt information by id
		Shirt shirt = shirtSystem.findOne(id);
		entity.addAttribute("shirt", shirt);
		return "shirtInfo";
	}

	// retrieve the updated information
	@RequestMapping("/updateShirt")
	public String updateBook(@RequestParam("id") Long id, Model entity) {
		// retrive the shirt information by id
		Shirt shirt = shirtSystem.findOne(id);
		entity.addAttribute("shirt", shirt);

		return "updateShirt";
	}

	
	@RequestMapping(value = "/updateShirt", method = RequestMethod.POST)
	public String updateShirtPost(@ModelAttribute("shirt") Shirt shirt, HttpServletRequest request) {
		
		// save all shirt information user has type 
		shirtSystem.save(shirt);
		
		// retrieve the image 
		MultipartFile shirtImage = shirt.getShirtImage();

		if (!shirtImage.isEmpty()) {
			try {
				
				// convert file content(png, jpg) to byte
				byte[] bit = shirtImage.getBytes();
				
				// retrieve the path of image local (1.png)
				String image = shirt.getId() + ".png";

				// delete the image from the path 
				Files.delete(Paths.get("src/main/resources/static/image/shirt/" + image));

				// now we know where the final image path are located
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File("src/main/resources/static/image/shirt/" + image)));
				
				// updated the image and end
				stream.write(bit);
				stream.close();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		return "redirect:/shirt/shirtInfo?id=" + shirt.getId();
	}

	@RequestMapping("/shirtList")
	public String shirtList(Model entity) {
		
		// retrieve all the shirtlist information 
		List<Shirt> shirtShelf = shirtSystem.findAll();
		entity.addAttribute("shirtList", shirtShelf);
		return "shirtList";

	}
	
	
	// remove the shirtlist item by press the button
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public String remove(@ModelAttribute("id") String id, Model entity) {
		
		// exclude oneShirt- length to get the id
		String digitSegement = id.substring("oneShirt-".length());
		
		//  convert the string to long value 
		Long shirtIdentiy = Long.parseLong(digitSegement);
		
		// remove the shirt item by id 
		shirtSystem.removeOne(shirtIdentiy);
		
		// retrive all item from shirt 
		List<Shirt> shirtShelf = shirtSystem.findAll();
		
		entity.addAttribute("shirtList", shirtShelf);
		
		return "redirect:/shirt/shirtList";
	}

}
