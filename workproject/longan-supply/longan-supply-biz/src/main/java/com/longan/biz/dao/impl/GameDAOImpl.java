package com.longan.biz.dao.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.longan.biz.dao.GameDAO;
import com.longan.biz.dataobject.Game;
import com.longan.biz.dataobject.GameQuery;

public class GameDAOImpl implements GameDAO {
	@Resource
	private SqlMapClient sqlMapClient;

	@SuppressWarnings("unchecked")
	@Override
	public List<Game> queryByPage(GameQuery gameQuery) throws SQLException {
		int count = (Integer) sqlMapClient.queryForObject("game.queryByPageCount", gameQuery);
		gameQuery.setTotalItem(count);
		return (List<Game>) sqlMapClient.queryForList("game.queryByPage", gameQuery);
	}

	@Override
	public Long insert(Game record) throws SQLException {
		return (Long) sqlMapClient.insert("game.insert", record);
	}

	@Override
	public Game selectByPrimaryKey(Long id) throws SQLException {
		Game key = new Game();
		key.setId(id);
		Game record = (Game) sqlMapClient.queryForObject("game.selectByPrimaryKey", key);
		return record;
	}

	@Override
	public int updateByPrimaryKey(Game record) throws SQLException {
		int rows = sqlMapClient.update("game.updateByPrimaryKey", record);
		return rows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Game> queryAll(GameQuery gameQuery) throws SQLException {
		return (List<Game>) sqlMapClient.queryForList("game.queryAll", gameQuery);
	}

}
