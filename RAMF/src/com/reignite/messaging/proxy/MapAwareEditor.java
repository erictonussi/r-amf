package com.reignite.messaging.proxy;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

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
