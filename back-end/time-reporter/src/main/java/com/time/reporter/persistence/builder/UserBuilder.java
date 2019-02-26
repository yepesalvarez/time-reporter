package com.time.reporter.persistence.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.time.reporter.domain.User;
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
			return new User(userEntity.getUsername(), userEntity.getPassword(), roleBuilder.roleEntityToRole(userEntity.getRole()), true);
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
		return userEntity;
	
	}

}
