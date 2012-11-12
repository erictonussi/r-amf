package com.reignite.codec.amf;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.reignite.messaging.amf.AMFMessage;
import com.reignite.messaging.proxy.MessagingProxy;

public class AMFRSerializeWorker extends BaseSerializeWorker {

	@Override
	public boolean worksWith(AMFMessage message) {
		return message.getVersion() == 99;
	}
	
	@Override
	public void writeMessageLength(int unknownContentLength) throws IOException {
		// do nothing, save the bandwidth.
	}

	@SuppressWarnings("unchecked")
	@Override
	public void writeObject(Object obj) throws IOException {
		if (obj == null) {
			write(AMFR_NULL_TYPE);
			return;
		}

		if (obj instanceof String || obj instanceof Character) {
			String s = obj.toString();
			write(AMFR_STRING_TYPE);
			writeReferencedUTFString(s);
		} else if (obj instanceof Number) {
			if (obj instanceof Integer || obj instanceof Short || obj instanceof Byte) {
				int i = ((Number) obj).intValue();
				writeAMFInt(i);
			} else {
				double d = ((Number) obj).doubleValue();
				write(AMFR_DOUBLE_TYPE);
				writeDouble(d);
			}
		} else if (obj instanceof Boolean) {
			write(((Boolean) obj) ? AMFR_TRUE_TYPE : AMFR_FALSE_TYPE);
		} else if (obj instanceof Date) {
			Date d = (Date) obj;
			writeAMFRDate(d);
		} else if (obj instanceof Calendar) {
			writeAMFRDate(((Calendar) obj).getTime());
		} else {
			// we don't deal with XML Documents because
			// they are just strings anyway so use them that way.
			// We have an Object or Array type...
			Class<?> cls = obj.getClass();

			if (obj instanceof Map) {
				writeMap((Map<Object, Object>) obj);
			} else if (obj instanceof List) {
				writeCollection(AMFR_LIST_TYPE, (List<Object>) obj);
			} else if (obj instanceof Set) {
				writeCollection(AMFR_SET_TYPE, (Set<Object>) obj);
			} else if (cls.isArray()) {
				writeArray(obj, cls.getComponentType());
			} else {
				writeCustomObject(obj);
			}
		}
	}
	
	protected void writeCustomObject(Object o) throws IOException {
		write(AMFR_OBJECT_TYPE);
		if (!byReference(o)) {
			MessagingProxy<Object> proxy = getProxy(o);

			writeTraits(proxy);
			if (proxy.isExternalizable()) {
				proxy.writeExternal(this);
			} else if (proxy.getProperties() != null) {
				for (String propName : proxy.getProperties()) {
					Object value = proxy.getValue(propName);
					writeObject(value);
				}
			}
		}
	}
	
	protected <T> void writeTraits(MessagingProxy<T> proxy) throws IOException {
		if (!byReference(proxy)) {
			int count = 0;
			boolean externalizable = proxy.isExternalizable();

			if (!externalizable) {
				if (proxy.getProperties() != null) {
					count = proxy.getProperties().size();
				}
			}

			boolean dynamic = false; // java centric, dynamic classes are less
										// than ideal

			writeUInt29(3 | (externalizable ? 4 : 0) | (dynamic ? 8 : 0) | (count << 4));
			writeReferencedUTFString(proxy.getProxyClassName(true));

			if (!externalizable && proxy.getProperties() != null) {
				for (String propName : proxy.getProperties()) {
					writeReferencedUTFString(propName);
				}
			}
		}
	}

	
	protected void writeArray(Object o, Class<?> componentType) throws IOException {
		if (componentType.isPrimitive()) {
			writePrimitiveArray(o);
		} else if (componentType.equals(Byte.class)) {
			writeByteArray((Byte[]) o);
		} else if (componentType.equals(Character.class)) {
			write(AMFR_STRING_TYPE);
			char[] ca = new char[Array.getLength(o)];
			Character[] cha = (Character[]) o;
			for (int i = 0; i < ca.length; i++) {
				ca[i] = cha[i] == null ? 0 : cha[i];
			}
			writeReferencedUTFString(new String(ca));
		} else {
			writeArray((Object[]) o);
		}
	}

