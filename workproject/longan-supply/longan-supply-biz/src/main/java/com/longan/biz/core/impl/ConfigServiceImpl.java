package com.longan.biz.core.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import com.longan.biz.core.BaseService;
import com.longan.biz.core.ConfigService;
import com.longan.biz.dao.ConfigDAO;
import com.longan.biz.dataobject.Config;

public class ConfigServiceImpl extends BaseService implements ConfigService {
	@Resource
	private ConfigDAO configDAO;

	@Override
	public List<Config> queryAllConfig() {
		try {
			return configDAO.queryAllConfig();
		} catch (SQLException e) {
			logger.error("queryAllConfig error", e);
		}
		return null;
	}
}
