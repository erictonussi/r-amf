package com.reignite.messaging.amf;

import java.util.ArrayList;
import java.util.List;

/**
 * An AMF message
 * 
 * @author Surrey
 * 
 */
public class AMFMessage {

	private int version;
	private List<AMFMessageHeader> headers;
	private List<AMFMessageBody> bodies;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<AMFMessageHeader> getHeaders() {
		if (headers == null) {
			headers = new ArrayList<AMFMessageHeader>();
		}
		return headers;
	}

	public void setHeaders(List<AMFMessageHeader> headers) {
		this.headers = headers;
	}

	public List<AMFMessageBody> getBodies() {
		if (bodies == null){
			bodies = new ArrayList<AMFMessageBody>();
		}
		return bodies;
	}

	public void setBodies(List<AMFMessageBody> bodies) {
		this.bodies = bodies;
	}

}