	protected void writeByteArray(Byte[] ba) throws IOException {
		write(AMFR_BYTEARRAY_TYPE);
		if (!byReference(ba)) {
			int length = ba.length;
			writeUInt29((length << 1) | 1);
			for (Byte b : ba) {
				if (b == null) {
					writeByte(0);
				} else {
					writeByte(b.byteValue());
				}
			}
		}
	}

	protected void writeByteArray(byte[] ba) throws IOException {
		write(AMFR_BYTEARRAY_TYPE);

		if (!byReference(ba)) {
			int length = ba.length;
			writeUInt29((length << 1) | 1);
			write(ba, 0, length);
		}
	}

	protected void writePrimitiveArray(Object obj) throws IOException {
		Class<?> aType = obj.getClass().getComponentType();

		if (aType.equals(Character.TYPE)) {
			String newString = new String((char[]) obj);
			write(AMFR_STRING_TYPE);
			writeReferencedUTFString(newString);
		} else if (aType.equals(Byte.TYPE)) {
			writeByteArray((byte[]) obj);
		} else {
			write(AMFR_ARRAY_TYPE);

			int length = Array.getLength(obj);
			if (!byReference(obj)) {
				if (aType.equals(Boolean.TYPE)) {
					boolean[] ba = (boolean[]) obj;
					writeUInt29((length << 1) | 1);

					for (boolean b : ba) {
						write(b ? AMFR_TRUE_TYPE : AMFR_FALSE_TYPE);
					}
				} else if (aType.equals(Integer.TYPE) || aType.equals(Short.TYPE)) {
					writeUInt29((length << 1) | 1);
					int[] ia = (int[]) obj;
					for (int i : ia) {
						writeAMFInt(i);
					}
				} else {
					// We write all of these as doubles...
					writeUInt29((length << 1) | 1);
					double[] da = (double[]) obj;
					for (double d : da) {
						write(AMFR_DOUBLE_TYPE);
						writeDouble(d);
					}
				}
			}
		}
	}

	protected void writeCollection(int type, Collection<Object> list) throws IOException {
		write(type);
		if (!byReference(list)) {
			writeUInt29((list.size() << 1) | 1);
			if (list.size() > 0) {
				for (Object object : list) {
					writeObject(object);
				}
			}
		}
	}

	protected void writeArray(Object[] array) throws IOException {
		write(AMFR_ARRAY_TYPE);
		if (!byReference(array)) {
			writeUInt29((array.length << 1) | 1);
			if (array.length > 0) {
				for (Object object : array) {
					writeObject(object);
				}
			}
		}
	}

	protected void writeMap(Map<Object, Object> map) throws IOException {
		write(AMFR_MAP_TYPE);
		if (!byReference(map)) {
			writeUInt29((map.size() << 1) | 1);
			if (map.size() > 0) {
				for (Object key : map.keySet()) {
					if (key != null) {
						writeObject(key);
					} else {
						writeObject(EMPTY_STRING);
					}
					writeObject(map.get(key));
				}
				write(AMFR_NULL_TYPE);
			}
		}

	}

	private void writeAMFRDate(Date d) throws IOException {
		write(AMFR_DATE_TYPE);

		if (!byReference(d)) {
			writeUInt29(1); // not a reference
			writeLong(d.getTime());
		}
	}

	protected void writeAMFInt(int i) throws IOException {
		if (i >= INT28_MIN_VALUE && i <= INT28_MAX_VALUE) {
			i = i & UINT29_MASK; // Mask is 2^29 - 1
			write(AMFR_INTEGER_TYPE);
			writeUInt29(i);
		} else {
			write(AMFR_DOUBLE_TYPE);
			writeDouble(i);
		}
	}

}
