package com.reignite.messaging.amf;

public class AMFMessageBody {

	public static final String RESULT_METHOD = "/onResult";
	public static final String STATUS_METHOD = "/onStatus";

	private String targetURI;
	private String responseURI;
	private Object data; //array of RemotingMessage

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * @return the targetURI
	 */
	public String getTargetURI() {
		return targetURI;
	}

	/**
	 * @param targetURI
	 *            the targetURI to set
	 */
	public void setTargetURI(String targetURI) {
		this.targetURI = targetURI;
	}

	/**
	 * @return the reponseURI
	 */
	public String getResponseURI() {
		return responseURI;
	}

	/**
	 * @param reponseURI
	 *            the reponseURI to set
	 */
	public void setResponseURI(String responseURI) {
		this.responseURI = responseURI;
	}
}
