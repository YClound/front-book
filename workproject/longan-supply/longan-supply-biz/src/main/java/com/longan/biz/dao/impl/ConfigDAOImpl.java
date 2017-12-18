package com.longan.biz.dao.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.longan.biz.dao.ConfigDAO;
import com.longan.biz.dataobject.Config;

public class ConfigDAOImpl implements ConfigDAO {
	@Resource
	private SqlMapClient sqlMapClient;

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Config> queryAllConfig() throws SQLException {
		List list = sqlMapClient.queryForList("config.queryAllConfig");
		return list;
	}

}
