package com.time.reporter.service;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.time.reporter.domain.User;
import com.time.reporter.domain.exceptions.UserDoesNotExistException;
import com.time.reporter.persistence.builder.UserBuilder;
import com.time.reporter.persistence.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
		
	@Autowired
	private UserBuilder userBuilder;
	
	private static final Logger LOGGER = Logger.getLogger(CustomUserDetailsService.class);
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userBuilder.userEntityToUser(userRepository.findByUsername(username));
		CustomUserDetails userDetails = null;
		if (user != null) {
			userDetails = new CustomUserDetails();
			userDetails.setUser(user);
		} else {
			LOGGER.error(new UserDoesNotExistException().getMessage() + ": " + username);
			throw new UserDoesNotExistException();
		}
		return userDetails;
	}

}
