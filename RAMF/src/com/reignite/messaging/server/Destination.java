/**
 * 
 */
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
 * A wrapper around a set of service calls identified by a name.
 * 
 * @author Surrey
 * 
 */
public interface Destination {

	/**
	 * @return the name
	 */
	String getName();

	/**
	 * @param name
	 *            the name to set.
	 */
	void setName(String name);

	/**
	 * Gets the service call from this destination with the given name and set
	 * of parameters.
	 * 
	 * @param operation
	 *            the operation name to distinguish the call.
	 * @param params
	 *            the parameters to be understood by the Service.
	 * @return a constructed and ready to invoke service.
	 */
	Service getService(String operation, Object[] params);

}
