package com.co.timereport.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table (name = "roles")
public class Role extends Entities {
	
	@Column(name = "name", unique = true)
	@Size(max = 32)
	private String name;
	
	@Column(name = "description")
	@Size(max = 500)
	private String description;

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

}
