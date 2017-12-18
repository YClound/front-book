package com.longan.getway.upstream.flow.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.cached.MemcachedService;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.flow.vo.JihuDayuFlowTransVO;
import com.longan.getway.utils.Utils;

public class JihuDayuFlowSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService{
	private boolean isAsync = true;
	@Resource
	private LocalCachedService localCachedService;
	@Resource
	private MemcachedService memcachedService ;
	
	public static String RES_ORDERSTATUS = "orderStatus";
	public static String RES_CODE = "errCode";
	public static String RES_MSG = "errDesc"; 	
	
	public static String ORDER_SUCCESS = "SUCCESS";//订单成功
	public static String ORDER_UNDERWAY = "UNDERWAY";//订单充值中
	public static String ORDER_FAILED = "FAILED";//订单失败
	public static String ORDER_NOT_EXIST = "ORDER_NOT_EXIST";//订单已存在
	public static String REQUEST_FAILED = "REQUEST_FAILED";//请求失败
	
	public static Map<String,String> orderStatusMap = new HashMap<String, String>();
	static{
		orderStatusMap.put(ORDER_SUCCESS, "充值成功");
		orderStatusMap.put(ORDER_UNDERWAY, "充值中");
		orderStatusMap.put(ORDER_FAILED, "充值失败");
		orderStatusMap.put(ORDER_NOT_EXIST, "订单已存在");
		orderStatusMap.put(REQUEST_FAILED, "请求失败");
	}
	
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
		String flowType = item.getItemExt2();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		String upStreamOrderId = parseUpStreamNo(Long.toString(supplyOrder.getId()));
		supplyOrder.setUpstreamSerialno(upStreamOrderId);
		JihuDayuFlowTransVO vo = new JihuDayuFlowTransVO(upStreamOrderId, supplyOrder.getItemUid(), flowType,itemSupply.getSupplyProductCode());
		SupplyResult<String> supplyResult = vo.charge();
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
				String orderStatus = jo.getString(RES_ORDERSTATUS);
				if(ORDER_UNDERWAY.equals(orderStatus)){
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					supplyOrder.setUpstreamSerialno(upStreamOrderId);
				}else if(ORDER_FAILED.equals(orderStatus) || REQUEST_FAILED.equals(orderStatus)){
					String errorMsg = jo.getString(RES_MSG);
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultMsg(errorMsg);
				}else{
					String errorMsg = jo.getString(RES_MSG);
					result.setStatus(SupplyResult.STATUS_UNCONFIRM);
					result.setResultMsg(errorMsg);
					return result;
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
		JihuDayuFlowTransVO vo = null;
		if(supplyOrder.getUpstreamSerialno() == null){
			String upStreamOrderId = parseUpStreamNo(Long.toString(supplyOrder.getId()));
			vo = new JihuDayuFlowTransVO(upStreamOrderId);
		}else{
			vo = new JihuDayuFlowTransVO(supplyOrder.getUpstreamSerialno());
		}
	
		SupplyResult<String> queryResult = vo.orderQuery();
		if(queryResult.isSuccess()){
			ChargeInfo chargeInfo = new ChargeInfo();
			result.setStatus(SupplyResult.STATUS_SUCCESS);
			JSONObject jo = JSONObject.fromObject(queryResult.getModule());
			String code = jo.getString(RES_CODE);
			String msg = jo.getString(RES_MSG);
			String orderStatus = jo.getString(RES_ORDERSTATUS);
			chargeInfo.setStatus(orderStatusMap.get(orderStatus));
			chargeInfo.setStatusDesc(msg+"【"+code+"】");
			result.setModule(chargeInfo);
		}else{
			result.setResultMsg("网络连接失败");
		}
		return result ;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		JihuDayuFlowTransVO vo = new JihuDayuFlowTransVO();
		SupplyResult<String> queryResult = vo.balanceQuery();
		if (queryResult.isSuccess()) {
			BalanceQueryInfo info = new BalanceQueryInfo();
			JSONObject jo = JSONObject.fromObject(queryResult.getModule());
			String queryState = jo.getString("queryState");
			String code = jo.getString(RES_CODE);
			String msg = jo.getString(RES_MSG);
			String balance = jo.getString("balance");
			if(ORDER_SUCCESS.equals(queryState)){
				result.setStatus(SupplyResult.STATUS_SUCCESS);
				result.setModule(info);
				info.setBalance(balance);
			}else{
				info.setMsg(msg+"【"+code+"】");
			}
		} else {
			result.setResultMsg("网络连接失败");
		}
		return result;
	}
	
	private String parseUpStreamNo(String idStr){
		int length = idStr.length();
		if(length<17){
			return Utils.FormatStringAddZero(idStr, 17);
		}
		return idStr;
	}

}
