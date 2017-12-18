package com.longan.biz.core.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import com.longan.biz.core.AcctLogService;
import com.longan.biz.core.BaseService;
import com.longan.biz.dao.AcctLogDAO;
import com.longan.biz.dataobject.AcctLog;
import com.longan.biz.dataobject.AcctLogQuery;
import com.longan.biz.domain.Result;

public class AcctLogServiceImpl extends BaseService implements AcctLogService {

	@Resource
	private AcctLogDAO acctLogDAO;

	@Override
	public Result<List<AcctLog>> queryAcctLog(AcctLogQuery acctLogQuery) {
		Result<List<AcctLog>> result = new Result<List<AcctLog>>();
		try {
			List<AcctLog> queryResult = acctLogDAO.queryByPage(acctLogQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("资金流水查询失败    msg: " + e.getMessage());
			logger.error("queryAcctLog error ", e);
		}
		return result;
	}

	@Override
	public Result<List<AcctLog>> queryAcctLogExport(AcctLogQuery acctLogQuery) {
		Result<List<AcctLog>> result = new Result<List<AcctLog>>();
		try {
			List<AcctLog> queryResult = acctLogDAO.queryByExport(acctLogQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("账户资金流水查询失败    msg: " + e.getMessage());
			logger.error("queryAcctLogPage error ", e);
		}

		return result;
	}

}
