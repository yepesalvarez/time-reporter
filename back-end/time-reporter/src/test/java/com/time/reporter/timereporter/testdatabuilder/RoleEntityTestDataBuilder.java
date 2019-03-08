package com.time.reporter.timereporter.testdatabuilder;

import com.time.reporter.persistence.entity.RoleEntity;

public class RoleEntityTestDataBuilder {
	
	static final Long ID = 1L;
	static final String NAME = "USER";
	static final String DESCRIPTION = "USER ROLE";
	
	private String roleName;
	private String roleDescription;
	private Long roleId;
	
	public RoleEntityTestDataBuilder() {
		this.roleId = ID;
		this.roleName = NAME;
		this.roleDescription = DESCRIPTION;
	}
	
	public RoleEntityTestDataBuilder withId (Long id) {
		this.roleId = id;
		return this;
	}
	
	public RoleEntityTestDataBuilder withName(String name) {
		this.roleName = name;
		return this;
	}
	
	public RoleEntityTestDataBuilder withDescription (String description) {
		this.roleDescription = description;
		return this;
	}
	
	public RoleEntity build() {
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setId(roleId);
		roleEntity.setName(roleName);
		roleEntity.setDescription(roleDescription);
		return roleEntity;
	}

}
