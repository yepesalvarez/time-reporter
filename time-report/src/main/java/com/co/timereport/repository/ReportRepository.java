package com.co.timereport.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.timereport.domain.Employee;
import com.co.timereport.domain.Project;
import com.co.timereport.domain.Report;

@Repository
public interface ReportRepository extends CrudRepository<Report, Long>, ReportRepositoryCustom {

	List<Report> findByDate(Date date);
	List<Report> findByProject(Project project);
	List<Report> findByEmployee(Employee employee);
 	
}
