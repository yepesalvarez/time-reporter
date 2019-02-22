package com.time.reporter.persistence.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.domain.Persistable;

@MappedSuperclass
public class AbstractEntity implements Persistable<Long> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public boolean isNew() {
		return (getId() == null);
	}

}
