/**
 * 
 */
package com.reignite.messaging.server;

import java.util.Map;

/**
 * A basic EndpointLocator that assumes it is operating in a web container and
 * the given context strings are the servlet context, path and info.
 * 
 * @author Surrey
 * 
 */
public class WebEndpointLocator implements EndpointLocator {

	private Map<String, Endpoint> endpointMap;

	/**
	 * @see com.reignite.messaging.server.EndpointLocator#getEndpoint(com.reignite.messaging.server.RAMFServer,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Endpoint getEndpoint(RAMFServer server, String applicationContext, String endpointContext,
			String endpointInfo) {
		String key = endpointContext + (endpointInfo != null ? endpointInfo : "");
		Endpoint endpoint = endpointMap.get(key);

		return endpoint;
	}

	/**
	 * @return the endpointMap
	 */
	public Map<String, Endpoint> getEndpointMap() {
		return endpointMap;
	}

	/**
	 * @param endpointMap
	 *            the endpointMap to set
	 */
	public void setEndpointMap(Map<String, Endpoint> endpointMap) {
		this.endpointMap = endpointMap;
	}

}
