package com.longan.getway.upstream.flow.service.impl;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.flow.vo.KuyichongFlowVO;

public class KuyichongFlowSupplyImpl extends BaseService implements UpstreamDirectSupplyService{
	private boolean isAsync = false;
	@Resource
	private LocalCachedService localCachedService;
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
		SupplyResult<String> supplyResult = KuyichongFlowVO.charge(String.valueOf(supplyOrder.getId()),supplyOrder.getItemUid(),itemSupply.getSupplyProductCode());
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
				String Code = jo.getString("Code");
				if("0".equals(Code) || "-10".equals(Code)){
					result.setStatus(SupplyResult.STATUS_SUCCESS);
				}else{
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultCode(Code);
					result.setResultMsg(jo.get("Message") == null ? "未返回失败的明确定义":jo.getString("Message"));
				}
			} catch (Exception e) {
				logger.error("parse from json error", e);
				result.setResultMsg("酷易充解析上游信息失败");
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				return result;
			}
		}
		return result ;
	}

	@Override
	public SupplyResult<ChargeInfo> chargeQuery(SupplyOrder supplyOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		// TODO Auto-generated method stub
		return null;
	}

}
