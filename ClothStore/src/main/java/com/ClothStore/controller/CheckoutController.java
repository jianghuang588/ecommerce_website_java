package com.ClothStore.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ClothStore.domain.BillingAddress;
import com.ClothStore.domain.CartItem;
import com.ClothStore.domain.Order;
import com.ClothStore.domain.Payment;
import com.ClothStore.domain.ShippingAddress;
import com.ClothStore.domain.ShoppingCart;
import com.ClothStore.domain.User;
import com.ClothStore.domain.UserBilling;
import com.ClothStore.domain.UserPayment;
import com.ClothStore.domain.UserShipping;
import com.ClothStore.service.BillingAddressService;
import com.ClothStore.service.CartItemService;
import com.ClothStore.service.OrderService;
import com.ClothStore.service.PaymentService;
import com.ClothStore.service.ShippingAddressService;
import com.ClothStore.service.ShoppingCartService;
import com.ClothStore.service.UserPaymentService;
import com.ClothStore.service.UserService;
import com.ClothStore.service.UserShippingService;
import com.ClothStore.utility.MailConstructor;
import com.ClothStore.utility.USConstants;


@Controller
public class CheckoutController {
	
	// utilize method on to ShippingAddress, BillingAddress, Payment
	private ShippingAddress deliveryAddress = new ShippingAddress();
	private BillingAddress statementAddress = new BillingAddress();
	private Payment transaction = new Payment();

	
	@Autowired
	private UserService userManagement;

	@Autowired
	private CartItemService cartOperations;

	@Autowired
	private ShippingAddressService addressManagementService;

	@Autowired
	private PaymentService transactionService;

	@Autowired
	private BillingAddressService addressVerificationService;

	@Autowired
	private UserShippingService shippingOperations;

	@Autowired
	private UserPaymentService userRemittanceService;

	@Autowired
	private JavaMailSender mailService;

	@Autowired
	private ShoppingCartService cartProcessingService;
	
	@Autowired
	private OrderService purchaseService;

	@Autowired
	private MailConstructor emailBuilder;

	
	 
