package com.longan.getway.upstream.callback.action;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
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
import com.longan.getway.upstream.unicomflow.service.impl.UnicomFlowHunanOfficServiceImpl;
import com.longan.getway.upstream.unicomflow.vo.UnicomFlowHunanCallbackResponse;
import com.longan.getway.upstream.unicomflow.vo.UnicomFlowHunanCallbackVO;

@Controller
@RequestMapping("callback/unicomFlowHunanCallback")
public class UnicomFlowHunanCallback extends BaseCallBackController {

	@Resource
	private CallBackService callBackService;

	private Long userId = Long.parseLong(com.longan.biz.utils.Utils
			.getProperty("unicomFlowHunanOff.traderId"));

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public UnicomFlowHunanCallbackResponse onRequest(HttpServletRequest request) {
		UnicomFlowHunanCallbackResponse response = new UnicomFlowHunanCallbackResponse();
		String stringBody = null;
		try {
			stringBody = super.getRequestBody(request);
			logger.warn("收到 ip : " + getRemoteIp(request) + " 的结果通知请求: " + stringBody);
		} catch (IOException e) {
			logger.error("unicomFlowHunanCallback get data error ", e);
			response.setCode("9");
			response.setMsg("读取信息失败");
			logger.warn("返回 ip : " + getRemoteIp(request) + response.toString());
			return response;
		}

		JSONObject jo = null;
		if (StringUtils.isEmpty(stringBody)) {
			response.setCode("9");
			response.setMsg("读取信息失败");
			logger.warn("返回 ip : " + getRemoteIp(request) + response.toString());
			return response;
		}
		jo = JSONObject.fromObject(stringBody);
		if (jo == null) {
			response.setCode("9");
			response.setMsg("读取信息失败");
			logger.warn("返回 ip : " + getRemoteIp(request) + response.toString());
			return response;
		}

		final UnicomFlowHunanCallbackVO vo = (UnicomFlowHunanCallbackVO) JSONObject.toBean(jo,
				UnicomFlowHunanCallbackVO.class);

		if (!vo.checkSign()) {
			// 签名失败
			response.setCode("1");
			response.setMsg("签名失败");
			logger.warn("返回 ip : " + getRemoteIp(request) + response.toString());
			return response;
		}

		callbackExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String transId = vo.getTransid();
				Result<SupplyOrder> supplyOrderResult = supplyOrderService
						.getSupplyOrderByUpstreamSerialno(transId, userId);
				if (!supplyOrderResult.isSuccess()) {
					logger.error("上游回调,获取供货单失败  serialno:  " + transId + " msg: "
							+ supplyOrderResult.getResultMsg());
					return;
				}

				SupplyOrder supplyOrder = supplyOrderResult.getModule();
				if (supplyOrder == null) {
					logger.error("上游回调,获取订单为空 serialno:  " + transId + " msg: "
							+ supplyOrderResult.getResultMsg());
					return;
				}

				CallBack upstreamCallBack = new CallBack();
				upstreamCallBack.setSupplyOrderId(supplyOrder.getId());
				// status 转换
				if ("0".equals(vo.getStatus())) {
					// 成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				} else {
					// 失败
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedCode(vo.getStatus());
					upstreamCallBack.setFailedMsg(UnicomFlowHunanOfficServiceImpl.queryResultMap
							.get(vo.getStatus()));
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
