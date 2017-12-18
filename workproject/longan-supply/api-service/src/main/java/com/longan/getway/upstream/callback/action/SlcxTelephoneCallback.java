package com.longan.getway.upstream.callback.action;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Utils;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.BaseCallBackController;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.common.domain.QueryRunnable;
import com.longan.getway.upstream.telephone.vo.SlcxCallbackResponse;
import com.longan.getway.upstream.telephone.vo.SlcxCallbackVO;
import com.thoughtworks.xstream.XStream;

@Controller
public class SlcxTelephoneCallback extends BaseCallBackController {

	@Resource
	private Map<String, UpstreamDirectSupplyService> upstreamSupplyMap;

	private final static Long userId = Long.parseLong(Utils.getProperty("slcxSupply.traderId"));

	@RequestMapping("callback/slcxTelephoneCallback")
	@ResponseBody
	public SlcxCallbackResponse onRequest(HttpServletRequest request) {
		SlcxCallbackResponse response = new SlcxCallbackResponse();
		String stringBody = null;
		try {
			stringBody = super.getRequestBody(request);
			logger.warn("收到 ip : " + getRemoteIp(request) + " 的结果通知请求: " + stringBody);
		} catch (IOException e) {
			logger.error("slcxTelephoneCallback get data error ", e);
			response.setError();
			logger.warn("返回 ip : " + getRemoteIp(request) + response.toString());
			return response;
		}

		if (StringUtils.isEmpty(stringBody)) {
			response.setError();
			logger.error("slcxTelephoneCallback 数据为空");
			logger.warn("返回 ip : " + getRemoteIp(request) + response.toString());
			return response;
		}

		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		xstream.alias("root", SlcxCallbackVO.class);

		final SlcxCallbackVO slcxCallbackVO = (SlcxCallbackVO) xstream.fromXML(stringBody);

		if (slcxCallbackVO == null) {
			response.setError();
			logger.error("slcxCallbackVO 为空");
			logger.warn("返回 ip : " + getRemoteIp(request) + response.toString());
			return response;
		}

		if (!slcxCallbackVO.checkSign()) {
			// 签名失败
			response.setError();
			logger.error("签名校验失败");
			logger.warn("返回 ip : " + getRemoteIp(request) + response.toString());
			return response;
		}

		callbackExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Long supplyOrderId = Long.parseLong(slcxCallbackVO.getJnoCli());
				Result<SupplyOrder> supplyOrderResult = supplyOrderService
						.getSupplyOrderByIdAndTraderId(supplyOrderId, userId);
				if (!supplyOrderResult.isSuccess()) {
					logger.error("上游回调,获取供货单失败  supplyOrderId:  " + supplyOrderId + " msg: "
							+ supplyOrderResult.getResultMsg());
					return;
				}

				SupplyOrder supplyOrder = supplyOrderResult.getModule();
				if (supplyOrder == null) {
					logger.error("上游回调,获取供货单为空 supplyOrder:  " + supplyOrder + " msg: "
							+ supplyOrderResult.getResultMsg());
					return;
				}

				CallBack upstreamCallBack = new CallBack();
				upstreamCallBack.setSupplyOrderId(supplyOrder.getId());

				// status 转换
				if ("000000".equals(slcxCallbackVO.getRetcode())) {
					// 成功
					upstreamCallBack.setStatus(SupplyResult.STATUS_SUCCESS);
				} else {
					// 失败
					upstreamCallBack.setStatus(SupplyResult.STATUS_FAILED);
					upstreamCallBack.setFailedMsg(slcxCallbackVO.getRetcode() + "_充值失败");
				}
				// 处理订单 ,退款，更新等操作
				bizDealService.dealBizOrder(supplyOrder, upstreamCallBack);

				// 特殊处理 ， 因为实际成本价格，是核单中获取的。

				UpstreamDirectSupplyService upstreamDirectService = null;
				try {
					upstreamDirectService = upstreamSupplyMap.get(String.valueOf(userId));
				} catch (Exception e) {
					logger.error("get supplyServier error", e);
					return;
				}

				if (upstreamDirectService == null) {
					return;
				}
				bizDealService.submitQueryRunnable(new SlcxTelephoneQueryRunnable("实力畅想话费",
						supplyOrder.getId(), supplyOrder.getBizOrderId(), upstreamDirectService));

			}
		});

		// 返回成功
		logger.warn("返回 ip : " + getRemoteIp(request) + response.toString());
		return response;
	}

	class SlcxTelephoneQueryRunnable extends QueryRunnable {
		private UpstreamDirectSupplyService upstreamDirectService;

		public SlcxTelephoneQueryRunnable(String queryName, Long supplyOrder, Long bizOrderId,
				UpstreamDirectSupplyService upstreamDirectService) {
			super(queryName, supplyOrder, bizOrderId, bizOrderService, bizDealService,
					supplyOrderService);
			this.upstreamDirectService = upstreamDirectService;
		}

		@Override
		protected CallBack chargeResultQuery() {
			Result<SupplyOrder> getSupplyOrderByIdResult = supplyOrderService
					.getSupplyOrderById(supplyOrderId);
			SupplyOrder supplyOrder = getSupplyOrderByIdResult.getModule();

			if (supplyOrder == null) {
				logger.error("getSupplyOrderById error supplyOrder is null id : " + supplyOrderId);
				return null;
			}

			if (upstreamDirectService == null) {
				logger.error("chargeResultQuery error upstreamDirectService is null");
				return null;
			}

			SupplyResult<ChargeInfo> chargeInfoResult = upstreamDirectService
					.chargeQuery(supplyOrder);

			ChargeInfo chargeInfo = chargeInfoResult.getModule();
			if (!chargeInfoResult.isSuccess() || chargeInfo == null) {
				logger.error("chargeQuery error : " + chargeInfoResult.getResultMsg());
				return null;
			}

			CallBack callBack = new CallBack();
			callBack.setSupplyOrderId(supplyOrderId);
			callBack.setActualCost(chargeInfo.getActualCost());

			if ("2".equals(chargeInfo.getStatus())) {
				callBack.setStatus(SupplyResult.STATUS_SUCCESS);
			} else if ("4".equals(chargeInfo.getStatus())) {
				callBack.setStatus(SupplyResult.STATUS_FAILED);
			} else {
				callBack.setStatus(SupplyResult.STATUS_UNCONFIRM);
			}
			return callBack;
		}

	}
}
