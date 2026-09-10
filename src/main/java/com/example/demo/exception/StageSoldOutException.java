package com.example.demo.exception;

public class StageSoldOutException extends RuntimeException {
	public StageSoldOutException(String message) {
		super(message);
	}

	public StageSoldOutException(String message, Throwable cause) {
		super(message, cause);
	}
}
