package com.longan.web.api.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.web.api.response.AjaxResponse;
import com.longan.web.biz.LoginService;
import com.longan.web.biz.domain.LoginToken;
import com.longan.web.utils.Constants;

@Controller
public class GetLoginToken {

	@Resource
	private LoginService loginService;

	@RequestMapping("api/getLoginToken")
	public @ResponseBody
	AjaxResponse request(
			@CookieValue(value = Constants.Cookie.KEY_TT_UID, defaultValue = "") String ttUid,
			HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		LoginToken loginToken = loginService.getTokenAndUpdate(ttUid, response);
		if (loginToken == null) {
			ajaxResponse.setErrorMsg("获取登录Token失败");
			return ajaxResponse;
		}
		ajaxResponse.setSuccess();
		ajaxResponse.setModule(loginToken);
		return ajaxResponse;
	}
}
