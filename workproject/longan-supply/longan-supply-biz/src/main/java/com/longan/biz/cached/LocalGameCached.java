package com.longan.biz.cached;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import com.longan.biz.dao.GameDAO;
import com.longan.biz.dataobject.Game;
import com.longan.biz.dataobject.GameQuery;
import com.longan.biz.utils.Constants;

public class LocalGameCached extends LocalCached<Long, Game> {
	@Resource
	private GameDAO gameDAO;

	@Override
	public void init() {
		super.init(120l, 120l);

	}

	@Override
	void reloadFromDb(ConcurrentMap<Long, Game> cached) {
		GameQuery gameQuery = new GameQuery();
		gameQuery.setStatus(Constants.GameCarrier.STATUS_NORMAL);
		try {
			List<Game> gameList = (List<Game>) gameDAO.queryAll(gameQuery);
			for (Game game : gameList) {
				cached.put(game.getId(), game);
			}
		} catch (SQLException e) {
			logger.error("reload game error", e);
		}
	}

}
