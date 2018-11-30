package com.co.timereport;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@EntityScan(basePackages = "com.co.timereport.domain")
@ComponentScan(basePackages = {
		"com.co.timereport",
		"com.co.timereport.domain",
		"com.co.timereport.repository",
		"com.co.timereport.service",
		"com.co.timereport.controller"
		})
@EnableJpaRepositories(basePackages = "com.co.timereport.repository")
@SpringBootApplication
public class TimeReportApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(TimeReportApplication.class, args);
	}
	
	@Value("${spring.datasource.url}") String urlDb;
	@Value("${spring.datasource.username}") String username;
	@Value("${spring.datasource.password}") String passw;
	@Value("${spring.datasource.driver-class-name}") String driver;
	@Bean
	public DataSource dataSource() {
	    BasicDataSource dataSource = new BasicDataSource();
	    dataSource.setDriverClassName(driver);
	    dataSource.setUrl(urlDb);
	    dataSource.setUsername(username);
	    dataSource.setPassword(passw);
	    return  dataSource;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	    vendorAdapter.setGenerateDdl(true);
	
	    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
	    factory.setJpaVendorAdapter(vendorAdapter);
	    factory.setPackagesToScan("com.co.timereport.domain");
	    factory.setDataSource(dataSource());
	    factory.setJpaProperties(hibernateProperties());
	    factory.afterPropertiesSet();
	    return factory.getObject();
	    
	  }

  @Bean
  public PlatformTransactionManager transactionManager() {
    JpaTransactionManager txManager = new JpaTransactionManager();
    txManager.setEntityManagerFactory(entityManagerFactory());
    return txManager;
    
  }
  
   private final Properties hibernateProperties() {
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty(
				"hibernate.hbm2ddl.auto", "validate");
		return hibernateProperties;
   }
   
   @Bean
   public ModelMapper modelMapper() {
       return new ModelMapper();
   }
   
   @Bean
   public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }
   
}