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
import com.longan.getway.biz.common.request.QueryGameItemListRequest;
import com.longan.getway.biz.common.response.QueryGameItemListResponse;
import com.longan.getway.biz.common.response.Response;
import com.longan.getway.biz.service.CommonDealService;

@Controller
@RequestMapping("game/queryGameItemList")
public class QueryGameItemList extends BaseController {
	@Resource
	private Validator validator;

	@Resource
	private CommonDealService commonDealService;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	Response onResut(
			@ModelAttribute("queryGameItemListRequest") QueryGameItemListRequest queryGameItemListRequest,
			BindingResult bindingResult, HttpServletRequest request) {
		// check param
		logger.warn("接受: ip : " + super.getRemoteIp(request) + " 的网游商品列表查询请求 : "
				+ queryGameItemListRequest);
		validator.validate(queryGameItemListRequest, bindingResult);
		if (bindingResult.hasErrors()) {
			return getErrorResponse(bindingResult);
		}

		// check sign
		Response checkSignResponse = commonDealService.checkSign(queryGameItemListRequest);
		if (!checkSignResponse.isSuccess()) {
			logger.warn("返回: " + checkSignResponse);
			return checkSignResponse;
		}

		QueryGameItemListResponse queryGameItemListResponse = commonDealService
				.queryGameItemList(queryGameItemListRequest);
		logger.warn("返回: " + queryGameItemListResponse);
		return queryGameItemListResponse;

	}
}
