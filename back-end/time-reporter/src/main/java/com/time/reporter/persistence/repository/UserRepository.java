package com.time.reporter.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.time.reporter.persistence.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

	List<UserEntity> findAll();
	Page<UserEntity> findAll(Pageable pageable);
	UserEntity findByUsername(String username);
	
}
