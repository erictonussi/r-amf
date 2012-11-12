package com.reignite.codec.amf;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import com.reignite.messaging.MessagingAliasRegistry;
import com.reignite.messaging.MessagingProxyRegistry;
import com.reignite.messaging.amf.AMFTypes;
import com.reignite.messaging.proxy.MessagingProxy;

public abstract class BaseSerializeWorker implements SerializeWorker, AMFTypes {

	private DataOutputStream out;

	private Map<String, Integer> strings = new HashMap<String, Integer>();
	private Map<MessagingProxy<?>, Integer> proxies = new HashMap<MessagingProxy<?>, Integer>();
	private Map<Object, Integer> objects = new IdentityHashMap<Object, Integer>();
	private MessagingProxyRegistry proxyRegistry;
	private MessagingAliasRegistry aliasRegistry;

	public BaseSerializeWorker() {
		super();
	}

	@Override
	public void reset() {
		objects.clear();
		proxies.clear();
		strings.clear();
	}

	protected <T> MessagingProxy<T> getProxy(T obj) {
		return proxyRegistry.getProxy(obj);
	}

	/**
	 * @throws IOException
	 * @see com.reignite.codec.amf.SerializeWorker#writeUTF(java.lang.String)
	 */
	@Override
	public void writeUTF(String string) throws IOException {
		out.writeUTF(string);
	}

	/**
	 * @throws IOException
	 * @see com.reignite.codec.amf.SerializeWorker#writeInt(int)
	 */
	@Override
	public void writeInt(int value) throws IOException {
		out.writeInt(value);
	}

	@Override
	public void writeShort(int value) throws IOException {
		out.writeShort(value);
	}

	/**
	 * @see com.reignite.codec.amf.SerializeWorker#getOutputStream()
	 */
	@Override
	public OutputStream getOutputStream() {
		return out;
	}

	@Override
	public void setOutputStream(OutputStream out) {
		if (out instanceof DataOutputStream) {
			this.out = (DataOutputStream) out;
		} else {
			this.out = new DataOutputStream(out);
		}
		reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.reignite.codec.amf.SerializeWorker#writeBoolean(boolean)
	 */
	@Override
	public void writeBoolean(boolean value) throws IOException {
		out.writeBoolean(value);
	}

	@Override
	public void writeDouble(double value) throws IOException {
		out.writeDouble(value);
	}

	protected void writeUInt29(int num) throws IOException {
		// Represent smaller integers with fewer bytes using the most
		// significant bit of each byte. The worst case uses 32-bits
		// to represent a 29-bit number, which is what we would have
		// done with no compression.

		// 0x00000000 - 0x0000007F : 0xxxxxxx
		// 0x00000080 - 0x00003FFF : 1xxxxxxx 0xxxxxxx
		// 0x00004000 - 0x001FFFFF : 1xxxxxxx 1xxxxxxx 0xxxxxxx
		// 0x00200000 - 0x3FFFFFFF : 1xxxxxxx 1xxxxxxx 1xxxxxxx xxxxxxxx
		// 0x40000000 - 0xFFFFFFFF : throw range exception
		if (num < 0x80) {
			// 0x00000000 - 0x0000007F : 0xxxxxxx
			out.writeByte(num);
		} else if (num < 0x4000) {
			// 0x00000080 - 0x00003FFF : 1xxxxxxx 0xxxxxxx
			out.writeByte(((num >> 7) & 0x7F) | 0x80);
			out.writeByte(num & 0x7F);

		} else if (num < 0x200000) {
			// 0x00004000 - 0x001FFFFF : 1xxxxxxx 1xxxxxxx 0xxxxxxx
			out.writeByte(((num >> 14) & 0x7F) | 0x80);
			out.writeByte(((num >> 7) & 0x7F) | 0x80);
			out.writeByte(num & 0x7F);

		} else if (num < 0x40000000) {
			// 0x00200000 - 0x3FFFFFFF : 1xxxxxxx 1xxxxxxx 1xxxxxxx xxxxxxxx
			out.writeByte(((num >> 22) & 0x7F) | 0x80);
			out.writeByte(((num >> 15) & 0x7F) | 0x80);
			out.writeByte(((num >> 8) & 0x7F) | 0x80);
			out.writeByte(num & 0xFF);

		} else {
			// 0x40000000 - 0xFFFFFFFF : throw range exception
			throw new IOException("Integer out of range: " + num);
		}
	}

	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		out.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
	}

	@Override
	public void flush() throws IOException {
		out.flush();
	}

	@Override
	public void close() throws IOException {
		out.close();
	}

	@Override
	public void writeByte(int v) throws IOException {
		out.writeByte(v);
	}

	@Override
	public void writeChar(int v) throws IOException {
		out.writeChar(v);
	}

	@Override
	public void writeLong(long v) throws IOException {
		out.writeLong(v);
	}

	@Override
	public void writeFloat(float v) throws IOException {
		out.writeFloat(v);
	}

	@Override
	public void writeBytes(String s) throws IOException {
		out.writeBytes(s);
	}

	@Override
	public void writeChars(String s) throws IOException {
		out.writeChars(s);
	}

	protected <T> boolean byReference(MessagingProxy<T> proxy) throws IOException {
		Integer ref = proxies.get(proxy);

		if (ref != null) {
			try {
				int refNum = ref.intValue();

				writeUInt29((refNum << 2) | 1);

			} catch (ClassCastException e) {
				throw new IOException("Proxy reference is not an Integer");
			}
		} else {
			proxies.put(proxy, new Integer(proxies.size()));
		}

		return (ref != null);
	}

	protected boolean byReference(Object o) throws IOException {
		Integer ref = objects.get(o);

		if (ref != null) {
			writeUInt29(ref << 1);
		} else {
			objects.put(o, new Integer(objects.size()));
		}

		return (ref != null);
	}

	protected boolean byReference(String s) throws IOException {
		Integer ref = strings.get(s);

		if (ref != null) {
			writeUInt29(ref << 1);
		} else {
			strings.put(s, new Integer(strings.size()));
		}

		return (ref != null);
	}

	/**
	 * @param proxyRegistry
	 *            the proxyRegistry to set
	 */
	@Override
	public void setProxyRegistry(MessagingProxyRegistry proxyRegistry) {
		this.proxyRegistry = proxyRegistry;
	}

	/**
	 * @param aliasRegistry
	 *            the aliasRegistry to set
	 */
	@Override
	public void setAliasRegistry(MessagingAliasRegistry aliasRegistry) {
		this.aliasRegistry = aliasRegistry;
	}

	protected Class<?> getAlias(String toAlias) throws ClassNotFoundException {
		return aliasRegistry.getAlias(toAlias);
	}

	protected void writeReferencedUTFString(String s) throws IOException {
		if (s.length() == 0) {
			writeUInt29(1);
		} else {
			if (!byReference(s)) {
				byte[] utf8 = s.getBytes("UTF-8");
				int utflen = utf8.length;

				writeUInt29((utflen << 1) | 1);
				write(utf8, 0, utflen);
			}
		}
	}
}