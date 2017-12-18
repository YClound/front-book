package com.longan.getway.upstream.unicomflow.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

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
import com.longan.getway.upstream.unicomflow.vo.ShuiJSFlowChargeRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class UnicomFlowShuiJSServiceImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;
	private static final String supplyName = "水晶树全国流量包";
	public static final Map<String, String> queryResultMap = new HashMap<String, String>();
	public static final Map<String, String> codeResultMap = new HashMap<String, String>();

	static {
		queryResultMap.put("0", "订购成功, 充值成功");
		queryResultMap.put("1", "订购失败, 可以做退款操作");
		queryResultMap.put("2", "查询失败, 无该订单");
		queryResultMap.put("3", "处理中, 请不要做退款操作");

		codeResultMap.put("1000", "系统错误");
		codeResultMap.put("1001", "参数格式不正确");
		codeResultMap.put("1002", "请求鉴权失败");
		codeResultMap.put("1003", "请求服务不存在");
		codeResultMap.put("1004", "请求参数验证失败");
		codeResultMap.put("1005", "非法的 IP 地址");
		codeResultMap.put("1006", "请求时间不合法");
		codeResultMap.put("2001", "订购用户不存在");
		codeResultMap.put("2002", "订购用户状态被停止");
		codeResultMap.put("2003", "订购用户欠费");
		codeResultMap.put("2004", "订购用户在黑名单");
		codeResultMap.put("2005", "订购关系已存在");
		codeResultMap.put("2006", "订购关系不存在");
		codeResultMap.put("2007", "不已订购的其他产品冲突");
		codeResultMap.put("2008", "不允许变更的产品");
		codeResultMap.put("2009", "订购接入通道参数错误");
		codeResultMap.put("2010", "用户套餐不能订购该业务");
		codeResultMap.put("2011", "其他原因");
		codeResultMap.put("2012", "订购接入通道订购失败");
		codeResultMap.put("3001", "客户信息不存在");
		codeResultMap.put("3002", "剩余可用帐户不足");
		codeResultMap.put("3003", "客户信用度低");
		codeResultMap.put("3004", "无手机号对应的客户产品");
		codeResultMap.put("3005", "剩余的订购关系数目不足");
		codeResultMap.put("3006", "产品归属地不手机号码归属地不匹配");
		codeResultMap.put("3007", "活动信息不存在");
		codeResultMap.put("3008", "活动不在服务时间内");
		codeResultMap.put("3009", "客户活动中无对应的产品信息");
		codeResultMap.put("3010", "客户未开通订购产品");
		codeResultMap.put("9001", "等待响应消息超时");
		codeResultMap.put("9002", "网络连接不正常");
		codeResultMap.put("9003", "数据库连接异常");
		codeResultMap.put("9999", "其他原因");
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);
		ShuiJSFlowChargeRequestVO vo = new ShuiJSFlowChargeRequestVO();
		vo.setPhoneNum(supplyOrder.getItemUid());
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}

		if (StringUtils.isEmpty(itemSupply.getSupplyProductCode())) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("上游商品编号为空");
			return result;
		}
		vo.setProductId(itemSupply.getSupplyProductCode());
		vo.setRealPrice(itemSupply.getItemCostPrice() / 10 + "");// 上游以分为单位
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByGetReturnString(vo.getChargeURL());
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
			String jsonString = supplyResult.getModule();
			if (StringUtils.isEmpty(jsonString)) {
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg("上游返回信息为空");
				return result;
			}
			try {
				JSONObject jo = JSONObject.fromObject(jsonString);
				String desc = jo.get("desc").toString();
				String status = jo.get("status").toString();
				if ("2".equals(status) || "7".equals(status)) {
					String data = jo.get("data").toString();
					JSONObject dataJo = JSONObject.fromObject(data);
					String ustreamSerialno = (String) dataJo.get("orderId");
					supplyOrder.setUpstreamSerialno(ustreamSerialno);
					String code = (String) dataJo.get("code");
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultMsg(desc + "_" + code + ":" + codeResultMap.get(code));
					return result;
				}
				if ("3".equals(status)) {
					String data = jo.get("data").toString();
					JSONObject dataJo = JSONObject.fromObject(data);
					String ustreamSerialno = (String) dataJo.get("orderId");
					supplyOrder.setUpstreamSerialno(ustreamSerialno);
					String code = (String) dataJo.get("code");
					result.setStatus(SupplyResult.STATUS_UNCONFIRM);
					result.setResultMsg(desc + "_" + code + ":" + codeResultMap.get(code));
					return result;
				}
				if ("0".equals(status) || "1".equals(status)) {
					String data = jo.get("data").toString();
					JSONObject dataJo = JSONObject.fromObject(data);
					String ustreamSerialno = (String) dataJo.get("orderId");
					supplyOrder.setUpstreamSerialno(ustreamSerialno);
				} else {
					result.setStatus(SupplyResult.STATUS_UNCONFIRM);
					return result;
				}

				bizDealService.submitQueryRunnable(new UnicomFlowShuiJSQueryRunnable(supplyName,
						supplyOrder.getId(), supplyOrder.getBizOrderId()));
				result.setStatus(SupplyResult.STATUS_SUCCESS);
			} catch (Exception e) {
				logger.error("parse to json error", e);
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("解析上游信息失败");
				return result;
			}
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
		ShuiJSFlowChargeRequestVO vo = new ShuiJSFlowChargeRequestVO();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByGetReturnString(vo.getQueryURL(supplyOrder.getUpstreamSerialno()));
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
			result.setStatus(SupplyResult.STATUS_SUCCESS);
			String jsonString = supplyResult.getModule();
			JSONObject jo = JSONObject.fromObject(jsonString);
			String status = jo.get("status").toString();
			chargeInfo.setStatus(status);
			chargeInfo.setStatusDesc(queryResultMap.get(status));
		}
		result.setModule(chargeInfo);
		return result;
	}

	class UnicomFlowShuiJSQueryRunnable extends QueryRunnable {
		public UnicomFlowShuiJSQueryRunnable(String queryName, Long supplyOrderId, Long bizOrderId) {
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
			if (!chargeInfoResult.isSuccess() || chargeInfo == null) {
				logger.error("chargeQuery error : " + chargeInfoResult.getResultMsg());
				return null;
			}

			CallBack callBack = new CallBack();
			callBack.setSupplyOrderId(supplyOrderId);
			if (!"0".equals(chargeInfo.getStatus())) {
				if ("1".equals(chargeInfo.getStatus())) {

					callBack.setStatus(SupplyResult.STATUS_FAILED);
					return callBack;
				} else if ("3".equals(chargeInfo.getStatus()) || "2".equals(chargeInfo.getStatus())) {
					callBack.setStatus(SupplyResult.STATUS_UNCONFIRM);
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
		BalanceQueryInfo balanceQueryInfo = new BalanceQueryInfo();
		result.setModule(balanceQueryInfo);
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		balanceQueryInfo.setMsg("该上游不支持余额查询");
		return result;
	}
}
