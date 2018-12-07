package com.co.timereport.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.co.timereport.domain.Project;
import com.co.timereport.repository.ProjectRepository;
import com.co.timereport.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	ProjectRepository projectRepository;
	
	private static final Logger lOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);
	
	@Override
	public Project saveProject(Project project) {
		try {
			return projectRepository.save(project);
		}catch(Exception e) {	
			lOGGER.debug(e.getMessage());
			return null;
		}
		
	}

	@Override
	public boolean deleteProject(Project project) {
		try {
			projectRepository.delete(project);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	@Override
	public boolean deleteProject(Long id) {
		try {
			projectRepository.deleteById(id);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	@Override
	public Project getProjectById(Long id) {
		return projectRepository.findById(id).orElse(null);
	}

	@Override
	public Project getProjectByCode(String code) {
		return projectRepository.findByCode(code);
	}

	@Override
	public List<Project> getProjectsByName(String name) {
		return projectRepository.getProjectsByName(name);
	}

	@Override
	public List<Project> getProjectsByClient(String client) {
		return projectRepository.getProjectsByClient(client);
	}

	@Override
	public List<Project> getAllProjects() {
		return (List<Project>) projectRepository.findAll();
	}

}
