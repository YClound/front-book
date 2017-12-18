package com.longan.getway.upstream.telephone.service.impl;

import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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
import com.longan.getway.upstream.telephone.vo.JuYouChargeRequestVO;
import com.longan.getway.upstream.telephone.vo.JuYouQueryRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class JuYouSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;
	private static final String supplyName = "聚优全国话费";
	public static final Map<String, String> codeResultMap = new HashMap<String, String>();

	static {
		// 查询
		codeResultMap.put("100", "充值成功");
		codeResultMap.put("101", "处理中");
		codeResultMap.put("102", "已退款");
		codeResultMap.put("103", "订单号不存在");
		// 异常
		codeResultMap.put("1000", "接口关闭中");
		codeResultMap.put("1001", "充值关闭中");
		codeResultMap.put("1002", "用户未开通功能");
		codeResultMap.put("1003", "参数提交错误");
		codeResultMap.put("1004", "暂不支持此区域号码缴费");
		codeResultMap.put("1005", "帐户余额不足");
		codeResultMap.put("1006", "此订单号已存在");
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);
		JuYouChargeRequestVO vo = new JuYouChargeRequestVO();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		vo.setHm(supplyOrder.getItemUid());
		vo.setMoney(supplyOrder.getItemFacePrice() / 1000 + "");
		vo.setOd(supplyOrder.getId() + "");
		vo.createChargeSign();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByPost(vo.getUrl(), vo.createChargeParam());

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
				String state = root.elementText("state");
				if (!"8888".equals(state)) {
					String err = root.elementText("err");
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultCode(err);
					result.setResultMsg(err + "_" + codeResultMap.get(err));
					return result;
				}
				String ustreamSerialno = root.elementText("od");
				supplyOrder.setUpstreamSerialno(ustreamSerialno);
				bizDealService.submitQueryRunnable(new JuYouQueryRunnable(supplyName, supplyOrder
						.getId(), supplyOrder.getBizOrderId()));
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
		SupplyResult<ChargeInfo> result = new SupplyResult<ChargeInfo>();
		if (supplyOrder == null) {
			result.setResultMsg("参数错误");
			return result;
		}

		if (StringUtils.isEmpty(supplyOrder.getUpstreamSerialno())) {
			result.setResultMsg("查询失败，上游流水号为空");
			return result;
		}

		JuYouQueryRequestVO vo = new JuYouQueryRequestVO();
		vo.setOd(supplyOrder.getUpstreamSerialno());
		vo.createQureySign();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByPost(vo.getUrl(), vo.createQueryParam());

		ChargeInfo chargeInfo = new ChargeInfo();
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
				if (!StringUtils.isEmpty(root.elementText("state"))) {
					String state = root.elementText("state");
					chargeInfo.setStatus(state);
					chargeInfo.setStatusDesc(codeResultMap.get(state));
					result.setStatus(SupplyResult.STATUS_SUCCESS);
				} else {
					String err = root.elementText("err");
					chargeInfo.setStatus(err);
					chargeInfo.setStatusDesc(codeResultMap.get(err));
					result.setStatus(SupplyResult.STATUS_SUCCESS);
				}

			} catch (DocumentException e) {
				logger.error("parse xml error", e);
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("解析上游信息失败");
				return result;
			}
		}
		result.setModule(chargeInfo);
		return result;
	}

	class JuYouQueryRunnable extends QueryRunnable {
		public JuYouQueryRunnable(String queryName, Long supplyOrderId, Long bizOrderId) {
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

			if (!"100".equals(chargeInfo.getStatus())) {
				if ("101".equals(chargeInfo.getStatus())) {
					callBack.setStatus(SupplyResult.STATUS_UNCONFIRM);
					return callBack;
				} else if ("102".equals(chargeInfo.getStatus())
						|| "103".equals(chargeInfo.getStatus())) {
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
		JuYouQueryRequestVO vo = new JuYouQueryRequestVO();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByPost(vo.getUrl(), vo.createQureyBalanceParam());
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
				String balance = root.elementText("balance");
				if (!StringUtils.isEmpty(balance)) { // 查询成功
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					balanceQueryInfo.setMsg("查询余额成功");
					balanceQueryInfo.setBalance(balance);
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
