package com.time.reporter.domain.enums;

public enum Roles {
	ADMIN ("ADMIN Role"), 
	USER ("USER Role");
	
	private final String description;
	
	Roles (String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
}
