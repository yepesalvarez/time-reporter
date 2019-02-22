package com.time.reporter.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table (name = "user")
public class UserEntity extends AbstractEntity {

	@Column(name = "username", unique = true)
	@NotNull
	String username;
	
	@Column(name = "password")
	@NotNull
	String password;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	RoleEntity role;

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

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof UserEntity)) {
			return false;
		}
		if (this == object) {
			return true;
		}
		final UserEntity otherUserEntity = (UserEntity) object; 
		return new EqualsBuilder()
				.append(this.getId(), otherUserEntity.getId())
				.append(this.getUsername(), otherUserEntity.getUsername())
				.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.id)
				.append(this.getUsername())
				.toHashCode();
	}

}
