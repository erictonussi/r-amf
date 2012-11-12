package com.reignite.codec.amf;

import java.io.IOException;
import java.io.OutputStream;

import com.reignite.codec.MessageSerializer;
import com.reignite.exception.MessageIncompatibleException;
import com.reignite.exception.MessagingException;
import com.reignite.logging.LogWriter;
import com.reignite.messaging.amf.AMFTypes;
import com.reignite.messaging.amf.AMFMessage;
import com.reignite.messaging.amf.AMFMessageBody;
import com.reignite.messaging.amf.AMFMessageHeader;

/**
 * An AMF 3 serializer
 * 
 * @author Surrey
 * 
 */
public class AMFMessageSerializer implements MessageSerializer, AMFTypes {

	private SerializeWorker worker;

	public AMFMessageSerializer(SerializeWorker worker) {
		this.worker = worker;
	}

	@Override
	public void writeMessage(AMFMessage message) throws MessagingException, IOException {
		if (worker.worksWith(message)) {
			try {
				worker.writeShort(message.getVersion());
				worker.writeShort(message.getHeaders().size());
				for (AMFMessageHeader header : message.getHeaders()) {
					writeHeader(header);
				}
				worker.writeShort(message.getBodies().size());
				for (AMFMessageBody body : message.getBodies()) {
					writeBody(body);
				}
			} catch (IOException e) {
				LogWriter.error(getClass(), "Failed to serialize message: " + message, e);
				throw e;
			}
		} else {
			throw new MessageIncompatibleException("Worker: " + worker.getClass().getCanonicalName()
					+ " can't work with: " + message);
		}
	}

	protected void writeBody(AMFMessageBody body) throws IOException {
		if (body.getTargetURI() == null) {
			worker.writeUTF(NULL_STRING);
		} else {
			worker.writeUTF(body.getTargetURI());
		}

		if (body.getResponseURI() == null) {
			worker.writeUTF(NULL_STRING);
		} else {
			worker.writeUTF(body.getResponseURI());
		}

		worker.writeMessageLength(UNKNOWN_CONTENT_LENGTH);
		worker.reset();

		writeObject(body.getData());
	}

	protected void writeObject(Object data) throws IOException {
		worker.writeObject(data);
	}

	protected void writeHeader(AMFMessageHeader header) throws IOException {
		worker.writeUTF(header.getName());
		worker.writeBoolean(header.getMustUnderstand());
		worker.writeMessageLength(UNKNOWN_CONTENT_LENGTH);
		worker.reset();
		writeObject(header.getData());
	}

	public OutputStream getOutputStream() {
		return worker.getOutputStream();
	}
}
