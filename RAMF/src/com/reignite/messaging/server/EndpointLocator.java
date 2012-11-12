package com.reignite.messaging.server;

/**
 * Locates an enpoint.
 * 
 * @author Surrey
 * 
 */
public interface EndpointLocator {

	/**
	 * Uses the applicationContext, endpointContext and endpointInfo to find and
	 * return an endpoint.
	 * 
	 * @param server
	 *            the RAMFServer requesting the endpoint.
	 * @param applicationContext
	 *            the application to locate an endpoint for. In the case of a
	 *            web application this is likely the application context path.
	 * @param endpointContext
	 *            the endpoint context to locate an endpoint in. In the case of
	 *            a web application this is likely the servlet path.
	 * @param endpointInfo
	 *            the endpoint info used to discriminate the endpoint. In the
	 *            case of a web application this is likely the servlet info
	 *            path.
	 * @return
	 */
	Endpoint getEndpoint(RAMFServer server, String applicationContext, String endpointContext, String endpointInfo);

}
