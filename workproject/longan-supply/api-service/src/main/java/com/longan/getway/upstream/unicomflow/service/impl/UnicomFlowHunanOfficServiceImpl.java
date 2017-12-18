package com.longan.getway.upstream.unicomflow.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.unicomflow.vo.HunanUnicomFlowChargeRequestVO;
import com.longan.getway.upstream.unicomflow.vo.HunanUnicomFlowQueryRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;
import com.longan.getway.utils.Utils;

public class UnicomFlowHunanOfficServiceImpl extends BaseService implements
		UpstreamDirectSupplyService {
	private boolean isAsync = true;

	public static final Map<String, String> queryResultMap = new HashMap<String, String>();

	@Resource
	private LocalCachedService localCachedService;

	static {
		queryResultMap.put("0", "订购成功");
		queryResultMap.put("100001", "号码不存在");
		queryResultMap.put("100002", "不是有效合同");
		queryResultMap.put("100003", "产品ID不存在");
		queryResultMap.put("100004", "合作伙伴费用不足");
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);

		HunanUnicomFlowChargeRequestVO vo = new HunanUnicomFlowChargeRequestVO();
		vo.setMobile(supplyOrder.getItemUid());
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
		vo.setProductid(itemSupply.getSupplyProductCode());
		vo.createTransId(Utils.FormatStringAddZero(supplyOrder.getId() + "", 8));
		vo.createSign();

		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByPost(vo.createChargeAciton(), vo.createRequestParam());
		supplyOrder.setUpstreamSerialno(vo.getTransid());
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
				String code = jo.get("code").toString();
				String msg = jo.get("msg").toString();
				if (!"0".equals(code)) {
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultMsg(code + "_" + msg);
					return result;
				}
			} catch (Exception e) {
				logger.error("parse to json error", e);
				result.setResultMsg("解析上游信息失败");
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

		HunanUnicomFlowQueryRequestVO vo = new HunanUnicomFlowQueryRequestVO();
		vo.setTransid(supplyOrder.getUpstreamSerialno());
		vo.createSign();

		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByPost(vo.createQueryAction(), vo.createRequestParam());
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
			String jsonString = supplyResult.getModule();
			JSONObject jo = JSONObject.fromObject(jsonString);
			String code = jo.get("code").toString();
			String msg = jo.get("msg").toString();

			if (!"0".equals(code)) {
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg(code + "_" + msg);
				return result;
			}

			String status = jo.get("status").toString();
			chargeInfo.setCode(code);
			chargeInfo.setMsg(msg);
			chargeInfo.setStatus(status);
			chargeInfo.setStatusDesc(queryResultMap.get(status));
		}

		result.setStatus(SupplyResult.STATUS_SUCCESS);
		result.setModule(chargeInfo);
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
