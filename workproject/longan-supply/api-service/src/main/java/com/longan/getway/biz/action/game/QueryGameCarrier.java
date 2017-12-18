package com.longan.getway.biz.action.game;

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
import com.longan.getway.biz.common.request.QueryGameCarrierRequest;
import com.longan.getway.biz.common.response.QueryGameCarrierResponse;
import com.longan.getway.biz.common.response.Response;
import com.longan.getway.biz.service.CommonDealService;

@Controller
@RequestMapping("game/queryGameCarrier")
public class QueryGameCarrier extends BaseController {
	@Resource
	private Validator validator;

	@Resource
	private CommonDealService commonDealService;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	Response onResut(
			@ModelAttribute("queryGameCarrierRequest") QueryGameCarrierRequest queryGameCarrierRequest,
			BindingResult bindingResult, HttpServletRequest request) {
		// check param
		logger.warn("接受: ip : " + super.getRemoteIp(request) + " 的游戏运营商查询请求 : "
				+ queryGameCarrierRequest);
		validator.validate(queryGameCarrierRequest, bindingResult);
		if (bindingResult.hasErrors()) {
			return getErrorResponse(bindingResult);
		}

		// check sign
		Response checkSignResponse = commonDealService.checkSign(queryGameCarrierRequest);
		if (!checkSignResponse.isSuccess()) {
			logger.warn("返回: " + checkSignResponse);
			return checkSignResponse;
		}

		QueryGameCarrierResponse queryGameCarrierResponse = commonDealService
				.queryGameCarrier(queryGameCarrierRequest);
		logger.warn("返回: " + queryGameCarrierResponse);
		return queryGameCarrierResponse;

	}
}
