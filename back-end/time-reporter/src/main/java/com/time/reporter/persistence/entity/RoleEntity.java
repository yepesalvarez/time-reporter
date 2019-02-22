package com.time.reporter.persistence.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table (name = "role")
public class RoleEntity extends AbstractEntity {

	@Column(name = "name")
	@NotNull
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@OneToMany(mappedBy = "role",
			fetch = FetchType.LAZY)
	private List<UserEntity> users;
	
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
	
	public void addUser(UserEntity userEntity) {
		userEntity.setRole(this);
		users.add(userEntity);
	}
	
	public void removeUser(UserEntity userEntity) { 
		userEntity.setRole(null);
		users.remove(userEntity);
	}

	public List<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}

}
