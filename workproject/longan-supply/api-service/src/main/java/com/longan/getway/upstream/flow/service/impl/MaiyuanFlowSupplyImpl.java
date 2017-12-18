package com.longan.getway.upstream.flow.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.flow.domain.MaiyuanFlow;

import net.sf.json.JSONObject;

public class MaiyuanFlowSupplyImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;

	@Resource
	private LocalCachedService localCachedService;

	private static final Map<String, String> codeResultMap = new HashMap<String, String>();

	static {
		codeResultMap.put("001", "参数错误");
		codeResultMap.put("002", "充值号码不合法");
		codeResultMap.put("003", "帐号密码错误");
		codeResultMap.put("004", "余额不足");
		codeResultMap.put("005", "不存在指定流量包");
		codeResultMap.put("006", "不支持该地区");
		codeResultMap.put("007", "卡号或者密码错误");
		codeResultMap.put("008", "该卡已使用过");
		codeResultMap.put("009", "该卡不支持(移动/电信/联通)号码");
		codeResultMap.put("100", "签名验证错误");
		codeResultMap.put("999", "其他错误");
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

		MaiyuanFlow maiyuanFlow = new MaiyuanFlow();
		maiyuanFlow.setMobile(supplyOrder.getItemUid());
		maiyuanFlow.setOutTradeNo(supplyOrder.getId() + "");
		maiyuanFlow.setProductId(itemSupply.getSupplyProductCode());
		maiyuanFlow.setRange(0 + "");
		SupplyResult<String> supplyResult = maiyuanFlow.charge();

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
				String code = job.get("Code").toString();
				if (!"0".equals(code)) {// 失败
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultCode(code);
					result.setResultMsg(code + "_" + codeResultMap.get(code));
					return result;
				}
				String orderId = job.get("TaskID").toString();
				supplyOrder.setUpstreamSerialno(orderId);
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
		result.setResultMsg("该接口不支持核单");
		return result;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		MaiyuanFlow maiyuanFlow = new MaiyuanFlow();
		SupplyResult<String> queryResult = maiyuanFlow.queryBalance();
		if (!queryResult.isSuccess()) {
			result.setResultMsg(queryResult.getResultMsg());
			return result;
		}

		String body = queryResult.getModule();
		JSONObject jo = JSONObject.fromObject(body);

		if (jo.get("Balance") == null) {
			result.setResultMsg("查询错误");
			return result;
		}
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		BalanceQueryInfo balanceQueryInfo = new BalanceQueryInfo();
		balanceQueryInfo.setBalance(jo.get("Balance").toString());
		result.setModule(balanceQueryInfo);
		return result;
	}

}
