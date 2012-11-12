package com.reignite.messaging.proxy;

import java.lang.reflect.Array;
import java.util.HashMap;
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
 * @author Surrey
 *
 */
public class MapAwareEditor implements TypeEditor {

	@Override
	public Object convert(Object value, Class<?> requiredType) {
		if (value != null && !Map.class.isAssignableFrom(value.getClass()) && Map.class.isAssignableFrom(requiredType)) {
			Map<Object, Object> map = new HashMap<Object, Object>();
			if (value.getClass().isArray()) {
				int length = Array.getLength(value);
				for (int i = 0; i < length; i++) {
					map.put(i, Array.get(value, i));
				}
			}
			value = map;
		}
		return value;
	}

}
