package com.longan.mng.utils;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.MapMaker;

public class CheckCodeContext {
	public final static ConcurrentMap<String, String> map = new MapMaker()
			.expiration(60, TimeUnit.SECONDS).concurrencyLevel(16).initialCapacity(100).makeMap();

}
