package com.longan.getway.core.impl;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.MapMaker;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.NotifyManager;
import com.longan.getway.upstream.common.domain.CallBack;

public class DefaultNotifyManagerImpl extends BaseService implements NotifyManager {
	public static final ConcurrentMap<Long, Object> NOTIFY_MAP = new MapMaker()
			.expiration(60 * 30, TimeUnit.SECONDS).concurrencyLevel(16).initialCapacity(100)
			.makeMap();

	public static final ConcurrentMap<Long, CallBack> RESULT_MAP = new MapMaker()
			.expiration(60 * 30, TimeUnit.SECONDS).concurrencyLevel(16).initialCapacity(100)
			.makeMap();

	private static long SYNC_TIME = 60 * 1000;

	@Override
	public Object addLock(Long bizOrderId) {
		return NOTIFY_MAP.put(bizOrderId, new Object());
	}

	@Override
	public Object getLock(Long bizOrderId) {
		return NOTIFY_MAP.get(bizOrderId);
	}

	@Override
	public CallBack waitForResult(Long supplyOrderId) {
		Object lock = getLock(supplyOrderId);
		if (lock == null) {
			return null;
		}
		synchronized (lock) {
			try {
				lock.wait(SYNC_TIME);
			} catch (InterruptedException e) {
				logger.error("wait interrupted", e);
			}
		}
		return RESULT_MAP.remove(supplyOrderId);

	}


	@Override
	public void notify(CallBack upstreamCallBack) {
		Long supplyOrderId = upstreamCallBack.getSupplyOrderId();
		if (supplyOrderId == null) {
			return;
		}
		RESULT_MAP.put(supplyOrderId, upstreamCallBack);

		Object lock = NOTIFY_MAP.remove(supplyOrderId);
		if (lock == null) {
			return;
		}
		synchronized (lock) {
			logger.warn("notify wait Thread supplyOrderId :  " + supplyOrderId);
			lock.notifyAll();
		}
	}

}
