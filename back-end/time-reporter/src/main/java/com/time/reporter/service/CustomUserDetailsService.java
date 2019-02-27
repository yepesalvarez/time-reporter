package com.time.reporter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.time.reporter.domain.User;
import com.time.reporter.persistence.builder.UserBuilder;
import com.time.reporter.persistence.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
		
	@Autowired
	private UserBuilder userBuilder;
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userBuilder.userEntityToUser(userRepository.findByUsername(username));
		CustomUserDetails userDetails = null;
		if (user != null) {
			userDetails = new CustomUserDetails();
			userDetails.setUser(user);
		} else {
			throw new UsernameNotFoundException(username + " does not exists");
		}
		return userDetails;
	}

}
