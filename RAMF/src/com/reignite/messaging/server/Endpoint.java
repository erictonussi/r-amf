package com.reignite.messaging.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.reignite.exception.MessagingException;
import com.reignite.messaging.MessageBodyData;

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
 * An endpoint is a collection of services to expose to RAMF / AMF clients.
 * 
 * @author Surrey
 * 
 */
public interface Endpoint {

	/**
	 * Reads the messages from the input stream then writes to the output
	 * stream. This method operates synchronously. That is it blocks until the
	 * message is serviced and a response sent.
	 * 
	 * @param in
	 *            the input stream to read the message from.
	 * @param out
	 *            the output stream to write the answer (service response or
	 *            error message) to.
	 */
	void service(InputStream in, OutputStream out);

	/**
	 * Takes the given data and passes it to an appropriate service. In the case
	 * of AMF it takes a RemotingMessage and returns an AcknowlegeMessage
	 * 
	 * @param data
	 *            the data to route to the service.
	 * @return the service response.
	 * @throws MessagingException 
	 */
	MessageBodyData routeMessageToService(MessageBodyData data) throws MessagingException;

	/**
	 * @return the destination map
	 */
	Map<String, Destination> getDestinationMap();

	/**
	 * @param destinationMap
	 *            the mapping of destination names to Destination
	 */
	void setDestinationMap(Map<String, Destination> destinationMap);
}
