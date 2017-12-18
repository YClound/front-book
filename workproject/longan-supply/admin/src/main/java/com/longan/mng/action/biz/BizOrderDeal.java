package com.longan.mng.action.biz;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.longan.biz.core.BizOrderService;
import com.longan.biz.core.OperationLogService;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.OperationLog;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.OperationVO;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.OperLogUtils;
import com.longan.client.remote.service.CallBackService;
import com.longan.mng.action.common.BaseController;

@Controller
@RequestMapping("biz/bizOrderDeal")
public class BizOrderDeal extends BaseController {
	@Resource
	private BizOrderService bizOrderService;

	@Resource
	private CallBackService callBackSerivce;

	@Resource
	private OperationLogService operationLogService;

	@RequestMapping(params = "type=refund")
	public String onRequestRefund(@RequestParam("bizOrderId") Long bizOrderId, HttpSession session, Model model, HttpServletRequest request) {
		UserInfo userInfo = super.getUserInfo(session);
		String returnUrl = "biz/queryBizOrder";

		boolean flag = request.getHeader("Referer").indexOf("queryAllBizOrder") >= 0;
		if (flag) {
			returnUrl = "statistic/queryAllBizOrder";
		}

		logger.warn(userInfo.getUserName() + "执行退款操作 订单号 : " + bizOrderId);
		Result<BizOrder> queryReuslt = bizOrderService.getBizOrderById(bizOrderId);
		if (!queryReuslt.isSuccess() || queryReuslt.getModule() == null) {
			alertError(model, "没有该订单");
			return returnUrl;
		}
		BizOrder bizOrder = queryReuslt.getModule();
		if (bizOrder.getStatus() != Constants.BizOrder.STATUS_UNCONFIRMED && bizOrder.getStatus() != Constants.BizOrder.STATUS_CHARGING) {
			alertError(model, "只能处理未确认或者处理中状态的订单");
			return returnUrl;
		}

		if (!bizOrder.canDeal()) {
			alertError(model, "该订单不允许做次操作");
			return returnUrl;
		}

		String successUrl = "queryBizOrder.do?bizId=" + bizOrder.getBizId() + "&id=" + bizOrderId;
		if (flag) {
			successUrl = "../statistic/queryAllBizOrder.do?id=" + bizOrderId;
		}

		bizOrder.setMemo("供货失败");
		bizOrder.setDealOperId(userInfo.getId());
		bizOrder.setDealOperName(userInfo.getUserName());

		Result<Boolean> cancelBizOrderResult = bizOrderService.cancelBizOrder(bizOrder);

		if (cancelBizOrderResult.isSuccess()) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (HashMap<String, String>) session.getAttribute("requestInfoMap");
			OperationLog operationLog = OperLogUtils.operationLogDeal(bizOrder, bizOrderService.getBizOrderById(bizOrderId).getModule(), userInfo, map.get("moduleName") + "(退款)", bizOrder.getBizId(), map.get("loginIp"));
			operationLogService.createOperationLog(operationLog);
			OperationVO operationVO = new OperationVO();
			operationVO.setUserInfo(userInfo);
			operationVO.setOperationMemo("供货失败");
			try {
				callBackSerivce.callBackAsync(bizOrder);
			} catch (Exception e) {
				logger.error("callBackAsync error", e);
			}

			alertSuccess(model, successUrl);
		} else {
			alertError(model, cancelBizOrderResult.getResultMsg());
		}
		return returnUrl;
	}

	@RequestMapping(params = "type=makeUp")
	public String onRequestMakeUp(@RequestParam("bizOrderId") Long bizOrderId, HttpSession session, Model model, HttpServletRequest request) {
		UserInfo userInfo = super.getUserInfo(session);
		String returnUrl = "biz/queryBizOrder";
		boolean flag = request.getHeader("Referer").indexOf("queryAllBizOrder") >= 0;
		if (flag) {
			returnUrl = "statistic/queryAllBizOrder";
		}

		logger.warn(userInfo.getUserName() + "执行补入操作 订单号 : " + bizOrderId);
		Result<BizOrder> queryReuslt = bizOrderService.getBizOrderById(bizOrderId);
		if (queryReuslt.isSuccess()) {
			if (queryReuslt.getModule() == null) {
				alertError(model, "没有该订单");
				return returnUrl;
			}
		} else {
			alertError(model, queryReuslt.getResultMsg());
			return returnUrl;
		}
		BizOrder bizOrder = queryReuslt.getModule();

		if (bizOrder.getStatus() != Constants.BizOrder.STATUS_UNCONFIRMED && bizOrder.getStatus() != Constants.BizOrder.STATUS_CHARGING) {
			alertError(model, "只能处理未确认或者处理中状态的订单");
			return returnUrl;
		}

		if (!bizOrder.canDeal()) {
			alertError(model, "该订单不允许做次操作");
			return returnUrl;
		}

		String successUrl = "queryBizOrder.do?bizId=" + bizOrder.getBizId() + "&id=" + bizOrderId;
		if (flag) {
			successUrl = "../statistic/queryAllBizOrder.do?id=" + bizOrderId;
		}

		bizOrder.setDealOperId(userInfo.getId());
		bizOrder.setDealOperName(userInfo.getUserName());

		Result<Boolean> comfirmBizOrderResult = bizOrderService.comfirmBizOrder(bizOrder);
		if (comfirmBizOrderResult.isSuccess() && comfirmBizOrderResult.getModule()) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (HashMap<String, String>) session.getAttribute("requestInfoMap");
			OperationLog operationLog = OperLogUtils.operationLogDeal(bizOrder, bizOrderService.getBizOrderById(bizOrderId).getModule(), userInfo, map.get("moduleName") + "(补入)", bizOrder.getBizId(), map.get("loginIp"));
			operationLogService.createOperationLog(operationLog);

			try {
				callBackSerivce.callBackAsync(bizOrder);
			} catch (Exception e) {
				logger.error("callBackAsync error", e);
			}

			alertSuccess(model, successUrl);
		} else {
			alertError(model, comfirmBizOrderResult.getResultMsg());
		}
		return returnUrl;
	}

	@RequestMapping(params = "type=quickCallback")
	public String onRequestQuickCallback(@RequestParam("bizOrderId") Long bizOrderId, HttpSession session, Model model, HttpServletRequest request) {
		UserInfo userInfo = super.getUserInfo(session);
		String returnUrl = "biz/queryBizOrder";
		boolean flag = request.getHeader("Referer").indexOf("queryAllBizOrder") >= 0;
		if (flag) {
			returnUrl = "statistic/queryAllBizOrder";
		}

		logger.warn(userInfo.getUserName() + "执行再次回调操作 订单号 : " + bizOrderId);
		Result<BizOrder> queryReuslt = bizOrderService.getBizOrderById(bizOrderId);
		if (queryReuslt.isSuccess()) {
			if (queryReuslt.getModule() == null) {
				alertError(model, "没有该订单");
				return returnUrl;
			}
		} else {
			alertError(model, queryReuslt.getResultMsg());
			return returnUrl;
		}
		BizOrder bizOrder = queryReuslt.getModule();

		if (!bizOrder.isOver()) {
			alertError(model, "只能对已终结的订单做此操作");
			return returnUrl;
		}

		try {
			callBackSerivce.callBackAsync(bizOrder);
		} catch (Exception e) {
			logger.error("callBackAsync error", e);
		}

		String successUrl = "queryBizOrder.do?bizId=" + bizOrder.getBizId() + "&id=" + bizOrder.getId();
		if (flag) {
			successUrl = "../statistic/queryAllBizOrder.do?id=" + bizOrder.getId();
		}

		alertSuccess(model, successUrl);
		return returnUrl;
	}
}
