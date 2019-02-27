package com.time.reporter.persistence.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.time.reporter.domain.User;
import com.time.reporter.domain.dto.UserDto;
import com.time.reporter.persistence.entity.UserEntity;
import com.time.reporter.persistence.repository.UserRepository;

@Component
public class UserBuilder {
	
	@Autowired
	RoleBuilder roleBuilder;
	
	@Autowired 
	UserRepository userRepository;

	public User userEntityToUser(UserEntity userEntity) {
		if(userEntity == null) {
			return null;
		} else {
			return new User(userEntity.getUsername(), userEntity.getPassword(), roleBuilder.roleEntityToRole(userEntity.getRole()), userEntity.isEnabled());
		}
	}
	
	public UserDto userEntityToUserDto(UserEntity userEntity) {
		if(userEntity == null) {
			return null;
		} else {
			return new UserDto(userEntity.getId(), userEntity.getUsername(), null, userEntity.getRole().getName(), userEntity.isEnabled());
		}
	}
	
	public UserEntity userToUserEntity(User user) {
		UserEntity userEntity = userRepository.findByUsername(user.getUsername());
		if(userEntity !=null) {
			return userEntity;
		}
		userEntity = new UserEntity();
		userEntity.setUsername(user.getUsername());
		userEntity.setPassword(user.getPassword());
		userEntity.setRole(roleBuilder.roleToRoleEntity(user.getRole()));
		userEntity.setEnabled(user.isActive());
		return userEntity;
	}
	
	public UserEntity userDtoToUserEntity(UserDto userDto) {
		UserEntity userEntity = userRepository.findByUsername(userDto.getUsername());
		if(userEntity == null) {
			userEntity = new UserEntity();
		}
		userEntity.setUsername(userDto.getUsername());
		userEntity.setPassword(userDto.getPassword());
		userEntity.setRole(roleBuilder.roleStringToRoleEntity(userDto.getRole()));
		userEntity.setEnabled(userDto.isEnabled());			
		return userEntity;
	}
	

}
