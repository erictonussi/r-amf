package com.reignite.codec;

import java.io.IOException;

import com.reignite.exception.MessagingException;
import com.reignite.messaging.amf.AMFMessage;

/**
 * Serializes a message.
 * 
 * @author Surrey
 * 
 */
public interface MessageSerializer {

	/**
	 * Writes the given message to some underlying output.
	 * 
	 * @param message
	 *            the message to serialize.
	 * @throws MessagingException
	 * @throws IOException
	 */
	public abstract void writeMessage(AMFMessage message) throws MessagingException, IOException;

}
