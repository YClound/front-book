package com.longan.biz.core;

import com.longan.biz.domain.Result;

public interface ChargingLimitService {

	Result<Boolean> hasReachLimit(Long supplyTraderId);

	Result<Boolean> incCount(Long supplyTraderId);

	Result<Boolean> decCount(Long supplyTraderId);

	Result<Long> getCount(Long supplyTraderId);

	Result<Boolean> initCharginCount();

}
