package com.longan.getway.upstream.unicomflow.service.impl;

import java.util.Random;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.common.domain.QueryRunnable;

public class UnicomFlowHunanOfficMockServiceImpl extends BaseService implements
		UpstreamDirectSupplyService {
	private boolean isAsync = true;
	private static final String supplyName = "湖南联通流量包";

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);

		Random random = new Random();
		int status = random.nextInt(2);
		status = SupplyResult.STATUS_SUCCESS;
		result.setStatus(status);

		// 受理成功，或者未确认
		if (status == SupplyResult.STATUS_SUCCESS || status == SupplyResult.STATUS_UNCONFIRM) {
			bizDealService.submitQueryRunnable(new UnicomFlowHunanOfficMockQueryRunnable(
					supplyName, supplyOrder.getId(), supplyOrder.getBizOrderId()));
		}

		return result;
	}

	@Override
	public SupplyResult<ChargeInfo> chargeQuery(SupplyOrder supplyOrder) {
		SupplyResult<ChargeInfo> result = new SupplyResult<ChargeInfo>();
		ChargeInfo chargeInfo = new ChargeInfo();
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		result.setModule(chargeInfo);
		return result;
	}

	class UnicomFlowHunanOfficMockQueryRunnable extends QueryRunnable {
		public UnicomFlowHunanOfficMockQueryRunnable(String queryName, Long supplyOrderId,
				Long bizOrderId) {
			super(queryName, supplyOrderId, bizOrderId, bizOrderService, bizDealService,
					supplyOrderService);
		}

		protected CallBack chargeResultQuery() {
			CallBack callBack = new CallBack();
			callBack.setSupplyOrderId(supplyOrderId);
			callBack.setActualCost(25000);
			Random random = new Random();
			int status = random.nextInt(2);
			callBack.setStatus(status);
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
