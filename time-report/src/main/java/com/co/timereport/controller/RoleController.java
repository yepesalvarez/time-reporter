package com.co.timereport.controller;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.co.timereport.domain.Role;
import com.co.timereport.domain.dto.RoleDto;
import com.co.timereport.service.RoleService;

@RestController
public class RoleController extends AbstractController {

	@Autowired
	RoleService roleService;
	
	@Autowired
	ModelMapper modelMapper;
	
	@PostMapping(value = "/api/role")
	public ResponseEntity<?> createRole(@RequestBody RoleDto roleDto){
		if(roleService.getRoleByName(roleDto.getName())!=null) {
			return new ResponseEntity<>(getStatusConflict(), HttpStatus.CONFLICT);
		}else {
			try {
				return new ResponseEntity<>(roleService.createRole(
						modelMapper.map(roleDto, Role.class)), HttpStatus.CREATED);
			}catch(Exception e){
				return new ResponseEntity<>(getStatusBadRequest(), HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	@GetMapping(value = "/api/role")
	public ResponseEntity<?> getAllRoles(){
		return new ResponseEntity<>(roleService.getAllRoles().stream()
				.map(role -> modelMapper.map(role, RoleDto.class))
				.collect(Collectors.toList()), HttpStatus.OK);
	}
	
}
