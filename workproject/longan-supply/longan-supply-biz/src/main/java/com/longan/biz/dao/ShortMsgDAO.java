package com.longan.biz.dao;

import java.sql.SQLException;

import com.longan.biz.dataobject.ShortMsg;

public interface ShortMsgDAO {
	Long insert(ShortMsg record) throws SQLException;

	int updateByPrimaryKey(ShortMsg record) throws SQLException;
}
