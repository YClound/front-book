package com.longan.getway.core;

import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;

public interface SupplyService {
//
//	Result<BizOrder> supply(BizOrder bizOrder);
//
//	Result<BizOrder> asyncSupply(final BizOrder bizOrder);

	Result<SupplyOrder> supply(SupplyOrder supplyOrder, BizOrder bizOrder);

	Result<SupplyOrder> asyncSupply(final SupplyOrder supplyOrder, final BizOrder bizOrder);
}
