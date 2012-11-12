package com.reignite.messaging.server;

import javax.servlet.ServletContext;

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
 * An initializer that takes a servlet context and configures a RAMF server from
 * it.
 * 
 * @author Surrey
 * 
 */
public abstract class WebInitializer implements Initializer {

	@Override
	public RAMFServer getServer(Object context) {
		ServletContext servletContext = (ServletContext) context;
		return getServerFromServletContext(servletContext);
	}

	/**
	 * WebInitializers extend this method as a convenience.
	 * 
	 * @param servletContext
	 *            the servlet context.
	 * @return a RAMFServer ready to use.
	 */
	protected abstract RAMFServer getServerFromServletContext(ServletContext servletContext);
}
