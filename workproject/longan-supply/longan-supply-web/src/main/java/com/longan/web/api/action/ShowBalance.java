package com.longan.web.api.action;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.core.AcctService;
import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.web.api.response.AjaxResponse;
import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.common.controller.BaseController;

@Controller
public class ShowBalance extends BaseController {
	@Resource
	private AcctService acctService;

	@RequestMapping(value = "api/showBalance", method = RequestMethod.GET)
	public @ResponseBody
	AjaxResponse queryBalance(HttpServletRequest request) {
		LoginUserInfo loginUserInfo = super.getUserInfo(request);
		UserInfo userInfo = loginUserInfo.getUserInfo();
		// 账户信息
		Result<AcctInfo> acctInfoResult = acctService.getAcctInfo(userInfo.getAcctId());
		AjaxResponse response = new AjaxResponse();
		if (!acctInfoResult.isSuccess()) {
			response.setErrorMsg(acctInfoResult.getResultMsg());
			return response;
		} else {
			AcctInfo acctInfo = acctInfoResult.getModule();
			if (acctInfo == null) {
				response.setErrorMsg("账户信息为空");
				return response;
			}
			if (StringUtils.isEmpty(acctInfo.getBalanceDesc())) {
				response.setErrorMsg("余额为空");
				return response;
			}
			response.setSuccess();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("balance", acctInfo.getBalanceDesc());
			response.setModule(map);
			return response;
		}
	}
}
