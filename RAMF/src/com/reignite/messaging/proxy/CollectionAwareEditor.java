package com.reignite.messaging.proxy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
