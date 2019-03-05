package com.time.reporter.domain.exceptions;

public class PasswordNotAllowedException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7648355189656379457L;
	public static final String MESSAGE = "Password does not fulfill the security policy";
	
	public PasswordNotAllowedException () {
		super(MESSAGE);
	}

}
