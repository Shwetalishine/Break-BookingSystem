package com.breakbooking.exception;

public class DuplicateResourceException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public DuplicateResourceException(String messgae) {
		super(messgae);
	}
}
