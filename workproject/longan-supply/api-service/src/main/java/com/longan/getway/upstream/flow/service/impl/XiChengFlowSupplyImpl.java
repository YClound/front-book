package com.longan.getway.upstream.flow.service.impl;

import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;

import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.flow.vo.XiChengFlowVO;
/**
 * 西城流量充值
 * @author Administrator
 *
 */
public class XiChengFlowSupplyImpl extends BaseService implements UpstreamDirectSupplyService{
	private boolean isAsync = true;
	private HashMap<String,String> stateMap = new HashMap<String,String>(){
		{
			put("-1", "无此订单号");
			put("0", "充值中");
			put("1", "充值成功");
			put("2", "充值失败");
		}
	};
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
		SupplyResult<Document> supplyResult = XiChengFlowVO.charge(supplyOrder.getItemUid(), String.valueOf(supplyOrder.getId()), itemSupply.getSupplyProductCode(), "1");
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
				Document doc = supplyResult.getModule();
				Element root = doc.getRootElement();
				String code = root.elementText("result");
				result.setResultCode(code);
				if("0000".equals(code)){//成功了
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					return result ;
				}
				String msg = root.elementText("desc");
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg(msg);
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
		try{
			SupplyResult<Document> queryResult = XiChengFlowVO.queryOrder(String.valueOf(supplyOrder.getId()), supplyOrder.getGmtCreate());
			if(!queryResult.isSuccess()){
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg(queryResult.getResultMsg());
				return result ;
			}
			Document doc = queryResult.getModule();
			Element root = doc.getRootElement();
			String resultCode = root.elementText("result");
			result.setResultCode(resultCode);
			if(!"0000".equals(resultCode)){
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg(root.elementText("desc"));
				return result ;
			}
			result.setStatus(SupplyResult.STATUS_SUCCESS);
			Element body = root.element("body");
			Iterator<Element> items = body.elementIterator("item");
			while(items.hasNext()){
				Element item = items.next();
				String orderId = item.elementText("orderId");
				if(String.valueOf(supplyOrder.getId()).equals(orderId)){
					ChargeInfo info = new ChargeInfo();
					String state = item.elementText("state");
					info.setStatus(state);
					info.setStatusDesc(stateMap.get(state));
					result.setModule(info);
					return result ;
				}
			}
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("未查询到订单信息");
			return result ;
		}catch (Exception e) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("查询异常");
			return result ;
		}
		
		
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		try {
			SupplyResult<Document> queryResult = XiChengFlowVO.queryBalance();
			if(!queryResult.isSuccess()){
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg(queryResult.getResultMsg());
				return result ;
			}
			Document doc = queryResult.getModule();
			Element root = doc.getRootElement();
			String resultCode = root.elementText("result");
			result.setResultCode(resultCode);
			if(!"0000".equals(resultCode)){
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg(root.elementText("desc"));
				return result ;
			}
			result.setStatus(SupplyResult.STATUS_SUCCESS);
			BalanceQueryInfo info = new BalanceQueryInfo();
			info.setBalance(root.elementText("balance"));
			result.setModule(info);
			return result ;
		} catch (Exception e) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("查询异常");
			return result ;
		}
	}
}
