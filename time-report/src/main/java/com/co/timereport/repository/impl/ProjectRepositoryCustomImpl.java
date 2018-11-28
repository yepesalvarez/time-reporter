package com.co.timereport.repository.impl;

import java.util.List;

import javax.persistence.EntityManagerFactory;

//import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.co.timereport.domain.Project;
import com.co.timereport.repository.ProjectRepositoryCustom;

@Repository
public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom {

//	@Autowired
//	SessionFactory sessionFactory;
	
	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getProjectsByName(String name) {
		String hql = "SELECT p.* FROM projects p WHERE p.name like :name";
//		List<Project> result = sessionFactory.getCurrentSession().createQuery(hql)
		List<Project> result = entityManagerFactory.createEntityManager().createQuery(hql)
				.setParameter("name", name)
				.getResultList();
				return result == null ? null : result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getProjectsByClient(String client) {
		String hql = "SELECT p.* FROM projects p WHERE p.client like :client";
//		List<Project> result = sessionFactory.getCurrentSession().createQuery(hql)
		List<Project> result = entityManagerFactory.createEntityManager().createQuery(hql)
				.setParameter("client", client)
				.getResultList();
		return result == null ? null : result;
	}

}
