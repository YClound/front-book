package com.longan.biz.cached;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import com.longan.biz.core.ConfigService;
import com.longan.biz.dataobject.Config;

public class LocalConfigCached extends LocalCached<String, Config> {
	@Resource
	private ConfigService configService;

	@Override
	public void init() {
		super.init(10L, 10L);
	}

	@Override
	void reloadFromDb(ConcurrentMap<String, Config> cached) {
		List<Config> configList = configService.queryAllConfig();
		if (configList != null) {
			for (Config config : configList) {
				cached.put(config.getConfigKey(), config);
			}
		}
	}

}
