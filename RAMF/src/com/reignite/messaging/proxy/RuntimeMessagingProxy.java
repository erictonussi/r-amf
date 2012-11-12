/**
 * 
 */
package com.reignite.messaging.proxy;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.reignite.logging.LogWriter;
import com.reignite.messaging.Aliased;

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
 * Created at runtime by the MessagingProxyRegistry.
 * 
 * @author Surrey
 * 
 */
public class RuntimeMessagingProxy<T> implements MessagingProxy<T> {

	private Class<T> proxyClass;
	private T instance;
	// convenience for the hashmap for speed.
	private List<String> properties = new ArrayList<String>();
	private List<String> readProperties = new ArrayList<String>();
	// fieldname/descriptor map
	private Map<String, PropertyDescriptor> propertyMap = new HashMap<String, PropertyDescriptor>();
	private TypeEditor editor = new NumberAwareEditor();

	public RuntimeMessagingProxy(Class<T> type) {
		setProxyClass(type);
	}

	public RuntimeMessagingProxy() {
	}

	/**
	 * @return the proxyClass
	 */
	public Class<T> getProxyClass() {
		return proxyClass;
	}

	/**
	 * @param proxyClass
	 *            the proxyClass to set
	 */
	public void setProxyClass(Class<T> proxyClass) {
		this.proxyClass = proxyClass;
		try {
			BeanInfo info = Introspector.getBeanInfo(proxyClass, Object.class);
			PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : descriptors) {
				if (descriptor.getReadMethod() != null && descriptor.getWriteMethod() != null) {
					propertyMap.put(descriptor.getName(), descriptor);
					properties.add(descriptor.getName());
				}
			}
		} catch (IntrospectionException e) {
			LogWriter.error(getClass(), "Unable to read object of type: " + proxyClass);
		}
	}

	public static void main(String[] args) {
		try {
			BeanInfo info = Introspector.getBeanInfo(RuntimeMessagingProxy.class);
			PropertyDescriptor[] d = info.getPropertyDescriptors();
			for (PropertyDescriptor des : d) {
				System.out.println(des.getName());
			}
		} catch (IntrospectionException e) {
			// LogWriter.error(getClass(), "Unable to read object of type: " +
			// proxyClass);
		}
	}

	@Override
	public MessagingProxy<T> copy() {
		RuntimeMessagingProxy<T> proxy = new RuntimeMessagingProxy<T>();
		// shallow copy
		proxy.properties = properties;
		proxy.propertyMap = propertyMap;
		proxy.proxyClass = proxyClass;
		proxy.editor = editor;
		return proxy;
	}

	/**
	 * @return the instance
	 */
	public T getInstance() {
		if (instance == null) {
			try {
				instance = proxyClass.newInstance();
			} catch (Exception e) {
				LogWriter.error(getClass(),
						"Failed to create new instance of object type: " + proxyClass.getCanonicalName());
			}
		}
		return instance;
	}

	/**
	 * @param instance
	 *            the instance to set
	 */
	public void setInstance(T instance) {
		this.instance = instance;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((proxyClass == null) ? 0 : proxyClass.getCanonicalName().hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass().getCanonicalName() != obj.getClass().getCanonicalName()) {
			return false;
		}
		RuntimeMessagingProxy<?> other = (RuntimeMessagingProxy<?>) obj;
		if (proxyClass == null) {
			if (other.proxyClass != null) {
				return false;
			}
		} else if (!proxyClass.getCanonicalName().equals(other.proxyClass.getCanonicalName())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isExternalizable() {
		return getInstance() instanceof Externalizable;
	}

	@Override
	public List<String> getProperties() {
		return properties;
	}

	@Override
	public String getProxyClassName(boolean legacy) {
		return instance instanceof Aliased && legacy ? ((Aliased) instance).getAlias() : instance.getClass()
				.getCanonicalName();
	}

	@Override
	public void writeExternal(ObjectOutput worker) throws IOException {
		((Externalizable) getInstance()).writeExternal(worker);
	}

	@Override
	public Object getValue(String propName) {
		try {
			return propertyMap.get(propName).getReadMethod().invoke(getInstance());
		} catch (Exception e) {
			LogWriter.error(getClass(),
					"Failed to get property: " + propName + " from object of type: " + proxyClass.getCanonicalName()
							+ e, e);
			return null;
		}
	}

	@Override
	public void setValue(String propertyName, Object value) {
		try {
			// numbers are often messed up. eg: javascript and actionscript
			// really
			// only have doubles so we have to massage types.
			Method writer = propertyMap.get(propertyName).getWriteMethod();
			if (editor != null) {
				Class<?> requiredType = writer.getParameterTypes()[0];
				value = editor.convert(value, requiredType);
			}
			writer.invoke(getInstance(), value);
		} catch (Exception e) {
			LogWriter.error(getClass(), "Failed to set property: " + propertyName + " with value: " + value
					+ "for obejct: " + proxyClass.getCanonicalName());
		}
	}

	@Override
	public void readExternal(ObjectInput worker) throws IOException, ClassNotFoundException {
		((Externalizable) instance).readExternal(worker);
	}

	@Override
	public void addReadProperty(String readString) {
		readProperties.add(readString);
	}

	@Override
	public List<String> getReadProperties() {
		return readProperties;
	}

	/**
	 * @param editor
	 *            the editor to set
	 */
	@Override
	public void setTypeEditor(TypeEditor editor) {
		this.editor = editor;
	}

}
