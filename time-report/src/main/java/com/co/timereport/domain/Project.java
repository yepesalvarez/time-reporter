package com.co.timereport.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table (name = "projects")
public class Project extends Entities {

	@NotNull
	@Column(name = "name")
	@Size(max = 200)
	private String name;
	
	@Column(name = "code")
	@Size(max = 200)
	private String code;
	
	@Column(name = "client")
	@Size(max = 200)
	private String client;
	
	@Column(name = "description")
	@Size(max = 500)
	private String description;
	
	@OneToMany(mappedBy = "project",
				fetch = FetchType.LAZY,
				cascade = CascadeType.ALL,
				orphanRemoval = true)
	private List<Report> reports;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public List<Report> getReports() {
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
