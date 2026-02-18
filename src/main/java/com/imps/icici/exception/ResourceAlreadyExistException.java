package com.imps.icici.exception;

public class ResourceAlreadyExistException extends RuntimeException {

	private String message;
	
	public ResourceAlreadyExistException(String message) {
		super();
		this.message = message;
	}
}
