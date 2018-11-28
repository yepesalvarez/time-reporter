package com.co.timereport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.timereport.domain.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByName(String name);
	
}
