package com.longan.getway.upstream.callback.action;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.domain.CallBack;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class MaiyuanFlowCallback extends BaseCallBackController {
	private final static String responseSuccess = "ok";

	private final static Long userId = Long.parseLong(Utils.getProperty("maiyuanFlow.traderId"));

	@RequestMapping(value = "callback/jiehangCallback.do")
	@ResponseBody
	public void onRequest(HttpServletRequest request, HttpServletResponse response) {
		String stringBody = null;
		super.logCallBack(request, "迈远流量包");
		@SuppressWarnings("unchecked")
		Map<String, String[]> map = (Map<String, String[]>) request.getParameterMap();
		StringBuffer sb = new StringBuffer("");
		for (Entry<String, String[]> entry : map.entrySet()) {
			sb.append(entry.getKey());
		}
		stringBody = sb.toString();
		if (StringUtils.isEmpty(stringBody)) {
			logger.error("MaiyuanFlowCallback get data error 2");
			logger.warn("返回 ip : " + getRemoteIp(request) + responseSuccess);
			responseStr(response, responseSuccess);
			return;
		}

		try {
			JSONArray ja = JSONArray.fromObject(stringBody);
			if (ja == null) {
				logger.error("MaiyuanFlowCallback get data error 3");
				logger.warn("返回 ip : " + getRemoteIp(request) + responseSuccess);
				responseStr(response, responseSuccess);
				return;
			}

			for (Object object : ja) {
				JSONObject jo = (JSONObject) object;
				final String status = jo.getString("Status").toString();
				final String taskId = jo.getString("TaskID").toString();
				callbackExecutor.execute(new Runnable() {
					@Override
					public void run() {
						Result<SupplyOrder> supplyOrderResult = supplyOrderService.getSupplyOrderByUpstreamSerialno(taskId, userId);
						if (!supplyOrderResult.isSuccess()) {
							logger.error("上游回调,获取供货单失败  upstreamNO:  " + taskId + " msg: " + supplyOrderResult.getResultMsg());
							return;
						}

						SupplyOrder supplyOrder = supplyOrderResult.getModule();
						if (supplyOrder == null) {
							logger.error("上游回调,获取供货单为空 upstreamNO:  " + taskId + " msg: " + supplyOrderResult.getResultMsg());
							return;
						}

						CallBack upstreamCallBack = new CallBack();
						upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
						if ("4".equals(status)) {
							// 成功
							upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
						} else if ("5".equals(status)) {
							// 失败
							upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
							upstreamCallBack.setFailedMsg(status + "_充值失败");
						} else {
							// 其他状态
							logger.error("非终极状态不做处理 状态： " + status);
							return;
						}
						// 处理订单 ，退款，更新，通知下游等操作
						bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
					}
				});

			}
		} catch (Exception e) {
			logger.error("解析失败", e);
		}
		// 返回成功
		logger.warn("返回 ip : " + getRemoteIp(request) + responseSuccess);
		responseStr(response, responseSuccess);
	}
}
