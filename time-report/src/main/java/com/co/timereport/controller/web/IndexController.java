package com.co.timereport.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.co.timereport.controller.AbstractController;

@Controller
public class IndexController extends AbstractController {
	
	@GetMapping(value = "/")
	public String index(Model model) {	
		model.addAttribute("indexPage", "Login");
		return "index";
	}
	
	@GetMapping(value = "/signup")
	public String indexSignUp(Model model) {
		model.addAttribute("indexPage", "SignUp");
		return "index";
	}
	
}
