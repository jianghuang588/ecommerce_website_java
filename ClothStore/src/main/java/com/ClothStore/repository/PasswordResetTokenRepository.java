package com.ClothStore.repository;

import java.util.Date;
import java.util.stream.Stream;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ClothStore.domain.User;
import com.ClothStore.domain.security.PasswordResetToken;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;





public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
	
	// find the unique identify on the table 
	PasswordResetToken findByToken(String token);
	
	PasswordResetToken findByUser(User user);
	
	Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);
	
	@Modifying
	@Query("delete from PasswordResetToken t where t.expiryDate <= ?1")
	void deleteAllExpiredSince(Date now);
	
}


