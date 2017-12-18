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
import com.longan.getway.upstream.flow.vo.TianyiFlowVO;
@Controller
public class TianyiFlowCallback extends BaseCallBackController{
	@RequestMapping(value = "callback/tianyiFlowCallback", method = RequestMethod.POST)
	@ResponseBody
	public void onRequest(@RequestBody String requestJson, HttpServletRequest request, HttpServletResponse response) {
		logger.warn("收到 ip : " + getRemoteIp(request) + "天翼800流量充值的结果通知请求: " + requestJson);
		final JSONObject jo = JSONObject.fromObject(requestJson);
		callbackExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String request_no = jo.getString("request_no");
				String result_code = jo.getString("result_code");
				String msg_id = jo.getString("msg_id");
				Result<SupplyOrder> supplyOrderResult = supplyOrderService.getSupplyOrderByIdAndTraderId(Long.valueOf(request_no), TianyiFlowVO.traderId);
				if (!supplyOrderResult.isSuccess()) {
					logger.error("天翼800流量充值回调,获取供货单失败  供货订单号:  " + request_no + " msg: " + supplyOrderResult.getResultMsg());
					return;
				}
				SupplyOrder supplyOrder = supplyOrderResult.getModule();
				if (supplyOrder == null) {
					logger.error("天翼800流量充值回调,获取供货单为空  供货订单号:  " + request_no + " msg: " + supplyOrderResult.getResultMsg());
					return;
				}
				supplyOrder.setUpstreamSerialno(msg_id);
				CallBack upstreamCallBack = new CallBack();
				upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
				if ("00000".equals(result_code)) {// 成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				} else if("10229".equals(result_code)) {// 失败
					logger.warn("天翼800流量充值回调 不是最终状态，不做处理. 供货订单号:"+request_no);
					return;
				}else{
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedMsg(TianyiFlowVO.resultMap.get(result_code));
				}
				// 处理订单 ,退款，更新等操作
				bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
			}
		});
		responseStr(response, "1");
	}
}
