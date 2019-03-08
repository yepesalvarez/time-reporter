package com.time.reporter.timereporter.testdatabuilder;

import com.time.reporter.domain.dto.UserDto;

public class UserDtoTestDataBuilder {
	
	static final Long ID = 999999L;
	static final String USERNAME = "user.test";
	static final String PASSW = "$Qwe123*";
	static final String ROLE = "USER";
	static final boolean ENABLED = true;
	
	private Long userId;
	private String userUsername;
	private String userPassw;
	private String userRole;
	private boolean userEnabled;
	
	public UserDtoTestDataBuilder() {
		this.userId = ID;
		this.userUsername = USERNAME;
		this.userPassw = PASSW;
		this.userRole = ROLE;
		this.userEnabled = ENABLED;
	}
	
	public UserDtoTestDataBuilder withId(Long id) {
		this.userId = id;
		return this;
	}
	
	public UserDtoTestDataBuilder withUserName(String username) {
		this.userUsername = username;
		return this;
	}
	
	public UserDtoTestDataBuilder withPassword(String passw) {
		this.userPassw = passw;
		return this;
	}
	
	public UserDtoTestDataBuilder withRole(String role) {
		this.userRole = role;
		return this;
	}
	
	public UserDtoTestDataBuilder withEnabled(boolean enabled) {
		this.userEnabled = enabled;
		return this;
	}
	
	public UserDto build() {
		return new UserDto(userId, userUsername, userPassw, userRole, userEnabled);
	}
	
}
