package com.longan.getway.upstream.flow.service.impl;

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
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.BigDecimalUtils;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.flow.domain.BaoziFlow;
import com.longan.getway.upstream.flow.vo.DingxinFlowTransVO;
import com.longan.getway.upstream.telecomflow.vo.SuKaTelecomFlowRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class BaoZiFlowSupplyImpl extends BaseService implements UpstreamDirectSupplyService{
	private boolean isAsync = true;

	public static final Map<String, String> codeResultMap = new HashMap<String, String>();

	@Resource
	private LocalCachedService localCachedService;
	static {
		codeResultMap.put("00", "充值成功");
		codeResultMap.put("10", "缺少参数，或者参数类型错误");
		codeResultMap.put("11", "校验码验证失败");

		codeResultMap.put("20", "商品状态相关错误");
		codeResultMap.put("21", "订单相关错误");
		codeResultMap.put("22", "流水号重复");
		codeResultMap.put("23", "充值号码与所选商品不符");
		codeResultMap.put("24", "手机号今日充值已经到达限额");
		codeResultMap.put("25", "手机归属地相关错误。");
		codeResultMap.put("30", "上游供货失败");
		codeResultMap.put("31", "上游供货超时 ");
		codeResultMap.put("40", "合作方账号类型或者状态错误");
		codeResultMap.put("41", "合作方资金账户错误");
		codeResultMap.put("42", "合作方余额不足等错误");
		codeResultMap.put("43", "合作方支付错误");
		codeResultMap.put("44", "合作方没有该业务权限");
		codeResultMap.put("50", "系统异常");
		codeResultMap.put("51", "交易繁忙，  上游供货繁忙。");
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);

		BaoziFlow vo = new BaoziFlow();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		vo.setNumber(supplyOrder.getItemUid());
		SimpleDateFormat formdate = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date();
		vo.setCreate_time(formdate.format(curDate));
		vo.setGoods(itemSupply.getSupplyProductCode());
//		vo.setPrice(supplyOrder.getItemFacePrice() / 1000 + "");
		vo.setOrder_no(supplyOrder.getId() + "");

		SupplyResult<Document> httpResult = MultiThreadedHttpConnection.getInstance().sendDataByGet(vo.createChargeUrl());
		result.setStatus(httpResult.getStatus());
		result.setResultMsg(httpResult.getResultMsg());
		if (httpResult.isSuccess()) {
			Document doc = httpResult.getModule();
			Element root = doc.getRootElement();
			logger.warn("鼎信流量充值返回: " + root.asXML());
			System.out.println(doc.asXML());
			String porderid = root.elementText("Porderid");// 第三方订单号
			String state = root.elementText("state");// 订单状态
			String error = root.elementText("error");// 提交状态
			if (DingxinFlowTransVO.isChargeing(state)) {
				if (isSubmitSuccess(error)) {
					// 充值提交成功
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					supplyOrder.setUpstreamSerialno(porderid);
				} else {
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultMsg(codeResultMap.get(error));
				}
			} else if (DingxinFlowTransVO.isWaitPay(state)) {
				// 等待扣款
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("充值处理中");
			} else if (DingxinFlowTransVO.isChargeFailed(state)) {
				// 充值失败
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg(codeResultMap.get(error));
			} else {
				// 未知状态
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("充值处理中");
			}
		} else if (httpResult.isUnConfirm()) {
			result.setStatus(SupplyResult.STATUS_UNCONFIRM);
			result.setResultMsg("充值处理中");
		} else {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("网络连接失败");
		}
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
		
		SuKaTelecomFlowRequestVO vo = new SuKaTelecomFlowRequestVO();
		vo.setSporderid(supplyOrder.getUpstreamSerialno());
		vo.createQuerySign();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendPostByMap(vo.createQueryAction(), vo.createQueryParamMap());
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
			SAXReader reader = new SAXReader();
			try {
				Document document = reader.read(new StringReader(supplyResult.getModule()));
				Element root = document.getRootElement();
				String resultno = root.elementText("resultno");
				if (!StringUtils.isEmpty(resultno)) { // 查询成功
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					chargeInfo.setStatus(resultno);
					chargeInfo.setStatusDesc(codeResultMap.get(resultno));
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

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		BalanceQueryInfo balanceQueryInfo = new BalanceQueryInfo();
		result.setModule(balanceQueryInfo);
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		balanceQueryInfo.setMsg("该上游不支持余额查询");
		return result;
	}
	/**
	 * 是否提交成功
	 * 
	 * @param error
	 * @return
	 */
	private boolean isSubmitSuccess(String error) {
		if (error == null || error.length() < 1 || "0".equals(error))
			return true;
		return false;
	}

}
