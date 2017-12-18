package com.longan.getway.upstream.flow.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.common.domain.QueryRunnable;
import com.longan.getway.upstream.flow.domain.YongLiuFlowBalance;
import com.longan.getway.upstream.flow.domain.YongLiuFlowQuery;
import com.longan.getway.upstream.flow.domain.YongLiuFlowSupply;

import net.sf.json.JSONObject;

public class YongLiuFlowSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;
	// private static final String supplyName = "永流流量";

	@Resource
	private LocalCachedService localCachedService;

	public static final Map<String, String> codeResultMap = new HashMap<String, String>();

	static {

		codeResultMap.put("0000", "请求成功");
		codeResultMap.put("1001", "缺少必要参数");
		codeResultMap.put("1002", "未开通流量充值权限");
		codeResultMap.put("1003", "非法访问ip");
		codeResultMap.put("1004", "签名失败");
		codeResultMap.put("1005", "未开通此商品权限");
		codeResultMap.put("1006", "商品已下架");
		codeResultMap.put("1007", "供应关闭");
		codeResultMap.put("1008", "创建订单失败");
		codeResultMap.put("1009", "支付失败");
		codeResultMap.put("2001", "交易失败");
		codeResultMap.put("3001", "无此订单");
		codeResultMap.put("9999", "异常");
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);

		YongLiuFlowSupply vo = new YongLiuFlowSupply();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}

		vo.setGoodsId(itemSupply.getSupplyProductCode());
		vo.setPhone(supplyOrder.getItemUid());
		vo.setTransNo(supplyOrder.getId() + "");

		SupplyResult<String> supplyResult = vo.supply();
		if (supplyResult.isFailed()) {
			result.setResultMsg(supplyResult.getResultMsg());
			return result;
		}

		if (supplyResult.isUnConfirm()) {
			result.setStatus(SupplyResult.STATUS_UNCONFIRM);
			result.setResultMsg("充值处理中");
			return result;
		}

		if (supplyResult.isUndifend()) {
			result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SYSTEM);
			return result;
		}
		if (supplyResult.isSuccess()) {
			try {
				JSONObject jo = JSONObject.fromObject(supplyResult.getModule());
				String transCode = jo.getString("transCode");
				String msg = jo.getString("transMsg");

				if ("0000".equals(transCode)) {
					String orderId = jo.getString("orderNo");
					supplyOrder.setUpstreamSerialno(orderId);
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					// bizDealService.submitQueryRunnable(new
					// YongLiuQueryRunnable(supplyName, supplyOrder.getId(),
					// supplyOrder.getBizOrderId()));
					return result;
				} else if ("9999".equals(transCode)) {
					logger.warn("yongliu supply code 9999 unconform msg :" + msg);
					result.setStatus(SupplyResult.STATUS_UNCONFIRM);
					return result;
				} else {
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultCode(transCode);
					result.setResultMsg(transCode + "_" + msg);
					return result;
				}

			} catch (Exception e) {
				logger.error("parse from json error", e);
				result.setResultMsg("解析上游信息失败");
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				return result;
			}
		}
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		return result;
	}

	class YongLiuQueryRunnable extends QueryRunnable {
		public YongLiuQueryRunnable(String queryName, Long supplyOrderId, Long bizOrderId) {
			super(queryName, supplyOrderId, bizOrderId, bizOrderService, bizDealService, supplyOrderService);
		}

		@Override
		protected CallBack chargeResultQuery() {
			Result<SupplyOrder> getSupplyOrderByIdResult = supplyOrderService.getSupplyOrderById(supplyOrderId);
			SupplyOrder supplyOrder = getSupplyOrderByIdResult.getModule();

			if (supplyOrder == null) {
				logger.error("getSupplyOrderById error supplyOrder is null id : " + supplyOrderId);
				return null;
			}

			SupplyResult<ChargeInfo> chargeInfoResult = chargeQuery(supplyOrder);
			ChargeInfo chargeInfo = chargeInfoResult.getModule();

			if (chargeInfo == null) {
				logger.error("chargeQuery error  chargeInfo is null supplyOrderId : " + supplyOrderId);
				return null;
			}

			CallBack callBack = new CallBack();
			callBack.setSupplyOrderId(supplyOrderId);

			if ("success".equals(chargeInfo.getStatus())) {
				callBack.setStatus(SupplyResult.STATUS_SUCCESS);
			} else if ("failed".equals(chargeInfo.getStatus())) {
				callBack.setStatus(SupplyResult.STATUS_FAILED);
			} else {
				callBack.setStatus(SupplyResult.STATUS_UNCONFIRM);
			}
			return callBack;
		}
	}

	@Override
	public SupplyResult<ChargeInfo> chargeQuery(SupplyOrder supplyOrder) {
		SupplyResult<ChargeInfo> result = new SupplyResult<ChargeInfo>();

		YongLiuFlowQuery yongLiuFlowQuery = new YongLiuFlowQuery();
		yongLiuFlowQuery.setTransNo(supplyOrder.getId() + "");
		SupplyResult<String> queryResult = yongLiuFlowQuery.query();

		if (queryResult.isSuccess()) {
			JSONObject jo = JSONObject.fromObject(queryResult.getModule());

			String transCode = jo.getString("transCode");
			String status = jo.getString("status");

			if ("0000".equals(transCode)) {
				result.setStatus(SupplyResult.STATUS_SUCCESS);
				ChargeInfo chargeInfo = new ChargeInfo();
				chargeInfo.setStatus(status);

				if ("success".equals(status)) {
					chargeInfo.setStatusDesc("充值成功");
				} else if ("failed".equals(status)) {
					chargeInfo.setStatusDesc("充值失败");
				} else {
					chargeInfo.setStatusDesc("处理中");
				}
				result.setModule(chargeInfo);
			} else {
				result.setResultMsg(codeResultMap.get(transCode));
			}

		} else {
			result.setResultMsg("网络连接失败");
		}
		return result;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		YongLiuFlowBalance yongLiuFlowBalance = new YongLiuFlowBalance();
		SupplyResult<String> query = yongLiuFlowBalance.query();

		if (query.isSuccess()) {
			JSONObject jo = JSONObject.fromObject(query.getModule());

			String transCode = jo.getString("transCode");

			if ("0000".equals(transCode)) {
				BalanceQueryInfo balanceQueryInfo = new BalanceQueryInfo();
				result.setStatus(SupplyResult.STATUS_SUCCESS);
				balanceQueryInfo.setBalance(jo.getString("balance"));
				result.setModule(balanceQueryInfo);
			} else {
				result.setResultMsg(codeResultMap.get(transCode));
			}

		} else {
			result.setResultMsg("网络连接失败");
		}
		return result;
	}

}
