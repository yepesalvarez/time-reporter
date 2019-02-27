package com.time.reporter.service;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.time.reporter.config.jwt.JwtTokenProvider;
import com.time.reporter.domain.User;
import com.time.reporter.domain.dto.UserDto;
import com.time.reporter.persistence.builder.UserBuilder;
import com.time.reporter.persistence.entity.UserEntity;
import com.time.reporter.persistence.repository.UserRepository;

@Service
public class LoginService {
	
	@Autowired
    AuthenticationManager authenticationManager;
	
	@Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    UserBuilder userBuilder;
	
    public static final Logger LOGGER = Logger.getLogger(LoginService.class);
    
	public UserDto login(User user) {
		 String username = user.getUsername();
         authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, user.getPassword()));
         UserEntity userEntity = userRepository.findByUsername(username);
         if (userEntity == null) {
        	 LOGGER.error(new UsernameNotFoundException("Username " + username + "not found"));
         	throw new UsernameNotFoundException("Username " + username + "not found");
         }
         List<String> rolesUser = new ArrayList<>();
         rolesUser.add(userEntity.getRole().getName());
         String token = jwtTokenProvider.createToken(username, rolesUser);
         UserDto userDto = userBuilder.userEntityToUserDto(userEntity);
         userDto.setToken(token);
         return userDto;
	}

}
