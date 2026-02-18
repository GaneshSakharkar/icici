package com.imps.icici.exception;

public class CertificateNotFoundException extends RuntimeException{

	private String message;
	
	public CertificateNotFoundException(String message) {
		super(message);
		this.message = message;
	}
}
