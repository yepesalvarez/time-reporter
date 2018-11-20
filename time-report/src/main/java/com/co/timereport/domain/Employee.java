package com.co.timereport.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table (name = "employees")
public class Employee extends Entities {
	
	@NotNull
	@Email
	@Column(name = "username")
	private String email;
	
	@NotNull
	@Column(name = "password")
	private String password;
	
	@NotNull
	@Column(name = "name")
	@Size(max = 60)
	private String name;
	
	@Column(name = "code")
	@Size(max = 20)
	private String code;
	
	@OneToOne
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	private Role role;
	
	@OneToMany(mappedBy = "employee",
				fetch = FetchType.LAZY, 
				cascade = CascadeType.ALL, 
				orphanRemoval = true)
	private List<Report> reports;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Report> getReports() {
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}
	
}
