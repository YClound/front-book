package com.longan.biz.cached;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import com.longan.biz.dao.GameCarrierDAO;
import com.longan.biz.dataobject.GameCarrier;
import com.longan.biz.dataobject.GameCarrierQuery;
import com.longan.biz.utils.Constants;

public class LocalGameCarrierCached extends LocalCached<Long, GameCarrier> {
	@Resource
	private GameCarrierDAO gameCarrierDAO;

	@Override
	public void init() {
		super.init(120l, 120l);
	}

	@Override
	void reloadFromDb(ConcurrentMap<Long, GameCarrier> cached) {
		GameCarrierQuery gameCarrierQuery = new GameCarrierQuery();
		gameCarrierQuery.setStatus(Constants.GameCarrier.STATUS_NORMAL);
		try {
			List<GameCarrier> gameCarrierList = (List<GameCarrier>) gameCarrierDAO
					.queryAll(gameCarrierQuery);
			for (GameCarrier gameCarrier : gameCarrierList) {
				cached.put(gameCarrier.getId(), gameCarrier);
			}
		} catch (SQLException e) {
			logger.error("reload gameCarrier error", e);
		}

	}

}
