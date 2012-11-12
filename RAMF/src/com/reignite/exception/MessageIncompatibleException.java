/**
 * 
 */
package com.reignite.exception;


/**
 * @author Surrey
 * 
 */
public class MessageIncompatibleException extends MessagingException {

	private static final long serialVersionUID = -3266187373239251023L;

	public MessageIncompatibleException() {
		super();
	}

	public MessageIncompatibleException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MessageIncompatibleException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageIncompatibleException(String message) {
		super(message);
	}

	public MessageIncompatibleException(Throwable cause) {
		super(cause);
	}

}
