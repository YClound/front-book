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
import com.longan.biz.core.SupplyOrderService;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.OperationLog;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.OperLogUtils;
import com.longan.client.remote.service.CallBackService;
import com.longan.client.remote.service.SupplyForRemoteService;
import com.longan.mng.action.common.BaseController;

@Controller
@RequestMapping("biz/supplyOrderDeal")
public class SupplyOrderDeal extends BaseController {

	@Resource
	private BizOrderService bizOrderService;

	@Resource
	private SupplyOrderService supplyOrderService;

	@Resource
	private CallBackService callBackSerivce;

	@Resource
	private OperationLogService operationLogService;

	@Resource
	private SupplyForRemoteService supplyForRemoteService;

	@RequestMapping(params = "type=refund")
	public String onRequestRefund(@RequestParam("supplyOrderId") Long supplyOrderId,
			HttpSession session, Model model, HttpServletRequest request) {
		UserInfo userInfo = super.getUserInfo(session);
		String returnUrl = "biz/querySupplyOrder";

		boolean flag = request.getHeader("Referer").indexOf("queryAllSupplyOrder") >= 0;
		if (flag) {
			returnUrl = "statistic/queryAllSupplyOrder";
		}

		logger.warn(userInfo.getUserName() + "执行退款操作 供货单号 : " + supplyOrderId);

		Result<SupplyOrder> queryReuslt = supplyOrderService.getSupplyOrderById(supplyOrderId);
		if (!queryReuslt.isSuccess() || queryReuslt.getModule() == null) {
			alertError(model, "没有该供货单");
			return returnUrl;
		}
		SupplyOrder supplyOrder = queryReuslt.getModule();

		if (!supplyOrder.canDeal()) {
			alertError(model, "该供货单不允许做此操作");
			return returnUrl;
		}

		String successUrl = "querySupplyOrder.do?bizId=" + supplyOrder.getBizId() + "&id="
				+ supplyOrder.getId();
		if (flag) {
			successUrl = "../statistic/queryAllSupplyOrder.do?id=" + supplyOrder.getId();
		}

		supplyOrder.setDealOperId(userInfo.getId());
		supplyOrder.setDealOperName(userInfo.getUserName());

		supplyOrder.setFinalType(Constants.SupplyOrder.FINAL_TYPE_YES);
		Result<Boolean> cancelSupplyOrderResult = supplyOrderService.cancelSupplyOrder(supplyOrder);

		if (!cancelSupplyOrderResult.isSuccess()) {
			alertError(model, cancelSupplyOrderResult.getResultMsg());
		}

		Result<BizOrder> bizOrderQueryReuslt = bizOrderService.getBizOrderById(supplyOrder
				.getBizOrderId());
		if (!bizOrderQueryReuslt.isSuccess()) {
			alertError(model, bizOrderQueryReuslt.getResultMsg());
			return returnUrl;
		}
		BizOrder bizOrder = bizOrderQueryReuslt.getModule();
		if (bizOrder == null) {
			alertError(model, "没有该订单");
			return returnUrl;
		}

		bizOrder.setDealOperId(userInfo.getId());
		bizOrder.setDealOperName(userInfo.getUserName());

		// 这里直接取消订单了。不做补充供货
		Result<Boolean> cancelBizOrderResult = bizOrderService.cancelBizOrder(bizOrder);

		if (!cancelBizOrderResult.isSuccess()) {
			alertError(model, cancelBizOrderResult.getResultMsg());
			return returnUrl;
		}

		try {
			callBackSerivce.callBackAsync(bizOrder);
		} catch (Exception e) {
			logger.error("callBackAsync error", e);
		}

		@SuppressWarnings("unchecked")
		Map<String, String> map = (HashMap<String, String>) session.getAttribute("requestInfoMap");
		OperationLog operationLog = OperLogUtils.operationLogDeal(supplyOrder, supplyOrderService
				.getSupplyOrderById(supplyOrderId).getModule(), userInfo, map.get("moduleName")
				+ "(退款)", bizOrder.getBizId(), map.get("loginIp"));
		operationLogService.createOperationLog(operationLog);

		alertSuccess(model, successUrl);
		return returnUrl;
	}

