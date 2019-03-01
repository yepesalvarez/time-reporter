package com.time.reporter.domain.exceptions;

public class UserInvalidDataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -363692008523324950L;
	
	private static final String MESSAGE = "Not valid parameters for user info saving";

	public UserInvalidDataException() {
		super(MESSAGE);		
	}

}
