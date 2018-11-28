package com.co.timereport.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.co.timereport.domain.Employee;
import com.co.timereport.domain.Role;
import com.co.timereport.repository.EmployeeRepository;
import com.co.timereport.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);
	
	@Override
	public Employee saveEmployee(Employee employee) {
		try {
			return employeeRepository.save(employee);
		}catch(Exception e) {
			LOGGER.debug("Entity Not created, {} -> {}", e, e.getMessage());
			return null;
		}
		
	}

	@Override
	public boolean deleteEmployee(Employee employee) {
		try {
			employeeRepository.delete(employee);
			LOGGER.debug("Entity with id {} deleted ", employee.getId());
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	@Override
	public boolean deleteEmployee(Long id) {
		try {
			employeeRepository.deleteById(id);
			LOGGER.debug("Entity with id {} deleted ", id);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	@Override
	public Employee getEmployeeById(Long id) {
		try {
			 return employeeRepository.findById(id).get();
		}catch(Exception e) {
			return null;
		}
		
	}

	@Override
	public Employee getEmployeeByCode(String code) {
			return employeeRepository.findByCode(code);
	}

	@Override
	public List<Employee> getAllEmployees() {
		return (List<Employee>) employeeRepository.findAll();
	}

	@Override
	public List<Employee> getEmployeesByRole(Role role) {
		return employeeRepository.findByRole(role);
	}

	@Override
	public Employee getEmployeeByUsername(String username) {
		return employeeRepository.findByUsername(username);
	}
	
}
