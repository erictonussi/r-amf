package com.reignite.messaging.server;

import javax.servlet.ServletContext;

/**
 * An initializer that takes a servlet context and configures a RAMF server from
 * it.
 * 
 * @author Surrey
 * 
 */
public abstract class WebInitializer implements Initializer {

	@Override
	public RAMFServer getServer(Object context) {
		ServletContext servletContext = (ServletContext) context;
		return getServerFromServletContext(servletContext);
	}

	/**
	 * WebInitializers extend this method as a convenience.
	 * 
	 * @param servletContext
	 *            the servlet context.
	 * @return a RAMFServer ready to use.
	 */
	protected abstract RAMFServer getServerFromServletContext(ServletContext servletContext);
}
