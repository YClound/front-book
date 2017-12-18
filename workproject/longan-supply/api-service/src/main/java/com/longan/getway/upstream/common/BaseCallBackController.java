package com.longan.getway.upstream.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.core.BizOrderService;
import com.longan.biz.core.SupplyOrderService;
import com.longan.getway.core.BizDealService;

public class BaseCallBackController {
	public Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	public BizDealService bizDealService;

	@Resource
	public BizOrderService bizOrderService;

	@Resource
	public SupplyOrderService supplyOrderService;

	protected static final ExecutorService callbackExecutor = Executors.newFixedThreadPool(32);

	public void logCallBack(HttpServletRequest request, String callbackName) {
		@SuppressWarnings("unchecked")
		Map<String, String[]> map = (Map<String, String[]>) request.getParameterMap();
		StringBuffer sb = new StringBuffer("");
		for (Entry<String, String[]> entry : map.entrySet()) {
			sb.append(entry.getKey()).append(":").append(entry.getValue()[0]).append(" ");
		}
		logger.warn("收到 ip : " + getRemoteIp(request) + " " + callbackName + "的结果通知请求: "
				+ sb.toString());
	}

	public String getRemoteIp(HttpServletRequest request) {
		String result = request.getHeader("X-Forwarded-For");
		if (StringUtils.isEmpty(result)) {
			result = request.getRemoteAddr();
		}
		return result;
	}

	public String getRequestBody(HttpServletRequest request) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),
				"UTF-8"));
		StringBuffer sb = new StringBuffer();
		String readStr = br.readLine();
		while (readStr != null) {
			sb.append(readStr);
			readStr = br.readLine();
		}
		return sb.toString();
	}

	public void responseStr(HttpServletResponse response, String responseBody) {
		response.setContentType("text/html;charset=UTF-8");
		try {
			response.getWriter().write(responseBody);
			response.getWriter().flush();
		} catch (IOException e) {
			logger.error("responseStr error", e);
		}
	}
}
