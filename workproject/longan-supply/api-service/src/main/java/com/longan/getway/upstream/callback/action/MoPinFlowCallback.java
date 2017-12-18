package com.longan.getway.upstream.callback.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

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
import com.longan.getway.upstream.flow.vo.MoPinFlowCallbackVO;

@Controller
public class MoPinFlowCallback extends BaseCallBackController {
	private final static String responseSuccess = "{\"status\":1}";

	private final static Long userId = Long.parseLong(Utils.getProperty("moPinFlow.traderId"));

	@RequestMapping(value = "callback/moPinFlowCallback", produces = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public void onRequest(HttpServletRequest request, HttpServletResponse response) {
		// logger.warn("Content-Type : " + request.getHeader("Content-Type"));
		//
		// logger.warn("Content-Length : " + request.getHeader("Content-Length"));
		String stringBody = null;
		try {
			stringBody = new String(getContentByteArray(request), "utf-8");
			logger.warn("收到 ip : " + getRemoteIp(request) + " 魔品流量包的结果通知请求: " + stringBody);
		} catch (IOException e) {
			logger.error("MoPinFlowCallback get data error 1", e);
			logger.warn("返回 ip : " + getRemoteIp(request) + responseSuccess);
			responseStr(response, responseSuccess);
			return;
		}
		if (StringUtils.isEmpty(stringBody)) {
			logger.error("MoPinFlowCallback get data error 2");
			logger.warn("返回 ip : " + getRemoteIp(request) + responseSuccess);
			responseStr(response, responseSuccess);
			return;
		}
		JSONObject jo = JSONObject.fromObject(stringBody);
		if (jo == null) {
			logger.error("MoPinFlowCallback get data error 3");
			logger.warn("返回 ip : " + getRemoteIp(request) + responseSuccess);
			responseStr(response, responseSuccess);
			return;
		}
		final MoPinFlowCallbackVO vo = (MoPinFlowCallbackVO) JSONObject.toBean(jo, MoPinFlowCallbackVO.class);
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
				Long supplyOrderId = Long.parseLong(vo.getChannelOrderId());
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
				if ("1".equals(vo.getStatus())) {
					// 成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				} else if ("0".equals(vo.getStatus())) {
					// 失败
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedMsg(vo.getStatus() + "_充值失败");
				} else {
					// 其他状态
					logger.error("非终极状态不做处理 状态： " + vo.getStatus());
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

	private byte[] getContentByteArray(HttpServletRequest request) throws IOException {
		if (null == request) {
			return new byte[0];
		}
		ByteArrayOutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			outputStream = new ByteArrayOutputStream();
			inputStream = request.getInputStream();
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = inputStream.read(b)) != -1) {
				outputStream.write(b, 0, len);
			}
			outputStream.flush();
			return outputStream.toByteArray();
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}
}
