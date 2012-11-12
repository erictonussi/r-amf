package com.reignite.codec.amf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.reignite.exception.DeserializeException;
import com.reignite.messaging.amf.AMFMessage;
import com.reignite.messaging.proxy.MessagingProxy;

public class AMFRDeserializeWorker extends BaseDeserializeWorker {

	@Override
	public boolean worksWith(AMFMessage message) {
		return message.getVersion() == 99;
	}

	@Override
	public InputStream getInputStream() {
		return super.getIn();
	}

	@Override
	public int readMessageLength() throws IOException {
		// nothing, not used.
		return -1;
	}

	@Override
	public Object readObject() throws ClassNotFoundException, IOException {
		int type = readByte();
		Object value = readObjectValue(type);
		return value;
	}

	protected Object readObjectValue(int type) throws ClassNotFoundException, IOException, DeserializeException {
		Object value = null;

		switch (type) {
			case AMFR_STRING_TYPE:
				value = readString();
				break;
			case AMFR_OBJECT_TYPE:
				try {
					value = readTypedObject();
				} catch (Exception e) {
					throw new DeserializeException("Failed to deserialised.", e);
				}
				break;
			case AMFR_ARRAY_TYPE:
				value = readArray();
				break;
			case AMFR_FALSE_TYPE:
				value = Boolean.FALSE;
				break;
			case AMFR_TRUE_TYPE:
				value = Boolean.TRUE;
				break;
			case AMFR_INTEGER_TYPE:
				int i = readUInt29();
				// Symmetric with writing an integer to fix sign bits for
				// negative values...
				i = (i << 3) >> 3;
				value = new Integer(i);
				break;
			case AMFR_DOUBLE_TYPE:
				value = new Double(readDouble());
				break;
			case AMFR_UNDEFINED_TYPE:
			case AMFR_NULL_TYPE:
				break;
			case AMFR_DATE_TYPE:
				value = readDate();
				break;
			case AMFR_BYTEARRAY_TYPE:
				value = readByteArray();
				break;
			case AMFR_LIST_TYPE:
				value = readList();
				break;
			case AMFR_SET_TYPE:
				value = readHashSet();
				break;
			case AMFR_MAP_TYPE:
				value = readMap();
				break;
			default:
				// Unknown object type tag {type}
				DeserializeException ex = new DeserializeException("Unknown object type: " + type);
				throw ex;
		}

		return value;
	}

	protected Object readMap() throws IOException, ClassNotFoundException {
		int ref = readUInt29();

		if ((ref & 1) == 0) {
			return getObject(ref >> 1);
		}
		int length = (ref >> 1);

		Map<Object, Object> map = null;
		if (length > 0) {
			map = new HashMap<Object, Object>();
			rememberObject(map);
			Object name = readObject();
			while (name != null) {
				Object value = readObject();
				map.put(name, value);
				name = readObject();
			}
		}

		return map;
	}

	protected Object readList() throws ClassNotFoundException, IOException {
		int ref = readUInt29();

		if ((ref & 1) == 0) {
			return getObject(ref >> 1);
		}

		int length = (ref >> 1);
		List<Object> list = null;
		if (length > 0) {
			list = new ArrayList<Object>();
			rememberObject(list);
			for (int i = 0; i < length; i++) {
				list.add(readObject());
			}
		}
		return list;
	}

	protected Object readHashSet() throws ClassNotFoundException, IOException {
		int ref = readUInt29();

		if ((ref & 1) == 0) {
			return getObject(ref >> 1);
		}

		int length = (ref >> 1);
		Set<Object> set = null;
		if (length > 0) {
			set = new HashSet<Object>();
			rememberObject(set);
			for (int i = 0; i < length; i++) {
				set.add(readObject());
			}
		}
		return set;
	}

	protected Object readArray() throws ClassNotFoundException, IOException {
		int ref = readUInt29();

		if ((ref & 1) == 0) {
			return getObject(ref >> 1);
		}
		int len = (ref >> 1);
		Object[] array = null;
		if (len > 0) { // zero length return null
			array = new Object[len];
			rememberObject(array);
			for (int i = 0; i < len; i++) {
				Object item = readObject();
				array[i] = item;
			}
		}
		return array;
	}

	protected Object readDate() throws IOException {
		int ref = readUInt29();

		if ((ref & 1) == 0) {
			// This is a reference
			return getObject(ref >> 1);
		} else {
			long time = (long) readLong();

			Date d = new Date(time);
			rememberObject(d);

			return d;
		}
	}

	protected Object readByteArray() throws IOException {
		int ref = readUInt29();

		if ((ref & 1) == 0) {
			return getObject(ref >> 1);
		} else {
			int len = (ref >> 1);

			byte[] ba = new byte[len];

			rememberObject(ba);

			readFully(ba, 0, len);

			return ba;
		}
	}

	protected Object readTypedObject() throws ClassNotFoundException, IOException, InstantiationException,
			IllegalAccessException {
		int ref = readUInt29();

		if ((ref & 1) == 0) {
			return getObject(ref >> 1);
		} else {
			MessagingProxy<?> proxy = readProxy(ref);

			rememberObject(proxy.getInstance());

			if (proxy.isExternalizable()) {
				proxy.readExternal(this);
			} else {
				for (String propName : proxy.getReadProperties()) {
					Object value = readObject();
					proxy.setValue(propName, value);
				}
			}

			return proxy.getInstance();
		}
	}

}
