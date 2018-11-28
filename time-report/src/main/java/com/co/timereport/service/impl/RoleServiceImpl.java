package com.co.timereport.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.co.timereport.domain.Role;
import com.co.timereport.repository.RoleRepository;
import com.co.timereport.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository roleRepository;
	
	@Override
	public Role createRole(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public Role updateRole(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public boolean deleteRole(Role role) {
		try {
			roleRepository.delete(role);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	@Override
	public boolean deleteRole(Long id) {
		try {
			roleRepository.deleteById(id);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	@Override
	public List<Role> getAllRoles() {
		return (List<Role>) roleRepository.findAll();
	}

	@Override
	public Role getRoleByName(String name) {
		return roleRepository.findByName(name);
	}

}
