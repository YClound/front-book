package com.longan.web.api.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.domain.Result;
import com.longan.web.api.response.AjaxResponse;
import com.longan.web.biz.RechargeService;
import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.common.controller.BaseController;

@Controller
public class PhoneQuery extends BaseController {
	@Resource
	private RechargeService rechargeService;

	@RequestMapping("api/phoneQuery")
	public @ResponseBody
	AjaxResponse query(@RequestParam("uid") String uid, HttpServletRequest request) {
		LoginUserInfo loginUserInfo = super.getUserInfo(request);
		AjaxResponse ajaxResponse = new AjaxResponse();
		AcctInfo acctInfo = localCachedService.getAcctInfoNot4Trade(loginUserInfo.getUserInfo()
				.getAcctId());
		if (acctInfo == null) {
			return ajaxResponse;
		}
		Result<com.longan.client.remote.domain.web.PhoneQuery> result = rechargeService
				.getItemList(uid, acctInfo);
		if (!result.isSuccess()) {
			ajaxResponse.setErrorMsg(result.getResultMsg());
			return ajaxResponse;
		}

		ajaxResponse.setSuccess();
		ajaxResponse.setModule(result.getModule());

		return ajaxResponse;
	}
}
