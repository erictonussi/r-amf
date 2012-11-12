package com.reignite.messaging.proxy;

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
 * A TypeEditor is used by a MessagingProxy to manipulate individual field types
 * into the type required by the proxy. For example Javascript and Actionscript
 * only send doubles and not longs and they can't send null values for numbers.
 * 
 * @author Surrey
 * 
 */
public interface TypeEditor {

	/**
	 * Attempts to convert the given value into the provided type.
	 * 
	 * @param value
	 *            the value to convert
	 * @param requiredType
	 *            the type to attempt to convert it to.
	 * @return the original value or the converted one if possible.
	 */
	Object convert(Object value, Class<?> requiredType);

}
