package com.Admin.service.impl;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Admin.domain.User;
import com.Admin.domain.security.UserRole;
import com.Admin.repository.RoleRepository;
import com.Admin.repository.UserRepository;
import com.Admin.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository clientRepository;

	@Autowired
	private RoleRepository userAccessRepository;

	@Override
	public User createUser(User client, Set<UserRole> accountRoles) {

		User internalUser = clientRepository.findByUsername(client.getUsername());

		if (internalUser != null) {
			LOG.info("Client {} already exists.  No action is needed.", client.getUsername());
		} else {
			for (UserRole ur : accountRoles) {
				userAccessRepository.save(ur.getRole());
			}

			client.getUserRoles().addAll(accountRoles);

			internalUser = clientRepository.save(client);
		}

		return internalUser;
	}

	@Override
	public User save(User client) {
		return clientRepository.save(client);
	}

	@Override
	public List<User> getAllUsers() {
		// call the repository method of findByEmail
		return clientRepository.findAll();
	}

	@Override
	public void lockUserById(Long identity) {
		// call the repository method of findById
		User client = clientRepository.findById(identity).orElseThrow(() -> new RuntimeException("Client not found"));
		// lock the user
		client.setEnable(false);
		clientRepository.save(client);
	}

	@Override
	public void unlockUserById(Long identity) {

		// call the repository method of findById
		User client = clientRepository.findById(identity).orElseThrow(() -> new RuntimeException("Client not found"));

		// unlock the user
		client.setEnable(true);
		clientRepository.save(client);
	}

	@Override
	public void lockUsersByIds(List<Long> userIdentity) {
		userIdentity.forEach(identity -> {
			// find all user id and lock them  
			User client = clientRepository.findById(identity)
					.orElseThrow(() -> new RuntimeException("Client not found"));
			client.setEnable(false);
			clientRepository.save(client);
		});
	}

	@Override
	public void unlockUsersByIds(List<Long> userIdentity) {
		userIdentity.forEach(identity -> {
			
			// find all user id and unlock them 
			User client = clientRepository.findById(identity)
					.orElseThrow(() -> new RuntimeException("Client not found"));
			client.setEnable(true);
			clientRepository.save(client);
		});
	}

}
