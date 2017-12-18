package com.longan.biz.dao;

import java.sql.SQLException;
import java.util.List;

import com.longan.biz.dataobject.Config;

public interface ConfigDAO {
	List<Config> queryAllConfig() throws SQLException;
}
