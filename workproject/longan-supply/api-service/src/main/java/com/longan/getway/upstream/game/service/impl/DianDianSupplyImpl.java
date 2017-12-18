package com.longan.getway.upstream.game.service.impl;

import java.io.StringReader;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.game.vo.DianDianRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class DianDianSupplyImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;
	public static final Map<String, String> statusResultMap = new HashMap<String, String>();
	@Resource
	private LocalCachedService localCachedService;
	static {
		statusResultMap.put("ORDER_FAILED", "订单创建失败");
		statusResultMap.put("CANCEL", "订单取消");
		statusResultMap.put("SUCCESS", "充值成功");
		statusResultMap.put("UNDERWAY", "正在处理");
		statusResultMap.put("FAILED", "充值失败");
		statusResultMap.put("REQUEST_FAILED", "请求失败");
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);

		DianDianRequestVO vo = new DianDianRequestVO();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		vo.setTranid("hztangtang" + supplyOrder.getId());
		vo.setProid(itemSupply.getSupplyProductCode());
		vo.setQuantity(supplyOrder.getAmt() + "");
		vo.setPrice(NumberFormat.getInstance().format(supplyOrder.getItemFacePriceDouble()));
		vo.setAccount(supplyOrder.getItemUid());
		if (StringUtils.isNotEmpty(itemSupply.getItemSupplyExt1())) {
			vo.setChargetype(itemSupply.getItemSupplyExt1());
		}
		if (StringUtils.isNotEmpty(itemSupply.getItemSupplyExt2())) {
			vo.setGamename(itemSupply.getItemSupplyExt2());
		}
		if (StringUtils.isNotEmpty(itemSupply.getItemSupplyExt3())) {
			vo.setAcctype(itemSupply.getItemSupplyExt3());
		}
		// vo.setGamearea(null);
		// vo.setGameserver(null);
		if (StringUtils.isNotEmpty(supplyOrder.getItemExt1())) {
			vo.setInfoa(supplyOrder.getItemExt1());
		}
		if (StringUtils.isNotEmpty(supplyOrder.getItemExt2())) {
			vo.setInfob(supplyOrder.getItemExt2());
		}
		if (StringUtils.isNotEmpty(supplyOrder.getItemExt3())) {
			vo.setInfoc(supplyOrder.getItemExt3());
		}
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByGetReturnGBKStr(vo.createChargeParam());
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
			logger.warn("response: " + supplyResult.getModule());
			SAXReader reader = new SAXReader();
			try {
				Document document = reader.read(new StringReader(supplyResult.getModule()));
				Element root = document.getRootElement();
				String orderStatus = root.elementText("orderStatus");
				String failedCode = root.elementText("failedCode");
				String failedReason = root.elementText("failedReason");
				String orderNo = root.elementText("orderNo");
				if ("ORDER_FAILED".equals(orderStatus)) {// 失败
					result.setStatus(SupplyResult.STATUS_FAILED);// 失败
					result.setResultCode(failedCode);
					result.setResultMsg(failedCode + "_" + failedReason);
					return result;
				}
				if (!"UNDERWAY".equals(orderStatus)) {// 未确认
					result.setResultCode(failedCode);
					result.setResultMsg(failedCode + "_" + failedReason);
					result.setStatus(SupplyResult.STATUS_UNCONFIRM);
					return result;
				}
				supplyOrder.setUpstreamSerialno(orderNo);
			} catch (DocumentException e) {
				logger.error("parse document error", e);
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("解析上游信息失败");
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
		DianDianRequestVO vo = new DianDianRequestVO();
		vo.setTranid("hztangtang" + supplyOrder.getId());
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByGetReturnGBKStr(vo.createQueryParam());
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
			result.setResultMsg(Constants.ErrorCode.CODE_ERROR_SYSTEM);
			return result;
		}

		if (supplyResult.isSuccess()) {
			if (StringUtils.isEmpty(supplyResult.getModule())) {
				result.setResultMsg("上游返回信息为空");
				return result;
			}
			SAXReader reader = new SAXReader();
			try {
				Document document = reader.read(new StringReader(supplyResult.getModule()));
				Element root = document.getRootElement();
				String orderStatus = root.elementText("orderStatus");
				String failedCode = root.elementText("failedCode");
				String failedReason = root.elementText("failedReason");
				if (StringUtils.isNotEmpty(orderStatus)) {
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					if (StringUtils.isNotEmpty(failedCode)) {
						chargeInfo.setStatus(failedCode);
						chargeInfo.setStatusDesc(failedReason);
					} else {
						chargeInfo.setStatus(orderStatus);
						chargeInfo.setStatusDesc(statusResultMap.get(orderStatus));
					}
				} else {
					result.setStatus(SupplyResult.STATUS_FAILED);
					return result;
				}
			} catch (DocumentException e) {
				logger.error("parse xml error", e);
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("解析上游信息失败");
				return result;
			}
		}
		return result;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		DianDianRequestVO vo = new DianDianRequestVO();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByGetReturnGBKStr(vo.createBalanceParam());
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
			SAXReader reader = new SAXReader();
			try {
				Document document = reader.read(new StringReader(supplyResult.getModule()));
				Element root = document.getRootElement();
				String amount = root.elementText("amount");
				String failedCode = root.elementText("failedCode");
				String failedReason = root.elementText("failedReason");
				if (StringUtils.isNotEmpty(amount)) {
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					balanceQueryInfo.setMsg("查询余额成功");
					balanceQueryInfo.setBalance(amount);
					return result;
				}
				if (StringUtils.isNotEmpty(failedCode)) {
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					balanceQueryInfo.setMsg(failedCode + ":" + failedReason);
					return result;
				} else {
					result.setStatus(SupplyResult.STATUS_FAILED);
					return result;
				}
			} catch (DocumentException e) {
				logger.error("parse document error", e);
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg("解析上游信息失败");
				return result;
			}
		}
		return result;
	}
}
