package com.co.timereport.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.co.timereport.controller.AbstractController;
import com.co.timereport.domain.dto.EmployeeInDto;

@Controller
public class IndexWebController extends AbstractController {
	
	@GetMapping(value = "/")
	public String index(Model model) {	
		model.addAttribute("indexPage", "Login");
		return "index";
	}
	
	@GetMapping(value = "/signup")
	public String indexSignUp(Model model) {
		model.addAttribute("indexPage", "SignUp");
		model.addAttribute("employeeInDto", new EmployeeInDto());
		return "index";
	}
	
}
