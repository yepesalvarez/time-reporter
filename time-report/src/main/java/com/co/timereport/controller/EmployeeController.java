package com.co.timereport.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.co.timereport.domain.Employee;
import com.co.timereport.domain.Report;
import com.co.timereport.domain.Role;
import com.co.timereport.domain.dto.EmployeeInDto;
import com.co.timereport.domain.dto.EmployeeOutDto;
import com.co.timereport.service.EmployeeService;
import com.co.timereport.service.RoleService;

@RestController
@RequestMapping(value = "/api")
public class EmployeeController extends AbstractController {
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	ModelMapper modelMapper;
	//-------------------------------------------------------------------------------------------
	@PostMapping(value = "/employee")
	public ResponseEntity<?> createEmployee(@RequestBody EmployeeInDto employeeDto){			
		if(employeeService.getEmployeeByUsername(employeeDto.getUsername())!=null) {
			return new ResponseEntity<>(getStatusConflict(), HttpStatus.CONFLICT);
		}else {
			Employee employee = employeeService.saveEmployee(convertToEntity(employeeDto));
			if (employee == null)
				return new ResponseEntity<>(getStatusBadRequest(), HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(modelMapper.map(employee, EmployeeOutDto.class)
					, HttpStatus.CREATED);
		}

	}
	//-------------------------------------------------------------------------------------------
	@PutMapping(value = "/employee")
	public ResponseEntity<?> updateEmployee(@RequestBody EmployeeInDto employeeDto){
		if (employeeDto.getId() == null || employeeDto.getUsername() == null || 
				employeeDto.getName() == null || employeeDto.getPassword() == null ||
				employeeDto.getRoleName() == null) 
			return new ResponseEntity<>(getStatusBadRequest(), HttpStatus.BAD_REQUEST);
		Employee employee = employeeService.saveEmployee(convertToEntity(employeeDto));
		if( employee == null)	
			return new ResponseEntity<>(getStatusBadRequest(), HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(modelMapper.map(employee, EmployeeOutDto.class)
				, HttpStatus.OK);
	}
	//-------------------------------------------------------------------------------------------
	@DeleteMapping(value ="/employee/{id}")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable ("id") Long id){
		if (employeeService.deleteEmployee(id)) 
			return new ResponseEntity<>(getStatusOK(), HttpStatus.OK);
		return new ResponseEntity<>(getStatusNotFound(), HttpStatus.NOT_FOUND);
	}
	//-------------------------------------------------------------------------------------------
	@DeleteMapping(value = "/employee")
	public ResponseEntity<?> deleteEmployee(@RequestBody EmployeeInDto employee){
		if(employeeService.deleteEmployee(modelMapper.map(employee, Employee.class)))
			return new ResponseEntity<>(getStatusOK(), HttpStatus.OK);
		return new ResponseEntity<>(getStatusNotFound(), HttpStatus.NOT_FOUND);
	}
	//-------------------------------------------------------------------------------------------
	@GetMapping(value = "/employee")
	public ResponseEntity<?> getAllEmployees(){
		List<Employee> employees = employeeService.getAllEmployees();
		if(employees.isEmpty())
			return new ResponseEntity<>(getStatusNoContent(), HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(employees.stream()
				.map(emp -> modelMapper.map(emp, EmployeeOutDto.class))
				.collect(Collectors.toList())
					, HttpStatus.OK);
	}
	//-------------------------------------------------------------------------------------------
	@GetMapping(value = "/employee/{id}")
	public ResponseEntity<?> getEmployeeById(@PathVariable ("id") Long id){
		Employee employee = employeeService.getEmployeeById(id);
		if (employee == null)
				return new ResponseEntity<>("Non-existent coincidences", HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(modelMapper.map(employee, EmployeeOutDto.class)
				, HttpStatus.OK);
							 
	}
	//-------------------------------------------------------------------------------------------
	@PostMapping(value = "code/employee")
	public ResponseEntity<?> getEmployeeByCode(@RequestParam (value = "code") String code){
		Employee employee = employeeService.getEmployeeByCode(code);
		if(employee == null)
			return new ResponseEntity<>(getStatusNotFound(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(modelMapper.map(employee, EmployeeOutDto.class)
				, HttpStatus.OK);
	}
	//-------------------------------------------------------------------------------------------
	@PostMapping(value = "role/employee")
	public ResponseEntity<?> getEmployeesByRole(@RequestParam (value = "role") String role){
		Role roleFound = roleService.getRoleByName(role);
		if (role == null)
			return new ResponseEntity<>(getStatusNotFound(), HttpStatus.NOT_FOUND);
		List<Employee> employees = employeeService.getEmployeesByRole(roleFound);
		if(employees.isEmpty())
			return new ResponseEntity<>(getStatusNotFound(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(employees.stream().map(emp ->
				modelMapper.map(emp, EmployeeOutDto.class)).collect(Collectors.toList())
				, HttpStatus.OK);
	}
	//-------------------------------------------------------------------------------------------
	@PostMapping(value = "username/employee")
	public ResponseEntity<?> getEmployeeByUserName(@RequestParam ("username") String username){
		Employee employee = employeeService.getEmployeeByUsername(username);
		if(employee == null)
			return new ResponseEntity<>(getStatusNotFound(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(modelMapper.map(employee, EmployeeOutDto.class)
				, HttpStatus.OK);
	}	
	//-------------------------------------------------------------------------------------------
	private Employee convertToEntity(EmployeeInDto employeeDto) {
		List<Report> reports = null;
		try{
			reports = employeeService.getEmployeeById(employeeDto.getId()).getReports();
		}catch(Exception e) {}
		employeeDto.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
		Employee employee = modelMapper.map(employeeDto, Employee.class);
		employee.setReports(reports);
		employee.setRole(roleService.getRoleByName(employee.getRole().getName()));
		return employee;
		
	}

}
