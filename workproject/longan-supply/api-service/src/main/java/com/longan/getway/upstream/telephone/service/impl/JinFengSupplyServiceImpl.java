package com.longan.getway.upstream.telephone.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.telephone.vo.JinFengRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class JinFengSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;
	public static final Map<String, String> codeResultMap = new HashMap<String, String>();

	static {
		codeResultMap.put("100000", "无参数");
		codeResultMap.put("100001", "P0_biztype值为空");
		codeResultMap.put("100002", "P0_biztype超长 11位");
		codeResultMap.put("100003", "P0_biztype错误");
		codeResultMap.put("100004", "P1_agentcode为空");
		codeResultMap.put("100005", "P1_agentcode长度有误");
		codeResultMap.put("100006", "P1_agentcode不存在");
		codeResultMap.put("100007", "P2_mobile为空");
		codeResultMap.put("100008", "P2_mobile长度有误");
		codeResultMap.put("100009", "不支持该号段");
		codeResultMap.put("100010", "P3_parvalue为空");
		codeResultMap.put("100011", "充值面额过大");
		codeResultMap.put("100012", "充值面额不能为负");
		codeResultMap.put("100013", "P4_productcode为空");
		codeResultMap.put("100014", "P4_productcode长度有误");
		codeResultMap.put("100015", "P5_requestid为空");
		codeResultMap.put("100016", "P5_requestid超长");
		codeResultMap.put("100017", "P6_callbackurl超长");
		codeResultMap.put("100018", "P7_extendinfo超长");
		codeResultMap.put("100019", "hmac为空");
		codeResultMap.put("100020", "hmac超长");
		codeResultMap.put("100021", "手机号码包含非数字字符");
		codeResultMap.put("100022", "面额包含非数字字符");
		codeResultMap.put("100023", "产品没开通");
		codeResultMap.put("100024", "产品状态没开通");
		codeResultMap.put("100025", "面额没有开通");
		codeResultMap.put("100026", "区域没有开通");
		codeResultMap.put("100027", "代理商余额不足");
		codeResultMap.put("100028", "hmac签名错误");
		codeResultMap.put("100029", "订单号重复");
		codeResultMap.put("100030", "提交产品类别不合法");
		codeResultMap.put("100031", "ip不正确");
		codeResultMap.put("100032", "扣款失败（通过查询或者客服去处理）");
		codeResultMap.put("200000", "无参数");
		codeResultMap.put("200001", "代理商编号为空");
		codeResultMap.put("200002", "代理商编号位数不对");
		codeResultMap.put("200003", "代理商编号不存在");
		codeResultMap.put("200004", "签名错误");
		codeResultMap.put("300000", "无参数");
		codeResultMap.put("300001", "代理商编号为空");
		codeResultMap.put("300002", "代理商编号位数不对");
		codeResultMap.put("300003", "代理商不存在");
		codeResultMap.put("300004", "请求订单号不能为空");
		codeResultMap.put("300005", "请求订单号超长");
		codeResultMap.put("300006", "签名错误");
		codeResultMap.put("300007", "不存在订单（下单后10~30分钟后查询,不要以此做依据）");
		codeResultMap.put("300008", "查询异常");
		codeResultMap.put("0", "排队中");
		codeResultMap.put("1", "充值中");
		codeResultMap.put("2", "充值成功");
		codeResultMap.put("3", "充值失败");
		codeResultMap.put("5", "正在退款");
		codeResultMap.put("6", "退款成功");
		codeResultMap.put("7", "部分退款");
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);
		JinFengRequestVO vo = new JinFengRequestVO();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		vo.setP2_mobile(supplyOrder.getItemUid());
		vo.setP3_parvalue(itemSupply.getSupplyProductCode());
		int bizId = supplyOrder.getBizId();
		if (bizId == 200) {// 联通
			vo.setP4_productcode("SHKC_CU");
		} else if (bizId == 201) {// 移动
			vo.setP4_productcode("SHKC");
		} else if (bizId == 202) {// 电信
			vo.setP4_productcode("SHKC_CT");
		} else if (bizId == Constants.BizInfo.CODE_FLOW_MOBILE) {
			vo.setP4_productcode("SHKCFA");
		} else if (bizId == Constants.BizInfo.CODE_FLOW_TELECOM) {
			vo.setP4_productcode("SHKCFA_CT");
		} else if (bizId == Constants.BizInfo.CODE_FLOW_UNICOM) {
			vo.setP4_productcode("SHKCFA_CU");
		}

		vo.setP5_requestid(supplyOrder.getId() + "");
		vo.createChargeHmac();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance().sendPostByMap(vo.createChargeAction(), vo.createChargeParam());
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
			String resultStr = supplyResult.getModule();
			if (!"000000".equals(resultStr)) {
				if ("100029".equals(resultStr) || "100032".equals(resultStr) || resultStr.length() < 6) {
					result.setStatus(SupplyResult.STATUS_UNCONFIRM);
					result.setResultMsg(resultStr + "_" + codeResultMap.get(resultStr));
					return result;
				} else {
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultCode(resultStr);
					result.setResultMsg(resultStr + "_" + codeResultMap.get(resultStr));
					return result;
				}
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
		JinFengRequestVO vo = new JinFengRequestVO();
		vo.setP5_requestid(supplyOrder.getId() + "");
		vo.createQueryHmac();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance().sendPostByMap(vo.createQueryAction(), vo.createQueryParam());
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
			String resultStr = supplyResult.getModule();
			String[] strs = resultStr.split("\\|");
			if (!"000000".equals(strs[0])) {
				result.setStatus(SupplyResult.STATUS_SUCCESS);
				chargeInfo.setStatus(strs[0]);
				chargeInfo.setStatusDesc(codeResultMap.get(strs[0]));
				return result;
			}
			result.setStatus(SupplyResult.STATUS_SUCCESS);
			chargeInfo.setStatus(strs[5]);
			chargeInfo.setStatusDesc(codeResultMap.get(strs[5]));
		}
		return result;
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		JinFengRequestVO vo = new JinFengRequestVO();
		vo.createQueryBalanceHmac();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance().sendPostByMap(vo.createQueryBalanceAction(), vo.createQueryBalanceParam());
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
			String resultStr = supplyResult.getModule();
			String[] strs = resultStr.split("\\|");
			if (!"000000".equals(strs[0])) {
				result.setStatus(SupplyResult.STATUS_SUCCESS);
				balanceQueryInfo.setMsg(strs[0] + ":" + codeResultMap.get(strs[0]));
				return result;
			}
			result.setStatus(SupplyResult.STATUS_SUCCESS);
			balanceQueryInfo.setMsg("查询余额成功");
			balanceQueryInfo.setBalance(strs[2]);
		}
		return result;
	}
	
}
