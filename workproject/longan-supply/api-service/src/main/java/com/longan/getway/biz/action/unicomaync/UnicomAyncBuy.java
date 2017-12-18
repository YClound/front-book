package com.longan.getway.biz.action.unicomaync;

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
import com.longan.getway.biz.common.request.BuyRequest;
import com.longan.getway.biz.common.response.BuyResponse;
import com.longan.getway.biz.common.response.NewBuyResponse;
import com.longan.getway.biz.common.response.Response;
import com.longan.getway.biz.service.CommonDealService;

@Controller
public class UnicomAyncBuy extends BaseController {
	@Resource
	private CommonDealService commonDealService;

	@Resource
	private Validator validator;
	/**
	 * 接口修改
	 * 原接口名：unicomAync/buy
	 * 新接口名：buy/aync
	 * 参数修改
	 * 
	 */
	@RequestMapping(value = "buy/aync", method = RequestMethod.GET)
	public @ResponseBody
	Response onResut(@ModelAttribute("buyRequest") BuyRequest buyRequest,
			BindingResult bindingResult, HttpServletRequest request) {
		logger.warn("接受: ip : " + super.getRemoteIp(request) + " 的aync下单请求 : " + buyRequest);
		//新参转旧参
		buyRequest.convertToOld();
		// check param
		validator.validate(buyRequest, bindingResult);
		if (bindingResult.hasErrors()) {
			return getErrorResponse(bindingResult);
		}
		//旧参转新参
		buyRequest.convertToNew();
		// check sign
		Response checkSignResponse = commonDealService.checkSign(buyRequest);
		if (!checkSignResponse.isSuccess()) {
			logger.warn("返回: " + checkSignResponse);
			return checkSignResponse;
		}
		//新参转旧参
		buyRequest.convertToOld();
		// service
		BuyResponse response = commonDealService.buy(buyRequest,
				com.longan.biz.utils.Constants.BizOrder.DOWNSTREAM_SUPPLY_WAY_ASYNC);
		logger.warn("返回: " + response);
		// return
		return new NewBuyResponse(response);
	}
}
