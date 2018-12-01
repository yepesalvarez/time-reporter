package com.co.timereport.controller.web;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.co.timereport.controller.AbstractController;
import com.co.timereport.domain.Employee;
import com.co.timereport.domain.Report;
import com.co.timereport.domain.dto.EmployeeInDto;
import com.co.timereport.domain.enums.Roles;
import com.co.timereport.service.EmployeeService;
import com.co.timereport.service.RoleService;

@Controller
public class EmployeeWebController extends AbstractController{
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired 
	ModelMapper modelMapper;
	
	@Autowired
	RoleService roleService;
	
	@Autowired 
	PasswordEncoder passwordEncoder;
	
	@PostMapping("/registerEmployee") 
	public String registerEmployee(Model model, @ModelAttribute EmployeeInDto employeeDto) {
		if(employeeService.getEmployeeByUsername(employeeDto.getUsername()) != null) {
			model.addAttribute("message", getStatusConflict());
			return "redirect:/";
		}else {
			Employee employee = employeeService.saveEmployee(convertToEntityNoRole(employeeDto));
			if (employee == null) {
				model.addAttribute("message", getStatusBadRequest());
				return "redirect:/";
			}else {
				model.addAttribute("employee", employee);
				return "redirect:/";
			}
		}

	}
	
	private Employee convertToEntityNoRole(EmployeeInDto employeeDto) {
		List<Report> reports = null;
		try{
			reports = employeeService.getEmployeeById(employeeDto.getId()).getReports();
		}catch(Exception e) {}
		employeeDto.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
		Employee employee = modelMapper.map(employeeDto, Employee.class);
		employee.setReports(reports);
		employee.setRole(roleService.getRoleByName(Roles.USER.toString()));
		return employee;
	}
	

}
