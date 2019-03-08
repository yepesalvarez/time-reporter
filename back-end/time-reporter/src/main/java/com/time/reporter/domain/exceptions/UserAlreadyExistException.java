package com.time.reporter.domain.exceptions;

public class UserAlreadyExistException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -19040828838737100L;
	private static final String MESSAGE = "This user already exists in the system";
	
	public UserAlreadyExistException() {
		super(MESSAGE);
	}

}
