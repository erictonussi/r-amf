package com.reignite.messaging.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.reignite.exception.InitializeException;
import com.reignite.logging.LogWriter;

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
 * A servlet implementation of the AMF server.
 * 
 * @author Surrey
 * 
 */
public class MessagingServlet extends HttpServlet {

	private static final String INITIALIZER_NAME = "initializer";

	private static final long serialVersionUID = -8612848535783613340L;

	/**
	 * The server that locates services and routes messages to and from them.
	 */
	private RAMFServer server;

	@Override
	public void init() throws ServletException {
		try {
			// get or create an initializer
			String initializerClassName = getServletConfig().getInitParameter(INITIALIZER_NAME);
			Class<?> initializerClass = Class.forName(initializerClassName);
			Initializer init = (Initializer) initializerClass.newInstance();
			if (init instanceof WebInitializer) {
				server = init.getServer(getServletContext());
			} else {
				throw new InitializeException(
						"You must supply a WebInitializer class name as an init param for MessagingServlet");
			}
		} catch (Exception e) {
			LogWriter.error(getClass(), "Failed to initialize RAMFServer: " + e, e);
		}
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		// set thread locals to provide resources to the filter chains.

		// get the requested endpoint. This server might have different
		// endpoints (collections of services and config).
		String applicationContext = request.getContextPath(); // web app context
																// name
		String endpointInfo = request.getPathInfo(); // everything after the
														// servlet
														// mapping
		String endpointContext = request.getServletPath(); // the servlet
															// mapping

		Endpoint endpoint = server.getEndpoint(applicationContext, endpointContext, endpointInfo);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		endpoint.service(request.getInputStream(), out);
		
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/x-amf; charset=UTF-8");
		out.writeTo(response.getOutputStream());
		response.flushBuffer();
	}
}
