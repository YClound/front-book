package com.longan.getway.upstream.telephone.service.impl;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.BigDecimalUtils;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.telephone.vo.DingChiRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class DingChiSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);
		DingChiRequestVO vo = new DingChiRequestVO();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		SimpleDateFormat formdate = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date();
		vo.setDtCreate(formdate.format(curDate));
		vo.setItemId(itemSupply.getSupplyProductCode());
		vo.setSerialno(supplyOrder.getId() + "");
		vo.setUid(supplyOrder.getItemUid());
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
			logger.warn("response: " + supplyResult.getModule());
			SAXReader reader = new SAXReader();
			try {
				Document document = reader.read(new StringReader(supplyResult.getModule()));
				Element root = document.getRootElement();
				String status = root.elementText("status");
				String code = root.elementText("code");
				String desc = root.elementText("desc");
				String amount = root.elementText("amount");
				String bizOrderId = root.elementText("bizOrderId");
				if ("failed".equals(status)) {
					if ("31".equals(code)) {// 未确认
						result.setResultCode(code);
						result.setResultMsg(code + "_" + desc);
						result.setStatus(SupplyResult.STATUS_UNCONFIRM);
						return result;
					}
					result.setStatus(SupplyResult.STATUS_FAILED);// 失败
					result.setResultCode(code);
					result.setResultMsg(code + "_" + desc);
					return result;
				}
				if (!("success".equals(status) && "00".equals(code))) {// 未确认
					result.setResultCode(code);
					result.setResultMsg(code + "_" + desc);
					result.setStatus(SupplyResult.STATUS_UNCONFIRM);
					return result;
				}
				supplyOrder.setSupplyActualCost(Integer.parseInt(amount));
				supplyOrder.setUpstreamSerialno(bizOrderId);
			} catch (DocumentException e) {
				logger.error("parse from xml error", e);
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
		DingChiRequestVO vo = new DingChiRequestVO();
		vo.setSerialno(supplyOrder.getId() + "");
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
				String status = root.elementText("status");
				String code = root.elementText("code");
				if (!("success".equals(status) && "00".equals(code))) {// 查询失败
					result.setStatus(SupplyResult.STATUS_FAILED);
					return result;
				}
				@SuppressWarnings("rawtypes")
				Iterator data = root.elementIterator("data");
				Element datanext = (Element) data.next();
				status = datanext.elementText("status");
				String statusDesc = datanext.elementText("statusDesc");

				result.setStatus(SupplyResult.STATUS_SUCCESS);
				chargeInfo.setStatus(status);
				chargeInfo.setStatusDesc(statusDesc);
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
		DingChiRequestVO vo = new DingChiRequestVO();
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
			SAXReader reader = new SAXReader();
			try {
				Document document = reader.read(new StringReader(supplyResult.getModule()));
				Element root = document.getRootElement();
				String status = root.elementText("status");
				String code = root.elementText("code");
				String balance = root.elementText("balance");
				if (!("success".equals(status) && "00".equals(code))) {// 查询失败
					result.setStatus(SupplyResult.STATUS_FAILED);
					return result;
				}
				balance = BigDecimalUtils.doubleDiveid(balance) + "";
				result.setStatus(SupplyResult.STATUS_SUCCESS);
				balanceQueryInfo.setMsg("查询余额成功");
				balanceQueryInfo.setBalance(balance);
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
