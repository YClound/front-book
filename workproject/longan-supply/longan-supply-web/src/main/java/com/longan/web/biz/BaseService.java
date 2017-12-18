package com.longan.web.biz;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseService {
	public Logger logger = LoggerFactory.getLogger(this.getClass());

	public String getRemoteIp(HttpServletRequest request) {
		String result = request.getHeader("X-Forwarded-For");
		if (StringUtils.isEmpty(result)) {
			result = request.getRemoteAddr();
		}
		return result;
	}

	public String getCookieValue(String cookieName, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				if (null != c.getName() && c.getName().equals(cookieName)) {
					return c.getValue();
				}
			}
		}
		return null;
	}

	public void addCookieLikeSession(String cookieKey, String cookieValue,
			HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieKey, cookieValue);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	public void addCookie(String cookieKey, String cookieValue, int exp,
			HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieKey, cookieValue);
		cookie.setPath("/");
		cookie.setMaxAge(exp);
		response.addCookie(cookie);
	}

	public void removeCookie(String cookieName, HttpServletRequest request,
			HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				if (null != c.getName() && c.getName().equals(cookieName)) {
					c.setMaxAge(0);
					c.setPath("/");
					response.addCookie(c);
					break;
				}
			}
		}
	}

	public void clearCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				c.setPath("/");
				c.setMaxAge(0);
				response.addCookie(c);
			}
		}
	}
}
