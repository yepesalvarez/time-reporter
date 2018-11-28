package com.co.timereport.repository;

import java.util.Date;
import java.util.List;

import com.co.timereport.domain.Report;

public interface ReportRepositoryCustom {
	
	List<Report> findByDate(Date startDate, Date endDate);

}
