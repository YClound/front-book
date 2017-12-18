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
import com.longan.getway.biz.common.request.QueryGameItemRequest;
import com.longan.getway.biz.common.response.QueryGameItemResponse;
import com.longan.getway.biz.common.response.Response;
import com.longan.getway.biz.service.CommonDealService;

@Controller
@RequestMapping("game/queryGameItem")
public class QueryGameItem extends BaseController {
	@Resource
	private Validator validator;

	@Resource
	private CommonDealService commonDealService;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	Response onResut(
			@ModelAttribute("queryGameItemRequest") QueryGameItemRequest queryGameItemRequest,
			BindingResult bindingResult, HttpServletRequest request) {
		// check param
		logger.warn("接受: ip : " + super.getRemoteIp(request) + " 的单个网游商品查询请求 : "
				+ queryGameItemRequest);
		validator.validate(queryGameItemRequest, bindingResult);
		if (bindingResult.hasErrors()) {
			return getErrorResponse(bindingResult);
		}

		// check sign
		Response checkSignResponse = commonDealService.checkSign(queryGameItemRequest);
		if (!checkSignResponse.isSuccess()) {
			logger.warn("返回: " + checkSignResponse);
			return checkSignResponse;
		}

		QueryGameItemResponse queryGameItemResponse = commonDealService
				.queryGameItem(queryGameItemRequest);
		logger.warn("返回: " + queryGameItemResponse);
		return queryGameItemResponse;

	}
}
