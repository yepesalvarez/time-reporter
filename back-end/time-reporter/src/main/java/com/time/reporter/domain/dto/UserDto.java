package com.time.reporter.domain.dto;


import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "user")
public class UserDto {
	
	@ApiModelProperty(notes = "unique identifier for user in the system", position = 5)
	private Long id;
	@ApiModelProperty(notes = "username for loggin", required = true, position = 1)
	private String username;
	@ApiModelProperty(notes = "password for loggin", required = true, position = 2)
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*])(?=\\S+$).{8,160}$")
	private String password;
	@ApiModelProperty(notes = "Role name", example = "USER", position = 4)
	private String  role;
	@ApiModelProperty(notes = "establish whether the user would be able to use the system or not, default value : false"
			, allowableValues = "true/false", position = 3)
	private boolean enabled;
	@ApiModelProperty(notes = "Bearer json web token for requests, it must be sent inside the header in the Authorization value in order to access to protected urls"
			, example = "Bearer eyJ0eXAiOiJKV1QiLCJhbG..", position = 6)
	private String token;
	
	public UserDto(Long id, String username, String password, String role, boolean enabled) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
	}
	
	public UserDto() {}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
