package com.longan.web.common.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.longan.web.utils.Constants;

public class ApplicationListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		Object staticServer = context.getAttribute(Constants.ASSETS_SERVER_KEY);
		if (null == staticServer)
			context.setAttribute(Constants.ASSETS_SERVER_KEY, Constants.ASSETS_SERVER);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
