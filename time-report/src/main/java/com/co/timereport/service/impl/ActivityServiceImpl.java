package com.co.timereport.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.co.timereport.domain.Activity;
import com.co.timereport.domain.Report;
import com.co.timereport.repository.ActivityRepository;
import com.co.timereport.service.ActivityService;

@Service
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	ActivityRepository activityRepository;
	
	@Override
	public Activity createActivity(Activity activity) {
		return activityRepository.save(activity);
	}

	@Override
	public Activity updateActivity(Activity activity) {
		return activityRepository.save(activity);
	}

	@Override
	public boolean deleteActivity(Activity activity) {
		try{
			activityRepository.delete(activity);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	@Override
	public boolean deleteActiviy(Long id) {
		try {
			activityRepository.deleteById(id);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	@Override
	public List<Activity> getActivitiesByReport(Report report) {
		return activityRepository.findByReport(report);
	}

}
