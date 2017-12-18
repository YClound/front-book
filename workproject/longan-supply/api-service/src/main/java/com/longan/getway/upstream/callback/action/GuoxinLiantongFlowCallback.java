package com.longan.getway.upstream.callback.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

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
import com.longan.getway.upstream.flow.vo.GuoxinLiantongFlowVO;
@Controller
public class GuoxinLiantongFlowCallback extends BaseCallBackController{
	@RequestMapping(value = "callback/guoxinLiantongFlowCallback", method = RequestMethod.POST)
	@ResponseBody
	public void onRequest(@RequestBody String requestJson, HttpServletRequest request, HttpServletResponse response) {
		logger.warn("收到 ip : " + getRemoteIp(request) + "国信联通流量充值的结果通知请求: " + requestJson);
		final JSONObject jo = JSONObject.fromObject(requestJson);
		callbackExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String status = jo.getString("status");
				String orderId = jo.getString("orderId");
				Result<SupplyOrder> supplyOrderResult = supplyOrderService.getSupplyOrderByUpstreamSerialno(orderId, GuoxinLiantongFlowVO.traderId);
				if (!supplyOrderResult.isSuccess()) {
					logger.error("国信联通流量充值回调,获取供货单失败  上游订单号:  " + orderId + " msg: " + supplyOrderResult.getResultMsg());
					return;
				}
				SupplyOrder supplyOrder = supplyOrderResult.getModule();
				if (supplyOrder == null) {
					logger.error("国信联通流量充值回调,获取供货单为空 上游订单号:  " + orderId + " msg: " + supplyOrderResult.getResultMsg());
					return;
				}
				CallBack upstreamCallBack = new CallBack();
				upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
				if ("8".equals(status)) {// 失败
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					String errorCode = jo.getString("errorCode") ;
					String errorDesc = jo.getString("errorDesc") ;
					upstreamCallBack.setFailedCode(errorCode);
					upstreamCallBack.setFailedMsg(errorDesc);
				} else if ("7".equals(status)) {// 成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				} else {
					logger.warn("国信联通流量充值回调 不是最终状态，不做处理. 上游订单号:"+orderId);
					return;
				}
				// 处理订单 ,退款，更新等操作
				bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
			}
		});
		Map<String,String> map = new HashMap<String,String>();
		map.put("code", "0");
		map.put("msg", "接收成功");
		responseStr(response, JSONObject.fromObject(map).toString());
	}
}
