package com.longan.getway.biz.action.directcharge;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.getway.biz.common.BaseController;
import com.longan.getway.biz.common.request.QueryBalanceRequest;
import com.longan.getway.biz.common.response.QueryBalanceResponse;
import com.longan.getway.biz.common.response.Response;
import com.longan.getway.biz.service.CommonDealService;

@Controller
@RequestMapping("directCharge/queryBalance")
public class DirectChargeQueryBalance extends BaseController {
	@Resource
	private Validator validator;

	@Resource
	private CommonDealService commonDealService;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	Response onResut(
			@ModelAttribute("queryBalanceRequest") QueryBalanceRequest queryBalanceRequest,
			BindingResult bindingResult, HttpServletRequest request) {
		// check param
		logger.warn("接受: ip : " + super.getRemoteIp(request) + " 的查询余额请求 : " + queryBalanceRequest);
		validator.validate(queryBalanceRequest, bindingResult);
		if (bindingResult.hasErrors()) {
			return getErrorResponse(bindingResult);
		}

		// check sign
		Response checkSignResponse = commonDealService.checkSign(queryBalanceRequest);
		if (!checkSignResponse.isSuccess()) {
			logger.warn("返回: " + checkSignResponse);
			return checkSignResponse;
		}

		QueryBalanceResponse queryBalanceResponse = commonDealService
				.queryBalance(queryBalanceRequest);
		logger.warn("返回: " + queryBalanceResponse);
		return queryBalanceResponse;

	}
}
