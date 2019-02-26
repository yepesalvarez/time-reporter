package com.time.reporter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.time.reporter.domain.User;
import com.time.reporter.service.UserService;

@RestController
@RequestMapping(value = "/api")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping(value = "/user")
	public String userAvailablePageTest() {
		return "hi user";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/user")
	public User adminOnlyAvailablePageTest(@RequestBody User user) {	
		return userService.saveUser(user);
	}

}
