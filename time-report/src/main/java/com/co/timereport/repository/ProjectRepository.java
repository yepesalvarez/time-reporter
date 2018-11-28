package com.co.timereport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.timereport.domain.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>, ProjectRepositoryCustom {

	Project findByCode(String code);

}
