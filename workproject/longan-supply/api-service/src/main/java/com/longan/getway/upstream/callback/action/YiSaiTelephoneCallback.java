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
import com.longan.getway.upstream.telephone.vo.YiSaiCallbackResponse;
import com.longan.getway.upstream.telephone.vo.YiSaiCallbackVO;

@Controller
public class YiSaiTelephoneCallback extends BaseCallBackController {
	private final static String callbackName = "易赛全国话费";
	private final static Long userId = Long.parseLong(Utils.getProperty("yiSaiSupply.traderId"));

	@RequestMapping(value = "callback/yiSaiTelephoneCallback.do", method = RequestMethod.POST)
	public void onRequest(HttpServletRequest request, HttpServletResponse response) {
		YiSaiCallbackResponse callbackResponse = new YiSaiCallbackResponse();
		logCallBack(request, callbackName);
		final YiSaiCallbackVO vo = new YiSaiCallbackVO();
		vo.setUserNumber(request.getParameter("UserNumber"));
		vo.setInOrderNumber(request.getParameter("InOrderNumber"));
		vo.setOutOrderNumber(request.getParameter("OutOrderNumber"));
		vo.setPayResult(request.getParameter("PayResult"));
		vo.setCustomInfo(request.getParameter("CustomInfo"));
		vo.setRecordKey(request.getParameter("RecordKey"));
		vo.setOrderType(request.getParameter("OrderType"));
		if (!vo.checkSign()) {
			// 签名失败
			callbackResponse.setError();
			logger.error("签名校验失败");
			logger.warn("返回 ip : " + getRemoteIp(request) + callbackResponse.toString());
			responseStr(response, callbackResponse.toString());
			return;
		}
		callbackExecutor.execute(new Runnable() {

			@Override
			public void run() {
				Long supplyOrderId = Long.parseLong(vo.getInOrderNumber());
				Result<SupplyOrder> supplyOrderResult = supplyOrderService
						.getSupplyOrderByIdAndTraderId(supplyOrderId, userId);
				SupplyOrder supplyOrder = supplyOrderResult.getModule();
				if (!supplyOrderResult.isSuccess()) {
					logger.error("上游回调,获取订单失败  upstreamSerialno:  " + vo.getInOrderNumber()
							+ " msg: " + supplyOrderResult.getResultMsg());
					return;
				}

				if (supplyOrder == null) {
					logger.error("上游回调,获取订单为空 upstreamSerialno:  " + vo.getInOrderNumber()
							+ " msg: " + supplyOrderResult.getResultMsg());
					return;
				}

				CallBack upstreamCallBack = new CallBack();
				upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
				if ("4".equals(vo.getPayResult())) {// 成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				} else if ("5".equals(vo.getPayResult())) {// 失败
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedMsg(vo.getPayResult() + "_充值失败");
				} else {// 其他状态
					logger.error("非终极状态不做处理 状态： " + vo.getPayResult());
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
