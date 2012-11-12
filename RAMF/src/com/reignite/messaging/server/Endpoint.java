package com.reignite.messaging.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.reignite.exception.MessagingException;
import com.reignite.messaging.MessageBodyData;

/**
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
