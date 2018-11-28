package com.co.timereport.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.co.timereport.domain.Project;
import com.co.timereport.repository.ProjectRepository;
import com.co.timereport.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	ProjectRepository projectRepository;
	
	@Override
	public Project createProject(Project project) {
		return projectRepository.save(project);
	}

	@Override
	public Project updateProject(Project project) {
		return projectRepository.save(project);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Project getProjectByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Project> getProjectsByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Project> getProjectsByClient(String client) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Project> getAllProjects() {
		// TODO Auto-generated method stub
		return null;
	}

}
