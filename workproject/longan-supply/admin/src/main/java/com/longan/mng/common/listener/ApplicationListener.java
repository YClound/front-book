package com.longan.mng.common.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.longan.mng.utils.Constants;

public class ApplicationListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		Object staticServer = context.getAttribute(Constants.STATIC_SERVER_KEY);
		if (null == staticServer)
			context.setAttribute(Constants.STATIC_SERVER_KEY, Constants.STATIC_SERVER);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
