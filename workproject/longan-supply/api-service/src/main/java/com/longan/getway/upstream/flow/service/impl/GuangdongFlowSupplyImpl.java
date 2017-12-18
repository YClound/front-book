package com.longan.getway.upstream.flow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.flow.vo.guangdong.CTConstant;
import com.longan.getway.upstream.flow.vo.guangdong.CtController;
import com.longan.getway.upstream.flow.vo.guangdong.SecurityHandler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GuangdongFlowSupplyImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = false;

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);
		try {
			ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
			if (itemSupply == null) {
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg("供货商品为空");
				return result;
			}
			Map<String, String> input = new HashMap<String, String>();
			input.put("PHONE_NUMBER", supplyOrder.getItemUid());
			input.put("PRD_CODE", itemSupply.getSupplyProductCode());

			// 生成数字信封(DIGITAL_ENVELOPE)
			String desKey = SecurityHandler.genRandomNum(16);
			String envelope = SecurityHandler.generateEnvelope(desKey);

			// 生成数字签名（DEGITAL_SIGNATURE）
			String signature = SecurityHandler.encodeBase64(SecurityHandler.sign(JSONObject.fromObject(input).toString().getBytes()));

			// 生成密文（DATA_JSON）
			byte[] data = SecurityHandler.encryptAES(JSONObject.fromObject(input).toString().getBytes("UTF-8"), desKey);
			String mw = SecurityHandler.encodeBase64(data);
			/**
			 * 构造提交前置参数，必须是post提交
			 */
			/*
			 * envelope =
			 * "S5qTPWeR/oMsCL/2SczP0mj5SHI8hINQmk+qfc+dqKElIWB9J0LkGdrBInM4HxEH/7gPdmtDepfcgmn3Q/aMNYUoj6UoPq0BbvVZp5eXCUMOxMTJVgL5BrEa3vTABSCGKap/Ha1rmJXQ/aEOR4WM4VvGkVBTindkPI+xyZL+kPE="
			 * ; signature =
			 * "0VeAJV+gRqrBeVx26qmonzEbcQOyH6dPQs79Ro209iZrXSkZvZvCRT1EGUt8BLF55/crwDMXbUyo+WoAKu7lWQt3mmlnBuj/5QQZj6nQb5qXJH9nKXPmBfeShC5QcPcW4boxOocWzgC4vQyNcQ7pe4wea9ZuzErWODJ9KceRQDY="
			 * ; mw =
			 * "+OKgU58AvD79FeSzOek0d+93F5bIcEj6vX+xg7IpDTeCHMZX9+9EzGfwoQKCq2FAVE+kNYbnPlOOYwxRFf/Gr4ebkGDksHuhmA21pkRZZFO3P/1+SsXW0kDR9iUen8R/43aBIPDJCTB/lpNtRft3mQDDgqZY5vGMIYTRHZ2RWQ4="
			 * ;
			 */
			String param = "SERVICE_TYPE=" + SecurityHandler.charge_serviceType + "&SAFE_MODE=RSA" + "&DIGITAL_ENVELOPE" + "=" + envelope + "&DEGITAL_SIGNATURE" + "=" + signature + "&MERCH_CODE=" + SecurityHandler.accountNum + "&DATA_JSON" + "=" + mw;

			logger.warn("广东省流量充值请求:" + param);
			/*
			 * System.out.println("envelope:" + envelope);
			 * System.out.println("signature:" + signature);
			 * System.out.println("密文:" + mw);
			 */
			/**
			 * 提交并获取响应
			 */
			// String ss =
			// SupplyResult<String> supplyResult =
			// MultiThreadedHttpConnection.getInstance().sendDataByPost(SecurityHandler.url+"/service.do",
			// param);
			// SecurityHandler.postHttp(SecurityHandler.url, "service.do",
			// param);

			String ss = SecurityHandler.postHttp(SecurityHandler.url, "/service.do", param);
			// String ss = supplyResult.getModule();
			if (ss == null) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("未有返回数据");
				return result;
			}
			// if (supplyResult.isFailed()) {
			// result.setResultMsg(supplyResult.getResultMsg());
			// return result;
			// }
			// if (supplyResult.isUnConfirm()) {
			// result.setStatus(SupplyResult.STATUS_UNCONFIRM);
			// result.setResultMsg("充值处理中");
			// return result;
			// }
			// if (supplyResult.isUndifend()) {
			// result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SYSTEM);
			// return result;
			// }
			logger.warn("广东省流量充值返回:" + ss);
			// System.out.println("return:" + ss);
			Map<String, Object> ret = parseJSON2Map(ss);
			logger.warn("广东省流量充值返回:" + ret);
			Object rc = ret.get("RESULT_CODE");
			Object transId = ret.get("TRANS_ID");
			if (transId != null) {
				supplyOrder.setUpstreamSerialno(String.valueOf(transId));
			}
			if (rc != null) {

				/**
				 * 20014是会话过期代码，这时需要调用登录接口先登录
				 */
				if (rc.equals("20014")) {
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultMsg("会话过期,请重新登录");
					return result;
				}

				// 只有成功后才能调用下面的代码
				if (rc.toString().equals("00000")) {
					// 获取签名
					String sign = ret.get("DEGITAL_SIGNATURE").toString();
					// 获取信封
					String enve = ret.get("DIGITAL_ENVELOPE").toString();
					// 获取密文
					String enData = ret.get("DATA_JSON").toString();

					// 解开信封
					byte[] d = SecurityHandler.decodeBase64(enve);
					byte[] b = SecurityHandler.decryptByPrivateKey(d);

					// 解开密文
					byte[] srcData = SecurityHandler.decryptAES(SecurityHandler.decodeBase64(enData), new String(b));

					// 验证签名
					if (SecurityHandler.verify(srcData, sign, SecurityHandler.threePublicKey)) {
						// 充值成功
						result.setStatus(SupplyResult.STATUS_SUCCESS);
						return result;
					} else {
						result.setStatus(SupplyResult.STATUS_FAILED);
						result.setResultMsg("验签失败，充值失败");
						return result;
					}
				} else {
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultMsg(ret.get("RESULT_INFO") == null ? "交易失败" : String.valueOf(ret.get("RESULT_INFO")));
					return result;
				}
			} else {
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg("接收到为空则前置接口有问题,请联系上游供货商");
				return result;
			}

		} catch (Exception e) {
			logger.error("", e);
			result.setStatus(SupplyResult.STATUS_UNCONFIRM);
			result.setResultMsg(e.getMessage());
			return result;
		}
	}

	@Override
	public SupplyResult<ChargeInfo> chargeQuery(SupplyOrder supplyOrder) {
		SupplyResult<ChargeInfo> result = new SupplyResult<ChargeInfo>();
		try {
			if (supplyOrder.getUpstreamSerialno() == null) {
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg("未记录上游订单号");
				return result;
			}
			CtController con = new CtController();
			Map<String, Object> input = new HashMap<String, Object>();
			input.put("TRANS_ID", supplyOrder.getUpstreamSerialno());
			input.put("SERVICE_TYPE", SecurityHandler.queryOrder_serviceType);
			input.put("SAFE_MODE", "RSA");
			input.put("MERCH_CODE", SecurityHandler.accountNum);

			String desKey = SecurityHandler.genRandomNum(16);
			String envelope = SecurityHandler.generateEnvelope(desKey);

			// 生成数字签名（DEGITAL_SIGNATURE）
			String signature = SecurityHandler.encodeBase64(SecurityHandler.sign(JSONObject.fromObject(input).toString().getBytes()));

			// 生成密文（DATA_JSON）
			byte[] data = SecurityHandler.encryptAES(JSONObject.fromObject(input).toString().getBytes("UTF-8"), desKey);
			String mw = SecurityHandler.encodeBase64(data);
			/**
			 * 构造提交前置参数，必须是post提交
			 */
			String param = "SERVICE_TYPE=" + input.get("SERVICE_TYPE") + "&SAFE_MODE=" + input.get("SAFE_MODE") + "&DIGITAL_ENVELOPE" + "=" + envelope + "&DEGITAL_SIGNATURE" + "=" + signature + "&MERCH_CODE=" + input.get("MERCH_CODE") + "&DATA_JSON" + "=" + mw;
			// SupplyResult<String> supplyResult =
			// MultiThreadedHttpConnection.getInstance().sendDataByPost(SecurityHandler.url+"/service.do",
			// param);
			// // SecurityHandler.postHttp(SecurityHandler.url, "service.do",
			// param);
			//
			// if (supplyResult.getModule() == null) {
			// result.setStatus(SupplyResult.STATUS_FAILED);
			// result.setResultMsg(supplyResult.getResultMsg());
			// return result ;
			// }
			// String ret = supplyResult.getModule();
			String ret = SecurityHandler.postHttp(SecurityHandler.url, "/service.do", param);
			if (ret == null) {
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg("没有收到返回的数据");
				return result;
			}
			result.setStatus(SupplyResult.STATUS_SUCCESS);
			ChargeInfo info = new ChargeInfo();
			Map<String, String> map = con.receive(ret);
			String code = map.get(CTConstant.RC);
			String msg = map.get(CTConstant.RI);
			info.setStatus(code);
			if (CTConstant.SUCCESS.equals(code)) {
				String dataj = map.get("DATA_JSON");
				info.setStatus(code);
				info.setStatusDesc(dataj);
			} else {
				info.setStatusDesc(msg);
			}
			result.setModule(info);
		} catch (Exception e) {
			logger.error("", e);

		}
		return result;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		result.setResultMsg("该接口不支持余额查询");
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseJSON2Map(String jsonStr) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		// 最外层解析
		JSONObject json = JSONObject.fromObject(jsonStr);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			// 如果内层还是数组的话，继续解析
			if (v instanceof JSONArray) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Iterator<JSONObject> it = ((JSONArray) v).iterator();
				while (it.hasNext()) {
					JSONObject json2 = it.next();
					list.add(parseJSON2Map(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}
}
