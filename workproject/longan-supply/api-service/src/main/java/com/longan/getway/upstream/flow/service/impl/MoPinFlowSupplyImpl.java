package com.longan.getway.upstream.flow.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.flow.vo.MoPinFlowRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

import net.sf.json.JSONObject;

public class MoPinFlowSupplyImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;
	@Resource
	private LocalCachedService localCachedService;
	public static final Map<String, String> codeResultMap = new HashMap<String, String>();

	static {
		codeResultMap.put("200", "请求成功");
		codeResultMap.put("500", "请求失败，原因不明确");
		codeResultMap.put("406", "系统当前处于维护中，请求拒绝");
		codeResultMap.put("900", "没有权限");
		codeResultMap.put("3000", "非法请求");
		codeResultMap.put("3001", "请求内容为空");
		codeResultMap.put("3002", "非法的token");
		codeResultMap.put("3003", "非法的请求参数");
		codeResultMap.put("3004", "非法的数字签名");
		codeResultMap.put("3005", "非法的摘要");
		codeResultMap.put("3006", "非法的开发者密钥");
		codeResultMap.put("3007", "非法的数字签名密钥");

		codeResultMap.put("0000", "订单提交成功");
		codeResultMap.put("1001", "充值的手机号码为空");
		codeResultMap.put("1002", "充值的手机号码不正确或不支持");
		codeResultMap.put("1003", "订单号为空");
		codeResultMap.put("1004", "订单号超出最大长度（30位）");
		codeResultMap.put("1005", "回调地址为空");
		codeResultMap.put("1006", "充值面值格式不正确");
		codeResultMap.put("1007", "充值类型不正确");
		codeResultMap.put("1008", "订单重复");
		codeResultMap.put("1009", "无法创建订单");
		codeResultMap.put("1010", "手机号码查询区域为空");
		codeResultMap.put("1011", "账户余额不足");
		codeResultMap.put("1012", "订单提交失败，扣款失败");
		codeResultMap.put("1013", "订单提交失败，退款失败");
		codeResultMap.put("1014", "订单提交失败，退款成功");
		codeResultMap.put("1015", "所充值的产品不存在");
		codeResultMap.put("1016", "所充值的产品没有报价信息");
		codeResultMap.put("1017", "请求参数不完整");
		codeResultMap.put("1018", "充值渠道暂不可用");
		codeResultMap.put("1019", "流量包可充值范围为空");
		codeResultMap.put("1020", "没有可与充值范围匹配的流量包");
		codeResultMap.put("2001", "平台接口异常");
		codeResultMap.put("2002", "平台接口不可用");
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);

		MoPinFlowRequestVO vo = new MoPinFlowRequestVO();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		vo.setAmount(itemSupply.getSupplyProductCode());
		vo.setChannelOrderId(supplyOrder.getId() + "");
		vo.setContent("");
		SimpleDateFormat formdate = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date();
		vo.setCreateTime(formdate.format(curDate));
		vo.setMobile(supplyOrder.getItemUid());
		vo.setType("1");
		Item item = localCachedService.getItem(itemSupply.getItemId());
		vo.setRange(item.getItemExt2());

		JSONObject jos = JSONObject.fromObject(vo);
		String reqParam = jos.toString();
		String chargeAction = vo.createChargeAction(reqParam);
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance().sendDataByPost(chargeAction, vo.createReqBody(reqParam));
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
			try {
				JSONObject job = JSONObject.fromObject(supplyResult.getModule());
				String statusCode = job.get("statusCode").toString();
				if (!"200".equals(statusCode)) {// 失败
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultCode(statusCode);
					result.setResultMsg(statusCode + "_" + codeResultMap.get(statusCode));
					return result;
				}
				String data = job.get("data").toString();
				JSONObject dataJo = JSONObject.fromObject(data);
				String status = dataJo.get("status").toString();
				if (!"0000".equals(status)) {// 失败
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultCode(status);
					result.setResultMsg(status + "_" + codeResultMap.get(status));
					return result;
				}
				String orderId = dataJo.get("orderId").toString();
				supplyOrder.setUpstreamSerialno(orderId);
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
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		ChargeInfo chargeInfo = new ChargeInfo();
		chargeInfo.setStatusDesc("<a href=\"http://http://121.40.193.238:8090/index.action\">点击进入上游平台核单</a>");
		result.setModule(chargeInfo);
		return result;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		BalanceQueryInfo balanceQueryInfo = new BalanceQueryInfo();
		result.setModule(balanceQueryInfo);
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		balanceQueryInfo.setMsg("该上游不支持余额查询	 <a href=\"http://121.40.193.238:8090/index.action\">点击进入上游平台查询</a>");
		return result;
	}

}
