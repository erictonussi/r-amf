package com.reignite.messaging;

/**
 * This class has another name for use during serialization / deserialization.
 * 
 * @author Surrey
 * 
 */
public interface Aliased {

	/**
	 * @return the class name to be used
	 */
	String getAlias();
}
