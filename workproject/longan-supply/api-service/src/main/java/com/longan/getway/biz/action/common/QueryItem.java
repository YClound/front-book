package com.longan.getway.biz.action.common;

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
import com.longan.getway.biz.common.request.QueryItemRequest;
import com.longan.getway.biz.common.response.QueryItemResponse;
import com.longan.getway.biz.common.response.Response;
import com.longan.getway.biz.service.CommonDealService;

@Controller
@RequestMapping("common/queryItem")
public class QueryItem extends BaseController {
	@Resource
	private Validator validator;

	@Resource
	private CommonDealService commonDealService;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	Response onResut(@ModelAttribute("queryItemRequest") QueryItemRequest queryItemRequest,
			BindingResult bindingResult, HttpServletRequest request) {
		// check param
		logger.warn("接受: ip : " + super.getRemoteIp(request) + " 的查询商品请求 : " + queryItemRequest);
		validator.validate(queryItemRequest, bindingResult);
		if (bindingResult.hasErrors()) {
			return getErrorResponse(bindingResult);
		}

		// check sign
		Response checkSignResponse = commonDealService.checkSign(queryItemRequest);
		if (!checkSignResponse.isSuccess()) {
			logger.warn("返回: " + checkSignResponse);
			return checkSignResponse;
		}

		QueryItemResponse queryItemResponse = commonDealService.queryItem(queryItemRequest);
		logger.warn("返回: " + queryItemResponse);
		return queryItemResponse;

	}
}
