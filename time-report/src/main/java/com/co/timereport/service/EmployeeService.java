package com.co.timereport.service;

import java.util.List;

import com.co.timereport.domain.Employee;
import com.co.timereport.domain.Role;

public interface EmployeeService {
	
	Employee saveEmployee(Employee employee);
	boolean deleteEmployee(Employee employee);
	boolean deleteEmployee(Long id);
	Employee getEmployeeById(Long id);
	Employee getEmployeeByCode(String code);
	Employee getEmployeeByUsername(String username);
	List<Employee> getAllEmployees();
	List<Employee> getEmployeesByRole(Role role);

}
