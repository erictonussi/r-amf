package com.reignite.messaging.amf;

import com.reignite.exception.MessagingException;
import com.reignite.logging.LogWriter;
import com.reignite.messaging.amfr.RemotingMessage;
import com.reignite.messaging.server.AMFServiceContext;
import com.reignite.messaging.server.Endpoint;

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
 * Last filter in the default endpoint chain. This filter calls all next filters
 * before routing the message to the server. That means any filters after this
 * one will not receive the response.
 * 
 * @author Surrey
 * 
 */
public class RAMFServerAMFFilter extends BaseAMFFilter {

	@Override
	public void invoke(AMFServiceContext context) {
		nextFilter(context);

		AMFMessageBody responseBody = new AMFMessageBody();
		context.setResponseBody(responseBody);

		AMFMessageBody messageBody = context.getRequestBody();

		Endpoint endpoint = context.getEndpoint();

		RemotingMessage remotingMessage = (RemotingMessage) messageBody.getData();
		try {
			responseBody.setData(endpoint.routeMessageToService(remotingMessage));
		} catch (MessagingException e) {
			LogWriter.error(getClass(), "Failed to route message to service: " + e, e);
			ErrorMessage errorBody = new ErrorMessage("Service destination could not be reached",
					remotingMessage.getDestination(), remotingMessage.getOperation(), remotingMessage.getParameters());
			responseBody.setData(errorBody);
		}
		responseBody.setResponseURI(messageBody.getResponseURI());
		if (responseBody.getData() instanceof ErrorMessage) {
			responseBody.setResponseURI(responseBody.getResponseURI() + AMFMessageBody.STATUS_METHOD);
		} else {
			responseBody.setResponseURI(responseBody.getResponseURI() + AMFMessageBody.RESULT_METHOD);
		}
		responseBody.setTargetURI(responseBody.getResponseURI());
	}
}
