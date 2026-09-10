package com.example.demo.exception;

public class BookingClosedException extends RuntimeException {
	public BookingClosedException(String message) {
		super(message);
	}

	public BookingClosedException(String message, Throwable cause) {
		super(message, cause);
	}
}
