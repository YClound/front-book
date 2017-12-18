package com.longan.getway.core.impl;

import javax.annotation.Resource;

import com.longan.biz.core.StockService;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.Stock;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.SupplyService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamSupplyDispatcherService;

public class CardForwardChargeSupplyServiceImpl extends BaseService implements SupplyService {

	@Resource
	private StockService stockService;

	@Resource
	private UpstreamSupplyDispatcherService upstreamSupplyDispatcherService;

	@Override
	public Result<SupplyOrder> supply(SupplyOrder supplyOrder, BizOrder bizOrder) {
		Result<SupplyOrder> result = new Result<SupplyOrder>();
		result.setModule(supplyOrder);

		if (bizOrder == null || supplyOrder == null) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD);
			result.setResultMsg("供货参数不对");
			return result;
		}

		// 锁定库存
		Result<Stock> stockResult = stockService.lockStock(supplyOrder);
		if (!stockResult.isSuccess()) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD);
			result.setResultMsg(stockResult.getResultMsg());
			return result;
		}

		Stock stock = stockResult.getModule();

		// 锁定库存单号跟新至供货单
		supplyOrder.setStockId(stock.getId());
		Result<Boolean> updateSupplyOrder = supplyOrderService.updateSupplyOrder(supplyOrder);

		if (!updateSupplyOrder.isSuccess()) {
			logger.warn("supply bizOrderId : " + bizOrder.getId() + " supllyOrderId : "
					+ supplyOrder.getId() + " updateSupplyOrder : failed msg : "
					+ updateSupplyOrder.getResultMsg());
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SYSTEM);
			result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SYSTEM);
			return result;
		}

		// 对外供货
		SupplyResult<SupplyOrder> chargeResult = upstreamSupplyDispatcherService.chargeByStock(
				supplyOrder, stock);

		return bizDealService.dealSyncSupplyResult(chargeResult, supplyOrder, bizOrder);
	}

	@Override
	public Result<SupplyOrder> asyncSupply(final SupplyOrder supplyOrder, final BizOrder bizOrder) {
		Result<SupplyOrder> result = new Result<SupplyOrder>();
		result.setModule(supplyOrder);

		if (bizOrder == null || supplyOrder == null) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD);
			result.setResultMsg("供货参数不对");
			return result;
		}

		// 锁定库存
		Result<Stock> stockResult = stockService.lockStock(supplyOrder);
		if (!stockResult.isSuccess()) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD);
			result.setResultMsg(stockResult.getResultMsg());
			return result;
		}

		final Stock stock = stockResult.getModule();

		// 锁定库存单号跟新至供货单
		supplyOrder.setStockId(stock.getId());
		Result<Boolean> updateSupplyOrder = supplyOrderService.updateSupplyOrder(supplyOrder);

		if (!updateSupplyOrder.isSuccess()) {
			logger.warn("supply bizOrderId : " + bizOrder.getId() + " supllyOrderId : "
					+ supplyOrder.getId() + " updateSupplyOrder : failed msg : "
					+ updateSupplyOrder.getResultMsg());
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SYSTEM);
			result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SYSTEM);
			return result;
		}

		es.execute(new Runnable() {
			@Override
			public void run() {
				// 对外供货
				SupplyResult<SupplyOrder> chargeResult = upstreamSupplyDispatcherService
						.chargeByStock(supplyOrder, stock);

				bizDealService.dealAsyncSupplyResult(chargeResult, supplyOrder, bizOrder);
			}

		});

		result.setSuccess(true);
		return result;
	}

}