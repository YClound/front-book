package com.longan.getway.upstream.callback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.telecomflow.vo.SuKaTelecomFlowCallbackResponse;
import com.longan.getway.upstream.telecomflow.vo.SuKaTelecomFlowCallbackVO;

@Controller
public class SuKaTelecomFlowCallback extends BaseCallBackController {

	private final static String callbackName = "速卡电信流量包";
	private final static Long userId = Long
			.parseLong(Utils.getProperty("suKaTelecomFlow.traderId"));

	@RequestMapping(value = "callback/suKaFlowCallback.do", method = RequestMethod.POST)
	public void onRequest(HttpServletRequest request, HttpServletResponse response) {
		SuKaTelecomFlowCallbackResponse callbackResponse = new SuKaTelecomFlowCallbackResponse();
		logCallBack(request, callbackName);
		final SuKaTelecomFlowCallbackVO vo = new SuKaTelecomFlowCallbackVO();
		vo.setUserid(request.getParameter("userid"));
		vo.setOrderid(request.getParameter("orderid"));
		vo.setSporderid(request.getParameter("sporderid"));
		vo.setMerchantsubmittime(request.getParameter("merchantsubmittime"));
		vo.setResultno(request.getParameter("resultno"));
		vo.setSign(request.getParameter("sign"));
		if (!vo.checkSign()) {
			// 签名失败
			logger.error("签名校验失败");
			logger.warn("返回 ip : " + getRemoteIp(request) + callbackResponse.toString());
			responseStr(response, callbackResponse.toString());
			return;
		}
		callbackExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Long supplyOrderId = Long.parseLong(vo.getSporderid());
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

				if ("1".equals(vo.getResultno())) {// 成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				} else if ("9".equals(vo.getResultno())) {// 失败
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedMsg(vo.getResultno() + "_充值失败");
				} else {// 其他状态
					logger.error("非终极状态不做处理 状态： " + vo.getResultno());
					return;
				}
				// 处理订单 ,退款，更新等操作
				bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
			}
		});
		// 返回成功
		logger.warn("返回 ip : " + getRemoteIp(request) + callbackResponse.toString());
		responseStr(response, callbackResponse.toString());
	}
}
