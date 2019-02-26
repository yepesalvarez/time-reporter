package com.time.reporter.service;

//import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.time.reporter.domain.User;
import com.time.reporter.persistence.builder.UserBuilder;
import com.time.reporter.persistence.repository.UserRepository;

@Service
public class UserService {

	//private static final Logger LOGGER = Logger.getLogger(UserService.class);
	
	@Autowired
	private UserBuilder userBuilder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public User saveUser(User user) {
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			return userBuilder.userEntityToUser(
					userRepository.save(
							userBuilder.userToUserEntity(user)));
		} catch(Exception e) {
			return null;
		}
	}
}
