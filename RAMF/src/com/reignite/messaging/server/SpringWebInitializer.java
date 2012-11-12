package com.reignite.messaging.server;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

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
 * Gets a configured RAMFServer from a Spring WebApplicaitonContext.
 * 
 * @author Surrey
 * 
 */
public class SpringWebInitializer extends WebInitializer {

	@Override
	protected RAMFServer getServerFromServletContext(ServletContext servletContext) {
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		String beanName = servletContext.getInitParameter("RAMFServerBeanName");
		if (beanName == null || beanName.trim().length() == 0) {
			beanName = "ramfServer";
		}

		return (RAMFServer) wac.getBean(beanName);
	}

}
