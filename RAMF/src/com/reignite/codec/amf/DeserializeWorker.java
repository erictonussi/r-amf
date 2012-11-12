package com.reignite.codec.amf;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;

import com.reignite.messaging.MessagingAliasRegistry;
import com.reignite.messaging.MessagingProxyRegistry;
import com.reignite.messaging.amf.AMFMessage;

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
 * Does the deserialize work
 * 
 * @author Surrey
 * 
 */
public interface DeserializeWorker extends ObjectInput {
	/**
	 * @param message
	 *            the message to check.
	 * @return true if this worker will work with this message.
	 */
	boolean worksWith(AMFMessage message);

	/**
	 * @return the input stream used by this worker
	 */
	InputStream getInputStream();

	/**
	 * @param in
	 *            the InputStream to set.
	 */
	void setInputStream(InputStream in);

	/**
	 * Resets the object reference count.
	 */
	void reset();

	/**
	 * @param aliasRegistry
	 *            the MessagingAliasRegistry for this worker
	 */
	void setAliasRegistry(MessagingAliasRegistry aliasRegistry);

	/**
	 * @param proxyRegistry
	 *            the MessagingProxyRegistry for this worker.
	 */
	void setProxyRegistry(MessagingProxyRegistry proxyRegistry);

	/**
	 * Reads the message length. AMF0/3 implementation of remoting write -1 as
	 * undefined length so this is redundant and just uses an extra 4 bytes we
	 * don't need.
	 * 
	 * @return int
	 * 
	 * @throws IOException
	 */
	int readMessageLength() throws IOException;

}
