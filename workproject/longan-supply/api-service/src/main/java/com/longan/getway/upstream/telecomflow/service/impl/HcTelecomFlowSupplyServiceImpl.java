package com.longan.getway.upstream.telecomflow.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.telecomflow.vo.HcTelecomFlowRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class HcTelecomFlowSupplyServiceImpl extends BaseService implements
		UpstreamDirectSupplyService {
	private boolean isAsync = true;

	@Resource
	private LocalCachedService localCachedService;

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);

		HcTelecomFlowRequestVO vo = new HcTelecomFlowRequestVO();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		vo.setPhone((supplyOrder.getItemUid()));
		vo.setProductId(itemSupply.getSupplyProductCode());
		vo.setOrderNo(supplyOrder.getId() + "");
		vo.createChargeParams();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendPostByMap(vo.getRequestURL(), vo.getParamMap());

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
			supplyOrder.setUpstreamDate(new Date());
			if (StringUtils.isEmpty(supplyResult.getModule())) {
				result.setResultMsg("上游返回信息为空");
				return result;
			}
			try {
				JSONObject jo = JSONObject.fromObject(supplyResult.getModule());
				String orderStatus = jo.get("orderStatus").toString();

				if ("ORDER_FAILED".equals(orderStatus) || "REFUNDED".equals(orderStatus)
						|| "UNDERWAY".equals(orderStatus) || "ORDER_SUCCESS".equals(orderStatus)) {
					// 重复充值，的情况下出现，所以做异常处理。
					result.setStatus(SupplyResult.STATUS_UNCONFIRM);
					return result;
				} else if ("REQUEST_FAILED".equals(orderStatus)) {
					// 参数错误等其他错误,失败
					String failedCode = jo.get("failedCode") != null ? jo.get("failedCode")
							.toString() : "";
					String failedReason = jo.get("failedReason") != null ? jo.get("failedReason")
							.toString() : "";
					result.setResultMsg(failedCode + "_" + failedReason);
					return result;
				} else if ("PAY_FAILED".equals(orderStatus)) {
					// 失败
					String failedCode = jo.get("failedCode") != null ? jo.get("failedCode")
							.toString() : "";
					String failedReason = jo.get("failedReason") != null ? jo.get("failedReason")
							.toString() : "";
					result.setResultMsg(failedCode + "_" + failedReason);
					return result;
				} else if ("PAY_SUCCESS".equals(orderStatus)) {
					// 受理成功
				} else {
					result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				}

			} catch (Exception e) {
				logger.error("parse from json error", e);
				result.setResultMsg("解析上游信息失败");
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
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

		HcTelecomFlowRequestVO vo = new HcTelecomFlowRequestVO();
		vo.setOrderNo(supplyOrder.getId() + "");
		vo.createQueryParams();

		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendPostByMap(vo.getRequestURL(), vo.getParamMap());
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
			result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SYSTEM);
			return result;
		}

		if (supplyResult.isSuccess()) {
			if (StringUtils.isEmpty(supplyResult.getModule())) {
				result.setResultMsg("上游返回信息为空");
				return result;
			}
			try {
				JSONObject jo = JSONObject.fromObject(supplyResult.getModule());
				String orderStatus = jo.get("orderStatus").toString();
				chargeInfo.setStatus(orderStatus);
				if ("REQUEST_FAILED".equals(orderStatus)) {
					// 参数错误等其他错误,失败
					String failedCode = jo.get("failedCode") != null ? jo.get("failedCode")
							.toString() : "";
					String failedReason = jo.get("failedReason") != null ? jo.get("failedReason")
							.toString() : "";
					result.setResultMsg(failedCode + "_" + failedReason);
					return result;
				} else if ("ORDER_FAILED".equals(orderStatus)) {
					chargeInfo.setStatusDesc("充值失败但,上游未退款，请不要做退款操作。");
				} else if ("REFUNDED".equals(orderStatus)) {
					chargeInfo.setStatusDesc("充值失败,上游已退款，可以做退款操作。");
				} else if ("PAY_FAILED".equals(orderStatus)) {
					// 失败
					String failedCode = jo.get("failedCode") != null ? jo.get("failedCode")
							.toString() : "";
					String failedReason = jo.get("failedReason") != null ? jo.get("failedReason")
							.toString() : "";
					chargeInfo.setStatusDesc("下单失败 " + failedCode + failedReason + " 可以做退款操作");
				} else if ("ORDER_NOT_EXIST".equals(orderStatus)) {
					chargeInfo.setStatusDesc("订单不存在， 可以做退款操作");
				} else if ("PAY_SUCCESS".equals(orderStatus)) {
					chargeInfo.setStatusDesc("处理中,请不要做退款操作。");
				} else if ("UNDERWAY".equals(orderStatus)) {
					chargeInfo.setStatusDesc("处理中,请不要做退款操作。");
				} else if ("NEED_PAY".equals(orderStatus)) {
					chargeInfo.setStatusDesc("处理中,请不要做退款操作。");
				} else if ("ORDER_SUCCESS".equals(orderStatus)) {
					chargeInfo.setStatusDesc("充值成功");
				} else {
					result.setResultMsg("解析上游信息失败");
					return result;
				}

			} catch (Exception e) {
				logger.error("parse from json error", e);
				result.setResultMsg("解析上游信息失败");
				return result;
			}
		}
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		return result;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		BalanceQueryInfo balanceQueryInfo = new BalanceQueryInfo();
		result.setModule(balanceQueryInfo);
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		balanceQueryInfo.setMsg("该上游不支持余额查询");
		return result;
	}
}
