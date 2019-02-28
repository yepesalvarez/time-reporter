package com.time.reporter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.time.reporter.domain.dto.UserDto;
import com.time.reporter.service.LoginService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Get user authenticated in the system", tags = "login")
@RestController
@RequestMapping
public class LoginController {
	
    @Autowired
    LoginService loginService;
    
    @ApiOperation(value = "Authenticates a user in the system returning a json web token for future requests")
    @PostMapping("/login")
    public UserDto signin(@RequestBody UserDto userDto) {
            return loginService.login(userDto);
    }

}
