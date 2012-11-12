package com.reignite.exception;

/**
 * Thrown when the deserializer hits an object it can't deserialize.
 * 
 * @author Surrey
 * 
 */
public class DeserializeException extends RuntimeException {

	private static final long serialVersionUID = 3031066442495522862L;

	public DeserializeException() {
	}

	public DeserializeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DeserializeException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeserializeException(String message) {
		super(message);
	}

	public DeserializeException(Throwable cause) {
		super(cause);
	}

}
