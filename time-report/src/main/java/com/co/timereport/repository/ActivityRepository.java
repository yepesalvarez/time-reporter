package com.co.timereport.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.timereport.domain.Activity;
import com.co.timereport.domain.Report;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {
	
	List<Activity> findByReport(Report report);

}
