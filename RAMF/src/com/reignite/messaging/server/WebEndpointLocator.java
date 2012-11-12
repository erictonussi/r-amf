/**
 * 
 */
package com.reignite.messaging.server;

import java.util.Map;

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
 * A basic EndpointLocator that assumes it is operating in a web container and
 * the given context strings are the servlet context, path and info.
 * 
 * @author Surrey
 * 
 */
public class WebEndpointLocator implements EndpointLocator {

	private Map<String, Endpoint> endpointMap;

	/**
	 * @see com.reignite.messaging.server.EndpointLocator#getEndpoint(com.reignite.messaging.server.RAMFServer,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Endpoint getEndpoint(RAMFServer server, String applicationContext, String endpointContext,
			String endpointInfo) {
		String key = endpointContext + (endpointInfo != null ? endpointInfo : "");
		Endpoint endpoint = endpointMap.get(key);

		return endpoint;
	}

	/**
	 * @return the endpointMap
	 */
	public Map<String, Endpoint> getEndpointMap() {
		return endpointMap;
	}

	/**
	 * @param endpointMap
	 *            the endpointMap to set
	 */
	public void setEndpointMap(Map<String, Endpoint> endpointMap) {
		this.endpointMap = endpointMap;
	}

}
