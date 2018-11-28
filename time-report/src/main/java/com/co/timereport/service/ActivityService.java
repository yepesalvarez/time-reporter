package com.co.timereport.service;

import java.util.List;

import com.co.timereport.domain.Activity;
import com.co.timereport.domain.Report;

public interface ActivityService {
	
	Activity createActivity(Activity activity);
	Activity updateActivity(Activity activity);
	boolean deleteActivity(Activity activity);
	boolean deleteActiviy(Long id);
	List<Activity> getActivitiesByReport(Report report);
	
}
