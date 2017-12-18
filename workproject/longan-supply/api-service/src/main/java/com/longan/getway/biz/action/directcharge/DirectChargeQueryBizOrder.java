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
import com.longan.getway.biz.common.request.QueryBizOrderRequest;
import com.longan.getway.biz.common.response.QueryBizOrderResponse;
import com.longan.getway.biz.common.response.Response;
import com.longan.getway.biz.service.CommonDealService;

@Controller
@RequestMapping("directCharge/queryBizOrder")
public class DirectChargeQueryBizOrder extends BaseController {
	@Resource
	private CommonDealService commonDealService;
	@Resource
	private Validator validator;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	Response onResut(
			@ModelAttribute("queryBizOrderRequest") QueryBizOrderRequest queryBizOrderRequest,
			BindingResult bindingResult, HttpServletRequest request) {
		// check param
		logger.warn("接受: ip : " + super.getRemoteIp(request) + " 的查询订单信息请求 : "
				+ queryBizOrderRequest);
		validator.validate(queryBizOrderRequest, bindingResult);
		if (bindingResult.hasErrors()) {
			return getErrorResponse(bindingResult);
		}

		// check sign
		Response checkSignResponse = commonDealService.checkSign(queryBizOrderRequest);
		if (!checkSignResponse.isSuccess()) {
			logger.warn("返回: " + checkSignResponse);
			return checkSignResponse;
		}

		// service
		QueryBizOrderResponse response = commonDealService.queryResponse(queryBizOrderRequest);
		logger.warn("返回: " + response);
		// return
		return response;
	}
}
