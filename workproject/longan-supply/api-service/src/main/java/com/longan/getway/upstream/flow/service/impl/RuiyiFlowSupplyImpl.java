package com.longan.getway.upstream.flow.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.cached.MemcachedService;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.flow.vo.RuiyiFlowTransVO;
/**
 * 瑞翼信息流量充值
 * @author Administrator
 *
 */
public class RuiyiFlowSupplyImpl extends BaseService implements UpstreamDirectSupplyService{
	private boolean isAsync = true;
	@Resource
	private LocalCachedService localCachedService;
	@Resource
	private MemcachedService memcachedService ;
	
	
	public static final Map<String, String> codeResultMap = new HashMap<String, String>();
	public static final Map<String, String> orderStateMap = new HashMap<String, String>();
	public static String  cachePublicKey = "RuiyiFlow_publicKey";//缓存publickey的key值
	
	/**
	 * 请求返回json key
	 */
	public static String RES_CODE = "code";
	public static String RES_MSG = "msg";
	public static String RES_DATA = "data";
	
	public static String SUCCESS_CODE ="0000";
	
	static {
		codeResultMap.put("0000", "请求成功");
		codeResultMap.put("0001", "请求失败");
		codeResultMap.put("1001", "未知错误");
		codeResultMap.put("1002", "系统异常");
		codeResultMap.put("1003", "接口鉴权失败");
		codeResultMap.put("2001", "未知参数错误");
		codeResultMap.put("2002", "必填参数为空");
		codeResultMap.put("2003", "参数范围错误");
		
		orderStateMap.put("0", "初始状态");
		orderStateMap.put("1", "成功");
		orderStateMap.put("2", "失败");
		orderStateMap.put("3", "异常");
		orderStateMap.put("4", "处理中");
		orderStateMap.put("5", "未知");
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
		RuiyiFlowTransVO vo = new RuiyiFlowTransVO(supplyOrder.getItemUid(), itemSupply.getSupplyProductCode(), String.valueOf(supplyOrder.getId()));
		//设置publicKey
		String setPublickeyResult = setPublicKey(vo);
		if(setPublickeyResult != null){
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg(setPublickeyResult);
			return result;
		}
		SupplyResult<String> supplyResult = vo.sendCharge();
		logger.warn("瑞翼流量充值返回: " + supplyResult.getModule());
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
				String code = jo.getString(RES_CODE);
				if(isSuccess(code)){
					String data = jo.getString(RES_DATA);
					JSONObject dataJO = JSONObject.fromObject(data);
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					supplyOrder.setUpstreamSerialno(dataJO.getString("orderNo"));
				}else if(isUnConfirm(code)){
					result.setStatus(SupplyResult.STATUS_UNCONFIRM);
					String msg = jo.getString(RES_MSG);
					result.setResultMsg(msg);
					return result;
				}else{
					result.setStatus(SupplyResult.STATUS_FAILED);
					String msg = jo.getString(RES_MSG);
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
	@Override
	public SupplyResult<ChargeInfo> chargeQuery(SupplyOrder supplyOrder) {
		SupplyResult<ChargeInfo> result = new SupplyResult<ChargeInfo>();
		RuiyiFlowTransVO vo = RuiyiFlowTransVO.createQueryOrderVO(String.valueOf(supplyOrder.getId()));
		SupplyResult<String> queryResult = vo.sendQueryOrder();
		if(queryResult.isSuccess()){
			result.setStatus(SupplyResult.STATUS_SUCCESS);
			ChargeInfo chargeInfo = new ChargeInfo();
			JSONObject jo = JSONObject.fromObject(queryResult.getModule());
			String code = jo.getString(RES_CODE);
			if(isSuccess(code)){
				String data = jo.getString(RES_DATA);
				JSONObject dataJO = JSONObject.fromObject(data);
				String status = dataJO.getString("status");
				chargeInfo.setStatus(status);
				chargeInfo.setStatusDesc(orderStateMap.get(status));
			}else{
				chargeInfo.setStatus(code);
				String msg = jo.getString(RES_MSG);
				chargeInfo.setStatusDesc(msg);
			}
			result.setModule(chargeInfo);
		}else{
			result.setResultMsg("网络连接失败");
		}
		return result;
	}
//	public static void main(String[] args) {
//		String s ="{\"code\":\"0000\",\"data\":{\"no\":\"123\",\"status\":\"1\"}}";
//		JSONObject jo = JSONObject.fromObject(s);
//		String data = jo.getString("data");
//		JSONObject dataJO = JSONObject.fromObject(data);
//		System.out.println(dataJO);
//	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		result.setResultMsg("该接口不支持余额查询");
		return result ;
	}
	/**
	 * 设置publickey
	 * @param vo
	 */
	private String  setPublicKey(RuiyiFlowTransVO vo){
		//先取缓存，没有则发交易获取
		Object obj = memcachedService.get(cachePublicKey);
		if(obj != null){
			vo.setPublicKey(obj.toString());
			return null;
		}
		//发交易获取publickkey
		SupplyResult<String> result  = vo.sendGetPublicKey();
		if (result.isSuccess()) {
			JSONObject jo = JSONObject.fromObject(result.getModule());
			String code = jo.getString(RES_CODE);
			if(isSuccess(code)){
				String data = jo.getString(RES_DATA);
				JSONObject dataJO = JSONObject.fromObject(data);
				String publicKey = dataJO.getString("publicKey");
				vo.setPublicKey(publicKey);
				memcachedService.set(cachePublicKey, 24*60*60, publicKey);
				return null ;
			}else{
				return jo.getString(RES_MSG) ;
			}
		}
		return "网络连接失败" ;
	}
	public static  boolean isSuccess(String code){
		if(SUCCESS_CODE.equals(code))return true ;
		return false ;
	}
	public static  boolean isUnConfirm(String code){
		if("1002".equals(code))return true ;
		return false ;
	}
}
