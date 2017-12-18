package com.longan.biz.core.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;

import com.longan.biz.core.BaseService;
import com.longan.biz.core.QueryService;
import com.longan.biz.dao.QueryAcctLogDAO;
import com.longan.biz.dao.QueryBizOrderDAO;
import com.longan.biz.dao.QueryPayOrderDAO;
import com.longan.biz.dao.QueryStockDAO;
import com.longan.biz.dao.QuerySupplyOrderDAO;
import com.longan.biz.dataobject.AcctLog;
import com.longan.biz.dataobject.AcctLogQuery;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.BizOrderQuery;
import com.longan.biz.dataobject.PayOrder;
import com.longan.biz.dataobject.PayOrderQuery;
import com.longan.biz.dataobject.Stock;
import com.longan.biz.dataobject.StockQuery;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.dataobject.SupplyOrderQuery;
import com.longan.biz.domain.BizOrderStatisic;
import com.longan.biz.domain.Result;
import com.longan.biz.domain.SupplyOrderStatisic;
import com.longan.biz.utils.Constants;

public class QueryServiceImpl extends BaseService implements QueryService {
	@Resource
	private QueryBizOrderDAO queryBizOrderDAO;

	@Resource
	private QueryAcctLogDAO queryAcctLogDAO;

	@Resource
	private QueryStockDAO queryStockDAO;

	@Resource
	private QueryPayOrderDAO queryPayOrderDAO;

	@Resource
	private QuerySupplyOrderDAO querySupplyOrderDAO;

