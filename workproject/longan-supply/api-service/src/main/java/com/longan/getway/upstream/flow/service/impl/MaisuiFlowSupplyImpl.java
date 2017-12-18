package com.longan.getway.upstream.flow.service.impl;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.bouncycastle.jce.provider.JDKMessageDigest.MD5;
import org.omg.IOP.Encoding;

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
import com.longan.getway.upstream.flow.vo.MaisuiFlowVO;
import com.longan.getway.utils.Utils;

public class MaisuiFlowSupplyImpl extends BaseService implements UpstreamDirectSupplyService{
private static boolean isAsync = true;
	
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
		if (item == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("商品为空");
			return result;
		}
		String flowPackage = String.valueOf(getFlowSize(item.getItemExt1()));
		SupplyResult<String> supplyResult = MaisuiFlowVO.charge(item.getItemExt2(), String.valueOf(supplyOrder.getId()), supplyOrder.getItemUid(), flowPackage);
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
				Object o = jo.get("TaskID");
				if(o != null){//交易成功了
					String taskid = String.valueOf(o);
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					supplyOrder.setUpstreamSerialno(taskid);
					return result ;
				}
				result.setStatus(SupplyResult.STATUS_FAILED);
				String code = jo.getString("Code");
				result.setResultCode(code);
				Object msg = jo.get("Message");
				result.setResultMsg(msg == null ? MaisuiFlowVO.resultMap.get(code):String.valueOf(msg) );
				return result ;
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
		result.setStatus(SupplyResult.STATUS_FAILED);
		result.setResultMsg("未开放此接口");
		return result ;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		SupplyResult<String> supplyResult = MaisuiFlowVO.queryBalance();
		if (!supplyResult.isSuccess()) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg(supplyResult.getResultMsg());
			return result;
		}
		JSONObject jo = JSONObject.fromObject(supplyResult.getModule());
		BalanceQueryInfo info = new BalanceQueryInfo();
		String code = jo.get("Code") == null ?"nocode":jo.getString("Code");
		String balance = jo.get("Balance") == null ? "":jo.getString("Balance");
		String msg = jo.get("Message") == null ? MaisuiFlowVO.resultMap.get(code):jo.getString("Message");
		info.setMsg(msg);
		info.setBalance(balance);
		result.setModule(info);
		return result ;
	}
	public static  int getFlowSize(String sizeString) {
		if (sizeString.contains("M")) { // 将带有M的字符串（如10M，50M）转为数字
			String s = sizeString.substring(0, sizeString.length() - 1);
			int size = Integer.parseInt(s);
			return size;
		} else if (sizeString.contains("G")) {// 将带有G的面值转为数字
			String s = sizeString.substring(0, sizeString.length() - 1);
			int size = Integer.parseInt(s) * 1000;//这里不能乘以1024  因为上游的1G要传过去1000
			return size;
		}
		return 0;
	}
}
