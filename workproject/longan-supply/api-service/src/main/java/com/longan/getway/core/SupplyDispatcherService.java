package com.longan.getway.core;

import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;

public interface SupplyDispatcherService {

	Result<SupplyOrder> supply(BizOrder bizOrder);

}
