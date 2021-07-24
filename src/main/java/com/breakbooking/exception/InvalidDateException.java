package com.breakbooking.exception;

public class InvalidDateException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public InvalidDateException(String messgae) {
		super(messgae);
	}
}
