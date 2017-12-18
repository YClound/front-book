package com.longan.getway.upstream.callback.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
@Controller
public class ChuangLanFlowCallback extends BaseCallBackController{
	@RequestMapping(value = "callback/chuangLanFlowCallback", method = RequestMethod.POST)
	@ResponseBody
	public void onRequest(@RequestBody String requestJson, HttpServletRequest request, HttpServletResponse response) {
		logger.warn("收到 ip : " + getRemoteIp(request) + "创蓝流链流量充值的结果通知请求: " + requestJson);
		try {
			requestJson = URLDecoder.decode(requestJson, "utf-8");
			String last = requestJson.substring(requestJson.length()-1);
			if("=".equals(last)){
				requestJson = requestJson.substring(0, requestJson.length()-1);
			}
		} catch (UnsupportedEncodingException e) {
			logger.warn("收到 ip : " + getRemoteIp(request) + "创蓝流链流量充值的结果通知请求解码失败");
		}
		final JSONObject jo = JSONObject.fromObject(requestJson);
		callbackExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try{
				String status = jo.getString("code");
				String orderId = jo.getString("ext_id");
				Result<SupplyOrder> supplyOrderResult = supplyOrderService.getSupplyOrder(Long.valueOf(orderId));
				if (!supplyOrderResult.isSuccess()) {
					logger.error("创蓝流链流量充值回调,获取供货单失败  上游订单号:  " + orderId + " msg: " + supplyOrderResult.getResultMsg());
					return;
				}
				SupplyOrder supplyOrder = supplyOrderResult.getModule();
				if (supplyOrder == null) {
					logger.error("创蓝流链流量充值回调,获取供货单为空 上游订单号:  " + orderId + " msg: " + supplyOrderResult.getResultMsg());
					return;
				}
				CallBack upstreamCallBack = new CallBack();
				upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
				if ("0".equals(status)) {//成功了
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				} else {
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedCode(status);
					upstreamCallBack.setFailedMsg(jo.get("desc") == null ? "充值失败":URLDecoder.decode(jo.getString("desc"), "utf-8"));
				}
				// 处理订单 ,退款，更新等操作
				bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
				}catch(Exception e){
					logger.error("创蓝流链流量充值回调处理异常");
					e.printStackTrace();
				}
				
			}
		});
		Map<String,String> map = new HashMap<String,String>();
		map.put("code", "0");
		map.put("msg", "接收成功");
		responseStr(response, JSONObject.fromObject(map).toString());
	}
}
