package com.Admin;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.Admin.domain.User;
import com.Admin.domain.security.Role;
import com.Admin.domain.security.UserRole;
import com.Admin.service.UserService;
import com.Admin.utility.SecurityUtility;


@SpringBootApplication
public class AdminApplication implements CommandLineRunner {
	
	@Autowired
	private UserService userService;
	
	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		User Client = new User();
		Client.setUsername("admin");
		Client.setPassword(SecurityUtility.passwordEncoder().encode("admin"));
		Client.setEmail("admin@gmail.com");
		Set<UserRole> userEdit = new HashSet<>();
		Role permissions= new Role();
		permissions.setRoleId(0);
		permissions.setName("ROLE_ADMIN");
		userEdit.add(new UserRole(Client, permissions));
		userService.createUser(Client, userEdit);
	}	
}
