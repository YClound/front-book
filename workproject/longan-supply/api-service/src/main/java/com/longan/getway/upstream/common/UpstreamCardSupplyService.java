package com.longan.getway.upstream.common;

import java.util.List;

import com.longan.biz.dataobject.Stock;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.client.remote.domain.CardChargeInfo;
import com.longan.client.remote.domain.CardCheck;
import com.longan.getway.core.domain.SupplyResult;

public interface UpstreamCardSupplyService {

	SupplyResult<CardCheck> cardCheck(Stock stock);

	SupplyResult<List<CardChargeInfo>> cardInfoQuery(Stock stock);

	SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder, Stock stock);
}
