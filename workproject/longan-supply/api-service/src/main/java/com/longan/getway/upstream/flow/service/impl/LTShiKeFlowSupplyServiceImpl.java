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
import com.longan.getway.upstream.flow.vo.LTShiKeFlowTransVO;

public class LTShiKeFlowSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService{
	private boolean isAsync = true;
	@Resource
	private LocalCachedService localCachedService;
	@Resource
	private MemcachedService memcachedService ;
	
	public static final String quanguo_code="000000";
	public  static final String success_code = "0";
	public static final String res_code = "code";
	public static final String res_desc = "desc";
	public static final String res_orderNo = "orderNo";
	
	public static final Map<String, String> codeResultMap = new HashMap<String, String>();
	public static final Map<String, String> provinceCodeMap = new HashMap<String, String>();
	static{
		codeResultMap.put(success_code, "提交成功");
		codeResultMap.put("10001", "参数错误");
		codeResultMap.put("10002", "数据验证错误");
		codeResultMap.put("10003", "系统错误");
		
		provinceCodeMap.put("110000", "beijing");
		provinceCodeMap.put("450000", "guangxi");
		provinceCodeMap.put("320000", "jiangsu");
		provinceCodeMap.put("350000", "fujian");
		provinceCodeMap.put("430000", "hunan");
		provinceCodeMap.put("420000", "hubei");
		provinceCodeMap.put("500000", "chongqing");
		provinceCodeMap.put("230000", "heilongjiang");
		provinceCodeMap.put(quanguo_code, "quanguo");
	}
	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);
		String provinceCode = null;
		Item item = localCachedService.getItem(supplyOrder.getItemId());
		if(item == null){
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("商品不存在或者已下架");
			return result;
		}
		if(item.isNationwide()){
			provinceCode = provinceCodeMap.get(quanguo_code);
		}else{
			provinceCode = provinceCodeMap.get(item.getSalesArea());
		}
		if(provinceCode == null){
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品不在此区域销售");
			return result;
		}
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		LTShiKeFlowTransVO vo = new LTShiKeFlowTransVO(provinceCode, itemSupply.getSupplyProductCode(), supplyOrder.getItemUid());
		SupplyResult<String> supplyResult = vo.sendCharge();
		logger.warn("联通时科流量充值返回: " + supplyResult.getModule());
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
				String code = jo.getString(res_code);
				if(success_code.equals(code)){
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					result.setAsync(false);
					supplyOrder.setUpstreamSerialno(jo.getString(res_orderNo));
				}else if("0000".equals(code) || "00000".equals(code)){
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					result.setAsync(true);
					supplyOrder.setUpstreamSerialno(jo.getString(res_orderNo));
				}else{
					result.setStatus(SupplyResult.STATUS_FAILED);
					String msg = jo.getString(res_desc);
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
		result.setResultMsg("该接口不支持订单查询");
		return result ;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		result.setResultMsg("该接口不支持余额查询");
		return result ;
	}

}
