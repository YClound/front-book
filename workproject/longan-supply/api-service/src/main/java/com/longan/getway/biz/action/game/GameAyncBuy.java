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
import com.longan.getway.biz.common.request.GameBuyRequest;
import com.longan.getway.biz.common.response.BuyResponse;
import com.longan.getway.biz.common.response.Response;
import com.longan.getway.biz.service.CommonDealService;

@Controller
public class GameAyncBuy extends BaseController {
	@Resource
	private CommonDealService commonDealService;

	@Resource
	private Validator validator;

	@RequestMapping(value = "game/aysncBuy", method = RequestMethod.GET)
	public @ResponseBody
	Response onResut(@ModelAttribute("buyRequest") GameBuyRequest buyRequest,
			BindingResult bindingResult, HttpServletRequest request) {
		logger.warn("接受: ip : " + super.getRemoteIp(request) + " 的网游下单请求 : " + buyRequest);
		// check param
		validator.validate(buyRequest, bindingResult);
		if (bindingResult.hasErrors()) {
			return getErrorResponse(bindingResult);
		}

		// check sign
		Response checkSignResponse = commonDealService.checkSign(buyRequest);
		if (!checkSignResponse.isSuccess()) {
			logger.warn("返回: " + checkSignResponse);
			return checkSignResponse;
		}

		// service
		BuyResponse response = commonDealService.buyGame(buyRequest,
				com.longan.biz.utils.Constants.BizOrder.DOWNSTREAM_SUPPLY_WAY_ASYNC);
		logger.warn("返回: " + response);
		// return
		return response;
	}
}
