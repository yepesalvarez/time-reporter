package com.co.timereport.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.domain.Persistable;

@MappedSuperclass
public abstract class Entities implements Persistable<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	protected Long id;
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public boolean isNew() {
		return (getId() == null);
	}
}
