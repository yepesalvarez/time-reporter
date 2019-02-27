package com.time.reporter.service;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.time.reporter.domain.dto.UserDto;
import com.time.reporter.domain.enums.Roles;
import com.time.reporter.domain.exceptions.UserNotModifiableException;
import com.time.reporter.persistence.builder.UserBuilder;
import com.time.reporter.persistence.entity.UserEntity;
import com.time.reporter.persistence.repository.UserRepository;

@Service
public class UserService {

	private static final Logger LOGGER = Logger.getLogger(UserService.class);
	
	@Autowired
	private UserBuilder userBuilder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public UserDto saveUser(UserDto userDto) {
		if(userDto.getUsername() == null || userDto.getPassword() == null
				||userDto.getUsername().isEmpty() || userDto.getPassword().isEmpty()) {
			LOGGER.error(new UserNotModifiableException().getMessage());
			throw new UserNotModifiableException();
		}
		UserEntity userEntity = userRepository.findByUsername(userDto.getUsername());
		if( userEntity != null && (userDto.getId() == null || !userDto.getId().equals(userEntity.getId()))) {
			LOGGER.error(new UserNotModifiableException().getMessage());
			throw new UserNotModifiableException();
		}
		if(userDto.getRole() == null) {
			userDto.setRole(Roles.USER.toString());
		}
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		return userBuilder.userEntityToUserDto(userRepository.save(
							userBuilder.userDtoToUserEntity(userDto)));
	}
}
