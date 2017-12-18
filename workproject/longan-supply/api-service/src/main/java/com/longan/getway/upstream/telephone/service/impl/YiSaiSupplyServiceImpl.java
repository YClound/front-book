package com.longan.getway.upstream.telephone.service.impl;

import java.io.StringReader;
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
import com.longan.getway.upstream.telephone.vo.YiSaiChargeRequestVO;
import com.longan.getway.upstream.telephone.vo.YiSaiQueryRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class YiSaiSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;
	public static final Map<String, String> chargeCodeMap = new HashMap<String, String>();
	public static final Map<String, String> queryCodeMap = new HashMap<String, String>();

	@Resource
	private LocalCachedService localCachedService;
	static {
		chargeCodeMap.put("success", "充值申请成功");
		chargeCodeMap.put("lost", "充值申请失败");
		chargeCodeMap.put("sameorder", "订单号重复");
		chargeCodeMap.put("ordererr", "内部订单号格式错误");
		chargeCodeMap.put("attrerr", "参数异常");
		chargeCodeMap.put("phoneerr", "号码格式错误");
		chargeCodeMap.put("moneyerr", "充值金额格式错误");
		chargeCodeMap.put("sellpriceerr", "销售价格格式异常");
		chargeCodeMap.put("dberr", "数据库连接异常(未确认是否成功)");
		chargeCodeMap.put("recordKeyerr", "验证密匙错误");
		chargeCodeMap.put("startTimeerr", "起始时间格式异常");
		chargeCodeMap.put("timeerr", "时间戳异常");
		chargeCodeMap.put("statuserr", "用户接口未开放");
		chargeCodeMap.put("signerr", "用户数据签名标示异常");
		chargeCodeMap.put("iperr", "错误IP 地址");
		chargeCodeMap.put("phoneareaerr", "充值号码与指定充值区域不符");
		chargeCodeMap.put("areaerr", "区域信息错误");
		chargeCodeMap.put("areafix", "区域面额维护");
		chargeCodeMap.put("syserr_", "系统内部错误(未确认)");
		chargeCodeMap.put("inList", "已在充值队列中");
		chargeCodeMap.put("syserr", "程序异常+ 异常原因");
		chargeCodeMap.put("none", "无此订单(谨慎确认)");
		chargeCodeMap.put("testerr", "测试错误");

		queryCodeMap.put("0", "等待受理");
		queryCodeMap.put("1", "受理充值");
		queryCodeMap.put("2", "充值队列");
		queryCodeMap.put("4", "充值成功");
		queryCodeMap.put("5", "充值失败");
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);
		YiSaiChargeRequestVO vo = new YiSaiChargeRequestVO();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		vo.createParam(supplyOrder.getItemUid(), supplyOrder.getItemFacePrice() / 1000 + "");
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendPostByMap(vo.createChargeAciton(), vo.createParamMap());

		// SupplyResult<String> supplyResult = new SupplyResult<String>();
		// String str =
		// "<?xml version='1.0' encoding='GB2312'?><esaipay><result>inList</result><inOrderNumber>37775</inOrderNumber><outOrderNumber>None</outOrderNumber><queryType>P</queryType><payResult>2</payResult><remark>test_6</remark><recordKey>EF7CA34A6E739016</recordKey></esaipay>";
		// supplyResult.setModule(str);
		// supplyResult.setStatus(SupplyResult.STATUS_SUCCESS);
		// System.out.println(supplyResult.getModule());

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
				String resultCode = root.elementText("result");
				if (!"success".equals(resultCode)) {
					if (StringUtils.isNotEmpty(resultCode)) {
						String otherCode = resultCode.substring(0, 7);
						if ("timeerr".equals(otherCode) || "syserr_".equals(otherCode)) {
							result.setResultCode(resultCode);
							result.setResultMsg(resultCode + "_" + chargeCodeMap.get("otherCode"));
						}
						if (!("syserr_".equals(otherCode) || "dberr".equals(resultCode) || "lost"
								.equals(resultCode))) {
							result.setStatus(SupplyResult.STATUS_FAILED);
							result.setResultCode(resultCode);
							result.setResultMsg(resultCode + "_" + chargeCodeMap.get("resultCode"));
						} else {
							result.setStatus(SupplyResult.STATUS_UNCONFIRM);
						}
					}

					return result;
				}
				String ustreamSerialno = root.elementText("inOrderNumber");
				supplyOrder.setUpstreamSerialno(ustreamSerialno);
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

		if (StringUtils.isEmpty(supplyOrder.getUpstreamSerialno())) {
			result.setResultMsg("查询失败，上游流水号为空");
			return result;
		}
		YiSaiQueryRequestVO vo = new YiSaiQueryRequestVO();
		vo.createQuerySign(supplyOrder.getUpstreamSerialno());
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendPostByMap(vo.createQueryAciton(), vo.createParamMap());
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
				String resultCode = root.elementText("result");
				if (!"success".equals(resultCode)) {
					if (StringUtils.isNotEmpty(resultCode) && resultCode.length() > 5) {
						String otherCode = resultCode.substring(0, 6);
						if ("syserr".equals(otherCode)) {
							chargeInfo.setStatus(otherCode);
							chargeInfo.setStatusDesc(resultCode);
						} else {
							chargeInfo.setStatus(resultCode);
							chargeInfo.setStatusDesc(chargeCodeMap.get(resultCode));
						}
					} else {
						chargeInfo.setStatus(resultCode);
						chargeInfo.setStatusDesc(chargeCodeMap.get(resultCode));
					}
				} else {
					String payResultCode = root.elementText("payResult");
					chargeInfo.setStatus(payResultCode);
					chargeInfo.setStatusDesc(queryCodeMap.get(payResultCode));
				}
			} catch (DocumentException e) {
				logger.error("parse xml error", e);
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("解析上游信息失败");
				return result;
			}
		}
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		result.setModule(chargeInfo);
		return result;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		YiSaiQueryRequestVO vo = new YiSaiQueryRequestVO();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendPostByMap(vo.createQueryBalanceAction(), vo.createBalanceParamMap());
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
				String resultCode = root.elementText("result");
				if ("success".equals(resultCode)) {
					String fund = root.elementText("fund");
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					balanceQueryInfo.setMsg(resultCode);
					balanceQueryInfo.setBalance(fund);
				} else if (!StringUtils.isEmpty(resultCode)) {
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					balanceQueryInfo.setMsg(resultCode + ":" + chargeCodeMap.get(resultCode));
				} else {
					result.setStatus(SupplyResult.STATUS_FAILED);
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
