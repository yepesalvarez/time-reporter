package com.co.timereport.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "activities")
public class Activity extends Entities {
	
	@NotNull
	@Column(name = "date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	
	@NotNull
	@Column(name = "time_duration")
	@DateTimeFormat(pattern = "HH:mm:ss")
	private Date time;
	
	@NotNull
	@Column(name = "name")
	@Size(max = 200)
	private String name;
	
	@Column(name = "description")
	@Size(max = 2000)
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_id", referencedColumnName = "id")
	private Report report;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}
}
