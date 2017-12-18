package com.longan.getway.biz.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.longan.biz.utils.Constants;
import com.longan.getway.biz.common.response.Response;

public class BaseController {
	public Logger logger = LoggerFactory.getLogger(this.getClass());

	public Response getErrorResponse(BindingResult bindingResult) {
		Response result = new Response();
		result.setCode(Constants.ErrorCode.CODE_ERROR_VALIDATE);

		StringBuffer sb = new StringBuffer();
		for (ObjectError objectError : bindingResult.getAllErrors()) {
			sb.append(objectError.getDefaultMessage()).append(",");
		}
		result.setDesc(sb.toString());
		logger.warn("返回: " + result);
		return result;
	}
	
	public String getRemoteIp(HttpServletRequest request) {
		String result = request.getHeader("X-Forwarded-For");
		if (StringUtils.isEmpty(result)) {
			result = request.getRemoteAddr();
		}

		return result;

	}
	
}
