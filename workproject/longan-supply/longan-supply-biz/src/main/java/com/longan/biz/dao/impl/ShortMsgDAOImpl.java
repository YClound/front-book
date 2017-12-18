package com.longan.biz.dao.impl;

import java.sql.SQLException;

import javax.annotation.Resource;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.longan.biz.dao.ShortMsgDAO;
import com.longan.biz.dataobject.ShortMsg;

public class ShortMsgDAOImpl implements ShortMsgDAO {

	@Resource
	private SqlMapClient sqlMapClient;

	@Override
	public Long insert(ShortMsg record) throws SQLException {
		return (Long) sqlMapClient.insert("short_msg.insert", record);
	}

	@Override
	public int updateByPrimaryKey(ShortMsg record) throws SQLException {
		int rows = sqlMapClient.update("short_msg.updateByPrimaryKey", record);
		return rows;
	}

}
