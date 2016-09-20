package com.upb.taxbilling.exceptions;

/**
 * Represents the exception that occurs typically because of a register's error. 
 * @author Allan Leon
 */
public class RegisterException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that receives a message as a parameter.
	 * @param message of the exception.
	 */
	public RegisterException(String message) {
		super(message);
	}
}
