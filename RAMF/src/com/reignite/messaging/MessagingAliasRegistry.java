package com.reignite.messaging;

import java.util.HashMap;
import java.util.Map;

import com.reignite.messaging.amf.AcknowledgeMessage;
import com.reignite.messaging.amf.RemotingMessage;

/**
 * This file is part of r-amf.
 * 
 * r-amf is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License.
 * 
 * r-amf is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * r-amf. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Created by Surrey Hughes of Reignite Pty Ltd <http://www.reignite.com.au>
 * 
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
