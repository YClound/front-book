package com.longan.getway.upstream.telephone.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.BigDecimalUtils;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.telephone.vo.SanTongRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class SanTongSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;
	public static final Map<String, String> codeResultMap = new HashMap<String, String>();
	static {
		codeResultMap.put("0", "充值成功");
		codeResultMap.put("1", "充值失败");
		codeResultMap.put("2", "充值中");

		codeResultMap.put("00001", "缺少必要参参数");
		codeResultMap.put("00002", "合作商不存在");
		codeResultMap.put("00003", "加密不正确");
		codeResultMap.put("00004", "产品不存在");
		codeResultMap.put("00005", "查询订单失败");
		codeResultMap.put("10426", "查询订单不存在");
		codeResultMap.put("10000", "创建订单成功");
		codeResultMap.put("10001", "创建订单失败");
		codeResultMap.put("10555", "余额不足");
		codeResultMap.put("40012", "非法的请求ip");
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);
		SanTongRequestVO vo = new SanTongRequestVO();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		vo.setChargeMoney(supplyOrder.getItemFacePrice() / 1000 + "");
		vo.setChargeNumber(supplyOrder.getItemUid());
		vo.setMerchantOrderNo(supplyOrder.getId() + "");
		vo.createChargeSign();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByGetReturnString(vo.createChargeParam());
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
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SYSTEM);
			return result;
		}

		if (supplyResult.isSuccess()) {
			supplyOrder.setUpstreamDate(new Date());
			if (StringUtils.isEmpty(supplyResult.getModule())) {
				result.setResultMsg("上游返回信息为空");
				return result;
			}
			try {
				logger.warn("response: " + supplyResult.getModule());
				JSONObject jo = JSONObject.fromObject(supplyResult.getModule());
				String errorCode = jo.get("errorCode").toString();
				String errorDesc = jo.get("errorDesc").toString();
				String hfOrderNo = jo.get("hfOrderNo").toString();
				String payMoney = jo.get("payMoney").toString();
				String orderStatus = jo.get("orderStatus").toString();
				if (!"00000".equals(errorCode)) {// 失败
					result.setResultCode(errorCode);
					if (StringUtils.isNotEmpty(errorDesc)) {
						result.setResultMsg(errorCode + "_" + errorDesc);
					} else {
						result.setResultMsg(errorCode + "_" + codeResultMap.get(errorCode));
					}
					result.setStatus(SupplyResult.STATUS_FAILED);
					return result;
				}
				if (!"10000".equals(orderStatus)) {// 未确认
					result.setResultCode(orderStatus);
					if (StringUtils.isNotEmpty(errorDesc)) {
						result.setResultMsg(orderStatus + "_" + errorDesc);
					} else {
						result.setResultMsg(orderStatus + "_" + codeResultMap.get(orderStatus));
					}
					result.setStatus(SupplyResult.STATUS_UNCONFIRM);
					return result;
				}
				supplyOrder.setSupplyActualCost(BigDecimalUtils.multInteger(payMoney));
				supplyOrder.setUpstreamSerialno(hfOrderNo);
			} catch (Exception e) {
				logger.error("parse to json error", e);
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("解析上游信息失败");
				return result;
			}
		}
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		return result;
	}

	@Override
	public SupplyResult<ChargeInfo> chargeQuery(SupplyOrder supplyOrder) {
		SupplyResult<ChargeInfo> result = new SupplyResult<ChargeInfo>();
		if (supplyOrder == null) {
			result.setResultMsg("参数错误");
			return result;
		}
		SanTongRequestVO vo = new SanTongRequestVO();
		vo.setMerchantOrderNo(supplyOrder.getId() + "");
		vo.createQuerySign();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByGetReturnString(vo.createQueryParam());
		ChargeInfo chargeInfo = new ChargeInfo();
		result.setModule(chargeInfo);
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
			if (StringUtils.isEmpty(supplyResult.getModule())) {
				result.setResultMsg("上游返回信息为空");
				return result;
			}
			try {
				logger.warn("response: " + supplyResult.getModule());
				JSONObject jo = JSONObject.fromObject(supplyResult.getModule());
				String errorCode = jo.get("errorCode").toString();
				String errorDesc = jo.get("errorDesc").toString();
				String orderStatus = jo.get("orderStatus").toString();
				if (!"00000".equals(errorCode)) {
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					chargeInfo.setStatus(errorCode);
					if (StringUtils.isNotEmpty(errorDesc)) {
						chargeInfo.setStatusDesc(errorDesc);
					} else {
						chargeInfo.setStatusDesc(codeResultMap.get(errorCode));
					}
					return result;
				}
				result.setStatus(SupplyResult.STATUS_SUCCESS);
				chargeInfo.setStatus(orderStatus);
				chargeInfo.setStatusDesc(codeResultMap.get(orderStatus));
			} catch (Exception e) {
				logger.error("parse document error", e);
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg("解析上游信息失败");
				return result;
			}
		}
		return result;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		SanTongRequestVO vo = new SanTongRequestVO();
		vo.createQueryBalanceSign();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByGetReturnString(vo.createQueryBalanceParam());
		BalanceQueryInfo balanceQueryInfo = new BalanceQueryInfo();
		result.setModule(balanceQueryInfo);
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
			if (StringUtils.isEmpty(supplyResult.getModule())) {
				result.setResultMsg("上游返回信息为空");
				return result;
			}
			logger.warn("response: " + supplyResult.getModule());
			result.setStatus(SupplyResult.STATUS_SUCCESS);
			balanceQueryInfo.setMsg("查询余额成功");
			balanceQueryInfo.setBalance(supplyResult.getModule());
		}
		return result;
	}

}
