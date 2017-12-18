package com.longan.biz.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeLogUtils {
	private long time = System.currentTimeMillis();
	private String name;
	public Logger logger = LoggerFactory.getLogger(TimeLogUtils.class);

	public TimeLogUtils(String name) {
		this.name = name;
	}

	public long endTime() {
		long now = System.currentTimeMillis();
		logger.warn(name + " cost:" + (System.currentTimeMillis() - time) + "ms");
		return now;
	}

	public long endTime(long startTime) {
		long now = System.currentTimeMillis();
		logger.warn(name + " cost:" + (System.currentTimeMillis() - startTime) + "ms");
		return now;
	}
}
