/**
 * 
 */
package com.reignite.codec;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.reignite.codec.amf.AMFMessageDeserializer;
import com.reignite.codec.amf.AMFMessageSerializer;
import com.reignite.codec.amf.DeserializeWorker;
import com.reignite.codec.amf.SerializeWorker;
import com.reignite.logging.LogWriter;
import com.reignite.messaging.MessagingAliasRegistry;
import com.reignite.messaging.MessagingProxyRegistry;
import com.reignite.messaging.amf.AMFMessage;

/**
 * Locates serializer and deserializer by inspecting the input stream to
 * determine the version of amf being used.
 * 
 * @author Surrey
 * 
 */
public class CodecLocatorImpl implements CodecLocator {

	private Map<Integer, Class<SerializeWorker>> serializeWorkers;
	private Map<Integer, Class<DeserializeWorker>> deserializeWorkers;
	private MessagingProxyRegistry proxyRegistry;
	private MessagingAliasRegistry aliasRegistry;

	/**
	 * @see com.reignite.codec.CodecLocator#locateDeserializer(java.io.InputStream)
	 */
	@Override
	public MessageDeserializer locateDeserializer(InputStream in) {
		MessageDeserializer deserializer = null;
		try {
			DataInputStream di = new DataInputStream(new BufferedInputStream(in));
			di.mark(3);
			int version = di.readUnsignedShort();
			di.reset();
			Class<DeserializeWorker> workerClass = deserializeWorkers.get(version);
			DeserializeWorker worker = workerClass.newInstance();
			worker.setInputStream(di);
			worker.setAliasRegistry(aliasRegistry);
			worker.setProxyRegistry(proxyRegistry);
			
			deserializer = new AMFMessageDeserializer(worker);
		} catch (Exception e) {
			LogWriter.error(getClass(), "Failed to create deserializer: " + e, e);
		}

		return deserializer;
	}

	/**
	 * @see com.reignite.codec.CodecLocator#locateSerializer(java.io.OutputStream,
	 *      com.reignite.messaging.amf.AMFMessage)
	 */
	@Override
	public MessageSerializer locateSerializer(OutputStream out, AMFMessage outMessage) {
		MessageSerializer serializer = null;
		try {
			Class<SerializeWorker> workerClass = serializeWorkers.get(outMessage.getVersion());
			SerializeWorker worker = workerClass.newInstance();
			worker.setOutputStream(out);
			worker.setAliasRegistry(aliasRegistry);
			worker.setProxyRegistry(proxyRegistry);
			serializer = new AMFMessageSerializer(worker);
		} catch (Exception e) {
			LogWriter.error(getClass(), "Failed to create serializer: " + e, e);
		}
		
		return serializer;
	}

	/**
	 * @return the serializeWorkers
	 */
	public Map<Integer, Class<SerializeWorker>> getSerializeWorkers() {
		return serializeWorkers;
	}

	/**
	 * @param serializeWorkers the serializeWorkers to set
	 */
	public void setSerializeWorkers(Map<Integer, Class<SerializeWorker>> serializeWorkers) {
		this.serializeWorkers = serializeWorkers;
	}

	/**
	 * @return the deserializeWorkers
	 */
	public Map<Integer, Class<DeserializeWorker>> getDeserializeWorkers() {
		return deserializeWorkers;
	}

	/**
	 * @param deserializeWorkers the deserializeWorkers to set
	 */
	public void setDeserializeWorkers(Map<Integer, Class<DeserializeWorker>> deserializeWorkers) {
		this.deserializeWorkers = deserializeWorkers;
	}

	/**
	 * @return the proxyRegistry
	 */
	public MessagingProxyRegistry getProxyRegistry() {
		return proxyRegistry;
	}

	/**
	 * @param proxyRegistry the proxyRegistry to set
	 */
	public void setProxyRegistry(MessagingProxyRegistry proxyRegistry) {
		this.proxyRegistry = proxyRegistry;
	}

	/**
	 * @return the aliasRegistry
	 */
	public MessagingAliasRegistry getAliasRegistry() {
		return aliasRegistry;
	}

	/**
	 * @param aliasRegistry the aliasRegistry to set
	 */
	public void setAliasRegistry(MessagingAliasRegistry aliasRegistry) {
		this.aliasRegistry = aliasRegistry;
	}

}
