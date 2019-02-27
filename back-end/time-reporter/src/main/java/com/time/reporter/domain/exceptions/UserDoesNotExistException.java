package com.time.reporter.domain.exceptions;

public class UserDoesNotExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5268846352682491374L;
	
	private static final String MESSAGE = "User does not exist";
	
	public UserDoesNotExistException() {
		super(MESSAGE);
	}

}
