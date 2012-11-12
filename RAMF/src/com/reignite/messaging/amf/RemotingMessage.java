package com.reignite.messaging.amf;

import com.reignite.messaging.amfr.RemoteResponseMessage;


/**
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
