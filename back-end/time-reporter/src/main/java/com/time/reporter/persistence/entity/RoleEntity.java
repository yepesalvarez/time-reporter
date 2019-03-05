package com.time.reporter.persistence.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table (name = "role")
public class RoleEntity extends AbstractEntity {

	@Column(name = "name")
	@NotNull
	@Size(max = 255, min = 4)
	private String name;
	
	@Column(name = "description")
	@Size(max = 500)
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
