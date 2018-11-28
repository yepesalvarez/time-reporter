package com.co.timereport.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.timereport.domain.Employee;
import com.co.timereport.domain.Role;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

	Employee findByCode(String code);
	List<Employee> findByRole(Role role);
	Employee findByUsername(String username);
	
}
