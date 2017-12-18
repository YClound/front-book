package com.longan.biz.dao;

import java.sql.SQLException;
import java.util.List;

import com.longan.biz.dataobject.Game;
import com.longan.biz.dataobject.GameQuery;

public interface GameDAO {
	List<Game> queryByPage(GameQuery gameQuery) throws SQLException;

	Long insert(Game record) throws SQLException;

	Game selectByPrimaryKey(Long id) throws SQLException;

	int updateByPrimaryKey(Game record) throws SQLException;
	
	List<Game> queryAll(GameQuery gameQuery) throws SQLException;
}
