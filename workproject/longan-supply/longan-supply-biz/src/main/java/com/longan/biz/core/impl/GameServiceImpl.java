package com.longan.biz.core.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import com.longan.biz.core.BaseService;
import com.longan.biz.core.GameService;
import com.longan.biz.dao.GameDAO;
import com.longan.biz.dataobject.Game;
import com.longan.biz.dataobject.GameQuery;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;

public class GameServiceImpl extends BaseService implements GameService {
	@Resource
	private GameDAO gameDAO;

	@Override
	public Result<List<Game>> queryGameList(GameQuery gameQuery) {
		Result<List<Game>> result = new Result<List<Game>>();
		try {
			List<Game> queryResult = gameDAO.queryByPage(gameQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("游戏列表查询失败    msg: " + e.getMessage());
			logger.error("queryGameList error ", e);
		}
		return result;
	}

	@Override
	public Result<Boolean> createGame(Game game) {
		Result<Boolean> result = new Result<Boolean>();
		try {
			GameQuery gameQuery = new GameQuery();
			gameQuery.setGameName(game.getGameName());
			List<Game> list = gameDAO.queryByPage(gameQuery);
			if (list.size() > 0) {
				result.setResultMsg("游戏名已存在");
				return result;
			}
			game.setStatus(Constants.Game.STATUS_NORMAL);
			Long id = gameDAO.insert(game);
			if (id == null) {
				result.setResultMsg("新增游戏失败");
				return result;
			}
			result.setSuccess(true);
			result.setModule(true);
		} catch (Exception e) {
			result.setResultMsg("新增游戏失败 ");
			logger.error("createNewGame error gameName : " + game.getGameName(), e);
			return result;
		}
		return result;
	}

	@Override
	public Result<Game> getGame(Long id) {
		Result<Game> result = new Result<Game>();
		try {
			Game game = gameDAO.selectByPrimaryKey(id);
			if (game != null) {
				result.setSuccess(true);
				result.setModule(game);
			} else {
				result.setResultMsg("不存在该游戏 ");
			}
		} catch (SQLException e) {
			result.setResultMsg("游戏查询异常  msg : " + e.getMessage());
			logger.error("getGame error  id = " + id, e);
		}
		return result;
	}

	@Override
	public Result<Boolean> updateGame(Game game) {
		Result<Boolean> result = new Result<Boolean>();
		try {
			int row = gameDAO.updateByPrimaryKey(game);
			result.setSuccess(row > 0);
			result.setModule(row > 0);
		} catch (SQLException e) {
			result.setResultMsg("更新游戏失败    msg: " + e.getMessage());
			logger.error("updateGame error ", e);
		}

		return result;
	}

	@Override
	public Result<List<Game>> queryAllGame(GameQuery gameQuery) {
		Result<List<Game>> result = new Result<List<Game>>();
		try {
			List<Game> queryResult = gameDAO.queryAll(gameQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("游戏列表查询失败    msg: " + e.getMessage());
			logger.error("queryGameList error ", e);
		}
		return result;
	}

}
