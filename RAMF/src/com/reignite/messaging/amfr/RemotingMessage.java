package com.reignite.messaging.amfr;

import java.util.Map;

import com.reignite.messaging.MessageBodyData;

/**
 * A remoting message used by AMFR (AMF version 99) It drops legacy support.
 * 
 * @author Surrey
 * 
 */
public class RemotingMessage implements MessageBodyData {

	private static final long serialVersionUID = 4025292893851888403L;

	private String operation;
	private Object[] parameters;
	protected String destination;
	private Map<String, Object> headers;

	public RemoteResponseMessage createResponse() {
		return new RemoteResponseMessage();
	}

	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 *            the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * @return the parameters
	 */
	public Object[] getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * @param destination
	 *            the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @return the headers
	 */
	public Map<String, Object> getHeaders() {
		return headers;
	}

	/**
	 * @param headers
	 *            the headers to set
	 */
	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}
}
