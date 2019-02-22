package com.time.reporter.domain;

public class Role {
	
	String name;
	String description;
	
	public Role(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}
	
	public Role() {}
	
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
