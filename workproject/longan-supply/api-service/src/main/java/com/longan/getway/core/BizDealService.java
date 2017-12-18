package com.longan.getway.core;

import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.common.domain.QueryRunnable;

public interface BizDealService {
	// public void dealBizOrder(BizOrder bizOrder, CallBack callBack);

	public void submitQueryRunnable(QueryRunnable runable);
	
	public void submitQbQueryRunnable(QueryRunnable runnable);

	public void dealBizOrder(SupplyOrder supplyOrder, CallBack callBack);

	public Result<SupplyOrder> dealSyncSupplyResult(SupplyResult<SupplyOrder> chargeResult,
			SupplyOrder supplyOrder, BizOrder bizOrder);

	public void dealAsyncSupplyResult(SupplyResult<SupplyOrder> chargeResult,
			SupplyOrder supplyOrder, BizOrder bizOrder);

}
