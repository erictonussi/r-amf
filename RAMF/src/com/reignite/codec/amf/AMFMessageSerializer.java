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
