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
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.common.domain.QueryRunnable;

public class AsyncMockSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;

	private static final String supplyName = "异步模拟供货";

	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setAsync(isAsync);
		result.setModule(supplyOrder);
		supplyOrder.setUpstreamDate(new Date());
		supplyOrder.setUpstreamSerialno(System.nanoTime() + "");
		bizDealService.submitQueryRunnable(new AsyncMockQueryRunnable(supplyName, supplyOrder
				.getId(), supplyOrder.getBizOrderId()));
		result.setStatus(SupplyResult.STATUS_SUCCESS);
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

	class AsyncMockQueryRunnable extends QueryRunnable {
		public AsyncMockQueryRunnable(String queryName, Long supplyOrderId, Long bizOrderId) {
			super(queryName, supplyOrderId, bizOrderId, bizOrderService, bizDealService,
					supplyOrderService);
		}

		protected CallBack chargeResultQuery() {
			CallBack callBack = new CallBack();
			callBack.setSupplyOrderId(supplyOrderId);
			callBack.setActualCost(25000);
			Random random = new Random();
			int status = random.nextInt(3);
			if (status == 1 || status == 2) {
				callBack.setStatus(1);
			} else {
				callBack.setStatus(0);
			}

			return callBack;
		}
	}

}
