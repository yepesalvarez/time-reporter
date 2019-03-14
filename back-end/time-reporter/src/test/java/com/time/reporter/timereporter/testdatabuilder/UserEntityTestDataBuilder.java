package com.time.reporter.timereporter.testdatabuilder;

import com.time.reporter.persistence.entity.RoleEntity;
import com.time.reporter.persistence.entity.UserEntity;

public class UserEntityTestDataBuilder {
	
	static final Long ID = null;
	static final String USERNAME = "user.test";
	static final String PASSW = "$Qwe123*";
	static final boolean ENABLED = true;
		
	private Long userEntityId;
	private String userEntityUsername;
	private String userEntityPassw;
	private boolean userEntityEnabled;
	private RoleEntity userEntityRoleEntity;
	
	public UserEntityTestDataBuilder() {
		this.userEntityId = ID;
		this.userEntityUsername = USERNAME;
		this.userEntityPassw = PASSW;
		this.userEntityEnabled = ENABLED;
		this.userEntityRoleEntity = new RoleEntityTestDataBuilder().build();
	}
	
	public UserEntityTestDataBuilder withId(Long id) {
		this.userEntityId = id;
		return this;
	}
	
	public UserEntityTestDataBuilder withUsername(String username) {
		this.userEntityUsername = username;
		return this;
	}
	
	public UserEntityTestDataBuilder withPassword(String password) {
		this.userEntityPassw = password;
		return this;
	}
	
	public UserEntityTestDataBuilder withEnabled(boolean enabled) {
		this.userEntityEnabled = enabled;
		return this;
	}
	
	public UserEntityTestDataBuilder withRoleEntity(RoleEntity roleEntity) {
		this.userEntityRoleEntity = roleEntity;
		return this;
	}
	
	public UserEntity build() {
		UserEntity userEntity = new UserEntity();
		userEntity.setId(userEntityId);
		userEntity.setUsername(userEntityUsername);
		userEntity.setPassword(userEntityPassw);
		userEntity.setEnabled(userEntityEnabled);
		userEntity.setRole(userEntityRoleEntity);
		return userEntity;
	}
	
	
	

}
