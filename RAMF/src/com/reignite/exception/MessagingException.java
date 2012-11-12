/**
 * 
 */
package com.reignite.exception;

/**
 * @author Surrey
 * 
 */
public class MessagingException extends Exception {

	private static final long serialVersionUID = 1947461675411566070L;

	public MessagingException() {
		super();
	}

	public MessagingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MessagingException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessagingException(String message) {
		super(message);
	}

	public MessagingException(Throwable cause) {
		super(cause);
	}

}
