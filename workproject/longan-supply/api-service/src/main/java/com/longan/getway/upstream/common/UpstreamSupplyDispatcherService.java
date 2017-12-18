package com.longan.getway.upstream.common;

import com.longan.biz.dataobject.Stock;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.getway.core.domain.SupplyResult;

public interface UpstreamSupplyDispatcherService {

	/**
	 * 充值
	 * 
	 * @param bizOrder
	 * @return
	 */
	SupplyResult<SupplyOrder> charge(SupplyOrder supplyOrder);

	/**
	 * 卡密转充值
	 * 
	 * @param bizOrder
	 * @param stock
	 * @return
	 */
	SupplyResult<SupplyOrder> chargeByStock(SupplyOrder supplyOrder, Stock stock);

}
