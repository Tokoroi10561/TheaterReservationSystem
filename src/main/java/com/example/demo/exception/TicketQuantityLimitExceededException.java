package com.example.demo.exception;

public class TicketQuantityLimitExceededException extends RuntimeException {
	public TicketQuantityLimitExceededException(String message) {
		super(message);
	}

	public TicketQuantityLimitExceededException(String message, Throwable cause) {
		super(message, cause);
	}
}
