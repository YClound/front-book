package com.longan.getway.upstream.common.impl;

import java.util.Map;

import javax.annotation.Resource;

import com.longan.biz.dataobject.Stock;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.NotifyManager;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamCardSupplyService;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.common.UpstreamSupplyDispatcherService;

public class UpstreamSupplyDispatcherServiceImpl extends BaseService implements
		UpstreamSupplyDispatcherService {

	@Resource
	private Map<String, UpstreamCardSupplyService> upstreamCardSupplyMap;

	@Resource
	private Map<String, UpstreamDirectSupplyService> upstreamSupplyMap;

	@Resource
	private NotifyManager notifyManager;

	@Override
	public SupplyResult<SupplyOrder> charge(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> chargeResult = new SupplyResult<SupplyOrder>();
		chargeResult.setModule(supplyOrder);

		UpstreamDirectSupplyService upstreamDirectService = null;
		try {
			upstreamDirectService = upstreamSupplyMap.get(String.valueOf(supplyOrder
					.getSupplyTraderId()));
		} catch (Exception e) {
			logger.error("get supplyServier error", e);
			chargeResult.setResultMsg("供货服务获取异常: " + supplyOrder.getSupplyTraderId());
			return chargeResult;
		}

		if (upstreamDirectService == null) {
			chargeResult.setResultMsg("没有该供货服务 : " + supplyOrder.getSupplyTraderId());
			return chargeResult;
		}
		try {
			chargeResult = upstreamDirectService.chargeRequest(supplyOrder);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("chargeRequest error", e);
			chargeResult.setResultMsg("供货失败 ");
			return chargeResult;
		}
		return chargeResult;
	}

	@Override
	public SupplyResult<SupplyOrder> chargeByStock(SupplyOrder supplyOrder, Stock stock) {
		SupplyResult<SupplyOrder> chargeResult = new SupplyResult<SupplyOrder>();
		chargeResult.setModule(supplyOrder);

		UpstreamCardSupplyService upstreamCardSupplyService = null;
		try {
			upstreamCardSupplyService = upstreamCardSupplyMap.get(String.valueOf(supplyOrder
					.getSupplyTraderId()));
		} catch (Exception e) {
			logger.error("get supplyServier error", e);
			chargeResult.setResultMsg("供货服务获取异常: " + supplyOrder.getSupplyTraderId());
			return chargeResult;
		}
		if (upstreamCardSupplyService == null) {
			chargeResult.setResultMsg("没有该供货服务 : " + supplyOrder.getSupplyTraderId());
			return chargeResult;
		}
		try {
			chargeResult = upstreamCardSupplyService.chargeRequest(supplyOrder, stock);
		} catch (Exception e) {
			logger.error("chargeRequest error", e);
			chargeResult.setResultMsg("供货失败 ");
			return chargeResult;
		}
		return chargeResult;
	}
}
