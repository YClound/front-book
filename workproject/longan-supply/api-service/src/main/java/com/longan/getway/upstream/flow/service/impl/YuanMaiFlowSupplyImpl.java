package com.longan.getway.upstream.flow.service.impl;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.longan.getway.upstream.flow.vo.YuanMaiFlowRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class YuanMaiFlowSupplyImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;

	public static final Map<String, String> codeResultMap = new HashMap<String, String>();

	@Resource
	private LocalCachedService localCachedService;
	static {
		codeResultMap.put("1", "充值成功");
		codeResultMap.put("2", "充值失败");
		codeResultMap.put("3", "部分成功");
		codeResultMap.put("4", "正在处理");

		codeResultMap.put("0000", "成功");
		codeResultMap.put("0001", "充值失败");
		codeResultMap.put("0004", "代理商未激活");
		codeResultMap.put("0005", "未开通流量充值功能");
		codeResultMap.put("0006", "IP地址不符合要求");
		codeResultMap.put("0007", "传入参数不完整");
		codeResultMap.put("0008", "合法性验证失败");
		codeResultMap.put("0009", "订单号不允许重复");
		codeResultMap.put("0010", "无法查到充值号码对应号段");
		codeResultMap.put("0011", "支付失败，发货未成功");
		codeResultMap.put("0012", "账户余额不足");
		codeResultMap.put("0013", "没有对应充值产品");
		codeResultMap.put("0014", "充值号码格式错误,或未知号段");
		codeResultMap.put("0015", "此产品超出当天限额，请联系业务人员");
		codeResultMap.put("0016", "系统异常，请稍后重试");
		codeResultMap.put("0017", "订单不存在");
		codeResultMap.put("0019", "该号码充值频率过高");
		codeResultMap.put("0021", "部分充值成功");
		codeResultMap.put("0022", "参数值错误");
		codeResultMap.put("0024", "黑名单用户");
		codeResultMap.put("0025", "系统维护，暂不能充值");
		codeResultMap.put("0026", "查询频率过高");
		codeResultMap.put("0027", "代理商没有冲正权限");
		codeResultMap.put("0028", "没有可冲正的订单");
		codeResultMap.put("0029", "该笔订单超过冲正次数");
		codeResultMap.put("0030", "该笔订单已存在冲正");
		codeResultMap.put("0031", "代理商冲正次数超过限制次数");
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);

		YuanMaiFlowRequestVO vo = new YuanMaiFlowRequestVO();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		vo.setFlowsize(itemSupply.getSupplyProductCode());
		vo.setOrderid(supplyOrder.getId() + "");
		SimpleDateFormat formdate = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date();
		vo.setOrdertime(formdate.format(curDate));
		vo.setPhotonum(supplyOrder.getItemUid());
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
				String resultno = root.elementText("resultno");
				String orderamount = root.elementText("orderamount");
				if (!"0000".equals(resultno)) {
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultCode(resultno);
					result.setResultMsg(resultno + "_" + codeResultMap.get(resultno));
					return result;
				}
				supplyOrder.setSupplyActualCost(BigDecimalUtils.multInteger(orderamount));
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
		YuanMaiFlowRequestVO vo = new YuanMaiFlowRequestVO();
		vo.setOrderid(supplyOrder.getId() + "");
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
				String resultno = root.elementText("resultno");
				String status = root.elementText("status");
				String errormsg = root.elementText("errormsg");
				if (!StringUtils.isEmpty(resultno)) { // 查询成功
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					chargeInfo.setStatus(resultno);
					if ("0000".equals(resultno)) {
						chargeInfo.setStatus(status);
						chargeInfo.setStatusDesc(codeResultMap.get(status));
					} else {
						try {
							errormsg = URLDecoder.decode(errormsg, "UTF-8");
							chargeInfo.setStatusDesc(errormsg);
						} catch (UnsupportedEncodingException e) {
							chargeInfo.setStatusDesc(codeResultMap.get(resultno));
						}
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
		YuanMaiFlowRequestVO vo = new YuanMaiFlowRequestVO();
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
			if (!StringUtils.isEmpty(codeResultMap.get(supplyResult.getModule()))) {
				result.setStatus(SupplyResult.STATUS_SUCCESS);
				balanceQueryInfo.setMsg(supplyResult.getModule() + "_"
						+ codeResultMap.get(supplyResult.getModule()));
				return result;
			}
			SAXReader reader = new SAXReader();
			try {
				Document document = reader.read(new StringReader(supplyResult.getModule()));
				Element root = document.getRootElement();
				String resultno = root.elementText("resultno");
				String balance = root.elementText("balance");
				if ("0000".equals(resultno)) {
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					balanceQueryInfo.setMsg(resultno + ":" + codeResultMap.get(resultno));
					balanceQueryInfo.setBalance(balance);
					return result;
				}
				if (!StringUtils.isEmpty(resultno)) { // 查询成功
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					balanceQueryInfo.setMsg(resultno + ":" + codeResultMap.get(resultno));
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
