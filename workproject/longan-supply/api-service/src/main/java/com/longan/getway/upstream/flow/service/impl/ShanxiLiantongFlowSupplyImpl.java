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
import com.longan.getway.upstream.flow.vo.MiaoxunFlowVO;
import com.longan.getway.upstream.flow.vo.ShanxiLiantongFlowVO;
import com.longan.getway.utils.Utils;

public class ShanxiLiantongFlowSupplyImpl extends BaseService implements UpstreamDirectSupplyService{
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
		Item item = localCachedService.getItem(supplyOrder.getItemId());
		if(item == null){
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("商品不存在或者已下架");
			return result;
		}
		String limit = String.valueOf(Utils.getFlowSize(item.getItemExt1()));
		SupplyResult<String> supplyResult = ShanxiLiantongFlowVO.orderFlow("1", limit, supplyOrder.getItemUid(), "0");
		System.out.println("返回:"+supplyResult.getModule());
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
				String res = supplyResult.getModule();
				logger.warn("陝西联通后向流量充值返回数据:"+res);
				String [] resArry = res.split("\\|");
				
				if("00".equals(resArry[0])){
					result.setStatus(SupplyResult.STATUS_SUCCESS);
				}else{
					result.setStatus(SupplyResult.STATUS_FAILED);
					if(resArry.length>1){
						result.setResultMsg(resArry[1]);
					}
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		// TODO Auto-generated method stub
		return null;
	}

}
