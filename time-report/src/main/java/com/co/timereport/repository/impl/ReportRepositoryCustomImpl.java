package com.co.timereport.repository.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

//import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.co.timereport.domain.Report;
import com.co.timereport.repository.ReportRepositoryCustom;

@Repository
public class ReportRepositoryCustomImpl implements ReportRepositoryCustom {

//	@Autowired
//	SessionFactory sessionFactory;
	
	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Report> findByDate(Date startDate, Date endDate) {
		String hql = "SELECT r.* FROM reports as r WHERE r.date >= :startDate and r.date <= :endDate";
//		List<Report> result = sessionFactory.getCurrentSession().createQuery(hql)
		List<Report> result = entityManagerFactory.createEntityManager().createQuery(hql)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
		return result == null ? null : result;
	}

}
