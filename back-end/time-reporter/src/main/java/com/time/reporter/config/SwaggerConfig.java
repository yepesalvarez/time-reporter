package com.time.reporter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	 @Bean
	 public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(Predicates.not(PathSelectors.regex("/error.*")))                          
          .build()
          .apiInfo(getApiInfo());
    }
	 
	private ApiInfo getApiInfo() {
	    return new ApiInfoBuilder()
	            .title("REST API - User Management")
	            .description("\"Simple Spring Boot REST Api for managing users implementing Json Web Tokens\"")
	            .version("1.0.0")
	            .license("Apache License Version 2.0")
	            .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
	            .contact(new Contact("Luis Yepes", "https://www.ceiba.com.co", "luis.yepes@ceiba.com.co"))
	            .build();
    }

}