	@RequestMapping(params = "type=makeUp")
	public String onRequestMakeUp(@RequestParam("supplyOrderId") Long supplyOrderId,
			HttpSession session, Model model, HttpServletRequest request) {
		UserInfo userInfo = super.getUserInfo(session);
		String returnUrl = "biz/querySupplyOrder";

		boolean flag = request.getHeader("Referer").indexOf("queryAllSupplyOrder") >= 0;
		if (flag) {
			returnUrl = "statistic/queryAllSupplyOrder";
		}

		logger.warn(userInfo.getUserName() + "执行确认操作 供货单号 :" + supplyOrderId);

		Result<SupplyOrder> queryReuslt = supplyOrderService.getSupplyOrderById(supplyOrderId);
		if (!queryReuslt.isSuccess() || queryReuslt.getModule() == null) {
			alertError(model, "没有该供货单");
			return returnUrl;
		}
		SupplyOrder supplyOrder = queryReuslt.getModule();

		if (!supplyOrder.canDeal()) {
			alertError(model, "该供货单不允许做此操作");
			return returnUrl;
		}

		String successUrl = "querySupplyOrder.do?bizId=" + supplyOrder.getBizId() + "&id="
				+ supplyOrder.getId();
		if (flag) {
			successUrl = "../statistic/queryAllSupplyOrder.do?id=" + supplyOrder.getId();
		}

		supplyOrder.setDealOperId(userInfo.getId());
		supplyOrder.setDealOperName(userInfo.getUserName());

		Result<Boolean> cancelSupplyOrderResult = supplyOrderService
				.comfirmSupplyOrder(supplyOrder);

		if (!cancelSupplyOrderResult.isSuccess()) {
			alertError(model, cancelSupplyOrderResult.getResultMsg());
		}

		Result<BizOrder> bizOrderQueryReuslt = bizOrderService.getBizOrderById(supplyOrder
				.getBizOrderId());
		if (!bizOrderQueryReuslt.isSuccess()) {
			alertError(model, bizOrderQueryReuslt.getResultMsg());
			return returnUrl;
		}
		BizOrder bizOrder = bizOrderQueryReuslt.getModule();
		if (bizOrder == null) {
			alertError(model, "没有该订单");
			return returnUrl;
		}

		bizOrder.setDealOperId(userInfo.getId());
		bizOrder.setDealOperName(userInfo.getUserName());

		Result<Boolean> cancelBizOrderResult = bizOrderService.comfirmBizOrder(bizOrder);

		if (!cancelBizOrderResult.isSuccess()) {
			alertError(model, cancelBizOrderResult.getResultMsg());
			return returnUrl;
		}

		try {
			callBackSerivce.callBackAsync(bizOrder);
		} catch (Exception e) {
			logger.error("callBackAsync error", e);
		}

		@SuppressWarnings("unchecked")
		Map<String, String> map = (HashMap<String, String>) session.getAttribute("requestInfoMap");
		OperationLog operationLog = OperLogUtils.operationLogDeal(supplyOrder, supplyOrderService
				.getSupplyOrderById(supplyOrderId).getModule(), userInfo, map.get("moduleName")
				+ "(确认)", bizOrder.getBizId(), map.get("loginIp"));
		operationLogService.createOperationLog(operationLog);

		alertSuccess(model, successUrl);
		return returnUrl;
	}

