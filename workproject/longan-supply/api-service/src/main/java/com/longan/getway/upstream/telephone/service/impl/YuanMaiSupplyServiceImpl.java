package com.longan.getway.upstream.telephone.service.impl;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.longan.getway.upstream.telephone.vo.YuanMaiChargeRequestVO;
import com.longan.getway.upstream.telephone.vo.YuanMaiQueryRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class YuanMaiSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;
	public static final Map<String, String> codeResultMap = new HashMap<String, String>();

	@Resource
	private LocalCachedService localCachedService;
	static {
		codeResultMap.put("1", "正在处理");
		codeResultMap.put("2", "成功");
		codeResultMap.put("3", "部分成功");
		codeResultMap.put("4", "充值失败");

		codeResultMap.put("0000", "成功");
		codeResultMap.put("0001", "下单失败，未扣款");
		codeResultMap.put("0002", "充值失败");
		codeResultMap.put("0003", "加密摘要验证失败");
		codeResultMap.put("0004", "代理商未激活");
		codeResultMap.put("0005", "未开通直冲功能");
		codeResultMap.put("0006", "IP地址不符合要求");
		codeResultMap.put("0007", "传入参数不完整");
		codeResultMap.put("0008", "合法性验证失败");
		codeResultMap.put("0009", "订单号不允许重复");
		codeResultMap.put("0010", "无法查到对应号段");
		codeResultMap.put("0011", "支付失败，发货未成功");
		codeResultMap.put("0012", "账户余额不足");
		codeResultMap.put("0013", "没有对应充值产品，号码与选择产品不符");
		codeResultMap.put("0014", "充值号码格式错误");
		codeResultMap.put("0015", "此产品超出当天限额，请联系业务人员");
		codeResultMap.put("0016", "系统异常，请稍后重试");
		codeResultMap.put("0017", "订单不存在");
		codeResultMap.put("0018", "产品与手机号不匹配");
		codeResultMap.put("0019", "该号码充值频率过高");
		codeResultMap.put("0020", "运营商系统升级，暂不能充值");
		codeResultMap.put("0021", "部分充值成功");
		codeResultMap.put("0022", "输入参数值错误");
		codeResultMap.put("0023", "非法的购买数量");
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
		YuanMaiChargeRequestVO vo = new YuanMaiChargeRequestVO();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		vo.setPhotonum(supplyOrder.getItemUid());
		vo.setAmount(supplyOrder.getItemFacePrice() / 1000 + "");
		vo.setOrderid(supplyOrder.getId() + "");
		SimpleDateFormat formdate = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date();
		vo.setOrdertime(formdate.format(curDate));
		vo.createChargeSign();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendPostByMap(vo.createChargeAciton(), vo.createParamMap());
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
				Element items = root.element("items");
				@SuppressWarnings("unchecked")
				List<Element> itemList = (List<Element>) items.elements("item");
				HashMap<String, String> map = new HashMap<String, String>();
				for (Element element : itemList) {
					map.put(element.attributeValue("name"), element.attributeValue("value"));
				}
				String upstreamSerialno = map.get("orderid");
				supplyOrder.setUpstreamSerialno(upstreamSerialno);
				if (!"0000".equals(map.get("resultno"))) {// 非成功
					String code = map.get("resultno");
					result.setResultCode(code);
					result.setResultMsg(code + "_" + codeResultMap.get(code));
					return result;
				}
				supplyOrder
						.setSupplyActualCost(BigDecimalUtils.multInteger(map.get("orderamount")));
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
		YuanMaiQueryRequestVO vo = new YuanMaiQueryRequestVO();
		vo.setOrderid(supplyOrder.getId() + "");
		vo.createQureySign();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendPostByMap(vo.createQueryAction(), vo.createParamMap());
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
			if (!StringUtils.isEmpty(codeResultMap.get(supplyResult.getModule()))) {
				chargeInfo.setStatus(supplyResult.getModule());
				chargeInfo.setStatusDesc(codeResultMap.get(supplyResult.getModule()));
				result.setStatus(SupplyResult.STATUS_SUCCESS);
				return result;
			}
			SAXReader reader = new SAXReader();
			try {
				Document document = reader.read(new StringReader(supplyResult.getModule()));
				Element root = document.getRootElement();
				Element items = root.element("items");
				@SuppressWarnings("unchecked")
				List<Element> itemList = (List<Element>) items.elements("item");
				HashMap<String, String> map = new HashMap<String, String>();
				for (Element element : itemList) {
					map.put(element.attributeValue("name"), element.attributeValue("value"));
				}
				if (!StringUtils.isEmpty(map.get("resultno"))) { // 查询成功
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					String code = map.get("resultno");
					chargeInfo.setStatus(code);
					if ("0021".equals(code)) {
						chargeInfo.setStatusDesc(codeResultMap.get(code) + "  实际充值："
								+ map.get("finishmoney"));
					} else {
						chargeInfo.setStatusDesc(codeResultMap.get(code));
					}
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
		YuanMaiQueryRequestVO vo = new YuanMaiQueryRequestVO();
		vo.createQureyBalanceSign();
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
