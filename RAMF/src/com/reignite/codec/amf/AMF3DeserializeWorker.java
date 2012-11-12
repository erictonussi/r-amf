package com.reignite.codec.amf;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.reignite.exception.DeserializeException;
import com.reignite.messaging.amf.AMFMessage;
import com.reignite.messaging.proxy.MessagingProxy;

public class AMF3DeserializeWorker extends BaseDeserializeWorker {

	private boolean legacyCollection = false;

	@Override
	public boolean worksWith(AMFMessage message) {
		return (message.getVersion() == 3 || message.getVersion() == 0);
	}

	@Override
	public InputStream getInputStream() {
		return super.getIn();
	}

	@Override
	public int readMessageLength() throws IOException{
		return readInt();
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
			case AMF0_NUMBER_TYPE:
				double d = readDouble();
				value = new Double(d);
				break;
			case AMF0_BOOLEAN_TYPE:
				value = Boolean.valueOf(readBoolean());
				break;
			case AMF0_STRING_TYPE:
				value = readUTF();
				break;
			case AMF0_AVMPLUSOBJECT_TYPE:
				value = readAMF3Object();
				break;
			case AMF0_STRICTARRAY_TYPE:
				value = readArrayValue();
				break;
			case AMF0_TYPEDOBJECT_TYPE:
				String typeName = readUTF();
				try {
					value = readObjectValue(typeName);
				} catch (Exception e1) {
					throw new DeserializeException("Failed to deserialised.", e1);
				}
				break;
			case AMF0_LONGSTRING_TYPE:
				value = readLongUTF();
				break;
			case AMF0_OBJECT_TYPE:
				try {
					value = readObjectValue(null);
				} catch (Exception e1) {
					throw new DeserializeException("Failed to deserialised.", e1);
				}
				break;
			case AMF0_DATE_TYPE:
				long time = (long) readDouble();
				readShort(); // consume timezone. Always UTC
				value = new Date(time);
				break;
			case AMF0_ECMAARRAY_TYPE:
				value = readECMAArrayValue();
				break;
			case AMF0_REFERENCE_TYPE:
				int refNum = readUnsignedShort();
				value = getObject(refNum);
				break;
			case AMF0_NULL_TYPE:
			case AMF0_UNDEFINED_TYPE:
				break;
			default:
				DeserializeException e = new DeserializeException("Can't deserialize type: " + type);
				throw e;
		}
		return value;
	}

	protected Map<String, Object> readECMAArrayValue() throws ClassNotFoundException, IOException {
		int size = readInt();
		Map<String, Object> h;
		if (size == 0) {
			h = new HashMap<String, Object>();
		} else {
			h = new HashMap<String, Object>(size);
		}

		rememberObject(h);

		String name = readUTF();
		int type = readByte();
		while (type != AMF0_OBJECTEND_TYPE) {
			Object value = readObjectValue(type);
			// blaze ignores "length", but what if you want to
			// send length?
			h.put(name, value);
			name = readUTF();
			type = readByte();
		}

		return h;
	}

	protected String readLongUTF() throws IOException {
		int utflen = readInt();
		byte bytearr[] = new byte[utflen];
		readFully(bytearr);
		String utf8 = new String(bytearr, "UTF-8");
		return utf8;
	}

	protected Object readObjectValue(String className) throws ClassNotFoundException, IOException,
			InstantiationException, IllegalAccessException {
		Object object = null;

		// Check for any registered class aliases
		Class<?> aliasedClass = getAlias(className);

		MessagingProxy<?> proxy = getProxy(aliasedClass);

		rememberObject(proxy.getInstance());

		String propertyName = readUTF();
		int type = readByte();
		while (type != AMF0_OBJECTEND_TYPE) {
			Object value = readObjectValue(type);
			proxy.setValue(propertyName, value);
			propertyName = readUTF();
			type = readByte();
		}

		return object;
	}

	protected Object readArrayValue() throws ClassNotFoundException, IOException {
		int size = readInt();
		List<Object> list = new ArrayList<Object>(size);
		rememberObject(list);

		for (int i = 0; i < size; i++) {
			int type = readByte();
			list.add(readObjectValue(type));
		}

		if (legacyCollection) {
			return list;
		} else {
			return list.toArray();
		}
	}

	public Object readAMF3Object() throws ClassNotFoundException, IOException {
		int type = readByte();
		Object value = readAMF3ObjectValue(type);
		return value;
	}

	protected Object readAMF3ObjectValue(int type) throws ClassNotFoundException, IOException {
		Object value = null;

		switch (type) {
			case AMF3_STRING_TYPE:
				value = readString();
				break;
			case AMF3_OBJECT_TYPE:
				try {
					value = readTypedObject();
				} catch (Exception e) {
					throw new DeserializeException("Failed to deserialised.", e);
				}
				break;
			case AMF3_ARRAY_TYPE:
				value = readArray();
				break;
			case AMF3_FALSE_TYPE:
				value = Boolean.FALSE;
				break;
			case AMF3_TRUE_TYPE:
				value = Boolean.TRUE;
				break;
			case AMF3_INTEGER_TYPE:
				int i = readUInt29();
				// Symmetric with writing an integer to fix sign bits for
				// negative
				// values...
				i = (i << 3) >> 3;
				value = new Integer(i);
				break;
			case AMF3_DOUBLE_TYPE:
				value = new Double(readDouble());
				break;
			case AMF3_UNDEFINED_TYPE:
			case AMF3_NULL_TYPE:
				break;
			case AMF3_DATE_TYPE:
				value = readDate();
				break;
			case AMF3_BYTEARRAY_TYPE:
				value = readByteArray();
				break;
			default:
				// Unknown object type tag {type}
				DeserializeException ex = new DeserializeException("Unknown object type: " + type);
				throw ex;
		}

		return value;
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

	protected Object readDate() throws IOException {
		int ref = readUInt29();

		if ((ref & 1) == 0) {
			// This is a reference
			return getObject(ref >> 1);
		} else {
			long time = (long) readDouble();

			Date d = new Date(time);
			rememberObject(d);

			return d;
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
					Object value = readAMF3Object();
					proxy.setValue(propName, value);
				}
			}

			return proxy.getInstance();
		}
	}

	protected Object readArray() throws ClassNotFoundException, IOException {
		int ref = readUInt29();

		if ((ref & 1) == 0) {
			return getObject(ref >> 1);
		} else {
			int len = (ref >> 1);
			Object array = null;
			Map<String, Object> map = null;
			String name = readString();
			while (name != null && name.length() > 0) {
				if (map == null) {
					map = new HashMap<String, Object>();
					array = map;

					rememberObject(array);
				}

				Object value = readAMF3Object();
				map.put(name, value);
				name = readString();
			}

			if (map == null) {
				// Legacy Flex 1.5 behavior was to return a
				// java.util.Collection
				// for Array
				if (legacyCollection) {
					List<Object> list = new ArrayList<Object>(len);
					array = list;

					rememberObject(array);

					for (int i = 0; i < len; i++) {
						Object item = readAMF3Object();
						list.add(item);
					}
				} else {
					// New Flex 2+ behavior is to return Object[] for AS3
					// Array
					array = new Object[len];

					rememberObject(array);

					for (int i = 0; i < len; i++) {
						Object item = readAMF3Object();
						Array.set(array, i, item);
					}
				}
			} else {
				for (int i = 0; i < len; i++) {
					Object item = readAMF3Object();
					map.put(Integer.toString(i), item);
				}
			}
			return array;
		}
	}

}
