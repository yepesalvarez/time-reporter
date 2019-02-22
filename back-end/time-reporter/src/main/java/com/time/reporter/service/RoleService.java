package com.time.reporter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.time.reporter.domain.Role;
import com.time.reporter.persistence.builder.RoleBuilder;
import com.time.reporter.persistence.repository.RoleRepository;

@Service
public class RoleService {
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	RoleBuilder roleBuilder;
	
	public Role getRoleByName(String roleName) {
		return roleBuilder.roleEntityToRole(roleRepository.findByName(roleName));
	}

}
