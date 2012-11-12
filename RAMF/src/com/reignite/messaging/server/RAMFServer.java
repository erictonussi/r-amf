package com.reignite.messaging.server;

import java.io.InputStream;
import java.io.OutputStream;

import com.reignite.codec.CodecLocator;
import com.reignite.codec.MessageDeserializer;
import com.reignite.codec.MessageSerializer;
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
 * Configured through bean getters and setters to allow maximum flexibility.
 * 
 * @author Surrey
 * 
 */
public class RAMFServer {

	private EndpointLocator endpointLocator;
	private CodecLocator codecLocator;
	private Integer errorVersion = 99; // default to AMFR

	/**
	 * Gets the appropriate end point. An endpoint is a collection of services
	 * that are available within a given context. The one server may service
	 * multiple applications and each application may have numerous endpoints.
	 * Each endpoint context may in turn be subdivided to discriminate.
	 * 
	 * For example in a web context the application context would be the web
	 * app. In this case there would usually only be one application, but the
	 * same RAMFServer may be accessible to multiple web apps. eg:
	 * applicationContext = /myWebApp endpointContext = /RAMF endpointInfo =
	 * /amf
	 * 
	 * @param applicationContext
	 *            the application to search. The one server may provide
	 *            endpoints for multiple applications.
	 * @param endpointContext
	 *            the context within the application to search.
	 * @param endpointInfo
	 *            Any additional qualifiers to the endpoint context.
	 * @return an Endpoint or null if none found.
	 */
	public Endpoint getEndpoint(String applicationContext, String endpointContext, String endpointInfo) {
		return endpointLocator.getEndpoint(this, applicationContext, endpointContext, endpointInfo);
	}

	/**
	 * @return the endpointLocator
	 */
	public EndpointLocator getEndpointLocator() {
		return endpointLocator;
	}

	/**
	 * @param endpointLocator
	 *            the endpointLocator to set
	 */
	public void setEndpointLocator(EndpointLocator endpointLocator) {
		this.endpointLocator = endpointLocator;
	}

	/**
	 * Delegates to a CodecLocator.
	 * 
	 * @param in
	 *            The input stream that will be deserialized. The locator may
	 *            use this to determine what sort of deserializer is needed.
	 * 
	 * @return a MessageDeserializer.
	 */
	public MessageDeserializer locateDeserializer(InputStream in) {
		return codecLocator.locateDeserializer(in);
	}

	/**
	 * @return the codecLocator
	 */
	public CodecLocator getCodecLocator() {
		return codecLocator;
	}

	/**
	 * @param codecLocator
	 *            the codecLocator to set
	 */
	public void setCodecLocator(CodecLocator codecLocator) {
		this.codecLocator = codecLocator;
	}

	public MessageSerializer locateSerializer(OutputStream out, AMFMessage outMessage) {
		return codecLocator.locateSerializer(out, outMessage);
	}

	/**
	 * @return the default version for error messages
	 */
	public Integer getErrorVersion() { 
		return errorVersion;
	}

	/**
	 * @param errorVersion the errorVersion to set
	 */
	public void setErrorVersion(Integer errorVersion) {
		this.errorVersion = errorVersion;
	}

}
