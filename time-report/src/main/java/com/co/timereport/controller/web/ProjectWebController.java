package com.co.timereport.controller.web;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.co.timereport.controller.AbstractController;
import com.co.timereport.domain.Project;
import com.co.timereport.domain.dto.ProjectOutDto;
import com.co.timereport.service.ProjectService;

@Controller
public class ProjectWebController extends AbstractController {
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	ModelMapper modelMapper;
	
	@GetMapping(value = "/projects")
	public String getAllProjects(Model model) {
		List<Project> projects = projectService.getAllProjects();
		if (projects != null && !projects.isEmpty()) {
			model.addAttribute("projects", 
					projects.stream().map(project -> 
					modelMapper.map(project, ProjectOutDto.class))
					.collect(Collectors.toList()));
		}else {
			model.addAttribute("message", getStatusNoContent());
		}
		
		return "projects/projects";
		
	}

}
