/**
 * 
 */
package com.reignite.messaging.amf;

import java.lang.reflect.Array;

import com.reignite.messaging.server.AMFServiceContext;

/**
 * Each inbound message could have multiple message payloads (RemotingMessages).
 * This filter breaks each message out and passes it on for processing.
 * 
 * @author Surrey
 * 
 */
public class BatchAMFFilter extends BaseAMFFilter {

	/**
	 * @see com.reignite.messaging.amf.AMFFilter#invoke(com.reignite.messaging.server.AMFServiceContext)
	 */
	@Override
	public void invoke(AMFServiceContext context) {
		AMFMessage inMessage = context.getRequestMessage();
		if (inMessage.getBodies() != null) {
			int bodyIndex = 0;
			for (AMFMessageBody body : inMessage.getBodies()) {
				Object data = body.getData();
				// AMFMessageBody data for RPC is array of RemotingMessage when
				// coming from Flash / Flex or compliant client
				if (data.getClass().isArray()) {
					data = Array.get(data, 0);
					body.setData(data);
				}
				context.setBodyInProcess(bodyIndex);
				nextFilter(context);
				bodyIndex++;
			}
		}
	}

}
