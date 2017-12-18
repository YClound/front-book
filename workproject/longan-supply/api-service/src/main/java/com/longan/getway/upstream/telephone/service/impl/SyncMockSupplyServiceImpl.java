package com.longan.getway.upstream.telephone.service.impl;

import java.util.Date;
import java.util.Random;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.utils.BigDecimalUtils;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;

public class SyncMockSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = false;

	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setAsync(isAsync);
		result.setModule(supplyOrder);
		int random = new Random().nextInt(3);
		if (random == SupplyResult.STATUS_FAILED) {
			result.setResultMsg("模拟供货失败。");
		}
		supplyOrder.setUpstreamDate(new Date());
		supplyOrder.setUpstreamSerialno(System.nanoTime() + "");
		result.setStatus(random);
		return result;
	}

	@Override
	public SupplyResult<ChargeInfo> chargeQuery(SupplyOrder supplyOrder) {
		SupplyResult<ChargeInfo> result = new SupplyResult<ChargeInfo>();
		ChargeInfo chargeInfo = new ChargeInfo();
		chargeInfo.setActualCost(BigDecimalUtils.multInteger("18.82"));
		chargeInfo.setStatus("0000");
		chargeInfo.setStatusDesc("mock充值成功");
		chargeInfo.setMsg("成功");
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
