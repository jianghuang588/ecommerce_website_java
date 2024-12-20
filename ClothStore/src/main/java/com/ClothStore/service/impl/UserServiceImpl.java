package com.ClothStore.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ClothStore.domain.ShoppingCart;
import com.ClothStore.domain.User;
import com.ClothStore.domain.UserBilling;
import com.ClothStore.domain.UserPayment;
import com.ClothStore.domain.UserShipping;
import com.ClothStore.domain.security.PasswordResetToken;
import com.ClothStore.domain.security.UserRole;
import com.ClothStore.repository.PasswordResetTokenRepository;
import com.ClothStore.repository.RoleRepository;
import com.ClothStore.repository.UserPaymentRepository;
import com.ClothStore.repository.UserRepository;
import com.ClothStore.repository.UserShippingRepository;
import com.ClothStore.service.UserService;




@Service
public class UserServiceImpl implements UserService{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private RoleRepository userRoleRepository;
	
	@Autowired
	private UserRepository userDirectory;
	
	@Autowired
	private PasswordResetTokenRepository  resetTokenRepository;
	
	@Autowired
	private UserPaymentRepository customerPaymentRepository;
	
	@Autowired
	private UserShippingRepository userShippingData;
	
	
	@Override
	public PasswordResetToken getPasswordResetToken(final String key) {
		// call the repository method find  
		return resetTokenRepository.findByToken(key);
	}
	
	@Override
	public void createPasswordResetTokenForUser(final User client, final String key) {
		// call the repository method save 
		final PasswordResetToken myKey = new PasswordResetToken(key, client);
		resetTokenRepository.save(myKey);	
	}
	
	@Override
	public User findById(Long identity){
		
		// call the repository method findById 
		return userDirectory.findById(identity).orElse(null);
	}
	
	 
	@Override
	public User findByUsername(String identity) {
		// call the repository method of findByUsername 
		return userDirectory.findByUsername(identity);
	}
	
	
	@Override
	public User findByEmail (String mail) {
		// call the repository method of findByEmail 
		return userDirectory.findByEmail(mail);
	}
	
	
	@Override
	//@Transactional  
	public User createUser(User client, Set<UserRole> accountRoles) {
		// call the repository method of findByUsername  
		User internalUser = userDirectory.findByUsername(client.getUsername());
		
		if(internalUser != null) {
			LOG.info("Client {} already exists. No action is needed.", client.getUsername());
		} else {
			for (UserRole ur : accountRoles) {
				userRoleRepository.save(ur.getRole());
			}
			
			client.getUserRoles().addAll(accountRoles);	
			

			ShoppingCart cart = new ShoppingCart();
			cart.setUser(client); 
			client.setShoppingCart(cart);
			
			client.setUserShippingList(new ArrayList<UserShipping>());
			client.setUserPaymentList(new ArrayList<UserPayment>());
			
			// CrudRepository interface provide save method can save the user 
			internalUser = userDirectory.save(client);
			
		}
		return internalUser; 
	}
	


	@Override
	public User save(User client) {
		
		// call the repository method save 
		return  userDirectory.save(client);
	}

	@Override
	public void updateUserBilling(UserBilling customerBilling, UserPayment accountPayment, User client) {
		// update the user payment by the user 
		accountPayment.setUser(client);
		// connect payment method to the billing address 
		accountPayment.setUserBilling(customerBilling);
		// make the payment method as default  
		accountPayment.setDefaultPayment(true);
		// connect billing address to specific payment method
		customerBilling.setUserPayment(accountPayment);
		// add the payment method to the user lisst
		client.getUserPaymentList().add(accountPayment);	
		// update the user payment method from database
		save(client);
	}
	
	@Override
	public void updateUserShipping(UserShipping userDelivery, User client){
		// connect the shipping method with user
		userDelivery.setUser(client);
		// set the default shipping method 
		userDelivery.setUserShippingDefault(true);
		// add the shipping method 
		client.getUserShippingList().add(userDelivery);
		// save the shipping information to that user 
		save(client);
	}
	
	
	@Override
	public void setUserDefaultPayment(Long identity, User client) {
		
		// payment repository 
		
		// find all the payment method
		List<UserPayment> accountPaymentList = (List<UserPayment>) customerPaymentRepository.findAll();
		
		// iterate through and if the current id is equal to what user choose, then set it as default, and save it 
		for (UserPayment userBilling : accountPaymentList) {
			if(userBilling.getId() == identity) {
				userBilling.setDefaultPayment(true);
				customerPaymentRepository.save(userBilling);
			} else {
				userBilling.setDefaultPayment(false);
				customerPaymentRepository.save(userBilling);
			}
		}
	}
	
	@Override
	public void setUserDefaultShipping(Long identity, User client) {
		
		// shipping repository 
		
		// find all the shipping method
		List<UserShipping> userDispatchList = (List<UserShipping>) userShippingData.findAll();
		
		// iterate through and if the current id is equal to what user choose, then set it as default, and save it 
		for (UserShipping shippingInfo : userDispatchList) {
			if(shippingInfo.getId() == identity) {
				shippingInfo.setUserShippingDefault(true);
				userShippingData.save(shippingInfo);
			} else {
				shippingInfo.setUserShippingDefault(false);
				userShippingData.save(shippingInfo);
			}
		}
	}

	
}
