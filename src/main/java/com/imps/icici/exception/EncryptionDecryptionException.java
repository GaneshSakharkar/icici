package com.imps.icici.exception;

public class EncryptionDecryptionException extends RuntimeException{

	private String message;
	
	public EncryptionDecryptionException(String message) {
		super(message);
		this.message = message;
	}
	
}
