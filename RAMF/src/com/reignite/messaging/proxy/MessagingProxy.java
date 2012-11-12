package com.reignite.messaging.proxy;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

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
 * Either handcrafted and added to the registry or auto generated at runtime.
 * Used to serialize / deserialize objects.
 * 
 * @author Surrey
 * 
 */
public interface MessagingProxy<T> {

	/**
	 * @return a copy of this proxy so this proxy can be used like a prototype.
	 */
	MessagingProxy<T> copy();

	/**
	 * @param instance
	 *            the instance to proxy.
	 */
	void setInstance(T instance);

	/**
	 * @return true if this proxy proxies a class that is externalizable.
	 */
	boolean isExternalizable();

	/**
	 * @return a List of the names of the properties proxied
	 */
	List<String> getProperties();

	/**
	 * @param legacy
	 *            true if this proxy is to produce class names suitable for
	 *            consumption by flex compatible clients.
	 * @return the name of the class being proxied.
	 */
	String getProxyClassName(boolean legacy);

	/**
	 * To allow the proxy full control over serialisation.
	 * 
	 * @param worker
	 * @throws IOException
	 */
	void writeExternal(ObjectOutput worker) throws IOException;

	/**
	 * To allow the proxy full control over serialisation.
	 * 
	 * @param worker
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	void readExternal(ObjectInput worker) throws IOException, ClassNotFoundException;

	/**
	 * Gets the value for the given property name.
	 * 
	 * @param propName
	 *            the property to get the value for.
	 * @return the value or null.
	 */
	Object getValue(String propName);

	/**
	 * Sets the given value on the property.
	 * 
	 * @param propertyName
	 *            the property to set.
	 * @param value
	 *            the value to set.
	 */
	void setValue(String propertyName, Object value);

	/**
	 * Returns the instance or creates a new instance if needed.
	 * 
	 * @return
	 */
	T getInstance();

	/**
	 * When deserializing, put the properties in order of reading into the
	 * properties list.
	 * 
	 * @param readString
	 */
	void addReadProperty(String readString);

	/**
	 * @return the list of properties in read order.
	 */
	List<String> getReadProperties();

	/**
	 * @param editor
	 *            The editor for the Proxy to use when reading values before
	 *            setting them.
	 */
	void setTypeEditor(TypeEditor editor);
}
