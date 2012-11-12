package com.reignite.codec.amf;

import java.io.IOException;

import com.reignite.codec.MessageDeserializer;
import com.reignite.exception.MessageIncompatibleException;
import com.reignite.messaging.amf.AMFTypes;
import com.reignite.messaging.amf.AMFMessage;
import com.reignite.messaging.amf.AMFMessageBody;
import com.reignite.messaging.amf.AMFMessageHeader;

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
 * Deserializes AMF messages.
 * 
 * @author Surrey
 * 
 */
public class AMFMessageDeserializer implements MessageDeserializer, AMFTypes {

	private DeserializeWorker worker;

	public AMFMessageDeserializer(DeserializeWorker worker) {
		this.worker = worker;
	}

	@Override
	public Object readMessage() throws ClassNotFoundException, IOException, MessageIncompatibleException {
		// Read packet header
		AMFMessage message = new AMFMessage();
		int version = worker.readUnsignedShort();
		message.setVersion(version);

		if (worker.worksWith(message)) {
			// Read headers
			int headerCount = worker.readUnsignedShort();
			for (int i = 0; i < headerCount; i++) {
				message.getHeaders().add(readHeader());
			}

			// Read bodies
			int bodyCount = worker.readUnsignedShort();
			for (int i = 0; i < bodyCount; i++) {
				message.getBodies().add(readBody());
			}
		} else {
			throw new MessageIncompatibleException("Worker: " + worker.getClass().getCanonicalName()
					+ " can't work with: " + message);
		}
		return message;
	}

	public AMFMessageHeader readHeader() throws ClassNotFoundException, IOException {
		AMFMessageHeader header = new AMFMessageHeader();
		String name = worker.readUTF();
		header.setName(name);
		boolean mustUnderstand = worker.readBoolean();
		header.setMustUnderstand(mustUnderstand);

		worker.readMessageLength();
		worker.reset();
		Object data = readObject();

		header.setData(data);
		return header;
	}

	public AMFMessageBody readBody() throws ClassNotFoundException, IOException {
		AMFMessageBody body = new AMFMessageBody();
		String targetURI = worker.readUTF();
		body.setTargetURI(targetURI);
		String responseURI = worker.readUTF();
		body.setResponseURI(responseURI);

		worker.readMessageLength();

		worker.reset();
		Object data = readObject();

		body.setData(data); //in amf3 data is RemotingMessage[1]
		return body;
	}

	public Object readObject() throws ClassNotFoundException, IOException {
		return worker.readObject();
	}
}
