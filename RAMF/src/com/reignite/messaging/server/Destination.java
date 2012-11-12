/**
 * 
 */
package com.reignite.messaging.server;

/**
 * A wrapper around a set of service calls identified by a name.
 * 
 * @author Surrey
 * 
 */
public interface Destination {

	/**
	 * @return the name
	 */
	String getName();

	/**
	 * @param name
	 *            the name to set.
	 */
	void setName(String name);

	/**
	 * Gets the service call from this destination with the given name and set
	 * of parameters.
	 * 
	 * @param operation
	 *            the operation name to distinguish the call.
	 * @param params
	 *            the parameters to be understood by the Service.
	 * @return a constructed and ready to invoke service.
	 */
	Service getService(String operation, Object[] params);

}
