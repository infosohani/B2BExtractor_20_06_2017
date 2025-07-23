package com.businessextractor.util;

/**
 * Templateable validator interface that defines the methods a validator must impose
 * @author user
 *
 * @param <T> The object type being validated
 * @param <E> The validation exception that can be thrown
 */
public interface Validator<T,E extends Exception> {
	
	/**
	 * Regex used for validating email addresses in the application
	 */
	public static final String EMAIL_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[_A-Za-z0-9-]+)";
	
	/**
	 * Abstract validate model function. Validates T and throws E if a validation error occured.
	 * @param t 
	 * @throws E
	 */
	public void validate( T t ) throws E;
	
	
}
