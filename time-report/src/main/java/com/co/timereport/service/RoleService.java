package com.co.timereport.service;

import java.util.List;

import com.co.timereport.domain.Role;

public interface RoleService {

	Role createRole(Role role);
	Role updateRole(Role role);
	boolean deleteRole(Role role);
	boolean deleteRole(Long id);
	Role getRoleByName(String name);
	List<Role> getAllRoles();
	
}
