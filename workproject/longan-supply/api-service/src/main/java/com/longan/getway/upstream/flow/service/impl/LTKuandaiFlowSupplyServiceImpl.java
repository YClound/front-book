package com.longan.getway.upstream.flow.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.longan.getway.upstream.flow.vo.LTKuandaiFlowVO;

public class LTKuandaiFlowSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService{
	private boolean isAsync = true;
	@Resource
	private LocalCachedService localCachedService;
	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);
		Item item = localCachedService.getItem(supplyOrder.getItemId());
		if(item == null){
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("商品不存在或者已下架");
			return result;
		}
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		SupplyResult<String> supplyResult = LTKuandaiFlowVO.charge
				(String.valueOf(supplyOrder.getId()), supplyOrder.getItemUid(), itemSupply.getSupplyProductCode(), null, timeStamp);
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
				String retCode = jo.getString("respCode");
				String msg = LTKuandaiFlowVO.codeResultMap.get(retCode);
				result.setResultMsg(msg == null ? retCode:msg);
				if("0000".equals(retCode) || "0001".equals(retCode) || "3011".equals(retCode) || "9001".equals(retCode)
						|| "9002".equals(retCode) || "9102".equals(retCode)){
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					try {
						String orderId = jo.getString("orderId");
						supplyOrder.setUpstreamSerialno(orderId);
					} catch (Exception e) {
						logger.error("联通宽带流量充值未返回订单号,返回数据:"+supplyResult.getModule());
						result.setResultMsg("未返回订单号");
						result.setStatus(SupplyResult.STATUS_UNCONFIRM);
						return result;
					}
					
				}else{
					result.setStatus(SupplyResult.STATUS_FAILED);
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

	@Override
	public SupplyResult<ChargeInfo> chargeQuery(SupplyOrder supplyOrder) {
		SupplyResult<ChargeInfo> result = new SupplyResult<ChargeInfo>();
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		SupplyResult<String> queryResult = LTKuandaiFlowVO.queryOrder(supplyOrder.getUpstreamSerialno(), timeStamp);
		if(queryResult.isSuccess()){
			JSONObject jo = JSONObject.fromObject(queryResult.getModule());
			result.setStatus(SupplyResult.STATUS_SUCCESS);
			ChargeInfo chargeInfo = new ChargeInfo();
			String retCode = jo.getString("respCode");
			String msg = LTKuandaiFlowVO.codeResultMap.get(retCode);
			chargeInfo.setStatus(retCode);
			chargeInfo.setStatusDesc(msg);
		}else{
			result.setResultMsg("网络连接失败");
		}
		return result;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		// TODO Auto-generated method stub
		return null;
	}

}
