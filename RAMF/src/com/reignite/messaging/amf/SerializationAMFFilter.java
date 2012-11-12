/**
 * 
 */
package com.reignite.messaging.amf;

import com.reignite.codec.MessageDeserializer;
import com.reignite.codec.MessageSerializer;
import com.reignite.logging.LogWriter;
import com.reignite.messaging.server.AMFServiceContext;

/**
 * Deserializes the request and serializes the response.
 * 
 * @author Surrey
 * 
 */
public class SerializationAMFFilter extends BaseAMFFilter {

	/**
	 * 
	 * @see com.reignite.messaging.amf.AMFFilter#invoke(com.reignite.messaging.server.AMFServiceContext)
	 */
	@Override
	public void invoke(AMFServiceContext context) {
		MessageDeserializer deserializer = context.locateDeserializer();
		context.setResponseMessage(new AMFMessage());
		AMFMessage message = null;
		try {
			message = (AMFMessage) deserializer.readMessage();
			context.getResponseMessage().setVersion(message.getVersion());
		} catch (Exception e) {
			LogWriter.error(getClass(), "Failed to deserialize message: " + e, e);
			context.getResponseMessage().setVersion(context.getErrorVersion());
			AMFMessageBody errorBody = new AMFMessageBody();
			ErrorMessage errorMessage = new ErrorMessage("Failed to deserialize message: " + e, "", "", null);
			errorBody.setData(errorMessage);
			errorBody.setResponseURI(AMFMessageBody.STATUS_METHOD);
			errorBody.setTargetURI(errorBody.getResponseURI());
			context.setResponseBody(errorBody);
		}

		context.setRequestMessage(message);
		if (message != null) {
			nextFilter(context);
		}

		AMFMessage response = context.getResponseMessage();
		MessageSerializer serializer = context.locateSerializer();
		try {
			serializer.writeMessage(response);
		} catch (Exception e) {
			LogWriter.error(getClass(), "Failed to serialize response: " + e, e);

			AMFMessageBody errorBody = new AMFMessageBody();
			ErrorMessage errorMessage = new ErrorMessage("Failed to serialize message: " + e, "", "", null);
			errorBody.setData(errorMessage);
			context.setResponseBody(errorBody);
			response = new AMFMessage();
			response.getBodies().add(errorBody);
			errorBody.setResponseURI(AMFMessageBody.STATUS_METHOD);
			errorBody.setTargetURI(errorBody.getResponseURI());
			try {
				serializer.writeMessage(response);
			} catch (Exception e1) {
				LogWriter.error(getClass(), "Could not serialize error message: " + e1, e1);
				throw new RuntimeException("Could not serialize error message", e1);
			}
		}
	}

}
