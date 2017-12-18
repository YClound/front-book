package com.longan.getway.upstream.callback.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Utils;
import com.longan.getway.biz.common.response.Response;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.domain.CallBack;

import net.sf.json.JSONObject;

@Controller
public class YongLiuFlowCallback extends BaseCallBackController {
	private final static String callbackName = "永流供货";
	private final static Long userId = Long.parseLong(Utils.getProperty("yongliu.traderId"));

	@RequestMapping(value = "callback/yongLiuFlowCallback.do", method = RequestMethod.POST)
	public @ResponseBody Response onRequest(HttpServletRequest request, HttpServletResponse response) {
		logger.warn("Content-Type : " + request.getHeader("Content-Type"));
		Response result = new Response();
		result.setSuccess();
		String requestBody = null;
		try {
			requestBody = super.getRequestBody(request);
			logger.warn("收到 ip : " + getRemoteIp(request) + callbackName + "的结果通知请求: " + requestBody);
		} catch (IOException e) {
			logger.error("getRequestBody error", e);
			return result;
		}

		if (StringUtils.isEmpty(requestBody)) {
			logger.error("getRequestBody is null");
			return result;
		}

		final JSONObject jo = JSONObject.fromObject(requestBody);

		if (jo.getString("transCode").equals("0000")) {
			callbackExecutor.execute(new Runnable() {
				@Override
				public void run() {

					long supplyOrderId = Long.parseLong(jo.getString("transNo"));

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
					String status = jo.getString("status");
					if ("success".equals(status)) {// 成功
						upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
					} else if ("failed".equals(status)) {// 失败
						upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
						upstreamCallBack.setFailedMsg(status + "_充值失败");
					} else {// 其他
						logger.error("非终极状态不做处理 状态： " + status);
						return;
					}
					// 处理订单 ,退款，更新等操作
					bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
				}
			});
		} else {
			logger.error("transCode : " + jo.getString("transCode"));
		}

		// 返回成功
		logger.warn("返回 ip : " + getRemoteIp(request) + result);
		return result;
	}

}
