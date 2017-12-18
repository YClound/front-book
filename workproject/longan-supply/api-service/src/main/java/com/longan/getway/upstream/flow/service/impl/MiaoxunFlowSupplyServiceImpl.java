package com.longan.getway.upstream.flow.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.flow.vo.MiaoxunFlowVO;

import net.sf.json.JSONObject;

public class MiaoxunFlowSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService{
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
		Date now = new Date();
		String otime = new SimpleDateFormat("yyyyMMddHHmmss").format(now);
		String range = item.getItemExt2();
		int size = getSize(item.getItemExt1());
		SupplyResult<String> supplyResult = 
					MiaoxunFlowVO.charge(Long.toString(supplyOrder.getId()), supplyOrder.getItemUid(), String.valueOf(size), range, otime);
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
				String retCode = jo.getString("retCode");
				if("0".equals(retCode)){
					result.setStatus(SupplyResult.STATUS_SUCCESS);
				}else{
					result.setStatus(SupplyResult.STATUS_FAILED);
					String msg = MiaoxunFlowVO.codeResultMap.get(retCode);
					result.setResultMsg(msg == null ? jo.getString("retMsg"): msg);
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
		result.setResultMsg("无核单接口");
		return result ;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		Date now = new Date();
		String otime = new SimpleDateFormat("yyyyMMddHHmmss").format(now);
		SupplyResult<String> queryResult =   MiaoxunFlowVO.balanceQuery(otime);
		if (!queryResult.isSuccess()) {
			result.setResultMsg(queryResult.getResultMsg());
			return result;
		}
		JSONObject jo = JSONObject.fromObject(queryResult.getModule());
		String retCode = jo.getString("retCode");
		String retMsg = jo.getString("retMsg");
		if("0".equals(retCode)){
			result.setStatus(SupplyResult.STATUS_SUCCESS);
			BalanceQueryInfo info = new BalanceQueryInfo();
			info.setBalance(retMsg);
			result.setModule(info);
			info.setMsg("查询成功");
		}else{
//			BalanceQueryInfo info = new BalanceQueryInfo();
//			info.setMsg(retMsg == null ? jo.getString("retMsg"): retMsg);
			result.setStatus(SupplyResult.STATUS_FAILED);
			String msg = MiaoxunFlowVO.codeResultMap.get(retCode);
			result.setResultMsg(msg == null ? retMsg : msg);
		}
		return result ;
		
	}
	private int getSize(String sizeString) {
		if (sizeString.contains("M")) { // 将带有M的字符串（如10M，50M）转为数字
			String s = sizeString.substring(0, sizeString.length() - 1);
			int size = Integer.parseInt(s);
			return size;
		} else if (sizeString.contains("G")) {// 将带有G的面值转为数字
			String s = sizeString.substring(0, sizeString.length() - 1);
			int size = Integer.parseInt(s) * 1024;
			return size;
		}
		return 0;
	}

}
