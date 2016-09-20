package com.upb.taxbilling.exceptions;

/**
 * Represents the exception that occurs typically because of a bill's error. 
 * @author Allan Leon
 */
public class BillException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that receives a message as a parameter.
	 * @param message of the exception.
	 */
	public BillException(String message) {
		super(message);
	}
}
