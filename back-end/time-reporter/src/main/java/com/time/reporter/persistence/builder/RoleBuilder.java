package com.time.reporter.persistence.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.time.reporter.domain.Role;
import com.time.reporter.domain.enums.Roles;
import com.time.reporter.persistence.entity.RoleEntity;
import com.time.reporter.persistence.repository.RoleRepository;

@Component
public class RoleBuilder {
	
	@Autowired
	RoleRepository roleRepository;
		
	public Role roleEntityToRole(RoleEntity roleEntity) {
		if (roleEntity == null) {
			return null;
		} else {
			return new Role(roleEntity.getName(), roleEntity.getDescription());
		}
	}
	
	public RoleEntity roleToRoleEntity(Role role) {
		RoleEntity roleEntity = roleRepository.findByName(role.getName());
		if (roleEntity != null) {
			return roleEntity;
		}
		roleEntity = new RoleEntity();
		roleEntity.setName(role.getName());
		roleEntity.setDescription(role.getDescription());
		return roleEntity;
	}

	public RoleEntity roleStringToRoleEntity(String role) {
		RoleEntity roleEntity = roleRepository.findByName(role);
		if (roleEntity != null) {
			return roleEntity;
		} else {
			roleEntity = roleRepository.findByName(Roles.USER.getDescription());
		}
		return roleEntity;
	}
}
