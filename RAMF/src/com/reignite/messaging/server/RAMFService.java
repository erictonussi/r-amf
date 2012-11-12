package com.reignite.messaging.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A service called by an endpoint in a RAMFServer.
 * 
 * @author Surrey
 * 
 */
public class RAMFService implements Service {

	private Object remoteService;
	private Method method;
	private Object[] params;

	public RAMFService(Object target, Method method, Object[] params) {
		this.remoteService = target;
		this.method = method;
		this.params = params;
	}

	@Override
	public Object invoke() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return method.invoke(remoteService, params);
	}

}
