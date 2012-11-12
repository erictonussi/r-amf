package com.reignite.messaging.server;

import java.lang.reflect.InvocationTargetException;

/**
 * The interface to all services exposed through an endpoint via a server.
 * 
 * @author Surrey
 * 
 */
public interface Service {

	/**
	 * Invokes the service.
	 * 
	 * @return the service response.
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	Object invoke() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;

}
