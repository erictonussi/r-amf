package com.reignite.messaging.proxy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
 * @author Surrey
 *
 */
public class CollectionAwareEditor implements TypeEditor {

	@Override
	public Object convert(Object value, Class<?> requiredType) {
		if (value != null) {
			if (value.getClass().isArray()) {
				if (Collection.class.isAssignableFrom(requiredType)) {
					Collection<Object> col = null;
					if (List.class.isAssignableFrom(requiredType)) {
						col = new ArrayList<Object>();
					} else if (Set.class.isAssignableFrom(requiredType)) {
						col = new HashSet<Object>();
					}
					if (col != null) {
						for (int i = 0; i < Array.getLength(value); i++) {
							col.add(Array.get(value, i));
						}
					}
					value = col;
				}
			}
		}
		return value;
	}

}
