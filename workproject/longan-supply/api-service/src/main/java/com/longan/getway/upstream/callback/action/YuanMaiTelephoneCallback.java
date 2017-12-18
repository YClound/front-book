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
import com.longan.getway.upstream.telephone.vo.YuanMaiCallbackVO;

@Controller
public class YuanMaiTelephoneCallback extends BaseCallBackController {
	private final static String responseSuccess = "0000";
	private final static String callbackName = "元迈全国话费";

	private final static Long userId = Long.parseLong(Utils.getProperty("yuanMaiSupply.traderId"));

	@RequestMapping(value = "callback/yuanMaiTelephoneCallback", method = RequestMethod.POST)
	public void onRequest(HttpServletRequest request, HttpServletResponse response) {
		logCallBack(request, callbackName);
		final YuanMaiCallbackVO vo = new YuanMaiCallbackVO();
		vo.setOrderid(request.getParameter("orderid"));
		vo.setStatus(request.getParameter("status"));
		vo.setOrdermoney(request.getParameter("ordermoney"));
		vo.setSign(request.getParameter("sign"));
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
				Long supplyOrderId = Long.parseLong(vo.getOrderid());
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
				if ("2".equals(vo.getStatus())) {
					// 成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				} else if ("3".equals(vo.getStatus())) {
					// 部分成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_UNCONFIRM);
					upstreamCallBack.setFailedMsg(vo.getStatus() + "_部分成功,实际充值： "
							+ vo.getOrdermoney() + " 元");
				} else if ("4".equals(vo.getStatus())) {
					// 失败
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedMsg(vo.getStatus() + "_充值失败");
				} else {
					// 其他状态
					logger.error("非终极状态不做处理 状态： " + vo.getStatus());
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
