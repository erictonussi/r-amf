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
 * Creates an configures a RAMFServer.
 * 
 * @author Surrey
 * 
 */
public interface Initializer {

	/**
	 * Creates and configures a RAMF server using the given context.
	 * 
	 * @param context
	 *            An object that can be used by the initializer to configure the
	 *            server. For example a servlet context or a properties file.
	 * @return a RAMFServer ready to be used.
	 */
	RAMFServer getServer(Object context);

}
