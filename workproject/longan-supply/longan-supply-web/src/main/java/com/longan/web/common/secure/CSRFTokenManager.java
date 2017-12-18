package com.longan.web.common.secure;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.spy.memcached.CASValue;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.longan.biz.cached.MemcachedService;

public class CSRFTokenManager {
	Logger logger = LoggerFactory.getLogger(CSRFTokenManager.class);
	@Resource
	private MemcachedService memcachedService;

	private final static String CSRF_TOKEN_KEY = "CSRF_";
	private final static int CSRF_EXP = 5 * 60; // 秒

	public void createToken(HttpServletRequest request, ModelAndView modelAndView, Long userId) {
		if (request.getRequestURL() == null) {
			return;
		}
		String url = request.getRequestURL().toString();
		String tokenKey = getTokenKey(url, userId);
		if (tokenKey == null) {
			return;
		}
		// 设置token
		String token = UUID.randomUUID().toString().replace("-", "");
		memcachedService.set(tokenKey, CSRF_EXP, token);
		modelAndView.addObject("tangtang_token", token);
	}

	public boolean checkToken(HttpServletRequest request, Long userId) {
		String url = StringUtils.substringBefore(request.getHeader("Referer"), "?");

		String token = request.getParameter("tangtang_token");
		if (StringUtils.isEmpty(token)) {
			return false;
		}
		String tokenKey = getTokenKey(url, userId);
		if (tokenKey == null) {
			return false;
		}
		String theToken = null;
		CASValue<Object> casValue = memcachedService.gets(tokenKey);

		if (casValue == null) {
			logger.warn("checkToken error casValue is null");
			return false;
		}
		theToken = (String) casValue.getValue();
		if (StringUtils.isEmpty(theToken)) {
			return false;
		}
		// 原子操作  1秒就过期
		boolean casFlag = memcachedService.cas(tokenKey, casValue.getCas(), 1, "");

		return casFlag && theToken.equals(token);
	}

	private String getTokenKey(String url, Long userId) {
		if (StringUtils.isEmpty(url)) {
			logger.warn("url is null");
			return null;
		}
		try {
			return CSRF_TOKEN_KEY + userId + "_" + URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.warn("encode url error", e);
		}
		return null;
	}

}
