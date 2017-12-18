package com.longan.biz.core.impl;

import java.sql.SQLException;

import javax.annotation.Resource;

import com.longan.biz.core.BaseService;
import com.longan.biz.core.ShortMsgService;
import com.longan.biz.dao.ShortMsgDAO;
import com.longan.biz.dataobject.ShortMsg;
import com.longan.biz.domain.Result;

public class ShortMsgServiceImpl extends BaseService implements ShortMsgService {
	@Resource
	private ShortMsgDAO shortMsgDAO;

	@Override
	public Result<ShortMsg> createShortMsg(ShortMsg shortMsg) {
		Result<ShortMsg> result = new Result<ShortMsg>();
		try {
			Long id = shortMsgDAO.insert(shortMsg);
			if (id != null) {
				shortMsg.setId(id);
				result.setSuccess(true);
				result.setModule(shortMsg);
			} else {
				result.setResultMsg("新增短信信息失败");
			}
		} catch (SQLException e) {
			result.setResultMsg("新增短信信息失败 msg :" + e.getMessage());
			logger.error("createNewShortMsg error mobile : " + shortMsg.getMobile(), e);
		}
		return result;
	}

	@Override
	public Result<Boolean> updateShortMsg(ShortMsg shortMsg) {
		Result<Boolean> result = new Result<Boolean>();
		try {
			int row = shortMsgDAO.updateByPrimaryKey(shortMsg);
			result.setSuccess(row > 0);
			result.setModule(row > 0);
		} catch (SQLException e) {
			result.setResultMsg("更新短信信息失败    msg: " + e.getMessage());
			logger.error("updateShortMsg error ", e);
		}

		return result;
	}

}
