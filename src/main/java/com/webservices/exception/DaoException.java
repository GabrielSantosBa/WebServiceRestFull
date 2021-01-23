package com.webservices.exception;

public class DaoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5918548610411619057L;

	
	private int code;
	
	public DaoException(String message, int code) {
		super(message);
		this.code = code;
		
	}
	
	public int getCode() {
		return code;
	}
}
