package com.time.reporter.domain;

public class User {
	
	private String username;
	private String password;
	private Role role;
	private boolean active;
	
	public User(String username, String password, Role role, boolean active) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.active = active;
	}
	
	public User() {}
	
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
