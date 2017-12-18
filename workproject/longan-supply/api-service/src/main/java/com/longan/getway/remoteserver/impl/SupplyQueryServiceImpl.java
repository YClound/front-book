package com.longan.getway.remoteserver.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.longan.biz.core.StockService;
import com.longan.biz.dataobject.Stock;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.CardChargeInfo;
import com.longan.client.remote.domain.CardCheck;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.client.remote.service.SupplyQueryService;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamCardSupplyService;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;

public class SupplyQueryServiceImpl extends BaseService implements SupplyQueryService {
	@Resource
	private StockService stockService;

	@Resource
	private Map<String, UpstreamCardSupplyService> upstreamCardSupplyMap;

	@Resource
	private Map<String, UpstreamDirectSupplyService> upstreamSupplyMap;

	@Override
	public Result<CardCheck> cardCheck(Long stockId) {
		Result<CardCheck> result = new Result<CardCheck>();
		if (stockId == null) {
			result.setResultMsg("参数错误");
			return result;
		}

		Result<Stock> stockResult = stockService.getStockById(stockId);
		if (!stockResult.isSuccess()) {
			result.setResultMsg(stockResult.getResultMsg());
			return result;
		}

		Stock stock = stockResult.getModule();
		if (stock == null) {
			result.setResultMsg("没有该库存");
			return result;
		}

		UpstreamCardSupplyService upstreamCardSupplyService = null;
		try {
			upstreamCardSupplyService = upstreamCardSupplyMap.get(String.valueOf(stock
					.getSupplyTraderId()));
		} catch (Exception e) {
			logger.error("get supplyServier error", e);
			result.setResultMsg("供货查询服务获取异常");
			return result;
		}
		if (upstreamCardSupplyService == null) {
			logger.error("get supplyServier error");
			result.setResultMsg("没有该供货查询服务");
			return result;
		}

		SupplyResult<CardCheck> checkCardResult = upstreamCardSupplyService.cardCheck(stock);

		if (checkCardResult.isFailed() || checkCardResult.isUnConfirm()) {
			result.setResultMsg(checkCardResult.getResultMsg());
			return result;
		}

		CardCheck cardCheck = checkCardResult.getModule();
		result.setSuccess(true);
		result.setModule(cardCheck);
		return result;
	}

	@Override
	public Result<List<CardChargeInfo>> cardChargeInfoQuery(Long stockId) {
		Result<List<CardChargeInfo>> result = new Result<List<CardChargeInfo>>();
		if (stockId == null) {
			result.setResultMsg("参数错误");
			return result;
		}

		Result<Stock> stockResult = stockService.getStockById(stockId);
		if (!stockResult.isSuccess()) {
			result.setResultMsg(stockResult.getResultMsg());
			return result;
		}

		Stock stock = stockResult.getModule();
		if (stock == null) {
			result.setResultMsg("没有该库存");
			return result;
		}

		UpstreamCardSupplyService upstreamCardSupplyService = null;
		try {
			upstreamCardSupplyService = upstreamCardSupplyMap.get(String.valueOf(stock
					.getSupplyTraderId()));
		} catch (Exception e) {
			logger.error("get supplyServier error", e);
			result.setResultMsg("供货查询服务获取异常");
			return result;
		}
		if (upstreamCardSupplyService == null) {
			logger.error("get supplyServier error");
			result.setResultMsg("没有该供货查询服务");
			return result;
		}

		SupplyResult<List<CardChargeInfo>> cardInfoQueryResult = upstreamCardSupplyService
				.cardInfoQuery(stock);

		if (cardInfoQueryResult.isFailed() || cardInfoQueryResult.isUnConfirm()) {
			result.setResultMsg(cardInfoQueryResult.getResultMsg());
			return result;
		}

		result.setSuccess(true);
		result.setModule(cardInfoQueryResult.getModule());
		return result;
	}

	@Override
	public Result<ChargeInfo> chargeInfoQuery(SupplyOrder supplyOrder) {
		Result<ChargeInfo> result = new Result<ChargeInfo>();

		if (supplyOrder == null || supplyOrder.getSupplyTraderId() == null) {
			result.setResultMsg("参数错误");
			return result;
		}

		UpstreamDirectSupplyService upstreamDirectService = null;
		try {
			upstreamDirectService = upstreamSupplyMap.get(String.valueOf(supplyOrder
					.getSupplyTraderId()));
		} catch (Exception e) {
			logger.error("get supplyServier error", e);
			result.setResultMsg("供货查询服务获取异常");
			return result;
		}

		if (upstreamDirectService == null) {
			result.setResultMsg("没有该供货查询服务");
			return result;
		}

		SupplyResult<ChargeInfo> chargeQueryResult = upstreamDirectService.chargeQuery(supplyOrder);
		if (chargeQueryResult.isFailed() || chargeQueryResult.isUnConfirm()) {
			result.setResultMsg(chargeQueryResult.getResultMsg());
			return result;
		}

		result.setSuccess(true);
		result.setModule(chargeQueryResult.getModule());
		return result;
	}

	@Override
	public Result<BalanceQueryInfo> balanceQuery(String userId) {
		Result<BalanceQueryInfo> result = new Result<BalanceQueryInfo>();
		UpstreamDirectSupplyService upstreamDirectService = null;
		try {
			upstreamDirectService = upstreamSupplyMap.get(userId);
		} catch (Exception e) {
			logger.error("get supplyServier error", e);
			result.setResultMsg("供货查询服务获取异常");
			return result;
		}
		if (upstreamDirectService == null) {
			result.setResultMsg("没有该供货查询服务");
			return result;
		}
		SupplyResult<BalanceQueryInfo> balanceQueryResult = upstreamDirectService.balanceQuery();
		if (balanceQueryResult.isFailed() || balanceQueryResult.isUnConfirm()) {
			result.setResultMsg(balanceQueryResult.getResultMsg());
			return result;
		}
		result.setSuccess(true);
		result.setModule(balanceQueryResult.getModule());
		return result;
	}

}
