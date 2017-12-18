package com.longan.getway.upstream.callback.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.telecomflow.vo.HcTelecomFlowCallbackResponse;
import com.longan.getway.upstream.telecomflow.vo.HcTelecomFlowCallbackVO;

@Controller
@RequestMapping("callback/hcFlowCallback")
public class HcTelecomFlowCallback extends BaseCallBackController {
	private final static String callbackName = "鸿程电信流量包";

	private final static Long userId = Long.parseLong(Utils.getProperty("hcTelecomFlow.traderId"));

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public HcTelecomFlowCallbackResponse onRequest(HttpServletRequest request) {
		HcTelecomFlowCallbackResponse response = new HcTelecomFlowCallbackResponse();
		logCallBack(request, callbackName);

		final HcTelecomFlowCallbackVO hcTelecomFlowCallbackVO = new HcTelecomFlowCallbackVO();

		hcTelecomFlowCallbackVO.setOrderNo(request.getParameter("orderNo"));
		hcTelecomFlowCallbackVO.setOrderStatus(request.getParameter("orderStatus"));
		hcTelecomFlowCallbackVO.setOrderSuccessTime(request.getParameter("orderSuccessTime"));
		hcTelecomFlowCallbackVO.setServiceType(request.getParameter("serviceType"));
		hcTelecomFlowCallbackVO.setCoopId(request.getParameter("coopId"));
		hcTelecomFlowCallbackVO.setSign(request.getParameter("sign"));
		hcTelecomFlowCallbackVO.setFailedCode(request.getParameter("failedCode"));
		hcTelecomFlowCallbackVO.setFailedReason(request.getParameter("failedReason"));

		if (!hcTelecomFlowCallbackVO.checkSign()) {
			// 签名失败
			response.setError();
			logger.error("签名校验失败");
			logger.warn("返回 ip : " + getRemoteIp(request) + response.toString());
			return response;
		}

		response.setOrderNo(hcTelecomFlowCallbackVO.getOrderNo());

		callbackExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Long supplyOrderId = Long.parseLong(hcTelecomFlowCallbackVO.getOrderNo());

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

				// status 转换
				if ("ORDER_SUCCESS".equals(hcTelecomFlowCallbackVO.getOrderStatus())) {
					// 成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				} else if ("REFUNDED".equals(hcTelecomFlowCallbackVO.getOrderStatus())) {
					// 失败
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedCode(hcTelecomFlowCallbackVO.getFailedCode());
					upstreamCallBack.setFailedMsg(hcTelecomFlowCallbackVO.getFailedReason()
							+ "_充值失败");
				} else {
					// 其他状态
					logger.error("非终极状态不做处理 状态： " + hcTelecomFlowCallbackVO.getOrderStatus());
					return;
				}
				// 处理订单 ,退款，更新等操作
				bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
			}
		});

		// 返回成功
		logger.warn("返回 ip : " + getRemoteIp(request) + response.toString());
		return response;
	}
}
