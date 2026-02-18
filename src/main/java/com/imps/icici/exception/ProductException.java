package com.imps.icici.exception;


public class ProductException extends Exception{

	private static String message;
	private String ex;
	
	public ProductException(String message , String ex) {
		super();
		this.message = message;
		this.ex = ex;
	}
}
