package com.co.timereport.service;

import java.util.Date;
import java.util.List;

import com.co.timereport.domain.Employee;
import com.co.timereport.domain.Project;
import com.co.timereport.domain.Report;

public interface ReportService {
	
	Report createReport(Report report);
	Report updateReport(Report report);
	boolean deleteReport(Long id);
	boolean deleteReport(Report report);
	Report getReportById(Long id);
	List<Report> getReportsByDate(Date date);
	List<Report> getReportsByDate(Date startDate, Date endDate);
	List<Report> getReportsByProject(Project project);
	List<Report> getReportsByEmployee(Employee employee);

}
