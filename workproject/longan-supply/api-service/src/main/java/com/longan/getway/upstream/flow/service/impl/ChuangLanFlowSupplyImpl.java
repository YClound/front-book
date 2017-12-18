package com.longan.getway.upstream.flow.service.impl;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.flow.vo.ChuangLanFlowVO;
import com.longan.getway.upstream.flow.vo.GuoxinLiantongFlowVO;
import com.longan.getway.upstream.flow.vo.JihuDayuFlowTransVO;

public class ChuangLanFlowSupplyImpl extends BaseService implements UpstreamDirectSupplyService{
	private boolean isAsync = true;
	@Resource
	private LocalCachedService localCachedService;
	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId()); // 根据供应商商品ID，查看是否存在该商品
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		SupplyResult<String> supplyResult = ChuangLanFlowVO.charge(supplyOrder.getItemUid(), itemSupply.getSupplyProductCode(), String.valueOf(supplyOrder.getId()));
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
				String code = jo.getString("code");
				if ("0".equals(code)) {// 成功
					result.setStatus(SupplyResult.STATUS_SUCCESS);
				}else{//失败
					String desc = jo.getString("desc");
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultCode(code);
					result.setResultMsg(desc);
				}
			} catch (Exception e) {
				logger.error("parse from json error", e);
				result.setResultMsg("解析上游信息失败");
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				return result;
			}
		}
		return result;
	}
	@Override
	public SupplyResult<ChargeInfo> chargeQuery(SupplyOrder supplyOrder) {
		String qOrderId = String.valueOf(supplyOrder.getId());
		SupplyResult<ChargeInfo> result = new SupplyResult<ChargeInfo>();
		SupplyResult<String> queryResult = ChuangLanFlowVO.chargeQuery(qOrderId);
		if(queryResult.isSuccess()){
			JSONArray ja = JSONArray.fromObject(queryResult.getModule());
			Object [] orders = ja.toArray();
			for(Object o : orders){
				JSONObject jo = JSONObject.fromObject(o);
				String orderId = jo.getString("ext_id");
				if(qOrderId.equals(orderId)){
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					String code = jo.getString("code");
					ChargeInfo info = new ChargeInfo();
					info.setStatus(code);
					if("0".equals(code)){
						info.setStatusDesc("充值成功");
					}else if("000099".equals(code)){
						info.setStatusDesc("充值中");
					}else if("000015".equals(code)){
						info.setStatusDesc(jo.get("desc") == null ? "充值失败":jo.getString("desc"));
					}
					result.setModule(info);
					return result ;
				}
			}
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("未查询到相关信息");
		}else{
			result.setResultMsg("网络连接失败");
		}
		return result ;
	}
	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		result.setStatus(SupplyResult.STATUS_FAILED);
		result.setResultMsg("未开放此接口");
		return result ;
	}
}
