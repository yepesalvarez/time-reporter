package com.co.timereport.service;

import java.util.List;

import com.co.timereport.domain.Project;

public interface ProjectService {
	
	Project saveProject(Project project);
	boolean deleteProject(Project project);
	boolean deleteProject(Long id);
	Project getProjectById(Long id);
	Project getProjectByCode(String code);
	List<Project> getProjectsByName(String name);
	List<Project> getProjectsByClient(String client);
	List<Project> getAllProjects();

}
