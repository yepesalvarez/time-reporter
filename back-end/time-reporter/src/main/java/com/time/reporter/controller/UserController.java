package com.time.reporter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.time.reporter.domain.dto.UserDto;
import com.time.reporter.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Users Management API", tags = "users")
@RequestMapping(value = "/api")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Creates a new user in the system, only administrator users can perform this action", httpMethod = "POST")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "Authorization", value = "Authorization token", dataType = "string", paramType = "header")
	})
	@PostMapping(value = "/users")
	public UserDto saveUser(@RequestBody UserDto userDto) {	
		return userService.saveUser(userDto);
	}

}
