package com.longan.biz.dao;

import java.sql.SQLException;
import java.util.List;

import com.longan.biz.dataobject.GameCarrier;
import com.longan.biz.dataobject.GameCarrierQuery;

public interface GameCarrierDAO {
	List<GameCarrier> queryByPage(GameCarrierQuery gameCarrierQuery) throws SQLException;

	Long insert(GameCarrier record) throws SQLException;

	GameCarrier selectByPrimaryKey(Long id) throws SQLException;

	int updateByPrimaryKey(GameCarrier record) throws SQLException;

	List<GameCarrier> queryAll(GameCarrierQuery gameCarrierQuery) throws SQLException;
}
