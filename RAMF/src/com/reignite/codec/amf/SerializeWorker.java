package com.reignite.codec.amf;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;

import com.reignite.messaging.MessagingAliasRegistry;
import com.reignite.messaging.MessagingProxyRegistry;
import com.reignite.messaging.amf.AMFMessage;

/**
 * Does the serialization work
 * 
 * @author Surrey
 * 
 */
public interface SerializeWorker extends ObjectOutput {

	/**
	 * @param message
	 *            the message to check.
	 * @return true if this worker will work with this message.
	 */
	boolean worksWith(AMFMessage message);

	/**
	 * @return the output stream used by this worker
	 */
	OutputStream getOutputStream();

	/**
	 * @param out
	 *            the OutputStream to set.
	 */
	void setOutputStream(OutputStream out);

	/**
	 * Resets the object reference count.
	 */
	void reset();

	/**
	 * @param aliasRegistry
	 *            the MessagingAliasRegistry to include for this worker.
	 */
	void setAliasRegistry(MessagingAliasRegistry aliasRegistry);

	/**
	 * @param proxyRegistry
	 *            the MessagingProxyRegistry for this worker.
	 */
	void setProxyRegistry(MessagingProxyRegistry proxyRegistry);

	/**
	 * AMF 0/3 write unknonwn length always. A complete waste of 4 bytes.
	 * disregard in AMFR.
	 * 
	 * @param unknownContentLength
	 * @throws IOException
	 */
	void writeMessageLength(int unknownContentLength) throws IOException;

}
