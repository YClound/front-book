package com.longan.getway.biz.action.unicomaync;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.getway.biz.common.BaseController;
import com.longan.getway.biz.common.request.QueryBizOrderRequest;
import com.longan.getway.biz.common.response.NewQueryBizOrderResponse;
import com.longan.getway.biz.common.response.QueryBizOrderResponse;
import com.longan.getway.biz.common.response.Response;
import com.longan.getway.biz.service.CommonDealService;
/**
 * 修改接口名
 * 原接口名：unicomAync/queryBizOrder.do
 * 新接口名：query/order
 * @author KID
 *
 */
@Controller
@RequestMapping("query/order")
public class UnicomAyncQueryBizOrder extends BaseController {
	@Resource
	private CommonDealService commonDealService;
	@Resource
	private Validator validator;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	Response onResut(
			@ModelAttribute("queryBizOrderRequest") QueryBizOrderRequest queryBizOrderRequest,
			BindingResult bindingResult, HttpServletRequest request) {
		logger.warn("接受: ip : " + super.getRemoteIp(request) + " 的查询订单信息请求 : "
				+ queryBizOrderRequest);
		//新参转旧参
		queryBizOrderRequest.convertToOld();
		// check param
		validator.validate(queryBizOrderRequest, bindingResult);
		if (bindingResult.hasErrors()) {
			return getErrorResponse(bindingResult);
		}
		//旧参转新参
		queryBizOrderRequest.convertToNew();
		// check sign
		Response checkSignResponse = commonDealService.checkSign(queryBizOrderRequest);
		if (!checkSignResponse.isSuccess()) {
			logger.warn("返回: " + checkSignResponse);
			return checkSignResponse;
		}
		//新参转旧参
		queryBizOrderRequest.convertToOld();
		// service
		QueryBizOrderResponse response = commonDealService.queryResponse(queryBizOrderRequest);
		logger.warn("返回: " + response);
		// return
		return new NewQueryBizOrderResponse(response);
	}
}
