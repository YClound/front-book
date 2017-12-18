package com.longan.getway.upstream.telephone.service.impl;

import java.io.StringReader;
import java.text.SimpleDateFormat;
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
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.common.domain.QueryRunnable;
import com.longan.getway.upstream.telephone.vo.DuanFengRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class DuanFengSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;
	private static final String supplyName = "端峰话费";
	public static final Map<String, String> SRetMap = new HashMap<String, String>();
	public static final Map<String, String> SResultMap = new HashMap<String, String>();

	@Resource
	private LocalCachedService localCachedService;
	static {
		SRetMap.put("0", "订单提交成功");
		SRetMap.put("1", "查询成功");
		SRetMap.put("5", "用户不存在");
		SRetMap.put("6", "数据库异常，未确认");
		SRetMap.put("7", "IP验证失败");
		SRetMap.put("8", "密钥验证失败");
		SRetMap.put("9", "记录已存在，订单号重复");
		SRetMap.put("10", "数据库异常，不代表失败");
		SRetMap.put("11", "写入数据库异常");
		SRetMap.put("12", "异常");
		SRetMap.put("13", "订单不存在");

		SResultMap.put("1", "充值成功");
		SResultMap.put("-1", "充值失败");
		SResultMap.put("-2", "冲正退款");
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);
		DuanFengRequestVO vo = new DuanFengRequestVO();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		vo.setSOutTrans(supplyOrder.getId() + "");
		vo.setSPhoneMoney(supplyOrder.getItemFacePrice() / 1000 + "");
		vo.setSPhoneNum(supplyOrder.getItemUid());
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendPostByMap(vo.createRequestUrl(), vo.createChargeParam());
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
				String SRet = root.elementText("SRet");
				if (!"0".equals(SRet)) {
					if ("6".equals(SRet) || "10".equals(SRet) || "11".equals(SRet)
							|| "12".equals(SRet)) {
						result.setStatus(SupplyResult.STATUS_UNCONFIRM);
						result.setResultMsg(SRet + "_" + SRetMap.get(SRet));
						return result;
					} else {
						result.setStatus(SupplyResult.STATUS_FAILED);
						result.setResultCode(SRet);
						result.setResultMsg(SRet + "_" + SRetMap.get(SRet));
						return result;
					}
				}
				bizDealService.submitQueryRunnable(new DuanFengQueryRunnable(supplyName,
						supplyOrder.getId(), supplyOrder.getBizOrderId()));
			} catch (DocumentException e) {
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
		System.out.println(34);
		SupplyResult<ChargeInfo> result = new SupplyResult<ChargeInfo>();
		if (supplyOrder == null) {
			result.setResultMsg("参数错误");
			return result;
		}
		DuanFengRequestVO vo = new DuanFengRequestVO();
		vo.setSOutTrans(supplyOrder.getId() + "");
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendPostByMap(vo.createRequestUrl(), vo.createQueryParam());
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
				String SRet = root.elementText("SRet");
				String SResult = root.elementText("SResult");
				if (StringUtils.isEmpty(SResult) && (!"1".equals(SRet))) {
					chargeInfo.setStatus(SRet);
					chargeInfo.setStatusDesc(SRetMap.get(SRet));
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					return result;
				}
				chargeInfo.setStatus(SResult);
				if (StringUtils.isNotEmpty(SResultMap.get(SResult))) {
					chargeInfo.setStatusDesc(SResultMap.get(SResult));
				} else {
					chargeInfo.setStatusDesc(SRet + "_订单处理中");
				}
				result.setStatus(SupplyResult.STATUS_SUCCESS);
			} catch (DocumentException e) {
				logger.error("parse xml error", e);
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("解析上游信息失败");
				return result;
			}
		}
		return result;
	}

	class DuanFengQueryRunnable extends QueryRunnable {
		public DuanFengQueryRunnable(String queryName, Long supplyOrderId, Long bizOrderId) {
			super(queryName, supplyOrderId, bizOrderId, bizOrderService, bizDealService,
					supplyOrderService);
		}

		@Override
		protected CallBack chargeResultQuery() {
			Result<SupplyOrder> getSupplyOrderByIdResult = supplyOrderService
					.getSupplyOrderById(supplyOrderId);
			SupplyOrder supplyOrder = getSupplyOrderByIdResult.getModule();

			if (supplyOrder == null) {
				logger.error("getSupplyOrderById error supplyOrder is null id : " + supplyOrderId);
				return null;
			}

			SupplyResult<ChargeInfo> chargeInfoResult = chargeQuery(supplyOrder);
			ChargeInfo chargeInfo = chargeInfoResult.getModule();

			if (chargeInfo == null) {
				logger.error("chargeQuery error  chargeInfo is null supplyOrderId : "
						+ supplyOrderId);
				return null;
			}

			CallBack callBack = new CallBack();
			callBack.setSupplyOrderId(supplyOrderId);

			if (!"1".equals(chargeInfo.getStatus())) {
				if ("-1".equals(chargeInfo.getStatus())) {
					callBack.setStatus(SupplyResult.STATUS_FAILED);
					return callBack;
				} else {
					callBack.setStatus(SupplyResult.STATUS_UNCONFIRM);
					return callBack;
				}
			}
			callBack.setStatus(SupplyResult.STATUS_SUCCESS);
			return callBack;
		}
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		DuanFengRequestVO vo = new DuanFengRequestVO();
		SimpleDateFormat formdate = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date();
		String sOutTrans = formdate.format(curDate);
		vo.setSOutTrans(sOutTrans);
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendPostByMap(vo.createRequestUrl(), vo.createQueryBalanceParam());
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
				String SRet = root.elementText("SRet");
				String SYEMoney = root.elementText("SYEMoney");
				if (StringUtils.isEmpty(SYEMoney)) {
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					balanceQueryInfo.setMsg(SRet + ":" + SRetMap.get(SRet));
					return result;
				}
				result.setStatus(SupplyResult.STATUS_SUCCESS);
				balanceQueryInfo.setMsg("查询余额成功");
				balanceQueryInfo.setBalance(SYEMoney);
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
