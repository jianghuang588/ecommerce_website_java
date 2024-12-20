package com.ClothStore;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.ClothStore.domain.User;
import com.ClothStore.domain.security.Role;
import com.ClothStore.domain.security.UserRole;
import com.ClothStore.service.UserService;
import com.ClothStore.utility.SecurityUtility;

@SpringBootApplication
public class ClothStoreApplication implements CommandLineRunner {
	
	@Autowired
	private UserService userService;
	
	public static void main(String[] args) {
		SpringApplication.run(ClothStoreApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		User Client = new User();
		Client.setFirstName("Huang");
		Client.setLastname("Jiang");
		Client.setUsername("h");
		Client.setPassword(SecurityUtility.passwordEncoder().encode("j"));
		Client.setEmail("jianghuangsecond@gmail.com");
		Set<UserRole> userEdit = new HashSet<>();
		Role permissions = new Role();
		permissions.setRoleId(1);
		permissions.setName("ROLE_USER");
		userEdit.add(new UserRole(Client, permissions));
		userService.createUser(Client, userEdit);
	}
}

