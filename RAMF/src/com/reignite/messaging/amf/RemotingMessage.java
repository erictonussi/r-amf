package com.reignite.messaging.amf;

import com.reignite.messaging.amfr.RemoteResponseMessage;


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
 * A remote method invocation request. Usually the data in an AMFMessageBody
 * 
 * clientId, destination, messageId, timestamp, timeToLive, headers
 * 
 * @author Surrey
 * 
 */
public class RemotingMessage extends com.reignite.messaging.amfr.RemotingMessage {

	private static final long serialVersionUID = -4242347429758789638L;

	private String source;
	private String remoteUsername;
	private String remotePassword;
	protected Object clientId;
	protected String messageId;
	protected long timestamp;
	protected long timeToLive;

	@Override
	public RemoteResponseMessage createResponse() {
		return new AcknowledgeMessage();
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String s) {
		source = s;
	}

	/**
	 * @return the remoteUsername
	 */
	public String getRemoteUsername() {
		return remoteUsername;
	}

	/**
	 * @param remoteUsername
	 *            the remoteUsername to set
	 */
	public void setRemoteUsername(String remoteUsername) {
		this.remoteUsername = remoteUsername;
	}

	/**
	 * @return the remotePassword
	 */
	public String getRemotePassword() {
		return remotePassword;
	}

	/**
	 * @param remotePassword
	 *            the remotePassword to set
	 */
	public void setRemotePassword(String remotePassword) {
		this.remotePassword = remotePassword;
	}

	/**
	 * @return the clientId
	 */
	public Object getClientId() {
		return clientId;
	}

	/**
	 * @param clientId
	 *            the clientId to set
	 */
	public void setClientId(Object clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * @param destination
	 *            the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId
	 *            the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the timeToLive
	 */
	public long getTimeToLive() {
		return timeToLive;
	}

	/**
	 * @param timeToLive
	 *            the timeToLive to set
	 */
	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}
}
