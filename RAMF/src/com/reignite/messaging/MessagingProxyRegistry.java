package com.reignite.messaging;

import java.util.HashMap;
import java.util.Map;

import com.reignite.messaging.proxy.MessagingProxy;
import com.reignite.messaging.proxy.RuntimeMessagingProxy;
import com.reignite.messaging.proxy.TypeEditor;

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
 * A registry for messaging proxies, either runtime as created by the serializer
 * / deserializer or custom as provided.
 * 
 * @author Surrey
 * 
 */
public class MessagingProxyRegistry {

	private Map<String, MessagingProxy<?>> proxyMap = new HashMap<String, MessagingProxy<?>>();
	private TypeEditor typeEditor;

	/**
	 * Gets or creates a MessagingProxy for the given type. The registry is
	 * first searched based on a canonical name match then a simple one. If no
	 * match is found a new RuntimeMessagingProxy is created and added to the
	 * proxy under the canonical name.
	 * 
	 * @param type
	 *            the class to proxy.
	 * @return a MessagingProxy.
	 */
	@SuppressWarnings("unchecked")
	public <T> MessagingProxy<T> getProxy(T instance) {
		MessagingProxy<T> proxy = null;
		Class<T> type = (Class<T>) instance.getClass();
		if (proxyMap.containsKey(type.getCanonicalName())) {
			proxy = (MessagingProxy<T>) proxyMap.get(type.getCanonicalName());
		} else if (proxyMap.containsKey(type.getSimpleName())) {
			proxy = (MessagingProxy<T>) proxyMap.get(type.getSimpleName());
		}
		if (proxy == null) {
			proxy = new RuntimeMessagingProxy<T>(type);
			if (typeEditor != null) {
				proxy.setTypeEditor(typeEditor);
			}
			proxyMap.put(type.getCanonicalName(), proxy);
		}
		proxy = proxy.copy();
		proxy.setInstance(instance);
		return proxy;
	}

	@SuppressWarnings("unchecked")
	public <T> MessagingProxy<T> getProxy(Class<?> clazz) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		Object instance = clazz.newInstance();
		return getProxy((T) instance);
	}

	public void setRegistry(Map<String, MessagingProxy<?>> registry) {
		if (registry != null) {
			for (String key : registry.keySet()) {
				proxyMap.put(key, registry.get(key));
			}
		}
	}

	/**
	 * @return the typeEditor
	 */
	public TypeEditor getTypeEditor() {
		return typeEditor;
	}

	/**
	 * @param typeEditor
	 *            the typeEditor to set
	 */
	public void setTypeEditor(TypeEditor typeEditor) {
		this.typeEditor = typeEditor;
	}
}
