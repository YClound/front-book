package com.longan.getway.upstream.callback.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.client.remote.service.CallBackService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.unicomflow.vo.UnicomFlowHunanCallbackResponse;

@Controller
@RequestMapping("callback/testCallback")
public class TestCallback extends BaseCallBackController {

	@Resource
	private CallBackService callBackService;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public UnicomFlowHunanCallbackResponse onRequest(HttpServletRequest request) {
		final Long userId = 63l;
		UnicomFlowHunanCallbackResponse response = new UnicomFlowHunanCallbackResponse();

		super.logCallBack(request, "压测回调");
		final String id = request.getParameter("id");
		final String status = request.getParameter("status");

		callbackExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String transId = id;
				Result<SupplyOrder> supplyOrderResult = supplyOrderService.getSupplyOrderByUpstreamSerialno(transId, userId);
				if (!supplyOrderResult.isSuccess()) {
					logger.error("上游回调,获取供货单失败  serialno:  " + transId + " msg: " + supplyOrderResult.getResultMsg());
					return;
				}

				SupplyOrder supplyOrder = supplyOrderResult.getModule();
				if (supplyOrder == null) {
					logger.error("上游回调,获取订单为空 serialno:  " + transId + " msg: " + supplyOrderResult.getResultMsg());
					return;
				}

				CallBack upstreamCallBack = new CallBack();
				upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
				// status 转换
				if ("1".equals(status)) {
					// 成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				} else {
					// 失败
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedCode(status);
				}

				// 处理订单 ,退款，更新等操作
				bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);
			}
		});

		response.setCode("0");
		response.setMsg("SUCCESS");
		logger.warn("返回 ip : " + getRemoteIp(request) + response.toString());
		return response;
	}
}
