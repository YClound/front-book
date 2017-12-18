package com.longan.getway.upstream.flow.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.flow.domain.ThreeTFlow;

import net.sf.json.JSONObject;

public class ThreeTFlowSupplyImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;

	private static final Map<String, String> statusMap = new HashMap<String, String>();

	static {
		statusMap.put("0", "订单未提交");
		statusMap.put("1", "准备充值");
		statusMap.put("2", "订单取消");
		statusMap.put("3", "充值中");
		statusMap.put("4", "充值成功");
		statusMap.put("5", "充值失败");
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);

		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}

		ThreeTFlow threeTFlow = new ThreeTFlow();
		threeTFlow.setChannelOrderNo(supplyOrder.getId() + "");
		threeTFlow.setCustomer(supplyOrder.getItemUid());
		threeTFlow.setProdValue(itemSupply.getSupplyProductCode());

		SupplyResult<String> supplyResult = threeTFlow.supply();
		if (threeTFlow.getOrderNo() != null) {
			supplyOrder.setUpstreamSerialno(threeTFlow.getOrderNo());
		}

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
				JSONObject job = JSONObject.fromObject(supplyResult.getModule());
				String resultCode = job.get("resultCode").toString();
				String resultReason = job.get("resultReason").toString();

				if (!"1000".equals(resultCode)) {// 失败
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultMsg(resultReason);
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

	@Override
	public SupplyResult<ChargeInfo> chargeQuery(SupplyOrder supplyOrder) {
		SupplyResult<ChargeInfo> result = new SupplyResult<ChargeInfo>();
		if (StringUtils.isEmpty(supplyOrder.getUpstreamSerialno())) {
			result.setResultMsg("上游流水号为空，请登录对方平台进行核单");
			return result;
		}

		ThreeTFlow threeTFlow = new ThreeTFlow();
		threeTFlow.setOrderNo(supplyOrder.getUpstreamSerialno());

		SupplyResult<String> queryResult = threeTFlow.query();
		if (!queryResult.isSuccess()) {
			result.setResultMsg("查询错误");
			return result;
		}

		try {
			String re = queryResult.getModule();
			JSONObject jo = JSONObject.fromObject(re);
			if (!"1000".equals(jo.get("resultCode").toString())) {
				result.setResultMsg(jo.get("resultReason").toString());
				return result;
			}
			JSONObject order = (JSONObject) jo.get("order");
			String status = order.get("orderStatus").toString();

			ChargeInfo c = new ChargeInfo();
			c.setStatus(status);
			c.setStatusDesc(statusMap.get(status));

			result.setModule(c);
			result.setStatus(SupplyResult.STATUS_SUCCESS);

		} catch (Exception e) {
			result.setResultMsg("查询错误");
			return result;
		}

		return result;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		ThreeTFlow threeTFlow = new ThreeTFlow();

		SupplyResult<String> queryResult = threeTFlow.queryBalance();
		if (!queryResult.isSuccess()) {
			result.setResultMsg(queryResult.getResultMsg());
			return result;
		}

		String body = queryResult.getModule();
		JSONObject jo = JSONObject.fromObject(body);

		if (jo.get("balance") == null) {
			result.setResultMsg("查询错误");
			return result;
		}
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		BalanceQueryInfo balanceQueryInfo = new BalanceQueryInfo();
		balanceQueryInfo.setBalance(jo.get("balance").toString());
		result.setModule(balanceQueryInfo);
		return result;
	}

}
