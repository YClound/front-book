package com.longan.client.remote.service;

import java.util.List;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.CardChargeInfo;
import com.longan.client.remote.domain.CardCheck;
import com.longan.client.remote.domain.ChargeInfo;

public interface SupplyQueryService {
	Result<CardCheck> cardCheck(Long stockId);

	Result<List<CardChargeInfo>> cardChargeInfoQuery(Long stockId);

	Result<ChargeInfo> chargeInfoQuery(SupplyOrder supplyOrder);

	Result<BalanceQueryInfo> balanceQuery(String userId);
}
