package com.longan.biz.core.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import com.longan.biz.core.BaseService;
import com.longan.biz.core.GameCarrierService;
import com.longan.biz.dao.GameCarrierDAO;
import com.longan.biz.dataobject.GameCarrier;
import com.longan.biz.dataobject.GameCarrierQuery;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;

public class GameCarrierServiceImpl extends BaseService implements GameCarrierService {
	@Resource
	private GameCarrierDAO gameCarrierDAO;

	@Override
	public Result<List<GameCarrier>> queryGameCarrierList(GameCarrierQuery gameCarrierQuery) {
		Result<List<GameCarrier>> result = new Result<List<GameCarrier>>();
		try {
			List<GameCarrier> queryResult = gameCarrierDAO.queryByPage(gameCarrierQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("厂商列表查询失败    msg: " + e.getMessage());
			logger.error("queryGameCarrierList error ", e);
		}

		return result;
	}

	@Override
	public Result<Boolean> createGameCarrier(GameCarrier gameCarrier) {
		Result<Boolean> result = new Result<Boolean>();
		try {
			GameCarrierQuery gameCarrierQuery = new GameCarrierQuery();
			gameCarrierQuery.setCarrierName(gameCarrier.getCarrierName());
			List<GameCarrier> list = gameCarrierDAO.queryByPage(gameCarrierQuery);
			if (list.size() > 0) {
				result.setResultMsg("厂商名已存在");
				return result;
			}
			gameCarrier.setStatus(Constants.GameCarrier.STATUS_NORMAL);
			Long id = gameCarrierDAO.insert(gameCarrier);
			if (id == null) {
				result.setResultMsg("新增厂商失败");
				return result;
			}
			result.setSuccess(true);
			result.setModule(true);
		} catch (Exception e) {
			result.setResultMsg("新增厂商失败 ");
			logger.error(
					"createNewGameCarrier error CarrierName : " + gameCarrier.getCarrierName(), e);
			return result;
		}
		return result;
	}

	@Override
	public Result<GameCarrier> getGameCarrier(Long id) {
		Result<GameCarrier> result = new Result<GameCarrier>();
		try {
			GameCarrier gameCarrier = gameCarrierDAO.selectByPrimaryKey(id);
			if (gameCarrier != null) {
				result.setSuccess(true);
				result.setModule(gameCarrier);
			} else {
				result.setResultMsg("不存在该厂商 ");
			}
		} catch (SQLException e) {
			result.setResultMsg("厂商查询异常  msg : " + e.getMessage());
			logger.error("getGameCarrier error  id = " + id, e);
		}
		return result;
	}

	@Override
	public Result<Boolean> updateGameCarrier(GameCarrier gameCarrier) {
		Result<Boolean> result = new Result<Boolean>();
		try {
			int row = gameCarrierDAO.updateByPrimaryKey(gameCarrier);
			result.setSuccess(row > 0);
			result.setModule(row > 0);
		} catch (SQLException e) {
			result.setResultMsg("更新厂商失败    msg: " + e.getMessage());
			logger.error("updateGameCarrier error ", e);
		}

		return result;
	}

	@Override
	public Result<List<GameCarrier>> queryAllGameCarrier(GameCarrierQuery gameCarrierQuery) {
		Result<List<GameCarrier>> result = new Result<List<GameCarrier>>();
		try {
			List<GameCarrier> queryResult = gameCarrierDAO.queryAll(gameCarrierQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("厂商列表查询失败    msg: " + e.getMessage());
			logger.error("queryGameCarrierList error ", e);
		}

		return result;
	}

}
