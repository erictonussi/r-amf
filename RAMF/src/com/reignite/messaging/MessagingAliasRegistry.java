package com.reignite.messaging;

import java.util.HashMap;
import java.util.Map;

import com.reignite.messaging.amf.AcknowledgeMessage;
import com.reignite.messaging.amf.RemotingMessage;

/**
 * A Registry for class alias names. This is referenced when reading messages
 * from a client so we can map client classes to server classes.
 * 
 * @author Surrey
 * 
 */
public class MessagingAliasRegistry {

	protected Map<String, Class<?>> aliasRegistry = new HashMap<String, Class<?>>();

	public MessagingAliasRegistry() {
		aliasRegistry.put("flex.messaging.messages.RemotingMessage", RemotingMessage.class);
		aliasRegistry.put("DSK", AcknowledgeMessage.class);
	}

	public Class<?> getAlias(String toAlias) throws ClassNotFoundException {
		if (aliasRegistry.containsKey(toAlias)) {
			return aliasRegistry.get(toAlias);
		}
		Class<?> fromAlias = Class.forName(toAlias);
		aliasRegistry.put(toAlias, fromAlias);
		return fromAlias;
	}

	public void setRegistry(Map<String, Class<?>> registry) {
		if (registry != null) {
			for (String key : registry.keySet()) {
				aliasRegistry.put(key, registry.get(key));
			}
		}
	}
}
