package com.example.demo.exception;

public class PublishException extends RuntimeException {

	public PublishException(String message){
		super(message);
	}
	
	public PublishException(String message, Throwable cause) {
		super(message, cause);
	}
}
