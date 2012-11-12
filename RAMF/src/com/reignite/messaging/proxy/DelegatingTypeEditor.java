/**
 * 
 */
package com.reignite.messaging.proxy;

import java.util.Collection;
import java.util.Map;

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
 * Delegates conversion to type specific editors.
 * 
 * @author Surrey
 * 
 */
public class DelegatingTypeEditor implements TypeEditor {

	private TypeEditor numberEditor = new NumberAwareEditor();
	private TypeEditor collectionEditor = new CollectionAwareEditor();
	private TypeEditor mapEditor = new MapAwareEditor();

	/**
	 * @see com.reignite.messaging.proxy.TypeEditor#convert(java.lang.Object,
	 *      java.lang.Class)
	 */
	@Override
	public Object convert(Object value, Class<?> requiredType) {
		if (value != null) {
			if (value instanceof Number) {
				return numberEditor.convert(value, requiredType);
			}
			if (Collection.class.isAssignableFrom(requiredType)) {
				return collectionEditor.convert(value, requiredType);
			}
			if (Map.class.isAssignableFrom(requiredType)){
				return mapEditor.convert(value, requiredType);
			}
		}
		return value;
	}

}
