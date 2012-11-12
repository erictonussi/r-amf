package com.reignite.messaging.server;


/**
 * Creates an configures a RAMFServer.
 * 
 * @author Surrey
 * 
 */
public interface Initializer {

	/**
	 * Creates and configures a RAMF server using the given context.
	 * 
	 * @param context
	 *            An object that can be used by the initializer to configure the
	 *            server. For example a servlet context or a properties file.
	 * @return a RAMFServer ready to be used.
	 */
	RAMFServer getServer(Object context);

}
