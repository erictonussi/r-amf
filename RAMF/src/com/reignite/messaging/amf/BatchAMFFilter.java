/**
 * 
 */
package com.reignite.messaging.amf;

import java.lang.reflect.Array;

import com.reignite.messaging.server.AMFServiceContext;

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
