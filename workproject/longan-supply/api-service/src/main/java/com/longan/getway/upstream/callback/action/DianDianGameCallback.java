package com.longan.getway.upstream.callback.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.game.vo.DianDianCallbackVO;

@Controller
public class DianDianGameCallback extends BaseCallBackController {
	private final static String responseSuccess = "<?xml version=\"1.0\" encoding=\"utf-8\"?><response><orderSuccess>T</orderSuccess><failedCode></failedCode><failedReason></failedReason></response>";
	private final static String callbackName = "点点游戏供货";

	private final static Long userId = Long.parseLong(Utils.getProperty("dianDianSupply.traderId"));

	@RequestMapping(value = "callback/dianDianGameCallback")
	@ResponseBody
	public void onRequest(HttpServletRequest request, HttpServletResponse response) {
		logCallBack(request, callbackName);
		final DianDianCallbackVO vo = new DianDianCallbackVO();
		if (StringUtils.isNotEmpty(request.getParameter("TranId"))) {
			vo.setTranId(request.getParameter("TranId").substring(10));
		}
		vo.setCoopId(request.getParameter("CoopId"));
		vo.setFailedCode(request.getParameter("FailedCode"));
		if (StringUtils.isNotEmpty(request.getParameter("FailedReason"))) {
			try {
				vo.setFailedReason(new String(request.getParameter("FailedReason").getBytes(
						"ISO-8859-1"), "GBK"));
			} catch (UnsupportedEncodingException e) {
				logger.error("DianDianGameCallback UnsupportedEncodingException:" + e);
			}
		}
		vo.setOrderNo(request.getParameter("OrderNo"));
		vo.setOrderStatus(request.getParameter("OrderStatus"));
		vo.setOrderSuccessTime(request.getParameter("OrderSuccessTime"));
		vo.setSign(request.getParameter("Sign"));

		if (!vo.checkSign()) {
			// 签名失败
			logger.error("签名校验失败");
			logger.warn("返回 ip : " + getRemoteIp(request) + responseSuccess);
			responseStr(response, responseSuccess);
			return;
		}
		callbackExecutor.execute(new Runnable() {

			@Override
			public void run() {
				long supplyOrderId = Long.parseLong(vo.getTranId());
				Result<SupplyOrder> supplyOrderResult = supplyOrderService
						.getSupplyOrderByIdAndTraderId(supplyOrderId, userId);
				if (!supplyOrderResult.isSuccess()) {
					logger.error("上游回调,获取供货单失败  supplyOrderId:  " + supplyOrderId + " msg: "
							+ supplyOrderResult.getResultMsg());
					return;
				}
				SupplyOrder supplyOrder = supplyOrderResult.getModule();
				if (supplyOrder == null) {
					logger.error("上游回调,获取供货单为空 supplyOrder:  " + supplyOrder + " msg: "
							+ supplyOrderResult.getResultMsg());
					return;
				}
				CallBack upstreamCallBack = new CallBack();
				upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
				if ("SUCCESS".equals(vo.getOrderStatus())) {// 成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				} else if ("FAILED".equals(vo.getOrderStatus())
						|| "CANCEL".equals(vo.getOrderStatus())) {// 失败
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedMsg(vo.getFailedCode() + "_" + vo.getFailedReason());
				} else {// 其他
					logger.error("非终极状态不做处理 状态： " + vo.getOrderStatus());
					return;
				}
				// 处理订单 ,退款，更新等操作
				bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
			}
		});
		// 返回成功
		logger.warn("返回 ip : " + getRemoteIp(request) + responseSuccess);
		responseStr(response, responseSuccess);
	}
}