	@Override
	public Result<List<BizOrder>> queryBizOrderPage(BizOrderQuery bizOrderQuery) {
		Result<List<BizOrder>> result = new Result<List<BizOrder>>();
		try {
			List<BizOrder> queryResult = queryBizOrderDAO.queryByPage(bizOrderQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("订单查询失败    msg: " + e.getMessage());
			logger.error("queryBizOrderPage error ", e);
		}

		return result;
	}

	@Override
	public Result<List<BizOrder>> queryBizOrderExport(BizOrderQuery bizOrderQuery) {
		Result<List<BizOrder>> result = new Result<List<BizOrder>>();
		try {
			List<BizOrder> queryResult = queryBizOrderDAO.queryByExport(bizOrderQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("订单查询失败    msg: " + e.getMessage());
			logger.error("queryBizOrderPage error ", e);
		}

		return result;
	}

	@Override
	public Result<List<AcctLog>> queryAcctLog(AcctLogQuery acctLogQuery) {
		Result<List<AcctLog>> result = new Result<List<AcctLog>>();
		try {
			List<AcctLog> queryResult = queryAcctLogDAO.queryByPage(acctLogQuery);
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
			List<AcctLog> queryResult = queryAcctLogDAO.queryByExport(acctLogQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("账户资金流水查询失败    msg: " + e.getMessage());
			logger.error("queryAcctLogPage error ", e);
		}

		return result;
	}

	@Override
	public Result<List<Stock>> queryStockPage(StockQuery stockQuery) {
		Result<List<Stock>> result = new Result<List<Stock>>();
		try {
			List<Stock> queryResult = queryStockDAO.queryByPage(stockQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("库存列表失败    msg: " + e.getMessage());
			logger.error("queryStockPage error ", e);
		}
		return result;
	}

	@Override
	public Result<List<PayOrder>> queryPayOrder(PayOrderQuery payOrderQuery) {
		Result<List<PayOrder>> result = new Result<List<PayOrder>>();
		try {
			List<PayOrder> queryResult = queryPayOrderDAO.queryByPage(payOrderQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("支付单查询失败    msg: " + e.getMessage());
			logger.error("queryPayOrder error ", e);
		}
		return result;
	}

	@Override
	public Result<BizOrderStatisic> countBizOrder(BizOrderQuery bizOrderQuery) {
		Result<BizOrderStatisic> result = new Result<BizOrderStatisic>();
		BizOrderStatisic bizOrderStatisic = new BizOrderStatisic();
		result.setModule(bizOrderStatisic);
		BizOrderQuery newBizOrderQuery = new BizOrderQuery();
		BeanUtils.copyProperties(bizOrderQuery, newBizOrderQuery);
		if (newBizOrderQuery.getId() != null) {
			result.setSuccess(true);
			return result;
		}
		if (newBizOrderQuery.getStartGmtCreate() == null) {
			newBizOrderQuery.setStartGmtCreate(new Date());
		}

		if (newBizOrderQuery.getEndGmtCreate() == null) {
			newBizOrderQuery.setEndGmtCreate(new Date());
		}

		try {
			newBizOrderQuery.setStatus(Constants.BizOrder.STATUS_SUCCESS);
			bizOrderStatisic.setSuccessCount(queryBizOrderDAO.queryCount(newBizOrderQuery));
			Long successCostPrice = queryBizOrderDAO.queryCostPriceCount(newBizOrderQuery);// 成功扣款总金额
			bizOrderStatisic.setCostPriceCount(successCostPrice);

			newBizOrderQuery.setStatus(Constants.BizOrder.STATUS_CHARGING);
			bizOrderStatisic.setChargingCount(queryBizOrderDAO.queryCount(newBizOrderQuery));

			newBizOrderQuery.setStatus(Constants.BizOrder.STATUS_FAILED);
			bizOrderStatisic.setFailedCount(queryBizOrderDAO.queryCount(newBizOrderQuery));

			newBizOrderQuery.setStatus(Constants.BizOrder.STATUS_UNCONFIRMED);
			bizOrderStatisic.setUnComfirmCount(queryBizOrderDAO.queryCount(newBizOrderQuery));

			newBizOrderQuery.setStatus(Constants.BizOrder.STATUS_EXCEPTION);
			bizOrderStatisic.setExceptionCount(queryBizOrderDAO.queryCount(newBizOrderQuery));
		} catch (SQLException e) {
			result.setResultMsg("订单数量统计失败 ，数据库异常");
			logger.error("countBizOrder error ", e);
			return result;
		}
		result.setSuccess(true);
		return result;
	}

	@Override
	public Result<SupplyOrderStatisic> countSupplyOrder(SupplyOrderQuery supplyOrderQuery) {
		Result<SupplyOrderStatisic> result = new Result<SupplyOrderStatisic>();
		SupplyOrderStatisic supplyOrderStatisic = new SupplyOrderStatisic();
		result.setModule(supplyOrderStatisic);
		SupplyOrderQuery newSupplyOrderQuery = new SupplyOrderQuery();
		BeanUtils.copyProperties(supplyOrderQuery, newSupplyOrderQuery);
		if (newSupplyOrderQuery.getId() != null || newSupplyOrderQuery.getBizOrderId() != null) {
			result.setSuccess(true);
			return result;
		}
		if (newSupplyOrderQuery.getStartGmtCreate() == null) {
			supplyOrderQuery.setStartGmtCreate(new Date());
		}

		if (newSupplyOrderQuery.getEndGmtCreate() == null) {
			supplyOrderQuery.setEndGmtCreate(new Date());
		}

		try {
			newSupplyOrderQuery.setSupplyStatus(Constants.SupplyOrder.STATUS_SUCCESS);
			supplyOrderStatisic
					.setSuccessCount(querySupplyOrderDAO.queryCount(newSupplyOrderQuery));

			newSupplyOrderQuery.setSupplyStatus(Constants.SupplyOrder.STATUS_CHARGING);
			supplyOrderStatisic.setChargingCount(querySupplyOrderDAO
					.queryCount(newSupplyOrderQuery));

			newSupplyOrderQuery.setSupplyStatus(Constants.SupplyOrder.STATUS_FAILED);
			supplyOrderStatisic.setFailedCount(querySupplyOrderDAO.queryCount(newSupplyOrderQuery));

			newSupplyOrderQuery.setSupplyStatus(Constants.SupplyOrder.STATUS_UNCONFIRMED);
			supplyOrderStatisic.setUnComfirmCount(querySupplyOrderDAO
					.queryCount(newSupplyOrderQuery));

			newSupplyOrderQuery.setSupplyStatus(Constants.SupplyOrder.STATUS_EXCEPTION);
			supplyOrderStatisic.setExceptionCount(querySupplyOrderDAO
					.queryCount(newSupplyOrderQuery));

			newSupplyOrderQuery.setSupplyStatus(Constants.SupplyOrder.STATUS_LOCK);
			supplyOrderStatisic.setLockedCount(querySupplyOrderDAO.queryCount(newSupplyOrderQuery));
		} catch (SQLException e) {
			result.setResultMsg("供货单数量统计失败 ，数据库异常");
			logger.error("countSupplyOrder error ", e);
			return result;
		}
		result.setSuccess(true);
		return result;
	}

	@Override
	public Result<List<SupplyOrder>> querySupplyOrderPage(SupplyOrderQuery supplyOrderQuery) {
		Result<List<SupplyOrder>> result = new Result<List<SupplyOrder>>();
		if (supplyOrderQuery == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			List<SupplyOrder> queryResult = querySupplyOrderDAO.queryByPage(supplyOrderQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("供货单查询失败    msg: " + e.getMessage());
			logger.error("querySupplyOrderPage error ", e);
		}

		return result;
	}

	@Override
	public Result<List<SupplyOrder>> querySupplyOrderExport(SupplyOrderQuery supplyOrderQuery) {
		Result<List<SupplyOrder>> result = new Result<List<SupplyOrder>>();
		if (supplyOrderQuery == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			List<SupplyOrder> queryResult = querySupplyOrderDAO.queryByExport(supplyOrderQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("供货查询失败    msg: " + e.getMessage());
			logger.error("querySupplyOrderPage error ", e);
		}

		return result;
	}

	@Override
	public Result<BizOrderStatisic> countDownStreamBizOrder(BizOrderQuery bizOrderQuery) {
		Result<BizOrderStatisic> result = new Result<BizOrderStatisic>();
		BizOrderStatisic bizOrderStatisic = new BizOrderStatisic();
		result.setModule(bizOrderStatisic);
		BizOrderQuery newBizOrderQuery = new BizOrderQuery();
		BeanUtils.copyProperties(bizOrderQuery, newBizOrderQuery);
		if (newBizOrderQuery.getId() != null) {
			result.setSuccess(true);
			return result;
		}
		if (newBizOrderQuery.getStartGmtCreate() == null) {
			newBizOrderQuery.setStartGmtCreate(new Date());
		}

		if (newBizOrderQuery.getEndGmtCreate() == null) {
			newBizOrderQuery.setEndGmtCreate(new Date());
		}

		try {
			newBizOrderQuery.setStatus(Constants.BizOrder.STATUS_SUCCESS);
			Integer successCount = queryBizOrderDAO.queryCount(newBizOrderQuery);
			bizOrderStatisic.setSuccessCount(successCount);
			Long successCostPrice = queryBizOrderDAO.queryCostPriceCount(newBizOrderQuery);// 成功扣款总金额
			bizOrderStatisic.setCostPriceCount(successCostPrice);

			newBizOrderQuery.setStatus(Constants.BizOrder.STATUS_FAILED);
			Integer failedCount = queryBizOrderDAO.queryCount(newBizOrderQuery);
			bizOrderStatisic.setFailedCount(failedCount);

			newBizOrderQuery.setStatus(null);
			bizOrderStatisic.setChargingCount(queryBizOrderDAO.queryCount(newBizOrderQuery)
					- (successCount + failedCount));
		} catch (SQLException e) {
			result.setResultMsg("订单数量统计失败 ，数据库异常");
			logger.error("countBizOrder error ", e);
			return result;
		}
		result.setSuccess(true);
		return result;
	}

}
