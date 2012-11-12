package com.reignite.codec;

import java.io.IOException;

import com.reignite.exception.MessageIncompatibleException;

/**
 * Deserialises a message.
 * 
 * @author Surrey
 * 
 */
public interface MessageDeserializer {

	public abstract Object readMessage() throws ClassNotFoundException, IOException, MessageIncompatibleException;

}
