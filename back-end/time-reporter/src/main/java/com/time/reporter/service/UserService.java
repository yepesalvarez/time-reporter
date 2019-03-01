package com.time.reporter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.time.reporter.domain.dto.UserDto;
import com.time.reporter.domain.enums.Roles;
import com.time.reporter.domain.exceptions.UserAlreadyExistException;
import com.time.reporter.domain.exceptions.UserDoesNotExistException;
import com.time.reporter.domain.exceptions.UserInvalidDataException;
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
	
	@Autowired
	private RoleService roleService;
	
	public UserDto saveUser(UserDto userDto) {
		UserEntity userEntity = validateUserDto(userDto);
		if (userEntity != null) {
			LOGGER.error(new UserAlreadyExistException());
			throw new UserAlreadyExistException();
		}
		if(userDto.getRole() == null) {
			userDto.setRole(Roles.USER.toString());
		}else {
			if(roleService.getRoleByName(userDto.getRole())== null) {
				LOGGER.error(new UserInvalidDataException().getMessage());
				throw new UserInvalidDataException();
			}
		}
		return persistUser(userDto);
	}
	
	public UserDto getUserById(Long idUser) {
		return userBuilder.userEntityToUserDto(userRepository.findById(idUser).orElseThrow(UserDoesNotExistException::new));
	}
	
	public List<UserDto> getAllUsers() {
		return userRepository.findAll().stream().map(user -> userBuilder.userEntityToUserDto(user)).collect(Collectors.toList());
	}
	
	public void removeUser(UserDto userDto) {
		try {
			userRepository.delete(userBuilder.userDtoToUserEntity(userDto));
		} catch (UserDoesNotExistException e) {
			LOGGER.error(e);
			throw new UserDoesNotExistException();
		}
	}
	
	public void removeUser(Long idUser) {
		try {
			userRepository.deleteById(idUser);
		} catch (UserDoesNotExistException e) {
			LOGGER.error(e);
			throw new UserDoesNotExistException();
		}
	}
	
	public UserDto updateUser(UserDto userDto) {
		UserEntity userEntity = validateUserDto(userDto);
		if( userEntity != null && (userDto.getId() == null || !userDto.getId().equals(userEntity.getId()))) {
			LOGGER.error(new UserInvalidDataException().getMessage());
			throw new UserInvalidDataException();
		}
		return persistUser(userDto);
	}
	
	public UserEntity validateUserDto(UserDto userDto) {
		if(userDto.getUsername() == null || userDto.getPassword() == null
				||userDto.getUsername().isEmpty() || userDto.getPassword().isEmpty()) {
			LOGGER.error(new UserInvalidDataException().getMessage());
			throw new UserInvalidDataException();
		}
		return userRepository.findByUsername(userDto.getUsername());
	}
	
	public UserDto persistUser(UserDto userDto) {
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		return userBuilder.userEntityToUserDto(userRepository.save(
				userBuilder.userDtoToUserEntity(userDto)));
	}
}
