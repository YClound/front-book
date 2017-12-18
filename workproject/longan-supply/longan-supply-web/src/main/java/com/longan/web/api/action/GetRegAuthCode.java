package com.longan.web.api.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.domain.Result;
import com.longan.biz.utils.Utils;
import com.longan.web.api.response.AjaxResponse;
import com.longan.web.biz.LoginService;
import com.longan.web.biz.RegService;
import com.longan.web.biz.domain.LoginToken;
import com.longan.web.utils.Constants;

@Controller
public class GetRegAuthCode {
	@Resource
	private LoginService loginService;

	@Resource
	private RegService regService;

	@RequestMapping(value = "api/getRegAuthCode", method = RequestMethod.GET)
	public @ResponseBody
	AjaxResponse request(
			@CookieValue(value = Constants.Cookie.KEY_TT_UID, defaultValue = "") String ttUid,
			HttpServletRequest request, HttpServletResponse response, String token) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		LoginToken loginToken = loginService.getTokenAndUpdate(ttUid, response);
		if (loginToken == null || loginToken.getToken().equals(token)) {
			ajaxResponse.setErrorMsg("密钥校验失败");
			return ajaxResponse;
		}

		String phone = request.getParameter("loginId");
		if (StringUtils.isBlank(phone)) {
			ajaxResponse.setErrorMsg("用户名不能为空");
			return ajaxResponse;
		}

		if (!Utils.isPhone(phone)) {
			ajaxResponse.setErrorMsg("用户名必须是手机号");
			return ajaxResponse;
		}

		// 查询用户是否存在
		Result<Boolean> isUserExistResult = regService.isUserExist(phone);
		if (isUserExistResult.isSuccess()) {
			ajaxResponse.setErrorMsg("用户已存在");
			return ajaxResponse;
		}

		Result<Boolean> sendRegMsgResult = regService.sendRegMsg(phone, loginToken);
		if (!sendRegMsgResult.isSuccess()) {
			ajaxResponse.setErrorMsg(sendRegMsgResult.getResultMsg());
			return ajaxResponse;
		}

		ajaxResponse.setSuccess();
		ajaxResponse.setModule(loginToken);
		return ajaxResponse;
	}
}
