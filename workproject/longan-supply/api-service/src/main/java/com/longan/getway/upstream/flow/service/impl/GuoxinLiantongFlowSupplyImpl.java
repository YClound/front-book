package com.longan.getway.upstream.flow.service.impl;

import javax.annotation.Resource;

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
import com.longan.getway.upstream.flow.vo.GuoxinLiantongFlowVO;
import com.longan.getway.upstream.flow.vo.JihuDayuFlowTransVO;

public class GuoxinLiantongFlowSupplyImpl extends BaseService implements UpstreamDirectSupplyService{
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
		Item item = localCachedService.getItem(supplyOrder.getItemId());
		if(item == null){
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("商品不存在或者已下架");
			return result;
		}
//		String flowType = item.getItemExt2();
		SupplyResult<String> supplyResult = GuoxinLiantongFlowVO.charge(itemSupply.getSupplyProductCode(), supplyOrder.getItemUid(), Long.toString(supplyOrder.getId()), "0");
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
					// 成功后处理
					String data = jo.getString("data");
					JSONObject djo = JSONObject.fromObject(data);
					supplyOrder.setUpstreamSerialno(djo.getString("orderId"));
				}else{//失败
					String msg = jo.getString("msg");
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultCode(code);
					result.setResultMsg(msg);
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
		SupplyResult<ChargeInfo> result = new SupplyResult<ChargeInfo>();
		SupplyResult<String> queryResult = GuoxinLiantongFlowVO.queryOrder(supplyOrder.getUpstreamSerialno(),Long.toString(supplyOrder.getId()));
		if(queryResult.isSuccess()){
			JSONObject jo = JSONObject.fromObject(queryResult.getModule());
			String code = jo.getString("code");
			if ("0".equals(code)) {// 成功
				// 成功后处理
				ChargeInfo chargeInfo = new ChargeInfo();
				result.setStatus(SupplyResult.STATUS_SUCCESS);
				String data = jo.getString("data");
				JSONObject djo = JSONObject.fromObject(data);
				String status = djo.getString("status");
				chargeInfo.setStatus(status);
				if("8".equals(status)){
					chargeInfo.setStatusDesc(djo.getString("errorDesc"));
				}else{
					chargeInfo.setStatusDesc(getMsg(status));
				}
				result.setModule(chargeInfo);
			}else{
				String msg = jo.getString("msg");
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultCode(code);
				result.setResultMsg(msg);
			}
		}else{
			result.setResultMsg("网络连接失败");
		}
		return result ;
	}
	private String getMsg(String status){
		if("0".equals(status))return "订单已受理";
		if("1".equals(status))return "订购中";
		if("7".equals(status))return "订购成功";
		if("8".equals(status))return "订购失败";
		return "未明确定义";
	}
	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		BalanceQueryInfo balanceQueryInfo = new BalanceQueryInfo();
		result.setModule(balanceQueryInfo);
		SupplyResult<String> supplyResult =GuoxinLiantongFlowVO.queryBalance();
		if (supplyResult.isFailed()) {
			result.setResultMsg(supplyResult.getResultMsg());
			return result;
		}
		if (supplyResult.isUnConfirm()) {
			result.setStatus(SupplyResult.STATUS_UNCONFIRM);
			result.setResultMsg("查询超时");
			return result;
		}
		if (supplyResult.isUndifend()) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SYSTEM);
			return result;
		}
		if (supplyResult.isSuccess()) {
			try {
				JSONObject jo = JSONObject.fromObject(supplyResult.getModule());
				String code = jo.getString("code");
				if ("0".equals(code)) {// 成功
					// 成功后处理
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					String data = jo.getString("data");
					JSONObject djo = JSONObject.fromObject(data);
					String accBalance = djo.getString("accBalance");
					String returnBalance = djo.getString("returnBalance");
					String creditLimit = djo.getString("creditLimit");
					String customerName = djo.getString("customerName");
					String info = String.format("【客户名称:%s】【账户余额:%s】【赠送余额:%s】【信用额度:%s】", customerName,accBalance,returnBalance,creditLimit);
					balanceQueryInfo.setBalance(info);
					balanceQueryInfo.setMsg(djo.toString());
				}else{
					String msg = jo.getString("msg");
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultCode(code);
					result.setResultMsg(msg);
				}
				
			} catch (Exception e) {
				logger.error("parse from json error", e);
				result.setResultMsg("解析上游信息失败");
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				return result;
			}
		}
		return result ;
	}
}
