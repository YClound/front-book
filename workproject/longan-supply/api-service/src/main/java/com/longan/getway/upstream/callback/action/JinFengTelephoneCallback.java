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
import com.longan.getway.upstream.telephone.vo.JinFengCallbackVO;

@Controller
public class JinFengTelephoneCallback extends BaseCallBackController {
	private final static String responseSuccess = "success";
	private final static String callbackName = "劲峰全国话费";

	private final static Long userId = Long.parseLong(Utils.getProperty("jinFengSupply.traderId"));

	@RequestMapping(value = "callback/jinFengTelephoneCallback")
	@ResponseBody
	public void onRequest(HttpServletRequest request, HttpServletResponse response) {
		logCallBack(request, callbackName);
		final JinFengCallbackVO vo = new JinFengCallbackVO();
		vo.setHmac(request.getParameter("Hmac"));
		vo.setR10_trxDate(request.getParameter("R10_trxDate"));
		vo.setR4_trxamount(request.getParameter("R4_trxamount"));
		vo.setR6_requestid(request.getParameter("R6_requestid"));
		vo.setR8_returncode(request.getParameter("R8_returncode"));
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
				Long supplyOrderId = Long.parseLong(vo.getR6_requestid());
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
				if ("2".equals(vo.getR8_returncode())) {
					// 成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				} else if ("7".equals(vo.getR8_returncode())) {
					// 部分成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_UNCONFIRM);
					upstreamCallBack.setFailedMsg(vo.getR8_returncode() + "_部分退款,实际扣款： "
							+ vo.getR4_trxamount() + " 元");
				} else if ("3".equals(vo.getR8_returncode())) {
					// 失败
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedMsg(vo.getR8_returncode() + "_充值失败");
				} else if ("6".equals(vo.getR8_returncode())) {
					// 退款
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedMsg(vo.getR8_returncode() + "_退款成功");
				} else {
					// 其他状态
					logger.error("非终极状态不做处理 状态： " + vo.getR8_returncode());
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
