package com.ClothStore.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ClothStore.domain.CartItem;
import com.ClothStore.domain.Order;
import com.ClothStore.domain.Shirt;
import com.ClothStore.domain.User;
import com.ClothStore.domain.UserBilling;
import com.ClothStore.domain.UserPayment;
import com.ClothStore.domain.UserShipping;
import com.ClothStore.domain.security.PasswordResetToken;
import com.ClothStore.domain.security.Role;
import com.ClothStore.domain.security.UserRole;
import com.ClothStore.service.CartItemService;
import com.ClothStore.service.OrderService;
import com.ClothStore.service.ShirtService;
import com.ClothStore.service.UserPaymentService;
import com.ClothStore.service.UserService;
import com.ClothStore.service.UserShippingService;
import com.ClothStore.service.impl.UserSecurityService;
import com.ClothStore.utility.MailConstructor;
import com.ClothStore.utility.SecurityUtility;
import com.ClothStore.utility.USConstants;

@Controller
public class HomeController {
	
	@Autowired
	private JavaMailSender mailDispatcher;

	@Autowired
	private MailConstructor mailBuilder;

	@Autowired
	private UserService clientService;

	@Autowired
	private UserSecurityService accountSecurityService;

	@Autowired
	private ShirtService shirtService;

	@Autowired
	private UserPaymentService accountPaymentService;

	@Autowired
	private UserShippingService consumerShippingService;

	@Autowired
	private CartItemService checkoutItemService;

	@Autowired
	private OrderService purchaseService;

