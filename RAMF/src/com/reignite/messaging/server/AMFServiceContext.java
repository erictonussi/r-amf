package com.reignite.messaging.server;

import java.io.InputStream;
import java.io.OutputStream;

import com.reignite.codec.MessageDeserializer;
import com.reignite.codec.MessageSerializer;
import com.reignite.messaging.amf.AMFMessage;
import com.reignite.messaging.amf.AMFMessageBody;

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
 * 
 * A visitor that keeps track of a service request process through an
 * AMFEndpoint and filterchain.
 * 
 * @author Surrey
 * 
 */
public class AMFServiceContext {

	private InputStream in;
	private OutputStream originalOut;
	private RAMFServer server;
	private AMFMessage inMessage;
	private AMFMessage outMessage;
	private int bodyInProcess = 0;
	private Endpoint endpoint;

	public AMFServiceContext(Endpoint endpoint, RAMFServer server, InputStream in, OutputStream out) {
		this.server = server;
		this.endpoint = endpoint;
		this.in = in;
		this.originalOut = out;
	}

	/**
	 * @return a MessageDeserializer
	 */
	public MessageDeserializer locateDeserializer() {
		MessageDeserializer deserializer = server.locateDeserializer(in);
		return deserializer;
	}

	/**
	 * @param message
	 *            the inbound request.
	 */
	public void setRequestMessage(AMFMessage message) {
		inMessage = message;
	}

	public AMFMessage getResponseMessage() {
		return outMessage;
	}

	/**
	 * @return a MessageSerializer.
	 */
	public MessageSerializer locateSerializer() {
		return server.locateSerializer(originalOut, outMessage);
	}

	/**
	 * @return the request message
	 */
	public AMFMessage getRequestMessage() {
		return inMessage;
	}

	/**
	 * Sets the AMFMessageBody currently being processed by the server
	 * 
	 * @param body
	 */
	public void setBodyInProcess(int index) {
		this.bodyInProcess = index;
	}

	/**
	 * @return the message body currently being processed.
	 */
	public AMFMessageBody getRequestBody() {
		return inMessage.getBodies().get(bodyInProcess);
	}

	public void setResponseMessage(AMFMessage responseMessage) {
		this.outMessage = responseMessage;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	/**
	 * @param responseBody
	 *            the body to set in the out message.
	 */
	public void setResponseBody(AMFMessageBody responseBody) {
		if (outMessage == null) {
			outMessage = new AMFMessage();
			outMessage.setVersion(inMessage.getVersion());
		}
		outMessage.getBodies().add(bodyInProcess, responseBody);
	}

	/**
	 * @return the version number for error messages
	 */
	public int getErrorVersion() {
		return server.getErrorVersion();
	}

}
