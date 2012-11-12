package com.reignite.exception;

/**
 * Thrown when initialization failes.
 * @author Surrey
 *
 */
public class InitializeException extends Exception {

	private static final long serialVersionUID = 3841965701234550334L;

	public InitializeException() {
	}

	public InitializeException(String message) {
		super(message);
	}

	public InitializeException(Throwable cause) {
		super(cause);
	}

	public InitializeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InitializeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
