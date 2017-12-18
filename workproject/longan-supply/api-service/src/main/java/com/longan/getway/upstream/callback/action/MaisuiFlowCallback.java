package com.longan.getway.upstream.callback.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.flow.vo.MaisuiFlowVO;

@Controller
public class MaisuiFlowCallback extends BaseCallBackController {
	@Resource
	private LocalCachedService localCachedService;
	
	@RequestMapping(value = "callback/maisuiFlowCallback", method = RequestMethod.POST)
	@ResponseBody
	public void onRequest(@RequestBody String requestJson, HttpServletRequest request, HttpServletResponse response) {
		String fromIp = getRemoteIp(request);
		try {
			requestJson = URLDecoder.decode(requestJson, "utf-8");
			requestJson = requestJson.substring(0, requestJson.length()-1);
		} catch (UnsupportedEncodingException e) {
			logger.warn("收到 ip : " + fromIp + "麦穗流量充值的结果通知请求: 解码失败!data:"+requestJson);
		} 
		logger.warn("收到 ip : " + fromIp + "麦穗流量充值的结果通知请求: " + requestJson);
		JSONArray ja = JSONArray.fromObject(requestJson);
		Object [] orders = ja.toArray();
		if(orders != null && orders.length>0){
			for(Object o : orders){
				final JSONObject jo = JSONObject.fromObject(o);
				callbackExecutor.execute(new Runnable() {
					@Override
					public void run() {
						 String TaskID = jo.getString("TaskID");
						 String status = jo.getString("Status");
						Result<SupplyOrder> supplyOrderResult = supplyOrderService.getSupplyOrderByUpstreamSerialno(TaskID, MaisuiFlowVO.traderId);
						if (!supplyOrderResult.isSuccess()) {
							logger.error("麦穗流量充值回调,获取供货单失败  上游订单号:  " + TaskID + " msg: " + supplyOrderResult.getResultMsg());
							return;
						}
						SupplyOrder supplyOrder = supplyOrderResult.getModule();
						if (supplyOrder == null) {
							logger.error("麦穗流量充值回调,获取供货单为空 .上游订单号:  " + TaskID + " msg: " + supplyOrderResult.getResultMsg());
							return;
						}
						CallBack upstreamCallBack = new CallBack();
						upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
						if ("4".equals(status)) {// 成功
							upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
						} else if ("5".equals(status)) {
							upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
							upstreamCallBack.setFailedCode(status);
							upstreamCallBack.setFailedMsg(jo.get("ReportCode") == null ? "充值失败":jo.getString("ReportCode"));
						} else {
							logger.error("麦穗流量充值回调  非终极状态不做处理 状态： " + status);
							return;
						}
						// 处理订单 ,退款，更新等操作
						bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
					}
				});
			}
		}
		responseStr(response, "ok");
}
}
