package com.reignite.messaging.server;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
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
