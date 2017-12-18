package com.longan.biz.core;

import java.util.List;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.dataobject.SupplyOrderQuery;
import com.longan.biz.domain.Result;

public interface SupplyOrderService {

	public Result<List<SupplyOrder>> querySupplyOrderPage(SupplyOrderQuery supplyOrderQuery);

	Result<SupplyOrder> createSupplyOrder(SupplyOrder supplyOrder);

	Result<Boolean> updateSupplyOrder(SupplyOrder supplyOrder);

	Result<SupplyOrder> getSupplyOrder(Long supplyOrderId);

	/**
	 * 确认供货 如果有处理中额度限制 ，计数减少
	 * 
	 * @param bizOrder
	 * @return
	 */
	public Result<Boolean> comfirmSupplyOrder(SupplyOrder supplyOrder);

	/**
	 * 取消订单， 有库存则返还库存， 并且退款给下游 ，如果有处理中额度限制 ，计数减少
	 * 
	 * @param bizOrder
	 * @return
	 */
	public Result<Boolean> cancelSupplyOrder(SupplyOrder supplyOrder);

	/**
	 * 设置供货未确认
	 * 
	 * @param bizOrder
	 * @return
	 */
	public Result<Boolean> unComfirmSupplyOrder(SupplyOrder supplyOrder);

	/**
	 * 设置供货处理中， 如果有处理中额度限制 则 计数增加
	 * 
	 * @param bizOrder
	 * @return
	 */
	public Result<Boolean> chargingSupplyOrderAndCounting(SupplyOrder supplyOrder);

	public Result<SupplyOrder> getSupplyOrderById(Long supplyOrderId);

	public Result<Integer> getCountInChargingByTraderId(Long traderId);

	public Result<SupplyOrder> getSupplyOrderByIdAndTraderId(Long id, Long traderId);

	public Result<SupplyOrder> getSupplyOrderByUpstreamSerialno(String UpstreamSerialno,
			Long traderId);

	public Result<List<SupplyOrder>> querySupplyOrderByBizOrder(Long bizOrderId);

	public Result<Boolean> updateSupplyOrderCheckStatus(SupplyOrder supplyOrder,
			List<Integer> statusList);

	public Result<List<SupplyOrder>> querySupplyOrderExport(SupplyOrderQuery supplyOrderQuery);
	
	
	
	public Result<Boolean> manualLockSupplyOrder(SupplyOrder supplyOrder);

	public Result<Boolean> manualUnLockSupplyOrder(SupplyOrder supplyOrder);

	public Result<Boolean> manualConfirmSupplyOrder(SupplyOrder supplyOrder);

	public Result<Boolean> manualCancelSupplyOrder(SupplyOrder supplyOrder);

	public Result<Boolean> manualUnConfirmSupplyOrder(SupplyOrder supplyOrder);

}