	@RequestMapping("/checkout")
	public String paymentProcess(@RequestParam("id") Long cartIdentifier,
			@RequestParam(value = "missingRequiredField", required = false) boolean incompleteInput, Model Entity,
			Principal userIdentity) {
		
		// retrive specific user  
		User client = userManagement.findByUsername(userIdentity.getName());
		
		if (cartIdentifier != client.getShoppingCart().getIdentity()) {
			return "badRequestPage";
		}
		
		// find the shopping cart that link to specific user  
		List<CartItem> purchaseList = cartOperations.findByShoppingCart(client.getShoppingCart());
		
		
		// shopping cart can't be empty 
		if (purchaseList.isEmpty()) {
			Entity.addAttribute("emptyCart", true);
			return "forward:/shoppingCart/cart";
		}
		
		// check the item is still availble on stock 
		for (CartItem BasketItem : purchaseList) {
			if (BasketItem.getShirt().getInStockNumber() < BasketItem.getQuality()) {
				Entity.addAttribute("notEnoughStock", true);
				return "forward:/shoppingCart/cart";
			}
		}
		
		// retrieve the user deliver and payment method 
		List<UserShipping> userDeliveryList = client.getUserShippingList();
		List<UserPayment> userTransactionList = client.getUserPaymentList();

		Entity.addAttribute("userShippingList", userDeliveryList);
		Entity.addAttribute("userPaymentList", userTransactionList);
		
		
		// the payment and shipping are only appear when credit card and shipping address are aviable  
		if (userTransactionList.size() == 0) {
			Entity.addAttribute("emptyPaymentList", true);
		} else {
			Entity.addAttribute("emptyPaymentList", false);
		}

		if (userDeliveryList.size() == 0) {
			Entity.addAttribute("emptyShippingList", true);
		} else {
			Entity.addAttribute("emptyShippingList", false);
		}
		
		
		// contains shipping information of the user 
		for (UserShipping userShipping : userDeliveryList) {
			if (userShipping.isUserShippingDefault()) {
				addressManagementService.setByUserShipping(userShipping, deliveryAddress);
				
			}
		}
		
		// contains transcation and payment information of the user 
		for (UserPayment deliveryInformation : userTransactionList) {
			if (deliveryInformation.isDefaultPayment()) {
				
				transactionService.setByUserPayment(deliveryInformation, transaction);
				
				addressVerificationService.setByUserBilling(deliveryInformation.getUserBilling(), statementAddress);
			}
		}
		
		// pass the data  to the front end 
		Entity.addAttribute("shippingAddress", deliveryAddress);
		Entity.addAttribute("payment", transaction);
		Entity.addAttribute("billingAddress", statementAddress);
		Entity.addAttribute("listItem", purchaseList);
		Entity.addAttribute("shoppingCart", client.getShoppingCart());

		List<String> ListOfStates = USConstants.listOfUSStatesCode;
		Collections.sort(ListOfStates);
		Entity.addAttribute("stateList", ListOfStates);

		Entity.addAttribute("classActiveShipping", true);

		if (incompleteInput) {
			Entity.addAttribute("missingRequiredField", true);
		}

		return "checkout";
	}
	
	
	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String orderSubmission(@ModelAttribute("shippingAddress") ShippingAddress deliveryDestination,
			@ModelAttribute("billingAddress") BillingAddress registeredAddress, @ModelAttribute("payment") Payment transaction,
			@ModelAttribute("billingSameAsShipping") String billingMatchesDeliveryAddress,
			@ModelAttribute("shippingMethod") String shippingChoice, Principal coreUser, Model entity) {
	
		// retreive the shopping base on the user name   
		ShoppingCart orderCart = userManagement.findByUsername(coreUser.getName()).getShoppingCart();
	
		// retrive all shopping cart item 
		List<CartItem> basketItemList = cartOperations.findByShoppingCart(orderCart);
		
		// pass to front end 
		entity.addAttribute("listItem", basketItemList);

		if (billingMatchesDeliveryAddress.equals("true")) {
			// user can use differnet shipping address
			
			// assign recipient name to usershipping
			registeredAddress.setBillingRecipientName(deliveryDestination.getNameOfShippingAddress());
			
			// assign address1 to usershipping 
			registeredAddress.setBillingStreetAddress1(deliveryDestination.getStreetOneOfShippingAddress());
			
			// assign address2 to usershipping 
			registeredAddress.setBillingStreetAddress2(deliveryDestination.getStreetTwoOfShippingAddress());
			
			// assign city to usershipping 
			registeredAddress.setBillingCityName(deliveryDestination.getCityOfShippingAddress());
			
			// assign state to usershipping 
			registeredAddress.setBillingStateCode(deliveryDestination.getStateOfShippingAddress());
			
			// assign country to usershipping 
			registeredAddress.setBillingCountryName(deliveryDestination.getCountryOfShippingAddress());
			
			// assign zipCode to usershipping 
			registeredAddress.setBillingPostalCode(deliveryDestination.getZipcodeOfShippingAddress());
		}

		// if shipping address information are missing, direct back to checkout page   
		if (deliveryDestination.getStreetOneOfShippingAddress().isEmpty() || deliveryDestination.getCityOfShippingAddress().isEmpty()
				|| deliveryDestination.getStateOfShippingAddress().isEmpty()
				|| deliveryDestination.getNameOfShippingAddress().isEmpty()
				|| deliveryDestination.getZipcodeOfShippingAddress().isEmpty() || transaction.getCardNumber().isEmpty()
				|| transaction.getCvc() == 0 || registeredAddress.getBillingStreetAddress1().isEmpty()
				|| registeredAddress.getBillingCityName().isEmpty() || registeredAddress.getBillingStateCode().isEmpty()
				|| registeredAddress.getBillingRecipientName().isEmpty()
				|| registeredAddress.getBillingPostalCode().isEmpty())
			return "redirect:/checkout?id=" + orderCart.getIdentity() + "&missingRequiredField=true";

		
		// find the username of the account 
		User clientUser = userManagement.findByUsername(coreUser.getName());
		
		// create order for the user 
		Order checkoutOrder = purchaseService.createOrder(orderCart, deliveryDestination, registeredAddress, transaction,
				shippingChoice, clientUser);
		
		// create order for the user  
		mailService.send(emailBuilder.constructOrderConfirmationEmail(clientUser, checkoutOrder, Locale.ENGLISH));

		// clear the shopping cart 
		cartProcessingService.clearShoppingCart(orderCart);

		// regular shipping take 5 day and membership take 3 day
		LocalDate currentDay = LocalDate.now();
		LocalDate anticipatedDeliveryDate;

		if (shippingChoice.equals("groundShipping")) {
			anticipatedDeliveryDate = currentDay.plusDays(5);
		} else {
			anticipatedDeliveryDate = currentDay.plusDays(3);
		}

		entity.addAttribute("estimatedDeliveryDate", anticipatedDeliveryDate);

		return "orderSubmittedPage";

	}

	
	@RequestMapping("/setShippingAddress")
	public String configureShippingAddress(@RequestParam("userShippingId") Long deliveryAddressId, Principal coreUser,
			Model entity) {
		
		
		// retrieve username 
		User subscriber = userManagement.findByUsername(coreUser.getName());
		
		// retrive usershipping id 
		UserShipping deliveryDetails = shippingOperations.findById(deliveryAddressId);

		if (deliveryDetails.getUser().getId() != subscriber.getId()) {
			return "badRequestPage";
		} else {
			
			// select the shipping address 
			addressManagementService.setByUserShipping(deliveryDetails, deliveryAddress);

			// select the shopping cart by specific user 
			List<CartItem> orderItemList = cartOperations.findByShoppingCart(subscriber.getShoppingCart());
			
			// pass the data to the front end 
			entity.addAttribute("shippingAddress", deliveryAddress);
			entity.addAttribute("payment", transaction);
			entity.addAttribute("billingAddress", statementAddress);
			entity.addAttribute("listItem", orderItemList);
			entity.addAttribute("shoppingCart", subscriber.getShoppingCart());

			List<String> geographicList = USConstants.listOfUSStatesCode;
			Collections.sort(geographicList);
			entity.addAttribute("stateList", geographicList);

			List<UserShipping> userDeliveryList = subscriber.getUserShippingList();
			List<UserPayment> paymentOptionList = subscriber.getUserPaymentList();

			entity.addAttribute("userShippingList", userDeliveryList);
			entity.addAttribute("userPaymentList", paymentOptionList);

			entity.addAttribute("shippingAddress", deliveryAddress);

			entity.addAttribute("classActiveShipping", true);

			if (paymentOptionList.size() == 0) {
				entity.addAttribute("emptyPaymentList", true);
			} else {
				entity.addAttribute("emptyPaymentList", false);
			}

			entity.addAttribute("emptyShippingList", false);

			return "checkout";
		}
	}

