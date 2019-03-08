package com.time.reporter.controller.exceptions;

public class ParametersMissmatchException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9067474625818632343L;
	
	private static final String MESSAGE = "Request's parameters do not match the expected types or values";

	public ParametersMissmatchException() {
		super(MESSAGE);
	}
	
}
