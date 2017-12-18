package com.longan.web.common.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.common.secure.CSRFTokenManager;
import com.longan.web.utils.Constants;

public class GlobleInterceptor implements HandlerInterceptor {
	@Resource
	private CSRFTokenManager csrfTokenManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView modelAndView) throws Exception {
		// ajex 调用没有modelAndView是空
		if (modelAndView != null) {
			// modelAndView.addObject(Constants.CONTEXT_PATH_KEY,
			// Constants.CONTEXT_PATH);
			// modelAndView.addObject(Constants.ASSETS_SERVER_KEY,
			// Constants.ASSETS_SERVER);
			if (request.getAttribute(Constants.SESSION_USER) != null
					&& request.getAttribute(Constants.SESSION_USER) instanceof LoginUserInfo) {
				LoginUserInfo loginUserInfo = (LoginUserInfo) request
						.getAttribute(Constants.SESSION_USER);
				if (loginUserInfo != null && loginUserInfo.getUserInfo() != null) {
					modelAndView.addObject(Constants.SESSION_USER, loginUserInfo.getUserInfo());

					// csrf 模块
					csrfTokenManager.createToken(request, modelAndView, loginUserInfo.getUserInfo()
							.getId());
				}
			}

		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) throws Exception {
		// doNothing
	}

}
