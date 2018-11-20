package com.co.timereport.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "reports")
public class Report extends Entities {
	
	@NotNull
	@Column(name = "date")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date date;
	
	@NotNull
	@Column(name = "time_duration")
	private Date time;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", referencedColumnName = "id")
	private Employee employee;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", referencedColumnName = "id")
	private Project project;
	
	@OneToMany(mappedBy = "report",
				fetch = FetchType.LAZY,
				cascade = CascadeType.ALL,
				orphanRemoval = true)
	private List<Activity> activities;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	
	

}
