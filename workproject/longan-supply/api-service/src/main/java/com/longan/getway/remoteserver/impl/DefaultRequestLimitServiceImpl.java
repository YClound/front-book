package com.longan.getway.remoteserver.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;

import com.alibaba.druid.util.StringUtils;
import com.longan.biz.core.BizOrderService;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.Utils;
import com.longan.client.remote.service.RequestLimitService;
import com.longan.getway.biz.common.BaseService;

public class DefaultRequestLimitServiceImpl extends BaseService implements RequestLimitService {
	@Resource
	private BizOrderService bizOrderService;

	public static final ConcurrentMap<Integer, AtomicInteger> map = new ConcurrentHashMap<Integer, AtomicInteger>();

	@Override
	public Result<Boolean> isMaxLimit(Integer bizId) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(true);

		Result<Integer> checkResult = checkProperties(bizId);
		if (!checkResult.isSuccess()) {
			result.setResultMsg(checkResult.getResultMsg());
			return result;
		}
		Integer maxLimitCount = checkResult.getModule();

		if (maxLimitCount == 0) {
			// 没设置限制
			result.setSuccess(true);
			result.setModule(true);
			return result;
		}

		Result<AtomicInteger> countResult = getCount(bizId);
		if (!countResult.isSuccess()) {
			result.setResultMsg(countResult.getResultMsg());
			return result;
		}

		AtomicInteger count = countResult.getModule();
		logger.warn("bizId ：  " + bizId + " charging count : " + count.get());

		if (count.get() + 1 > maxLimitCount) {
			result.setResultMsg(Constants.ErrorCode.DESC_ERROR_BUSI);
			return result;
		}
		result.setSuccess(true);
		result.setModule(true);

		return result;
	}

	@Override
	public Result<Boolean> putInQueue(Integer bizId) {

		Result<Boolean> result = new Result<Boolean>();
		result.setModule(true);

		if (bizId == null) {
			result.setResultMsg("bizId is null");
			return result;
		}

		Result<AtomicInteger> countResult = getCount(bizId);
		if (!countResult.isSuccess()) {
			result.setResultMsg(countResult.getResultMsg());
			return result;
		}

		AtomicInteger count = countResult.getModule();

		count.incrementAndGet();
		result.setSuccess(true);
		result.setModule(true);
		return result;
	}

	@Override
	public Result<Boolean> outQueue(Integer bizId) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(true);

		if (bizId == null) {
			result.setResultMsg("bizId is null");
			return result;
		}

		Result<AtomicInteger> countResult = getCount(bizId);
		if (!countResult.isSuccess()) {
			result.setResultMsg(countResult.getResultMsg());
			return result;
		}
		AtomicInteger count = countResult.getModule();
		count.decrementAndGet();
		result.setSuccess(true);
		result.setModule(true);
		return result;
	}

	@Override
	public Result<Integer> getQueueCount(Integer bizId) {
		Result<Integer> result = new Result<Integer>();

		if (bizId == null) {
			result.setResultMsg("bizId is null");
			return result;
		}
		Result<AtomicInteger> countResult = getCount(bizId);
		if (!countResult.isSuccess()) {
			result.setResultMsg(countResult.getResultMsg());
			return result;
		}
		AtomicInteger count = countResult.getModule();

		result.setSuccess(true);
		result.setModule(count.get());
		return result;
	}

	private Result<Integer> checkProperties(Integer bizId) {
		Result<Integer> result = new Result<Integer>();

		if (bizId == null) {
			result.setResultMsg("bizId is null");
			return result;
		}

		String maxLimit = Utils.getProperty("maxLimit_" + bizId);
		if (StringUtils.isEmpty(maxLimit)) {
			result.setSuccess(true);
			result.setModule(0);
			return result;
		}
		if (!NumberUtils.isNumber(maxLimit)) {
			result.setSuccess(true);
			result.setModule(0);
			return result;
		}

		Integer maxLimitCount = Integer.parseInt(maxLimit);
		result.setSuccess(true);
		result.setModule(maxLimitCount);
		return result;
	}

	private Result<AtomicInteger> getCount(Integer bizId) {
		Result<AtomicInteger> result = new Result<AtomicInteger>();
		AtomicInteger count = map.get(bizId);

		if (count == null) {
			count = new AtomicInteger(0);
			synchronized (this) {
				// double check
				if (map.get(bizId) == null) {
					Result<Integer> dbResult = bizOrderService.getCountInCharging(bizId);
					if (dbResult.isSuccess()) {
						count.set(dbResult.getModule());
						map.put(bizId, count);
					} else {
						result.setResultMsg("getCountInCharging error msg : "
								+ dbResult.getResultMsg());
						return result;
					}
				} else {
					count = map.get(bizId);
				}

			}
		}

		result.setSuccess(true);
		result.setModule(count);
		return result;
	}

}
