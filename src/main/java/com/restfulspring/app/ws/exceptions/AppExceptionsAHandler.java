package com.restfulspring.app.ws.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AppExceptionsAHandler {
	
	@ExceptionHandler(value= {UserServiceException.class})
	public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request){
		
		return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(),  HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	
	@ExceptionHandler(value= {Exception.class})
	public ResponseEntity<Object> handleOtherException(Exception ex, WebRequest request){
		
		return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(),  HttpStatus.INTERNAL_SERVER_ERROR);
		
	}

	
	
	
}
