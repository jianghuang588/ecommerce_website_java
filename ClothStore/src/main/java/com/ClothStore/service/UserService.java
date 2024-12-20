package com.ClothStore.service;

import java.util.Set;

import com.ClothStore.domain.User;
import com.ClothStore.domain.UserBilling;
import com.ClothStore.domain.UserPayment;
import com.ClothStore.domain.UserShipping;
import com.ClothStore.domain.security.PasswordResetToken;
import com.ClothStore.domain.security.UserRole;



public interface UserService {
	
	// define contract to get the user information 
	PasswordResetToken getPasswordResetToken(final String token);
	
	// define contract to generate random password for user 
	void createPasswordResetTokenForUser(final User user, final String token);
	
	// define contract to find username for user
	User findByUsername(String username);
	
	// define contract to find email for user
	User findByEmail (String email);
	
	// define contract to create the user 
	User createUser(User user, Set<UserRole> userRoles) throws Exception;
	
	// define contract to find the id for user 
	User findById(Long id);
	
	// define contract to save the user information
	User save(User user);

	// define contract to update user billing
	void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);
	
	// define contract to update user shipping 
	void updateUserShipping(UserShipping userShipping, User user);

	// define contract to set user default payment
	void setUserDefaultPayment(Long userPaymentId, User user);
	
	// define contract to set user default shipping 
	void setUserDefaultShipping(Long userShippingId, User user);
	

}
