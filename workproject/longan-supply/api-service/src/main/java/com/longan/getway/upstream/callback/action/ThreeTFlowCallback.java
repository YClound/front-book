package com.longan.getway.upstream.callback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.domain.CallBack;

@Controller
public class ThreeTFlowCallback extends BaseCallBackController {
	private final static String responseSuccess = "SUCCESS";

	private final static Long userId = Long.parseLong(Utils.getProperty("threeTFlow.traderId"));

	@RequestMapping(value = "callback/threeTFlowCallback.do")
	public void onRequest(HttpServletRequest request, HttpServletResponse response) {
		logCallBack(request, "三体供货");

		final String channelOrderNo = request.getParameter("channelOrderNo");
		final String orderStatus = request.getParameter("orderStatus");

		callbackExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Long supplyOrderId = Long.parseLong(channelOrderNo);
				Result<SupplyOrder> supplyOrderResult = supplyOrderService.getSupplyOrderByIdAndTraderId(supplyOrderId, userId);
				if (!supplyOrderResult.isSuccess()) {
					logger.error("上游回调,获取供货单失败  supplyOrderId:  " + supplyOrderId + " msg: " + supplyOrderResult.getResultMsg());
					return;
				}

				SupplyOrder supplyOrder = supplyOrderResult.getModule();
				if (supplyOrder == null) {
					logger.error("上游回调,获取供货单为空 supplyOrder:  " + supplyOrder + " msg: " + supplyOrderResult.getResultMsg());
					return;
				}

				CallBack upstreamCallBack = new CallBack();
				upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
				if ("4".equals(orderStatus)) {
					// 成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				} else if ("5".equals(orderStatus)) {
					// 失败
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedMsg(orderStatus + "_充值失败");
				} else {
					// 其他状态
					logger.error("非终极状态不做处理 状态： " + orderStatus);
					return;
				}
				// 处理订单 ，退款，更新，通知下游等操作
				bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
			}
		});

		// 返回成功
		logger.warn("返回 ip : " + getRemoteIp(request) + responseSuccess);
		responseStr(response, responseSuccess);
	}
}
