package com.reignite.codec.amf;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;

import com.reignite.messaging.MessagingAliasRegistry;
import com.reignite.messaging.MessagingProxyRegistry;
import com.reignite.messaging.amf.AMFMessage;

/**
 * Does the deserialize work
 * 
 * @author Surrey
 * 
 */
public interface DeserializeWorker extends ObjectInput {
	/**
	 * @param message
	 *            the message to check.
	 * @return true if this worker will work with this message.
	 */
	boolean worksWith(AMFMessage message);

	/**
	 * @return the input stream used by this worker
	 */
	InputStream getInputStream();

	/**
	 * @param in
	 *            the InputStream to set.
	 */
	void setInputStream(InputStream in);

	/**
	 * Resets the object reference count.
	 */
	void reset();

	/**
	 * @param aliasRegistry
	 *            the MessagingAliasRegistry for this worker
	 */
	void setAliasRegistry(MessagingAliasRegistry aliasRegistry);

	/**
	 * @param proxyRegistry
	 *            the MessagingProxyRegistry for this worker.
	 */
	void setProxyRegistry(MessagingProxyRegistry proxyRegistry);

	/**
	 * Reads the message length. AMF0/3 implementation of remoting write -1 as
	 * undefined length so this is redundant and just uses an extra 4 bytes we
	 * don't need.
	 * 
	 * @return int
	 * 
	 * @throws IOException
	 */
	int readMessageLength() throws IOException;

}
