package com.time.reporter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.time.reporter.config.jwt.JwtTokenProvider;
import com.time.reporter.domain.User;
import com.time.reporter.persistence.builder.RoleBuilder;
import com.time.reporter.persistence.entity.UserEntity;
import com.time.reporter.persistence.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RoleBuilder roleBuilder;

    @PostMapping("/signin")
    public ResponseEntity<Map<Object, Object>> signin(@RequestBody User user) {

        try {
            String username = user.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, user.getPassword()));
            UserEntity userEntity = userRepository.findByUsername(username);
            if (userEntity == null) {
            	throw new UsernameNotFoundException("Username " + username + "not found");
            }
            List<String> rolesUser = new ArrayList<>();
            rolesUser.add(userEntity.getRole().getName());
            String token = jwtTokenProvider.createToken(username, rolesUser);
            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return new ResponseEntity<>(model, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

}
