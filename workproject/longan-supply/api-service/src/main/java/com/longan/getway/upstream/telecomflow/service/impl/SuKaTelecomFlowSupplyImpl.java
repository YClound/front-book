package com.longan.getway.upstream.telecomflow.service.impl;

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
import com.longan.biz.utils.BigDecimalUtils;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.telecomflow.vo.SuKaTelecomFlowRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class SuKaTelecomFlowSupplyImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;

	public static final Map<String, String> codeResultMap = new HashMap<String, String>();

	@Resource
	private LocalCachedService localCachedService;
	static {
		codeResultMap.put("0", "等待充值");
		codeResultMap.put("1", "充值成功");
		codeResultMap.put("2", "充值中");
		codeResultMap.put("9", "充值失败已退款");

		codeResultMap.put("5001", "代理商不存在");
		codeResultMap.put("5002", "代理商余额不足");
		codeResultMap.put("5003", "此商品暂时不可购买");
		codeResultMap.put("5004", "充值号码与所选商品不符");
		codeResultMap.put("5005", "充值请求验证错误");
		codeResultMap.put("5006", "代理商订单号重复");
		codeResultMap.put("5007", "所查询的订单不存在");
		codeResultMap.put("5008", "交易亏损不能充值");
		codeResultMap.put("5009", "Ip不符");
		codeResultMap.put("5010", "商品编号与充值金额不符");
		codeResultMap.put("5011", "商品数量不支持");
		codeResultMap.put("5012", "缺少必要参数或参数值不合法");
		codeResultMap.put("9999", "未知错误,需进入平台查询核实");
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);

		SuKaTelecomFlowRequestVO vo = new SuKaTelecomFlowRequestVO();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		vo.setMobile(supplyOrder.getItemUid());
		SimpleDateFormat formdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date();
		vo.setSpordertime(formdate.format(curDate));
		vo.setProductid(itemSupply.getSupplyProductCode());
		vo.setPrice(supplyOrder.getItemFacePrice() / 1000 + "");
		vo.setSporderid(supplyOrder.getId() + "");
		vo.createChargeSign();

		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendPostByMap(vo.createChargeAction(), vo.createChargeParamMap());
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
			logger.warn("response: " + supplyResult.getModule());
			SAXReader reader = new SAXReader();
			try {
				Document document = reader.read(new StringReader(supplyResult.getModule()));
				Element root = document.getRootElement();
				String sporderid = root.elementText("sporderid");
				String resultno = root.elementText("resultno");
				String ordercash = root.elementText("ordercash");// 订单金额
				supplyOrder.setUpstreamSerialno(sporderid);
				if ("0".equals(resultno) || "2".equals(resultno) || "1".equals(resultno)) {
					// 受理成功
					result.setResultCode(resultno);
					result.setResultMsg(resultno + "_" + codeResultMap.get(resultno));
				} else if ("9999".equals(resultno)) {
					// 未知状态
					result.setResultCode(resultno);
					result.setResultMsg(resultno + "_" + codeResultMap.get(resultno));
					result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				} else {
					// 失败
					result.setResultCode(resultno);
					result.setResultMsg(resultno + "_" + codeResultMap.get(resultno));
					return result;
				}
				if (!StringUtils.isEmpty(ordercash)) {
					supplyOrder.setSupplyActualCost(BigDecimalUtils.multInteger(ordercash));
				}
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

}
