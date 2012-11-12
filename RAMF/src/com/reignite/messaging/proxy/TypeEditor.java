package com.reignite.messaging.proxy;

/**
 * A TypeEditor is used by a MessagingProxy to manipulate individual field types
 * into the type required by the proxy. For example Javascript and Actionscript
 * only send doubles and not longs and they can't send null values for numbers.
 * 
 * @author Surrey
 * 
 */
public interface TypeEditor {

	/**
	 * Attempts to convert the given value into the provided type.
	 * 
	 * @param value
	 *            the value to convert
	 * @param requiredType
	 *            the type to attempt to convert it to.
	 * @return the original value or the converted one if possible.
	 */
	Object convert(Object value, Class<?> requiredType);

}
