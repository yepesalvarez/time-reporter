package com.co.timereport.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:messages.properties")
public abstract class AbstractController {
	
	@Value("${controller.status.ok}") private String statusOK;
	@Value("${controller.status.conflict}")	private String statusConflict;
	@Value("${controller.status.bad_request}") private String statusBadRequest;
	@Value("${controller.status.not_found}") private String statusNotFound;
	@Value("${controller.status.no_content}") private String statusNoContent;
	@Value("${controller.status.internal_server_error}") private String statusInternalServerErr;

	public String getStatusOK() {
		return statusOK;
	}

	public String getStatusConflict() {
		return statusConflict;
	}

	public String getStatusBadRequest() {
		return statusBadRequest;
	}

	public String getStatusNotFound() {
		return statusNotFound;
	}

	public String getStatusNoContent() {
		return statusNoContent;
	}

	public String getStatusInternalServerErr() {
		return statusInternalServerErr;
	}

}
