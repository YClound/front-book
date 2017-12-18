package com.longan.web.common.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.longan.biz.domain.Result;
import com.longan.web.api.response.AjaxResponse;
import com.longan.web.biz.LoginService;
import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.utils.Constants;

public class LoginFilter extends OncePerRequestFilter {

	Logger logger = LoggerFactory.getLogger(LoginFilter.class);

	@Resource
	private LoginService loginService;

	public static boolean isAjaxRequest(HttpServletRequest request) {
		String path = request.getRequestURI();
		String contextPath = request.getContextPath();
		path = path.replace(contextPath, "");
		return path.startsWith("/api");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			throw new ServletException("OncePerRequestFilter just supports HTTP requests");
		}

		if (isOpenPage(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		Result<LoginUserInfo> checkLoginResult = loginService.checkLogin(response, request);
		if (!checkLoginResult.isSuccess() || checkLoginResult.getModule() == null) {
			if (StringUtils.isNotEmpty(checkLoginResult.getResultMsg())) {
				//TODO
				throw new ServletException(checkLoginResult.getResultMsg());
			}
			noLoginRedirect(response, request);
			return;
		}

		LoginUserInfo loginUserInfo = checkLoginResult.getModule();

		// 保持登录状态
		loginService.keepUserLogin(loginUserInfo, request);

		// 设置用户信息至 request 方便之后用
		request.setAttribute(Constants.SESSION_USER, loginUserInfo);

		filterChain.doFilter(request, response);
		return;

	}

	private void noLoginRedirect(HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		boolean isAjaxRequest = isAjaxRequest(request);

		if (isAjaxRequest) {
			setNoLoginResponse(response);
			return;
		}
		redirectIndex(response, request);
		return;
	}

	private void redirectIndex(HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		PrintWriter out = response.getWriter();
		String url = request.getContextPath() + "/index.htm";
		out.println("<script language=\"javascript\">");
		out.println("top.location=\"" + url + "\";");
		out.println("</script>");
	}

	private static boolean isOpenPage(HttpServletRequest request) {
		String url = StringUtils.substringAfter(request.getRequestURI(), request.getContextPath());
		return Constants.NO_NEED_LOGIN_URL_SET.contains(url);
	}

	private static void setNoLoginResponse(HttpServletResponse response) throws IOException {
		// response.sendError(HttpStatus.UNAUTHORIZED.value(), "您的权限不够");
		AjaxResponse ajaxResponse = new AjaxResponse();
		ajaxResponse.setErrorMsg("您未登录，或太长时间没有操作,请重新登录");
		responseStr(response, ajaxResponse.toString());
	}

	public static void responseStr(HttpServletResponse response, String responseBody)
			throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(responseBody);
		response.getWriter().flush();
	}

}
