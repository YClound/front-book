package com.longan.getway.upstream.callback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.flow.vo.JihuDayuFlowTransVO;

import net.sf.json.JSONObject;

@Controller
public class JihuDayuFlowCallback extends BaseCallBackController {
	@RequestMapping(value = "callback/jihuDayuFlowCallback", method = RequestMethod.POST)
	@ResponseBody
	public void onRequest(@RequestBody String requestJson, HttpServletRequest request, HttpServletResponse response) {
		logger.warn("收到 ip : " + getRemoteIp(request) + "大禹流量充值的结果通知请求: " + requestJson);
		JSONObject jo = JSONObject.fromObject(requestJson);
		final String reqOrderId = jo.getString("reqOrderId");
		final String state = jo.getString("state");
		// 判空
		if (hasBlank(reqOrderId, state)) {
			// 参数不完整
			logger.warn("大禹流量充值回调：参数不完整。 ip : " + getRemoteIp(request));
			responseStr(response, "N");
			return;
		}
		callbackExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Result<SupplyOrder> supplyOrderResult = supplyOrderService.getSupplyOrderByUpstreamSerialno(reqOrderId, JihuDayuFlowTransVO.traderId);
				if (!supplyOrderResult.isSuccess()) {
					logger.error("大禹流量充值回调,获取供货单失败  supplyOrderId:  " + reqOrderId + " msg: " + supplyOrderResult.getResultMsg());
					return;
				}
				SupplyOrder supplyOrder = supplyOrderResult.getModule();
				if (supplyOrder == null) {
					logger.error("大禹流量充值回调,获取供货单为空 supplyOrder:  " + reqOrderId + " msg: " + supplyOrderResult.getResultMsg());
					return;
				}
				CallBack upstreamCallBack = new CallBack();
				upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
				if ("0".equals(state)) {// 失败
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
				} else if ("1".equals(state)) {// 成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				} else {
					logger.warn("大禹流量充值回调 状态未确认不做处理");
					return;
				}
				// 处理订单 ,退款，更新等操作
				bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
			}
		});
		responseStr(response, "Y");
	}

	private boolean hasBlank(String... ss) {
		for (String s : ss) {
			if (s == null || s.isEmpty())
				return true;
		}
		return false;
	}
}