	@RequestMapping("/setPaymentMethod")
	public String configurePaymentMethod(@RequestParam("userPaymentId") Long consumerPaymentId, Principal primaryUser,
			Model entity) {
		
		// retrive the username 
		User client = userManagement.findByUsername(primaryUser.getName());
		
		// retrive payment id for user 
		UserPayment consumerPayment = userRemittanceService.findById(consumerPaymentId);
		
		// retrive the billing information base on the payment id 
		UserBilling clientBilling = consumerPayment.getUserBilling();

		if (consumerPayment.getUser().getId() != client.getId()) {
			return "badRequestPage";
		} else {
			
			// select the payment user choose 
			transactionService.setByUserPayment(consumerPayment, transaction);

			// retrive the shopping cart base on the user
			List<CartItem> purchaseItemList = cartOperations.findByShoppingCart(client.getShoppingCart());

			// set the billing address  
			addressVerificationService.setByUserBilling(clientBilling, statementAddress);
			
			entity.addAttribute("shippingAddress", deliveryAddress);
			entity.addAttribute("payment", transaction);
			entity.addAttribute("billingAddress", statementAddress);
			entity.addAttribute("listItem", purchaseItemList);
			entity.addAttribute("shoppingCart", client.getShoppingCart());

			List<String> categoryList = USConstants.listOfUSStatesCode;
			Collections.sort(categoryList);
			entity.addAttribute("stateList", categoryList);

			List<UserShipping> subscriberShippingList = client.getUserShippingList();
			List<UserPayment> participantPaymentList = client.getUserPaymentList();

			entity.addAttribute("userShippingList", subscriberShippingList);
			entity.addAttribute("userPaymentList", participantPaymentList);

			entity.addAttribute("shippingAddress", deliveryAddress);

			entity.addAttribute("classActivePayment", true);

			entity.addAttribute("emptyPaymentList", false);

			if (subscriberShippingList.size() == 0) {
				entity.addAttribute("emptyShippingList", true);
			} else {
				entity.addAttribute("emptyShippingList", false);
			}

			return "checkout";
		}
	}
}
