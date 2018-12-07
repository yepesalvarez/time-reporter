package com.co.timereport.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.co.timereport.domain.Project;
import com.co.timereport.domain.dto.ProjectInDto;
import com.co.timereport.domain.dto.ProjectOutDto;
import com.co.timereport.service.ProjectService;

@RestController
@RequestMapping(value = "/api")
public class ProjectController extends AbstractController {

	@Autowired
	ProjectService projectService;
	
	@Autowired
	ModelMapper modelMapper;
	
	@GetMapping(value = "/project")
	public ResponseEntity<?> getAllProjects(){
		List<Project> projects = projectService.getAllProjects();
		if(projects!=null && !projects.isEmpty()) {
			return new ResponseEntity<>(projects, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(getStatusNoContent(), HttpStatus.NO_CONTENT);
		}
	}
	
	@PostMapping(value = "/project")
	public ResponseEntity<?> createProject(@RequestBody ProjectInDto projectDto){
		Project project = projectService.saveProject(modelMapper.map(projectDto, Project.class));
		if(project != null) {
			return new ResponseEntity<>(modelMapper.map(project, ProjectOutDto.class), HttpStatus.OK);
		}else{
			return new ResponseEntity<>(getStatusInternalServerErr(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
	
	
}
