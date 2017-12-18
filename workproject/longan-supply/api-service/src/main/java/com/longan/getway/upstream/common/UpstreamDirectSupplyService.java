package com.longan.getway.upstream.common;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.core.domain.SupplyResult;

public interface UpstreamDirectSupplyService {

	SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder);

	SupplyResult<ChargeInfo> chargeQuery(SupplyOrder supplyOrder);

	SupplyResult<BalanceQueryInfo> balanceQuery();
}
