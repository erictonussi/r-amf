package com.reignite.messaging.amf;

import com.reignite.messaging.server.AMFServiceContext;

/**
 * A filter that forms a part of a chain through the AMFEndpoint lifecycle.
 * 
 * @author Surrey
 * 
 */
public interface AMFFilter {

	/**
	 * Does work on the given visitor (AMFServiceContext) This method should
	 * invoke the next filter in the chain
	 * 
	 * @param context
	 *            the visitor with the input and output stream.
	 */
	void invoke(AMFServiceContext context);

	/**
	 * @param next
	 *            the next filter in the chain
	 */
	void setNext(AMFFilter next);

	/**
	 * @return the next filter in the chain or null.
	 */
	AMFFilter getNext();

}
