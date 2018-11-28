package com.co.timereport.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.co.timereport.domain.Employee;
import com.co.timereport.domain.Project;
import com.co.timereport.domain.Report;
import com.co.timereport.repository.ReportRepository;
import com.co.timereport.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	ReportRepository reportRepository;
	
	@Override
	public Report createReport(Report report) {
		return reportRepository.save(report);
	}

	@Override
	public Report updateReport(Report report) {
		return reportRepository.save(report);
	}

	@Override
	public boolean deleteReport(Long id) {
		try {
			reportRepository.deleteById(id);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	@Override
	public boolean deleteReport(Report report) {
		try {
			reportRepository.delete(report);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	@Override
	public List<Report> getReportsByDate(Date date) {
		return reportRepository.findByDate(date);
	}

	@Override
	public List<Report> getReportsByDate(Date startDate, Date endDate) {
		return reportRepository.findByDate(startDate, endDate);
	}

	@Override
	public List<Report> getReportsByProject(Project project) {
		return reportRepository.findByProject(project);
	}

	@Override
	public List<Report> getReportsByEmployee(Employee employee) {
		return reportRepository.findByEmployee(employee);
	}

	@Override
	public Report getReportById(Long id) {
		return reportRepository.findById(id).orElse(null);
	}

}
