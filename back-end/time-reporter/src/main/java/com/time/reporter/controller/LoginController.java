package com.time.reporter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.time.reporter.domain.User;
import com.time.reporter.domain.dto.UserDto;
import com.time.reporter.service.LoginService;

@RestController
@RequestMapping
public class LoginController {
	
    @Autowired
    LoginService loginService;
    
    @PostMapping("/login")
    public UserDto signin(@RequestBody User user) {
            return loginService.login(user);
    }

}
