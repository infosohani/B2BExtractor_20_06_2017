package com.businessextractor.services.business.exception;

/**
 * Exception thrown when a business's fields cannot be validated.
 * @author user
 *
 */
public class BusinessValidationException
	extends Exception {

	public BusinessValidationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BusinessValidationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BusinessValidationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BusinessValidationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
		
}
