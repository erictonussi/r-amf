/**
 * 
 */
package com.reignite.codec.amf;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import com.reignite.exception.SerializeException;
import com.reignite.logging.LogWriter;
import com.reignite.messaging.amf.AMFMessage;
import com.reignite.messaging.proxy.MessagingProxy;

import flex.messaging.io.ArrayCollection;

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
public class AMF3SerializeWorker extends BaseSerializeWorker {

	private boolean legacyBigNumbers = false;

	private boolean legacyMap = false;
	private boolean legacyCollection = true;

	/**
	 * @see com.reignite.codec.amf.SerializeWorker#worksWith(com.reignite.messaging.amf.AMFMessage)
	 */
	@Override
	public boolean worksWith(AMFMessage message) {
		return (message.getVersion() == 3 || message.getVersion() == 0);
	}

	@Override
	public void writeMessageLength(int unknownContentLength) throws IOException {
		writeInt(unknownContentLength);
	}

	/**
	 * @throws IOException
	 * @see com.reignite.codec.amf.SerializeWorker#writeObject(java.lang.Object)
	 */
	@Override
	public void writeObject(Object data) throws IOException {
		// delegate to amf0 until we hit amf3 object
		if (data == null) {
			write(AMF0_NULL_TYPE);
		} else {
			if (data instanceof String) {
				writeAMF0String((String) data);
			} else if (data instanceof Number) {
				if (!legacyBigNumbers && (data instanceof BigInteger || data instanceof BigDecimal)) {
					writeUTF(((Number) data).toString());
				} else {
					write(AMF0_NUMBER_TYPE);
					writeDouble(((Number) data).doubleValue());
				}
			} else if (data instanceof Boolean) {
				write(AMF0_BOOLEAN_TYPE);
				writeBoolean(((Boolean) data).booleanValue());
			} else if (data instanceof Character) {
				String s = data.toString();
				writeAMF0String(s);
			} else if (data instanceof Date) {
				Date d = (Date) data;
				writeAMF0Date(d);
			} else if (data instanceof Calendar) {
				writeAMF0Date(((Calendar) data).getTime());
			} else {
				write(AMF0_AVMPLUSOBJECT_TYPE);
				try {
					writeAMF3Object(data);
				} catch (ClassNotFoundException e) {
					LogWriter.error(getClass(), "Failed to serialize : " + e, e);
					throw new SerializeException("Couldn't serialize to AMF3: " + e, e);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void writeAMF3Object(Object o) throws IOException, ClassNotFoundException {
		if (o == null) {
			write(AMF3_NULL_TYPE);
			return;
		}

		if (o instanceof String || o instanceof Character) {
			String s = o.toString();
			write(AMF3_STRING_TYPE);
			writeReferencedUTFString(s);
		} else if (o instanceof Number) {
			if (o instanceof Integer || o instanceof Short || o instanceof Byte) {
				int i = ((Number) o).intValue();
				writeAMFInt(i);
			} else if (!legacyBigNumbers && (o instanceof BigInteger || o instanceof BigDecimal)) {
				writeUTF(((Number) o).toString());
			} else {
				double d = ((Number) o).doubleValue();
				write(AMF3_DOUBLE_TYPE);
				writeDouble(d);
			}
		} else if (o instanceof Boolean) {
			write(((Boolean) o) ? AMF3_TRUE_TYPE : AMF3_FALSE_TYPE);
		} else if (o instanceof Date) {
			Date d = (Date) o;
			writeAMF3Date(d);
		} else if (o instanceof Calendar) {
			writeAMF3Date(((Calendar) o).getTime());
		} else {
			// we don't deal with XML Documents because
			// they are just strings anyway so use them that way.
			// We have an Object or Array type...
			// we also don't deal with ASObjects because that is not a good way
			// to write RMI interfaces
			Class<?> cls = o.getClass();

			if (legacyMap && o instanceof Map) {
				writeMapAsECMAArray((Map<?, ?>) o);
			} else if (o instanceof Collection) {
				if (legacyCollection) {
					writeCollection((Collection<Object>) o);
				} else {
					writeArrayCollection((Collection<Object>) o);
				}
			} else if (cls.isArray()) {
				writeAMFArray(o, cls.getComponentType());
			} else {
				// Don't bother with rowsets or throwables.
				// Seriously? You want to remote a throwable through AMF?
				writeCustomObject(o);
			}
		}
	}

	protected void writeCustomObject(Object o) throws IOException, ClassNotFoundException {
		write(AMF3_OBJECT_TYPE);
		if (!byReference(o)) {
			MessagingProxy<Object> proxy = getProxy(o);

			writeProxy(proxy);
		}
	}

	protected void writeAMFArray(Object o, Class<?> componentType) throws IOException, ClassNotFoundException {
		if (componentType.isPrimitive()) {
			writePrimitiveArray(o);
		} else if (componentType.equals(Byte.class)) {
			writeAMFByteArray((Byte[]) o);
		} else if (componentType.equals(Character.class)) {
			write(AMF3_STRING_TYPE);
			char[] ca = new char[Array.getLength(o)];
			Character[] cha = (Character[]) o;
			for (int i = 0; i < ca.length; i++) {
				ca[i] = cha[i] == null ? 0 : cha[i];
			}
			writeReferencedUTFString(new String(ca));
		} else {
			writeObjectArray((Object[]) o);
		}
	}

	protected void writeObjectArray(Object[] values) throws IOException, ClassNotFoundException {
		write(AMF3_ARRAY_TYPE);

		if (!byReference(values)) {

			writeUInt29((values.length << 1) | 1);
			// Send an empty string to imply no named keys
			writeReferencedUTFString(EMPTY_STRING);

			for (Object item : values) {
				writeAMF3Object(item);
			}
		}
	}

	protected void writePrimitiveArray(Object obj) throws IOException {
		Class<?> aType = obj.getClass().getComponentType();

		if (aType.equals(Character.TYPE)) {
			String newString = new String((char[]) obj);
			write(AMF3_STRING_TYPE);
			writeReferencedUTFString(newString);
		} else if (aType.equals(Byte.TYPE)) {
			writeAMFByteArray((byte[]) obj);
		} else {
			write(AMF3_ARRAY_TYPE);

			int length = Array.getLength(obj);
			if (!byReference(obj)) {
				if (aType.equals(Boolean.TYPE)) {
					boolean[] ba = (boolean[]) obj;
					writeUInt29((length << 1) | 1);
					// Send an empty string to imply no named keys
					writeReferencedUTFString(EMPTY_STRING);

					for (boolean b : ba) {
						write(b ? AMF3_TRUE_TYPE : AMF3_FALSE_TYPE);
					}
				} else if (aType.equals(Integer.TYPE) || aType.equals(Short.TYPE)) {
					writeUInt29((length << 1) | 1);
					// Send an empty string to imply no named keys
					writeReferencedUTFString(EMPTY_STRING);
					int[] ia = (int[]) obj;
					for (int i : ia) {
						writeAMFInt(i);
					}
				} else {
					// We write all of these as doubles...
					writeUInt29((length << 1) | 1);
					// Send an empty string to imply no named keys
					writeReferencedUTFString(EMPTY_STRING);
					double[] da = (double[]) obj;
					for (double d : da) {
						write(AMF3_DOUBLE_TYPE);
						writeDouble(d);
					}
				}
			}
		}
	}

	protected void writeAMFByteArray(Byte[] ba) throws IOException {
		write(AMF3_BYTEARRAY_TYPE);
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

	protected void writeAMFByteArray(byte[] ba) throws IOException {
		write(AMF3_BYTEARRAY_TYPE);

		if (!byReference(ba)) {
			int length = ba.length;
			writeUInt29((length << 1) | 1);
			write(ba, 0, length);
		}
	}

	/**
	 * Specifically to support flex 3+ that has ArrayCollections.
	 * 
	 * @param col
	 * @param desc
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	protected void writeArrayCollection(Collection<Object> col) throws IOException, ClassNotFoundException {
		write(AMF3_OBJECT_TYPE);

		if (!byReference(col)) {
			ArrayCollection ac;

			if (col instanceof ArrayCollection) {
				ac = (ArrayCollection) col;
			} else {
				// Wrap any Collection in an ArrayCollection
				ac = new ArrayCollection(col);
			}

			MessagingProxy<ArrayCollection> proxy = getProxy(ac);
			writeProxy(proxy);
		}
	}

	/**
	 * More or less every custom object ends up here.
	 * 
	 * @param <T>
	 * @param proxy
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	protected <T> void writeProxy(MessagingProxy<T> proxy) throws IOException, ClassNotFoundException {
		writeTraits(proxy);
		if (proxy.isExternalizable()) {
			proxy.writeExternal(this);
		} else if (proxy.getProperties() != null) {
			for (String propName : proxy.getProperties()) {
				Object value = proxy.getValue(propName);
				writeAMF3Object(value);
			}
		}
	}

	public <T> void writeTraits(MessagingProxy<T> proxy) throws IOException {
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

	protected void writeCollection(Collection<Object> c) throws IOException, ClassNotFoundException {
		write(AMF3_ARRAY_TYPE);

		if (!byReference(c)) {
			writeUInt29((c.size() << 1) | 1);
			// Send an empty string to imply no named keys
			writeReferencedUTFString(EMPTY_STRING);

			for (Object item : c) {
				writeAMF3Object(item);
			}
		}
	}

	protected void writeMapAsECMAArray(Map<?, ?> map) throws IOException, ClassNotFoundException {
		write(AMF3_ARRAY_TYPE);

		if (!byReference(map)) {
			writeUInt29((0 << 1) | 1);
			for (Object key : map.keySet()) {
				if (key != null) {
					String propName = key.toString();
					writeReferencedUTFString(propName);
					writeAMF3Object(map.get(key));
				}
			}

			writeReferencedUTFString(EMPTY_STRING);
		}
	}

	private void writeAMF3Date(Date d) throws IOException {
		write(AMF3_DATE_TYPE);

		if (!byReference(d)) {
			writeUInt29(1); // not a reference
			writeDouble((double) d.getTime());
		}
	}

	protected void writeAMF0Date(Date d) throws IOException {
		write(AMF0_DATE_TYPE);
		// Write the time as 64bit value in ms
		writeDouble((double) d.getTime());
		int nCurrentTimezoneOffset = TimeZone.getDefault().getRawOffset();
		writeShort(nCurrentTimezoneOffset / 60000);
	}

	private void writeAMF0String(String str) throws IOException {
		byte[] utf8 = str.getBytes("UTF-8");
		int utflen = utf8.length;

		int type = AMF0_STRING_TYPE;
		if (utflen > 65535) {
			type = AMF0_LONGSTRING_TYPE;
		}

		write(type);

		int count = 0;
		byte[] bytearr = new byte[(type == AMF0_STRING_TYPE ? 2 : 4)];
		if (type == AMF0_LONGSTRING_TYPE) {
			bytearr[count++] = (byte) ((utflen >>> 24) & 0xFF);
			bytearr[count++] = (byte) ((utflen >>> 16) & 0xFF);
		}
		bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);
		bytearr[count++] = (byte) ((utflen >>> 0) & 0xFF);
		write(bytearr, 0, bytearr.length);
		write(utf8, 0, utf8.length);
	}

	protected void writeAMFInt(int i) throws IOException {
		if (i >= INT28_MIN_VALUE && i <= INT28_MAX_VALUE) {
			i = i & UINT29_MASK; // Mask is 2^29 - 1
			write(AMF3_INTEGER_TYPE);
			writeUInt29(i);
		} else {
			write(AMF3_DOUBLE_TYPE);
			writeDouble(i);
		}
	}

}
