package com.time.reporter.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.time.reporter.domain.dto.UserDto;
import com.time.reporter.domain.enums.Roles;
import com.time.reporter.domain.exceptions.RoleDoesNotExistException;
import com.time.reporter.domain.exceptions.UserAlreadyExistException;
import com.time.reporter.domain.exceptions.UserDoesNotExistException;
import com.time.reporter.domain.exceptions.UserInvalidDataException;
import com.time.reporter.domain.exceptions.UserNotAllowedToException;
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
	public static final String USERNAME_KEY = "username";
	public static final String USERNAME_ROLE_KEY = "role";
	
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
			if(unathorizedUser(idUser)) {
				LOGGER.error(new UserNotAllowedToException().getMessage());
				throw new UserNotAllowedToException();
			}
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
	
	public UserDto updateUser(Long idUser, UserDto userDto) {
		if(unathorizedUser(idUser)) {
			LOGGER.error(new UserNotAllowedToException().getMessage());
			throw new UserNotAllowedToException();
		}
		UserEntity userEntity = validateUserDto(userDto);
		if(userDto.getId() == null || userDto.getRole() == null || userDto.getRole().isEmpty() || roleService.getRoleByName(userDto.getRole()) == null
				|| (userEntity != null &&  (!userDto.getId().equals(userEntity.getId()) || !idUser.equals(userDto.getId())))) {
			LOGGER.error(new UserInvalidDataException().getMessage());
			throw new UserInvalidDataException();
		} else {
			return persistUser(userDto);
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
	
	public boolean unathorizedUser(String usernameInDto) {
		 Map<String, String> userRol = getLoggedUser();
		return (!userRol.get(USERNAME_KEY).equalsIgnoreCase(usernameInDto) && !userRol.get(USERNAME_ROLE_KEY).equals("ROLE_" + Roles.ADMIN.toString()));
	}
	
	public boolean unathorizedUser(Long idUser) {
		 Map<String, String> userRol = getLoggedUser();
		return (!idUser.equals(userRepository.findByUsername(userRol.get(USERNAME_KEY)).getId())) && !userRol.get(USERNAME_ROLE_KEY).equals("ROLE_" + Roles.ADMIN.toString());
	}
	
	public Map<String, String> getLoggedUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = "";
		String role = "";
		Map<String, String> userRol = new HashMap<>();
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
			GrantedAuthority authority = ((UserDetails)principal).getAuthorities().stream().findFirst().orElse(null);
			role = authority.getAuthority();
		} else {
			username = principal.toString();
		}
		userRol.put(USERNAME_KEY, username);
		userRol.put(USERNAME_ROLE_KEY, role);
		return userRol;
	}
}
