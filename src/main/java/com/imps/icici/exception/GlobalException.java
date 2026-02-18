package com.imps.icici.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.imps.icici.util.ErrorCodeUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ControllerAdvice
public class GlobalException {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,String>> methodbArgumentNotValidException(MethodArgumentNotValidException ex){
		Map<String,String> map = new HashMap<>();
		ex.getFieldErrors().forEach(e ->{
			map.put(e.getField() , e.getDefaultMessage());
		});
		return new ResponseEntity<Map<String,String>>(map,HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handlerResourceNotFound(ResourceNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error",ex.getMessage()));
	}
	
	@ExceptionHandler(CertificateNotFoundException.class)
	public ResponseEntity<?> handlerCertificateNotFound(CertificateNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error",ex.getMessage()));
	}
	
	@ExceptionHandler(EncryptionDecryptionException.class)
	public ResponseEntity<?> handleEncryptionDecryption(EncryptionDecryptionException ex){
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error",ex.getMessage()));
	}
	
	@ExceptionHandler(ResourceAlreadyExistException.class)
	public ResponseEntity<?> handleResourceAlreadyExist(ResourceAlreadyExistException ex){
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
	}

}
