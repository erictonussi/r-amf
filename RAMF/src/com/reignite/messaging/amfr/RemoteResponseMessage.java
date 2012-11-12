/**
 * 
 */
package com.reignite.messaging.amfr;

import com.reignite.messaging.MessageBodyData;

/**
 * Replacement for AcknowledgeMessage without the alias requirement.
 * 
 * @author Surrey
 * 
 */
public class RemoteResponseMessage implements MessageBodyData {

	private static final long serialVersionUID = -3435643374191904400L;

	private Object body;

	/**
	 * @return the body
	 */
	public Object getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(Object body) {
		this.body = body;
	}
}
