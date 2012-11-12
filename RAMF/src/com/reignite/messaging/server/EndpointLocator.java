package com.reignite.messaging.server;

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
 * Locates an enpoint.
 * 
 * @author Surrey
 * 
 */
public interface EndpointLocator {

	/**
	 * Uses the applicationContext, endpointContext and endpointInfo to find and
	 * return an endpoint.
	 * 
	 * @param server
	 *            the RAMFServer requesting the endpoint.
	 * @param applicationContext
	 *            the application to locate an endpoint for. In the case of a
	 *            web application this is likely the application context path.
	 * @param endpointContext
	 *            the endpoint context to locate an endpoint in. In the case of
	 *            a web application this is likely the servlet path.
	 * @param endpointInfo
	 *            the endpoint info used to discriminate the endpoint. In the
	 *            case of a web application this is likely the servlet info
	 *            path.
	 * @return
	 */
	Endpoint getEndpoint(RAMFServer server, String applicationContext, String endpointContext, String endpointInfo);

}
