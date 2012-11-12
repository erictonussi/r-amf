package com.reignite.codec.amf;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.reignite.logging.LogWriter;
import com.reignite.messaging.MessagingAliasRegistry;
import com.reignite.messaging.MessagingProxyRegistry;
import com.reignite.messaging.amf.AMFTypes;
import com.reignite.messaging.proxy.MessagingProxy;

public abstract class BaseDeserializeWorker implements DeserializeWorker, AMFTypes {

	private DataInputStream in = null;

	private List<String> strings = new ArrayList<String>();
	private List<Object> objects = new ArrayList<Object>();
	private List<MessagingProxy<Object>> proxies = new ArrayList<MessagingProxy<Object>>();
	private MessagingProxyRegistry proxyRegistry;
	private MessagingAliasRegistry aliasRegistry;

	@Override
	public void reset() {
		strings.clear();
		objects.clear();
		proxies.clear();
	}

	protected int rememberObject(Object obj) {
		int id = objects.size();
		objects.add(obj);
		return id;
	}

	protected int rememberProxy(MessagingProxy<Object> proxy) {
		int id = proxies.size();
		proxies.add(proxy);
		return id;
	}

	protected int rememberString(String string) {
		int id = strings.size();
		strings.add(string);
		return id;
	}

	protected Object getObject(int ref) {
		return objects.get(ref);
	}

	protected String getString(int ref) {
		return strings.get(ref);
	}

	protected <T> MessagingProxy<T> getProxy(T ob) {
		return proxyRegistry.getProxy(ob);
	}

	protected <T> MessagingProxy<T> getProxy(Class<?> clazz) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		return proxyRegistry.getProxy(clazz);
	}

	protected MessagingProxy<Object> getProxyByReference(int ref) {
		MessagingProxy<Object> proxy = proxies.get(ref);
		MessagingProxy<Object> copy = null;
		if (proxy != null) {
			copy = proxy.copy();
			try {
				copy.setInstance(proxy.getInstance().getClass().newInstance());
				if (proxy.getReadProperties() != null){
					for(String prop : proxy.getReadProperties()){
						copy.addReadProperty(prop);
					}
				}
			} catch (Exception e) {
				LogWriter.error(getClass(),
						"Failed to get proxy by reference: Could not instantiate a new proxied instance.", e);
			}
		}
		return copy;
	}

	protected Class<?> getAlias(String toAlias) throws ClassNotFoundException {
		return aliasRegistry.getAlias(toAlias);
	}

	/**
	 * AMF 3 represents smaller integers with fewer bytes using the most
	 * significant bit of each byte. e would have done with no compression.
	 * <p/>
	 * 0x00000000 - 0x0000007F : 0xxxxxxx 0x00000080 - 0x00003FFF : 1xxxxxxx
	 * 0xxxxxxx 0x00004000 - 0x001FFFFF : 1xxxxxxx 1xxxxxxx 0xxxxxxx 0x00200000
	 * - 0x3FFFFFFF : 1xxxxxxx 1xxxxxxx 1xxxxxxx xxxxxxxx 0x40000000 -
	 * 0xFFFFFFFF : throw range exception
	 * 
	 * @return A int capable of holding an unsigned 29 bit integer.
	 * @throws IOException
	 */
	protected int readUInt29() throws IOException {
		int value;
		// Each byte must be treated as unsigned
		int b = in.readByte() & 0xFF;

		if (b < 128) {
			return b;
		}

		value = (b & 0x7F) << 7;
		b = in.readByte() & 0xFF;

		if (b < 128) {
			return (value | b);
		}

		value = (value | (b & 0x7F)) << 7;
		b = in.readByte() & 0xFF;

		if (b < 128) {
			return (value | b);
		}

		value = (value | (b & 0x7F)) << 8;
		b = in.readByte() & 0xFF;

		return (value | b);
	}

	@Override
	public int read() throws IOException {
		return in.read();
	}

	@Override
	public int read(byte[] b) throws IOException {
		return in.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return in.read(b, off, len);
	}

	@Override
	public long skip(long n) throws IOException {
		return in.skip(n);
	}

	@Override
	public int available() throws IOException {
		return in.available();
	}

	@Override
	public void close() throws IOException {
		in.close();
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		in.readFully(b);
	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		in.readFully(b, off, len);
	}

	@Override
	public int skipBytes(int n) throws IOException {
		return in.skipBytes(n);
	}

	@Override
	public boolean readBoolean() throws IOException {
		return in.readBoolean();
	}

	@Override
	public byte readByte() throws IOException {
		return in.readByte();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return in.readUnsignedByte();
	}

	@Override
	public short readShort() throws IOException {
		return in.readShort();
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return in.readUnsignedShort();
	}

	@Override
	public char readChar() throws IOException {
		return in.readChar();
	}

	@Override
	public int readInt() throws IOException {
		return in.readInt();
	}

	@Override
	public long readLong() throws IOException {
		return in.readLong();
	}

	@Override
	public float readFloat() throws IOException {
		return in.readFloat();
	}

	@Override
	public double readDouble() throws IOException {
		return in.readDouble();
	}

	@Override
	@Deprecated
	public String readLine() throws IOException {
		return in.readLine();
	}

	@Override
	public String readUTF() throws IOException {
		return in.readUTF();
	}

	/**
	 * @return the in
	 */
	public DataInputStream getIn() {
		return in;
	}

	/**
	 * @param in
	 *            the in to set
	 */
	public void setInputStream(InputStream in) {
		if (in instanceof DataInputStream) {
			this.in = (DataInputStream) in;
		} else {
			this.in = new DataInputStream(in);
		}
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

	protected String readString() throws IOException {
		int ref = readUInt29();

		if ((ref & 1) == 0) {
			// This is a reference
			return getString(ref >> 1);
		} else {
			// Read the string in
			int len = (ref >> 1);

			// writeString() special cases the empty string
			// to avoid creating a reference.
			if (0 == len) {
				return EMPTY_STRING;
			}

			String str = readUTF(len);

			rememberString(str);

			return str;
		}
	}

	protected String readUTF(int utflen) throws IOException {
		byte[] bytearr = new byte[utflen];

		readFully(bytearr, 0, utflen);

		String str = new String(bytearr, "UTF-8");
		return str;
	}

	protected MessagingProxy<?> readProxy(int ref) throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		if ((ref & 3) == 1) {
			return getProxyByReference(ref >> 2);
		} else {
			int count = (ref >> 4); /* uint29 */
			String className = readString();
			Class<?> clazz = getAlias(className);
			MessagingProxy<Object> proxy = getProxy(clazz);

			// Remember Trait Info
			rememberProxy(proxy);

			for (int i = 0; i < count; i++) {
				// consume property names
				proxy.addReadProperty(readString());
			}

			return proxy;
		}
	}

}
