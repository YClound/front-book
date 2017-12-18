package com.longan.getway.biz.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.core.BizOrderService;
import com.longan.biz.core.SupplyOrderService;
import com.longan.biz.dataobject.TraderInfo;
import com.longan.biz.utils.Constants;
import com.longan.getway.core.BizDealService;

public class BaseService {
	public Logger logger = LoggerFactory.getLogger(this.getClass());

	protected static final ExecutorService es = Executors.newFixedThreadPool(120);

	protected static final ExecutorService notifyExecutor = new ThreadPoolExecutor(50, 50, 0L,
			TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1000));

	@Resource
	protected LocalCachedService localCachedService;

	@Resource
	protected SupplyOrderService supplyOrderService;

	@Resource
	protected BizOrderService bizOrderService;

	@Resource
	protected BizDealService bizDealService;

	public String getDownStreamKey(String userId) {
		if (!StringUtils.isNumeric(userId)) {
			return null;
		}
		TraderInfo traderInfo = localCachedService.getTraderInfo(Long.parseLong(userId));
		if (traderInfo == null
				|| traderInfo.getTraderType() != Constants.TraderInfo.TRADER_TYPE_DOWNSTREAM) {
			return null;
		}
		return traderInfo.getDownstreamKey();
	}

	public String getDownStreamCallBackUrl(String userId) {
		if (!StringUtils.isNumeric(userId)) {
			return null;
		}
		TraderInfo traderInfo = localCachedService.getTraderInfo(Long.parseLong(userId));
		if (traderInfo == null
				|| traderInfo.getTraderType() != Constants.TraderInfo.TRADER_TYPE_DOWNSTREAM) {
			return null;
		}
		return traderInfo.getCallbackUrl();
	}

	public void sleepFewSecond() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
	}

}
