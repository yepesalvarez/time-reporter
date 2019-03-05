package com.time.reporter.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(value = "/api/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@ApiOperation(value = "Creates a new user in the system, only administrator users can perform this action", httpMethod = "POST")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "Authorization", value = "Authorization token", dataType = "string", paramType = "header")
	})
	@PostMapping
	public UserDto saveUser(@Valid @RequestBody UserDto userDto) {	
		return userService.saveUser(userDto);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@ApiOperation(value = "Returns a user in the system searching by id", httpMethod = "GET")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "Authorization", value = "Authorization token", dataType = "string", paramType = "header")
	})
	@GetMapping("/{id}")
	public UserDto getUserById(@PathVariable ("id") Long id) {	
		return userService.getUserById(id);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Returns all users in the system, only administrator users can perform this action", httpMethod = "GET")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "Authorization", value = "Authorization token", dataType = "string", paramType = "header")
	})
	@GetMapping
	public List<UserDto> getAllUsers() {	
		return userService.getAllUsers();
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Removes a user in the system searching by id, only administrator users can perform this action", httpMethod = "DELETE")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "Authorization", value = "Authorization token", dataType = "string", paramType = "header")
	})
	@DeleteMapping("/{id}")
	public String removeUserById(@PathVariable("id") Long id) {	
		return userService.removeUser(id);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Removes a user in the system, only administrator users can perform this action", httpMethod = "DELETE")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "Authorization", value = "Authorization token", dataType = "string", paramType = "header")
	})
	@DeleteMapping
	public String removeUser(@RequestBody UserDto userDto) {	
		return userService.removeUser(userDto);
	}

}