	@RequestMapping(params = "type=repeatCharge")
	public String onRequestBizOrder(@RequestParam("supplyOrderId") Long supplyOrderId,
			HttpSession session, Model model, HttpServletRequest request) {
		UserInfo userInfo = super.getUserInfo(session);
		String returnUrl = "biz/querySupplyOrder";

		boolean flag = request.getHeader("Referer").indexOf("queryAllSupplyOrder") >= 0;
		if (flag) {
			returnUrl = "statistic/queryAllSupplyOrder";
		}

		logger.warn(userInfo.getUserName() + "执行补充操作 供货单号 :" + supplyOrderId);

		Result<SupplyOrder> queryReuslt = supplyOrderService.getSupplyOrderById(supplyOrderId);
		if (!queryReuslt.isSuccess() || queryReuslt.getModule() == null) {
			alertError(model, "没有该供货单");
			return returnUrl;
		}
		SupplyOrder supplyOrder = queryReuslt.getModule();

		if (!supplyOrder.canDeal()) {
			alertError(model, "该供货单不允许做补充操作");
			return returnUrl;
		}

		supplyOrder.setDealOperId(userInfo.getId());
		supplyOrder.setDealOperName(userInfo.getUserName());

		Result<Boolean> cancelSupplyOrderResult = supplyOrderService.cancelSupplyOrder(supplyOrder);

		if (!cancelSupplyOrderResult.isSuccess()) {
			alertError(model, cancelSupplyOrderResult.getResultMsg());
			return returnUrl;
		}

		Result<BizOrder> bizOrderQueryReuslt = bizOrderService.getBizOrderById(supplyOrder
				.getBizOrderId());
		if (!bizOrderQueryReuslt.isSuccess()) {
			alertError(model, bizOrderQueryReuslt.getResultMsg());
			return returnUrl;
		}
		BizOrder bizOrder = bizOrderQueryReuslt.getModule();
		if (bizOrder == null) {
			alertError(model, "没有该订单");
			return returnUrl;
		}

		bizOrder.setIsRepeat(true);
		bizOrder.setManualType(Constants.SupplyOrder.MANUAL_TYPE_YES);
		bizOrder.setItemSupplyId(supplyOrder.getItemSupplyId());

		Result<SupplyOrder> supplyResult = null;
		try {
			supplyResult = supplyForRemoteService.supply(bizOrder);
		} catch (Exception e) {
			logger.error("repeatCharge error ", e);
			alertError(model, "补充供货异常");
			return returnUrl;
		}

		if (!supplyResult.isSuccess()) {
			if (Constants.ErrorCode.CODE_ERROR_SUPPLY_UNCONFIRM
					.equals(supplyResult.getResultCode())) {
				alertError(model, "补充供货处理中");
			} else {
				alertError(model, supplyResult.getResultMsg());
				if (supplyResult.getModule() != null) {
					supplyResult.getModule().setFinalType(Constants.SupplyOrder.FINAL_TYPE_YES);
					supplyOrderService.cancelSupplyOrder(supplyResult.getModule());
				} else {
					// 如果没创建供货单，则这里将上一个供货单更新未最终供货单。
					supplyOrder.setFinalType(Constants.SupplyOrder.FINAL_TYPE_YES);
					supplyOrder.setSupplyStatus(Constants.SupplyOrder.STATUS_FAILED);
					supplyOrderService.updateSupplyOrder(supplyOrder);
				}
				bizOrderService.cancelBizOrder(bizOrder);
			}
			return returnUrl;
		}

		String successUrl = "querySupplyOrder.do?bizId=" + supplyOrder.getBizId() + "&id="
				+ supplyResult.getModule().getId();
		if (flag) {
			successUrl = "../statistic/queryAllSupplyOrder.do?id="
					+ supplyResult.getModule().getId();
		}

		@SuppressWarnings("unchecked")
		Map<String, String> map = (HashMap<String, String>) session.getAttribute("requestInfoMap");
		OperationLog operationLog = OperLogUtils.operationLogDeal(supplyOrder, supplyOrderService
				.getSupplyOrderById(supplyOrderId).getModule(), userInfo, map.get("moduleName")
				+ "(补充)", supplyOrder.getBizId(), map.get("loginIp"));
		operationLogService.createOperationLog(operationLog);

		alertSuccess(model, successUrl);
		return returnUrl;
	}
}
