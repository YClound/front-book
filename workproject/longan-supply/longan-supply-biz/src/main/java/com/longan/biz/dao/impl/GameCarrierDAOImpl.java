package com.longan.biz.dao.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.longan.biz.dao.GameCarrierDAO;
import com.longan.biz.dataobject.GameCarrier;
import com.longan.biz.dataobject.GameCarrierQuery;

public class GameCarrierDAOImpl implements GameCarrierDAO {
	@Resource
	private SqlMapClient sqlMapClient;

	@SuppressWarnings("unchecked")
	@Override
	public List<GameCarrier> queryByPage(GameCarrierQuery gameCarrierQuery) throws SQLException {
		int count = (Integer) sqlMapClient.queryForObject("game_carrier.queryByPageCount",
				gameCarrierQuery);
		gameCarrierQuery.setTotalItem(count);
		return (List<GameCarrier>) sqlMapClient.queryForList("game_carrier.queryByPage",
				gameCarrierQuery);
	}

	@Override
	public Long insert(GameCarrier record) throws SQLException {
		return (Long) sqlMapClient.insert("game_carrier.insert", record);
	}

	@Override
	public GameCarrier selectByPrimaryKey(Long id) throws SQLException {
		GameCarrier key = new GameCarrier();
		key.setId(id);
		GameCarrier record = (GameCarrier) sqlMapClient.queryForObject(
				"game_carrier.selectByPrimaryKey", key);
		return record;
	}

	@Override
	public int updateByPrimaryKey(GameCarrier record) throws SQLException {
		int rows = sqlMapClient.update("game_carrier.updateByPrimaryKey", record);
		return rows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GameCarrier> queryAll(GameCarrierQuery gameCarrierQuery) throws SQLException {
		return (List<GameCarrier>) sqlMapClient.queryForList("game_carrier.queryAll",
				gameCarrierQuery);
	}

}
