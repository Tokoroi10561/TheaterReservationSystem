package com.example.demo.exception;

public class NotReservedException extends RuntimeException {

	public NotReservedException(String message){
		super(message);
	}
	
	public NotReservedException(String message, Throwable cause){
		super(message, cause);
	}
}
