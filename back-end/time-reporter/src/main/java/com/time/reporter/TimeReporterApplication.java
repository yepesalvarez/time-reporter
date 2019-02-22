package com.time.reporter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {
		"com.time.reporter.persistence.entity"
})
@ComponentScan(basePackages = {
		"com.time.reporter.config",
		"com.time.reporter.domain",
		"com.time.reporter.persistence",
		"com.time.reporter.service",
		"com.time.reporter.controller"
})
@EnableJpaRepositories(basePackages = {
		"com.time.reporter.persistence.repository"
})
@SpringBootApplication
public class TimeReporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeReporterApplication.class, args);
	}

}
