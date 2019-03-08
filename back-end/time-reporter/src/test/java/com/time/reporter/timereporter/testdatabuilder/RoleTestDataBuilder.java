package com.time.reporter.timereporter.testdatabuilder;

import com.time.reporter.domain.Role;

public class RoleTestDataBuilder {

	static final String NAME = "USER";
	static final String DESCRIPTION = "USER ROLE";
	
	private String name;
	private String description;
	
	public RoleTestDataBuilder () {
		this.name = NAME;
		this.description = DESCRIPTION;
	}
	
	public RoleTestDataBuilder withName(String name) {
		this.name = name;
		return this;
	}
	
	public RoleTestDataBuilder withDescription (String description) {
		this.description = description;
		return this;
	}
	
	public Role build() {
		return new Role(name, description);
	}
	
}
