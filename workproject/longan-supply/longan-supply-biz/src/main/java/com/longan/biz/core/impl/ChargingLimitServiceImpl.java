package com.longan.biz.core.impl;

import java.util.List;

import javax.annotation.Resource;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.cached.MemcachedService;
import com.longan.biz.core.BaseService;
import com.longan.biz.core.BizOrderService;
import com.longan.biz.core.ChargingLimitService;
import com.longan.biz.core.SupplyOrderService;
import com.longan.biz.dataobject.TraderInfo;
import com.longan.biz.domain.Result;

public class ChargingLimitServiceImpl extends BaseService implements ChargingLimitService {
	@Resource
	private MemcachedService memcachedService;

	@Resource
	private LocalCachedService localCachedService;

	@Resource
	private BizOrderService bizOrderService;

	@Resource
	private SupplyOrderService supplyOrderService;

	private static final String CHARGING_LIMIT_KEY = "CHARGING_LIMIT_";

	private static final Integer EXP = 0;

	@Override
	public Result<Boolean> hasReachLimit(Long supplyTraderId) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);

		TraderInfo traderInfo = localCachedService.getTraderInfo(supplyTraderId);
		if (traderInfo == null || traderInfo.getChargingLimit() == null) {
			// 为了让业务走下去，traderInfo == null 也返回true
			result.setModule(true);
			result.setSuccess(true);
			return result;
		}
		Integer maxLimit = traderInfo.getChargingLimit();

		Long count = memcachedService.getLongValue(getCharingLimitKey(supplyTraderId));
		if (count == null) {
			// 查询失败 或者 没有值也返回true
			result.setModule(true);
			result.setSuccess(true);
			return result;
		}

		if (count >= maxLimit) {
			logger.warn("supply Trader id: " + supplyTraderId + " has reached limit . count : "
					+ count);
			result.setResultMsg("hasReachLimit");
			return result;
		} else {
			logger.warn("supply Trader id: " + supplyTraderId + " check charging count : " + count);
		}

		// 后面可以加上 当日数量 等逻辑

		result.setModule(true);
		result.setSuccess(true);
		return result;
	}

	@Override
	public Result<Boolean> incCount(Long supplyTraderId) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);

		TraderInfo traderInfo = localCachedService.getTraderInfo(supplyTraderId);
		if (traderInfo == null || traderInfo.getChargingLimit() == null) {
			// 为了让业务走下去，traderInfo == null 也返回true
			result.setModule(true);
			result.setSuccess(true);
			return result;
		}
		Long count = memcachedService.inc(getCharingLimitKey(supplyTraderId), EXP, 1);
		// System.out.println(memcachedService.get(getCharingLimitKey(supplyTraderId)));

		if (count == null) {
			logger.error("supply Trader id: " + supplyTraderId + " inc count error");
			result.setResultMsg("supply Trader id: " + supplyTraderId + " inc count error");
			return result;
		}

		logger.warn("supply Trader id: " + supplyTraderId + " charging  inc count : " + count);

		// 后面可以加上 增加当日数量 等逻辑
		result.setModule(true);
		result.setSuccess(true);
		return result;
	}

	@Override
	public Result<Boolean> decCount(Long supplyTraderId) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);

		TraderInfo traderInfo = localCachedService.getTraderInfo(supplyTraderId);
		if (traderInfo == null || traderInfo.getChargingLimit() == null) {
			// 为了让业务走下去，traderInfo == null 也返回true
			result.setModule(true);
			result.setSuccess(true);
			return result;
		}
		Long count = memcachedService.dec(getCharingLimitKey(supplyTraderId), 1);

		if (count == null) {
			logger.error("supply Trader id: " + supplyTraderId + " dec count error");
			result.setResultMsg("supply Trader id: " + supplyTraderId + " dec count error");
			return result;
		}

		logger.warn("supply Trader id: " + supplyTraderId + " charging  dec count : " + count);

		// 后面可以加上 减少当日数量 等逻辑
		result.setModule(true);
		result.setSuccess(true);
		return result;
	}

	@Override
	public Result<Long> getCount(Long supplyTraderId) {
		Result<Long> result = new Result<Long>();

		Long count = memcachedService.getLongValue(getCharingLimitKey(supplyTraderId));
		if (count == null) {
			logger.error("supply Trader id: " + supplyTraderId + "  count error");
			result.setResultMsg("supply Trader id: " + supplyTraderId + "  count error");
			return result;
		}
		result.setSuccess(true);
		result.setModule(count);
		return result;
	}

	private String getCharingLimitKey(Long supplyTraderId) {
		return CHARGING_LIMIT_KEY + supplyTraderId;
	}

	@Override
	public Result<Boolean> initCharginCount() {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		List<TraderInfo> list = localCachedService.getTraderInfoList();
		if (list != null) {
			for (TraderInfo traderInfo : list) {
				if (traderInfo.getChargingLimit() != null && traderInfo.getChargingLimit() != 0) {
					if (traderInfo.getUserId() != null) {
						Result<Integer> queryResult = supplyOrderService
								.getCountInChargingByTraderId(traderInfo.getUserId());
						if (queryResult.isSuccess() && queryResult.getModule() != null) {
							logger.warn("memcached set  getCharingLimitKey : "
									+ getCharingLimitKey(traderInfo.getUserId())
									+ " charging count : " + queryResult.getModule());
							memcachedService.initCount(getCharingLimitKey(traderInfo.getUserId()),
									EXP, queryResult.getModule().longValue());
						}
					}
				}
			}
			result.setSuccess(true);
			result.setModule(true);
		}
		return result;
	}
}
