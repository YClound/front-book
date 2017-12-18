package com.longan.getway.upstream.callback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.telephone.vo.SanTongCallbackVO;

@Controller
public class SanTongTelephoneCallback extends BaseCallBackController {
	private final static String responseSuccess = "OK";
	private final static String callbackName = "三通全国话费";

	private final static Long userId = Long.parseLong(Utils.getProperty("sanTongSupply.traderId"));

	@RequestMapping(value = "callback/sanTongTelephoneCallback")
	@ResponseBody
	public void onRequest(HttpServletRequest request, HttpServletResponse response) {
		logCallBack(request, callbackName);
		final SanTongCallbackVO vo = new SanTongCallbackVO();
		vo.setEsaleOrderId(request.getParameter("esaleOrderId"));
		vo.setChargeStatus(request.getParameter("chargeStatus"));

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
				long supplyOrderId = Long.parseLong(vo.getEsaleOrderId());
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
				if ("0".equals(vo.getChargeStatus())) {// 成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				} else if ("1".equals(vo.getChargeStatus())) {// 失败
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedMsg(vo.getChargeStatus() + "_充值失败");
				} else {// 其他
					logger.error("非终极状态不做处理 状态： " + vo.getChargeStatus());
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
