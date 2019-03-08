package com.time.reporter.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.time.reporter.domain.dto.UserDto;
import com.time.reporter.domain.enums.Roles;
import com.time.reporter.domain.exceptions.RoleDoesNotExistException;
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

	public static final String USER_REMOVED = "user successfully removed";
	
	public UserDto saveUser(UserDto userDto) {
		UserEntity userEntity = validateUserDto(userDto);
		if (userEntity != null) {
			LOGGER.error(new UserAlreadyExistException().getMessage());
			throw new UserAlreadyExistException();
		}
		if(userDto.getRole() == null) {
			userDto.setRole(Roles.USER.toString());
		}else {
			if(roleService.getRoleByName(userDto.getRole().toUpperCase()) == null) {
				LOGGER.error(new RoleDoesNotExistException().getMessage());
				throw new RoleDoesNotExistException();
			}
		}
		return persistUser(userDto);
	}
	
	public UserDto getUserById(Long idUser) {
		try {
			return userBuilder.userEntityToUserDto(userRepository.findById(idUser).orElseThrow(UserDoesNotExistException::new));
		} catch (MethodArgumentTypeMismatchException e) {
			LOGGER.error(new MethodArgumentTypeMismatchException(e, e.getClass(), e.getName(), e.getParameter(), e.getCause()).getMessage());
			throw new MethodArgumentTypeMismatchException(e, e.getClass(), e.getName(), e.getParameter(), e.getCause());
		}
		
	}
	
	public List<UserDto> getAllUsers() {
		return userRepository.findAll().stream().map(user -> userBuilder.userEntityToUserDto(user)).collect(Collectors.toList());
	}
	
	public List<UserDto> getAllUsers(int page, int size, String sort, String propertyOrderBy) {
		return userRepository.findAll(PageRequest.of(page, size, Direction.fromString(sort), propertyOrderBy))
				.stream().map(user -> userBuilder.userEntityToUserDto(user)).collect(Collectors.toList());
	}
	
	public String removeUser(Long idUser) {
		try {
			userRepository.deleteById(idUser);
			return USER_REMOVED;
		} catch (UserDoesNotExistException e) {
			LOGGER.error(e);
			throw new UserDoesNotExistException();
		}
	}
	
	public UserDto updateUser(UserDto userDto) {
		UserEntity userEntity = validateUserDto(userDto);
		if (userEntity == null) {
			LOGGER.error(new UserDoesNotExistException().getMessage() + " : " + userDto.getUsername());
			throw new UserDoesNotExistException();
		} else {
			if(userDto.getId() == null || !userDto.getId().equals(userEntity.getId())
					|| userDto.getRole() == null || userDto.getRole().isEmpty() || roleService.getRoleByName(userDto.getRole()) == null) {
				LOGGER.error(new UserInvalidDataException().getMessage());
				throw new UserInvalidDataException();
			} else {
				return persistUser(userDto);
			}	
		}
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
		try {
			userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
			return userBuilder.userEntityToUserDto(userRepository.save(
					userBuilder.userDtoToUserEntity(userDto)));
		} catch (ConstraintViolationException e) {
			LOGGER.error(e.getMessage());
			throw new UserInvalidDataException();
		}	
	}
}
