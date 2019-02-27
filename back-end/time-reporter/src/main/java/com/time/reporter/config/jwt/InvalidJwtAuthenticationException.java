package com.time.reporter.config.jwt;

public class InvalidJwtAuthenticationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7991564098599254222L;
	
	public static final String MESSAGE = "Expired or invalid JWT token";
	
	public InvalidJwtAuthenticationException() {
		super(MESSAGE);		
	}

}
