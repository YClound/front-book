package com.longan.getway.core;

import javax.annotation.Resource;

import com.longan.biz.core.ChargingLimitService;

public class RequestLimitInitService {
	@Resource
	private ChargingLimitService chargingLimitService;

	void init() {
		// 只在GETWAY 启动的时候 init
		chargingLimitService.initCharginCount();
	}
}