	//
	@RequestMapping("/")
	public String home() {
		return "home";
	}

	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "myAccount";
	}
	
	// 
	@RequestMapping("/hours")
	public String hours() {
		return "hours";
	}

	//
	@RequestMapping("/faq")
	public String faq() {
		return "faq";
	}

	@RequestMapping("/shirtOne")
	public String shirtOne() {
		return "shirtOne";
	}

	@RequestMapping("/shirtTwo")
	public String shirtTwo() {
		return "shirtTwo";
	}

	@RequestMapping("/shirtThree")
	public String shirtThree() {
		return "shirtThree";
	}

	@RequestMapping("/shirtFour")
	public String shirtFour() {
		return "shirtFour";
	}

	@RequestMapping("/shirtFive")
	public String shirtFive() {
		return "shirtFive";
	}

	@RequestMapping("/shirtSix")
	public String shirtSix() {
		return "shirtSix";
	}

	@RequestMapping("/shirtSeven")
	public String shirtSeven() {
		return "shirtSeven";
	}

	@RequestMapping("/shirtEight")
	public String shirtEight() {
		return "shirtEight";
	}

	@RequestMapping("/shirtNine")
	public String shirtNine() {
		return "shirtNine";
	}

	@RequestMapping("/shirtTen")
	public String shirtTen() {
		return "shirtTen";
	}

	@RequestMapping("/shirtEleven")
	public String shirtEleven() {
		return "shirtEleven";
	}

	@RequestMapping("/shirtTwelve")
	public String shirtTwelve() {
		return "shirtTwelve";
	}

	@RequestMapping("/shirtThirteen")
	public String shirtThirteen() {
		return "shirtThirteen";
	}

	@RequestMapping("/shirtFourteen")
	public String shirtFourTeen() {
		return "shirtFourteen";
	}

	@RequestMapping("/shirtFifteen")
	public String shirtFifteen() {
		return "shirtFifteen";
	}

	@RequestMapping("/shirtSixteen")
	public String shirtSixteen() {
		return "shirtSixteen";
	}

	@RequestMapping("/shirtSeventeen")
	public String shirtSeventeen() {
		return "shirtSeventeen";
	}

	@RequestMapping("/shirtEighteen")
	public String shirtEighteen() {
		return "shirtEighteen";
	}
	
	@RequestMapping("/shirtshelf")
	public String clothingShelf(Model entity, Principal mainUser) {
		// principle is the method provide by spring security that check if user exit
		if (mainUser != null) {
			// retrive and outprint the name of the user
			String username = mainUser.getName();
			User client = clientService.findByUsername(username);
			entity.addAttribute("user", client);
		}

		// show the availble shirt on the shirshelf
		List<Shirt> shirtList = shirtService.findAll();
		entity.addAttribute("shirtList", shirtList);
		entity.addAttribute("activeAll", true);

		return "shirtShelf";
	}
	
	@RequestMapping("/shirtDetail")
	public String outfitDetail(@PathParam("id") Long id, Model entity, Principal mainUser) {

		// outprint the name on shirtdetail page
		if (mainUser != null) {
			String userID = mainUser.getName();
			User client = clientService.findByUsername(userID);
			entity.addAttribute("user", client);
		}

		// find the correspond shirt id when add to shopping card
		// retrieve entire row that contain shirt.id, listPrice, ourPrices
		Shirt shirt = shirtService.findOne(id);

		// receive the data from mysql to find the (shirt.listPrice - shirt.ourPrice)
		entity.addAttribute("shirt", shirt);

		List<Integer> countList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		// allow user to view the quality of 10 with initial of 1
		entity.addAttribute("qtyList", countList);
		entity.addAttribute("qty", 1);

		return "shirtDetail";
	}

	@RequestMapping("/forgetPassword")
	public String resetPassword(HttpServletRequest inquiry, @ModelAttribute("email") String electronicMail,
			Model entity) {

		// show the initial forget password html page
		entity.addAttribute("classActiveForgetPassword", true);

		// use findByEmail method to search for specigic user
		User client = clientService.findByEmail(electronicMail);

		// emailNotExist alert appear
		if (client == null) {
			entity.addAttribute("emailNotExist", true);
			return "myAccount";
		}

		// genearte random key
		String securityKey = SecurityUtility.randomPassword();

		// generate the new password and override the password from mysql
		String encodedPassword = SecurityUtility.passwordEncoder().encode(securityKey);
		client.setPassword(encodedPassword);
		clientService.save(client);

		// send email
		String accessToken = UUID.randomUUID().toString();
		clientService.createPasswordResetTokenForUser(client, accessToken);
		String appEndpoint = "http://" + inquiry.getServerName() + ":" + inquiry.getServerPort()
				+ inquiry.getContextPath();
		SimpleMailMessage updatedEmail = mailBuilder.constructResetTokenEmail(appEndpoint, inquiry.getLocale(),
				accessToken, client, securityKey);

		mailDispatcher.send(updatedEmail);

		// retrive the backend data
		entity.addAttribute("forgetPasswordEmailSent", "true");

		return "myAccount";
	}

	// make sure register information send to the correct link
	@RequestMapping("/newUser")
	public String newUser(Locale region, @RequestParam("token") String credential, Model entity) {

		// find the unique identifer for the the current user
		PasswordResetToken submitToken = clientService.getPasswordResetToken(credential);

		if (submitToken == null) {
			String note = "incorrect note";
			entity.addAttribute("message", note);
			return "redirect:/badRequest";
		}

		// find user and it name
		User client = submitToken.getUser();
		String userID = client.getUsername();

		// load the user by it name
		UserDetails memberDetails = accountSecurityService.loadUserByUsername(userID);

		// pass the user data for authentication process
		Authentication verification = new UsernamePasswordAuthenticationToken(memberDetails,
				memberDetails.getPassword(), memberDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(verification);

		// if user information is not null list welcome to specific user on the top left
		// of the page
		entity.addAttribute("user", client);

		// if user information is correct than show the myProfile page information
		entity.addAttribute("classActiveEdit", true);

		return "myProfile";

	}

	// create new user and send register informations to user email
	@RequestMapping(value = "/newUser", method = RequestMethod.POST)
	public String newAccountPost(HttpServletRequest inquiry, @ModelAttribute("email") String clientEmail,
			@ModelAttribute("username") String loginName, Model entity) throws Exception {

		// output the input for user information for user to enter
		entity.addAttribute("classActiveNewAccount", true);
		entity.addAttribute("email", clientEmail);
		entity.addAttribute("username", loginName);

		// call the service layer if the user exit
		if (clientService.findByUsername(loginName) != null) {
			entity.addAttribute("usernameExists", true);
			return "myAccount";
		}

		// call the service layer if the email exit
		if (clientService.findByEmail(clientEmail) != null) {
			entity.addAttribute("emailExists", true);
			return "myAccount";
		}

		// create a new user and assign to msql database
		User account = new User();
		account.setUsername(loginName);
		account.setEmail(clientEmail);
		String securityKey = SecurityUtility.randomPassword();
		String hashedPassword = SecurityUtility.passwordEncoder().encode(securityKey);
		account.setPassword(hashedPassword);

		// hardcode user
		Role task = new Role();
		task.setRoleId(1);
		task.setName("ROLE_USER");
		Set<UserRole> clientRoles = new HashSet<>();
		clientRoles.add(new UserRole(account, task));

		// create the user by assign the account information with the corresponding
		// role(normal_user)
		clientService.createUser(account, clientRoles);

		// send email
		String authenticationToken = UUID.randomUUID().toString();
		clientService.createPasswordResetTokenForUser(account, authenticationToken);
		String serviceUrl = "http://" + inquiry.getServerName() + ":" + inquiry.getServerPort()
				+ inquiry.getContextPath();

		SimpleMailMessage electronicMail = mailBuilder.constructResetTokenEmail(serviceUrl, inquiry.getLocale(),
				authenticationToken, account, securityKey);
		mailDispatcher.send(electronicMail);

		// return to the register page
		entity.addAttribute("emailSent", "true");
		return "myAccount";
	}

	@RequestMapping("/myProfile")
	public String myInformation(Model entity, Principal mainUser) {

		// retrive user order information base on the username
		User client = clientService.findByUsername(mainUser.getName());
		entity.addAttribute("user", client);
		entity.addAttribute("userPaymentList", client.getUserPaymentList());
		entity.addAttribute("userShippingList", client.getUserShippingList());
		entity.addAttribute("orderList", client.getOrderList());
		UserShipping userDeliever = new UserShipping();
		entity.addAttribute("userShipping", userDeliever);
		entity.addAttribute("listOfCreditCards", true);
		entity.addAttribute("listOfShippingAddresses", true);
		List<String> areaUSA = USConstants.listOfUSStatesCode;
		Collections.sort(areaUSA);
		entity.addAttribute("stateList", areaUSA);
		entity.addAttribute("classActiveEdit", true);

		return "myProfile";
	}

	// retrive user list of credit card information base on the username
	@RequestMapping("/listOfCreditCards")
	public String userCredit(Model entity, Principal mainCharacter) {
		User client = clientService.findByUsername(mainCharacter.getName());
		entity.addAttribute("user", client);
		entity.addAttribute("userPaymentList", client.getUserPaymentList());
		entity.addAttribute("userShippingList", client.getUserShippingList());
		entity.addAttribute("orderList", client.getOrderList());

		entity.addAttribute("listOfCreditCards", true);
		entity.addAttribute("classActiveBilling", true);
		entity.addAttribute("listOfShippingAddresses", true);

		return "myProfile";
	}

	// display credit card information when click add credit card button
	@RequestMapping("/addNewCreditCard")
	public String userCreditCardAdd(Model entity, Principal mainUser) {
		User client = clientService.findByUsername(mainUser.getName());
		entity.addAttribute("user", client);

		entity.addAttribute("addNewCreditCard", true);
		entity.addAttribute("classActiveBilling", true);
		entity.addAttribute("listOfShippingAddresses", true);

		// object can retrive what UserBilling and UserPayment information
		UserBilling userCharge = new UserBilling();
		UserPayment userCost = new UserPayment();

		entity.addAttribute("userBilling", userCharge);
		entity.addAttribute("userPayment", userCost);

		List<String> zipUSA = USConstants.listOfUSStatesCode;
		Collections.sort(zipUSA);
		entity.addAttribute("stateList", zipUSA);
		entity.addAttribute("userPaymentList", client.getUserPaymentList());
		entity.addAttribute("userShippingList", client.getUserShippingList());
		entity.addAttribute("orderList", client.getOrderList());

		return "myProfile";
	}

	// add new credit card
	// retrive what user has type and the previous bound
	// data(usserbilling.databaseInformatiion)
	@RequestMapping(value = "/addNewCreditCard", method = RequestMethod.POST)
	public String registerNewCreditCard(@ModelAttribute("userPayment") UserPayment clientPayment,
			@ModelAttribute("userBilling") UserBilling memberBilling, Principal mainUser, Model entity) {

		// retrive the username
		User client = clientService.findByUsername(mainUser.getName());

		// update the update User Billing
		clientService.updateUserBilling(memberBilling, clientPayment, client);

		entity.addAttribute("user", client);
		entity.addAttribute("userPaymentList", client.getUserPaymentList());
		entity.addAttribute("userShippingList", client.getUserShippingList());
		entity.addAttribute("listOfCreditCards", true);
		entity.addAttribute("classActiveBilling", true);
		entity.addAttribute("listOfShippingAddresses", true);
		entity.addAttribute("orderList", client.getOrderList());
		return "myProfile";
	}

	// remove credit card
	@RequestMapping("/removeCreditCard")
	public String deleteCreditCard(@ModelAttribute("id") Long identification, Principal mainUser, Model entity) {

		// retrive the user by it name
		User client = clientService.findByUsername(mainUser.getName());

		// retrive the specific id by the user
		UserPayment customerTransaction = accountPaymentService.findById(identification);

		if (client.getId() != customerTransaction.getUser().getId()) {
			return "badRequestPage";
		} else {
			// show the remove credit card informatioin
			entity.addAttribute("user", client);
			accountPaymentService.removeById(identification);

			entity.addAttribute("listOfCreditCards", true);
			entity.addAttribute("classActiveBilling", true);
			entity.addAttribute("listOfShippingAddresses", true);

			entity.addAttribute("userPaymentList", client.getUserPaymentList());
			entity.addAttribute("userShippingList", client.getUserShippingList());

			return "myProfile";
		}
	}

	// set default for payment
	@RequestMapping(value = "/setDefaultPayment", method = RequestMethod.POST)
	public String setPrimaryPayment(@ModelAttribute("defaultUserPaymentId") Long standardPaymentId, Principal mainUser,
			Model entity) {

		// retrive user name
		User client = clientService.findByUsername(mainUser.getName());
		// set default
		clientService.setUserDefaultPayment(standardPaymentId, client);

		entity.addAttribute("user", client);
		entity.addAttribute("listOfCreditCards", true);
		entity.addAttribute("classActiveBilling", true);
		entity.addAttribute("listOfShippingAddresses", true);

		entity.addAttribute("userPaymentList", client.getUserPaymentList());
		entity.addAttribute("userShippingList", client.getUserShippingList());
		entity.addAttribute("orderList", client.getOrderList());

		return "myProfile";
	}

	// retrive user list of shipping address and display initially 
	@RequestMapping("/listOfShippingAddresses")
	public String userShipping(Model entity, Principal mainUser) {
		// retrive name of user for the account
		User client = clientService.findByUsername(mainUser.getName());
		entity.addAttribute("user", client);
		entity.addAttribute("userPaymentList", client.getUserPaymentList());
		entity.addAttribute("userShippingList", client.getUserShippingList());
		entity.addAttribute("orderList", client.getOrderList());

		entity.addAttribute("listOfCreditCards", true);
		entity.addAttribute("classActiveShipping", true);
		entity.addAttribute("listOfShippingAddresses", true);

		return "myProfile";
	}

	
	@RequestMapping("/updateUserShipping")
	public String editUserShippingInformation(@ModelAttribute("id") Long indentification, Principal mainUser,
			Model entity) {
		
		// retrive the username 
		User client = clientService.findByUsername(mainUser.getName());
		
		// retrive shipping id 
		UserShipping userDeliever = consumerShippingService.findById(indentification);

		if (client.getId() != userDeliever.getUser().getId()) {
			return "badRequestPage";
		} else {
			
			// display the updateshipping information
			entity.addAttribute("user", client);

			entity.addAttribute("userShipping", userDeliever);

			List<String> zipUSA = USConstants.listOfUSStatesCode;
			Collections.sort(zipUSA);
			entity.addAttribute("stateList", zipUSA);

			entity.addAttribute("addNewShippingAddress", true);
			entity.addAttribute("classActiveShipping", true);
			entity.addAttribute("listOfCreditCards", true);

			entity.addAttribute("userPaymentList", client.getUserPaymentList());
			entity.addAttribute("userShippingList", client.getUserShippingList());

			return "myProfile";
		}
	}


	// display shipping address information when click add button
	@RequestMapping("/addNewShippingAddress")
	public String storeNewShippingAddress(Model entity, Principal mainUser) {
		
		// retrive username 
		User client = clientService.findByUsername(mainUser.getName());
		entity.addAttribute("user", client);

		entity.addAttribute("addNewShippingAddress", true);
		entity.addAttribute("classActiveShipping", true);
		entity.addAttribute("listOfCreditCards", true);

		UserShipping clientShipping = new UserShipping();

		entity.addAttribute("userShipping", clientShipping);

		List<String> areaList = USConstants.listOfUSStatesCode;
		Collections.sort(areaList);
		entity.addAttribute("stateList", areaList);
		entity.addAttribute("userPaymentList", client.getUserPaymentList());
		entity.addAttribute("userShippingList", client.getUserShippingList());
		entity.addAttribute("orderList", client.getOrderList());

		return "myProfile";
	}
	
	// add new shipping address 
	// retrive what user has type and the previous bound
	// data(usershipping.databaseInformation)
	@RequestMapping(value = "/addNewShippingAddress", method = RequestMethod.POST)
	public String insertNewShippingAddressPost(@ModelAttribute("userShipping") UserShipping userShippingData,
			Principal mainUser, Model entity) {
		
		// retrive the username 
		User client = clientService.findByUsername(mainUser.getName());
		// update the usershipping information 
		clientService.updateUserShipping(userShippingData, client);
		
		entity.addAttribute("user", client);
		entity.addAttribute("userPaymentList", client.getUserPaymentList());
		entity.addAttribute("userShippingList", client.getUserShippingList());
		entity.addAttribute("listOfShippingAddresses", true);
		entity.addAttribute("classActiveShipping", true);
		entity.addAttribute("listOfCreditCards", true);
		entity.addAttribute("orderList", client.getOrderList());

		return "myProfile";
	}
	
	@RequestMapping("/removeUserShipping")
	public String deleteUserShipping(@ModelAttribute("id") Long identification, Principal mainUser, Model entity) {
		// retrive the ussername 
		User client = clientService.findByUsername(mainUser.getName());
		
		// retrive the id for shipping 
		UserShipping memberShipping = consumerShippingService.findById(identification);

		if (client.getId() != memberShipping.getUser().getId()) {
			return "badRequestPage";
		} else {
			entity.addAttribute("user", client);
			// remove the user by specific id
			consumerShippingService.removeById(identification);

			entity.addAttribute("listOfShippingAddresses", true);
			entity.addAttribute("classActiveShipping", true);

			entity.addAttribute("userPaymentList", client.getUserPaymentList());
			entity.addAttribute("userShippingList", client.getUserShippingList());
			entity.addAttribute("orderList", client.getOrderList());

			return "myProfile";
		}
	}
	
	// 
	@RequestMapping(value = "/setDefaultShippingAddress", method = RequestMethod.POST)
	public String setPrimaryShippingAddress(@ModelAttribute("defaultShippingAddressId") Long mainShippingId,
			Principal mainUser, Model entity) {
		
		// retrive user name 
		User client = clientService.findByUsername(mainUser.getName());
		
		// set as default for shipping 
		clientService.setUserDefaultShipping(mainShippingId, client);

		entity.addAttribute("user", client);
		entity.addAttribute("listOfCreditCards", true);
		entity.addAttribute("classActiveShipping", true);
		entity.addAttribute("listOfShippingAddresses", true);

		entity.addAttribute("userPaymentList", client.getUserPaymentList());
		entity.addAttribute("userShippingList", client.getUserShippingList());
		entity.addAttribute("orderList", client.getOrderList());

		return "myProfile";
	}
	
	

	// udpate profile information for user 
	@RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
	public String modifyUserInfo(@ModelAttribute("user") User client,
			@ModelAttribute("newPassword") String updatedPassword, Model entity) throws Exception {
		
		// retrieve by id 
		User existingUser = clientService.findById(client.getId());

		if (existingUser == null) {
			throw new Exception("User did not exist");
		}

		/* confirm the existence of the email */
		if (clientService.findByEmail(client.getEmail()) != null) {
			if (clientService.findByEmail(client.getEmail()).getId() != existingUser.getId()) {
				entity.addAttribute("emailExists", true);
				return "myProfile";
			}
		}

		/* verify the current username */
		if (clientService.findByUsername(client.getUsername()) != null) {
			if (clientService.findByUsername(client.getUsername()).getId() != existingUser.getId()) {
				entity.addAttribute("usernameExists", true);
				return "myProfile";
			}
		}

		/* Change the password */
		if (updatedPassword != null && !updatedPassword.isEmpty() && !updatedPassword.equals("")) {
			BCryptPasswordEncoder credentialHasher = SecurityUtility.passwordEncoder();
			String databaseAccessKey = existingUser.getPassword();
			if (credentialHasher.matches(client.getPassword(), databaseAccessKey)) {
				existingUser.setPassword(credentialHasher.encode(updatedPassword));

			} else {
				entity.addAttribute("incorrectPassword", true);

				return "myProfile";
			}
		}

		existingUser.setFirstName(client.getFirstName());
		existingUser.setLastName(client.getLastName());
		existingUser.setUsername(client.getUsername());
		existingUser.setEmail(client.getEmail());
		
		// save the update data 
		clientService.save(existingUser);

		entity.addAttribute("updateSuccess", true);

		entity.addAttribute("user", existingUser);
		entity.addAttribute("classActiveEdit", true);

		entity.addAttribute("listOfShippingAddresses", true);
		entity.addAttribute("listOfCreditCards", true);

		
		// stop the profile update if information is incorrect 
		UserDetails profileDetails = accountSecurityService.loadUserByUsername(existingUser.getUsername());

		Authentication accessAuthentication = new UsernamePasswordAuthenticationToken(profileDetails,
				profileDetails.getPassword(), profileDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(accessAuthentication);
		entity.addAttribute("orderList", client.getOrderList());

		return "myProfile";
	}
	
	
	// retrive the order from user profile 
	@RequestMapping("/orderDetail")
	public String purchaseDetail(@RequestParam("id") Long identification, Principal mainUser, Model entity) {
		
		// retrive username 
		User client = clientService.findByUsername(mainUser.getName());
		
		// retrive specific id of the product 
		Order transaction = purchaseService.findOne(identification);

		if (transaction.getUser().getId() != client.getId()) {
			return "badRequestPage";
			
		} else {
			
			// select * from cart_item where order_id = 12345;
			// retrive the id that represent the product
			List<CartItem> cartContents = checkoutItemService.findByOrder(transaction);
			
			// 
			// display all the order detail information page
			// the @Table(name="user_order") tell us order refer to user_order table
			entity.addAttribute("listItem", cartContents);
			entity.addAttribute("user", client);
			entity.addAttribute("order", transaction);

			entity.addAttribute("userPaymentList", client.getUserPaymentList());
			entity.addAttribute("userShippingList", client.getUserShippingList());
			entity.addAttribute("orderList", client.getOrderList());
			
			UserShipping memberShipping = new UserShipping();
			entity.addAttribute("userShipping", memberShipping);

			List<String> regionList = USConstants.listOfUSStatesCode;
			Collections.sort(regionList);
			entity.addAttribute("stateList", regionList);

			entity.addAttribute("listOfShippingAddresses", true);
			entity.addAttribute("classActiveOrders", true);
			entity.addAttribute("listOfCreditCards", true);
			entity.addAttribute("displayOrderDetail", true);

			return "myProfile";
		}
	}

}
