package com.reignite.codec;

import java.io.InputStream;
import java.io.OutputStream;

import com.reignite.messaging.amf.AMFMessage;

/**
 * Locates MessageDeserializers and MessageSerializers for a RAMFServer.
 * 
 * @author Surrey
 * 
 */
public interface CodecLocator {

	/**
	 * Locates a MessageDeserializer
	 * 
	 * @param in
	 *            The input stream that will be deserialized.
	 * @return A MessageDeserializer.
	 */
	MessageDeserializer locateDeserializer(InputStream in);

	/**
	 * Locates a MessageSerializer
	 * 
	 * @param out
	 *            the output stream the MessageSerializer is expected to write
	 *            to.
	 * @param outMessage
	 *            the message to be serialized.
	 * @return A MessageSerializer.
	 */
	MessageSerializer locateSerializer(OutputStream out, AMFMessage outMessage);

}
