package com.co.timereport.repository;

import java.util.List;

import com.co.timereport.domain.Project;

public interface ProjectRepositoryCustom {
	
	List<Project> getProjectsByName(String name);
	List<Project> getProjectsByClient(String client);

}
