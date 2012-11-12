package com.reignite.messaging.amf;

import com.reignite.messaging.Aliased;
import com.reignite.messaging.amfr.RemoteResponseMessage;

/**
 * The response from an AMFEndpoint routed remote call.
 * 
 * @author Surrey
 * 
 */
public class AcknowledgeMessage extends RemoteResponseMessage implements Aliased {

	private static final long serialVersionUID = -3060421139350506857L;
	
	@Override
	public String getAlias() {
		return "flex.messaging.messages.AcknowledgeMessage";
	}

}
