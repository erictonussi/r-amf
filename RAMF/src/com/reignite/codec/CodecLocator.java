package com.reignite.codec;

import java.io.InputStream;
import java.io.OutputStream;

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
 * 
 * Locates MessageDeserializers and MessageSerializers for a RAMFServer.
 * 
 * @author Surrey
 * 
 */
public interface CodecLocator {

	/**
	 * Locates a MessageDeserializer
	 * 
	 * @param in
	 *            The input stream that will be deserialized.
	 * @return A MessageDeserializer.
	 */
	MessageDeserializer locateDeserializer(InputStream in);

	/**
	 * Locates a MessageSerializer
	 * 
	 * @param out
	 *            the output stream the MessageSerializer is expected to write
	 *            to.
	 * @param outMessage
	 *            the message to be serialized.
	 * @return A MessageSerializer.
	 */
	MessageSerializer locateSerializer(OutputStream out, AMFMessage outMessage);

}
