package com.longan.biz.core.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.longan.biz.core.BaseService;
import com.longan.biz.core.ChargingLimitService;
import com.longan.biz.core.StockService;
import com.longan.biz.core.SupplyOrderService;
import com.longan.biz.dao.SupplyOrderDAO;
import com.longan.biz.dataobject.Stock;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.dataobject.SupplyOrderQuery;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;

public class SupplyOrderServiceImpl extends BaseService implements SupplyOrderService {
	@Resource
	private SupplyOrderDAO supplyOrderDAO;

	@Resource
	private ChargingLimitService chargingLimitService;

	@Resource
	private StockService stockService;

	@Override
	public Result<List<SupplyOrder>> querySupplyOrderPage(SupplyOrderQuery supplyOrderQuery) {
		Result<List<SupplyOrder>> result = new Result<List<SupplyOrder>>();
		if (supplyOrderQuery == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			List<SupplyOrder> queryResult = supplyOrderDAO.queryByPage(supplyOrderQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("供货单查询失败    msg: " + e.getMessage());
			logger.error("querySupplyOrderPage error ", e);
		}

		return result;
	}

	@Override
	public Result<SupplyOrder> createSupplyOrder(SupplyOrder supplyOrder) {
		Result<SupplyOrder> result = new Result<SupplyOrder>();
		result.setModule(supplyOrder);
		if (supplyOrder == null) {
			result.setResultMsg("入参错误");
			return result;
		}
		try {
			Long id = supplyOrderDAO.insert(supplyOrder);
			if (id != null) {
				supplyOrder.setId(id);
				result.setSuccess(true);
				result.setModule(supplyOrder);
			} else {
				result.setResultMsg("创建供货单失败");
			}
		} catch (SQLException e) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_BIZORDER);
			result.setResultMsg("创建供货单失败,数据库异常");
			logger.error("createSupplyOrder error", e);
		}

		return result;
	}

	@Override
	public Result<Boolean> updateSupplyOrder(SupplyOrder supplyOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (supplyOrder == null || supplyOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}
		try {
			int id = supplyOrderDAO.updateSupplyOrder(supplyOrder);
			if (id > 0) {
				result.setSuccess(true);
				result.setModule(true);
			} else {
				result.setResultMsg("更新供货单失败");
			}
		} catch (SQLException e) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_BIZORDER);
			result.setResultMsg("更新供货单失败 ,数据库异常");
			logger.error("updateSupplyOrder error", e);
		}

		return result;
	}

	@Override
	public Result<Boolean> updateSupplyOrderCheckStatus(SupplyOrder supplyOrder,
			List<Integer> statusList) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (supplyOrder == null || supplyOrder.getId() == null || statusList == null) {
			result.setResultMsg("入参错误");
			return result;
		}
		try {
			boolean flag = supplyOrderDAO.updateSupplyOrderCheckStatus(supplyOrder, statusList);
			if (flag) {
				result.setSuccess(true);
				result.setModule(true);
			} else {
				logger.warn("供货单已被其他线程处理,supplyOrderId : " + supplyOrder.getId());
				result.setResultMsg("更新供货单失败,供货单已被其他线程处理");
			}
		} catch (SQLException e) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_BIZORDER);
			result.setResultMsg("更新供货单失败 ,数据库异常");
			logger.error("updateSupplyOrderCheckStatus error", e);
		}

		return result;
	}

	@Override
	public Result<SupplyOrder> getSupplyOrder(Long supplyOrderId) {
		Result<SupplyOrder> result = new Result<SupplyOrder>();
		if (supplyOrderId == null) {
			result.setResultMsg("入参错误");
			return result;
		}
		try {
			SupplyOrder supplyOrder = supplyOrderDAO.getSupplyOrderById(supplyOrderId);
			result.setSuccess(true);
			result.setModule(supplyOrder);
		} catch (SQLException e) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_BIZORDER);
			result.setResultMsg("查询供货单失败 ,数据库异常");
			logger.error("getSupplyOrder error", e);
		}

		return result;
	}

	@Override
	public Result<Boolean> comfirmSupplyOrder(SupplyOrder supplyOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (supplyOrder == null || supplyOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		if (supplyOrder.isSupplyFromStock() && supplyOrder.getStockId() != null) {
			Result<Stock> getStockResult = stockService.getStockById(supplyOrder.getStockId());
			// 不影响主流程。
			if (!getStockResult.isSuccess()) {
				logger.error("comfirmSupplyOrder getStockById error stockId:"
						+ supplyOrder.getStockId() + " msg : " + getStockResult.getResultMsg());
			} else {
				Stock stock = getStockResult.getModule();
				if (stock != null) {
					stockService.deliveryFromStorage(stock);
				}
			}
		}

		// 记录供货时间。
		if (supplyOrder.getGmtCreate() == null) {
			Result<SupplyOrder> queryResult = getSupplyOrder(supplyOrder.getId());
			if (queryResult.getModule() == null) {
				result.setResultMsg("找不到该供货单");
				return result;
			}
			supplyOrder.setSupplyCostTime(supplyOrder.computCostTime(queryResult.getModule()
					.getGmtCreate()));
		} else {
			supplyOrder.setSupplyCostTime(supplyOrder.computCostTime(supplyOrder.getGmtCreate()));
		}

		supplyOrder.setSupplyStatus(Constants.SupplyOrder.STATUS_SUCCESS);
		// 如果以后是组合供货，这个逻辑要改。
		supplyOrder.setFinalType(Constants.SupplyOrder.FINAL_TYPE_YES);

		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(Constants.SupplyOrder.STATUS_CHARGING);
		statusList.add(Constants.SupplyOrder.STATUS_UNCONFIRMED);
		statusList.add(Constants.SupplyOrder.STATUS_INIT);
		statusList.add(Constants.SupplyOrder.STATUS_EXCEPTION);
		result = updateSupplyOrderCheckStatus(supplyOrder, statusList);

		if (!result.isSuccess()) {
			return result;
		}

		if(result.getModule()){
			// 处理中限制 减减
			chargingLimitService.decCount(supplyOrder.getSupplyTraderId());
		}


		return result;
	}

	@Override
	public Result<Boolean> cancelSupplyOrder(SupplyOrder supplyOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (supplyOrder == null || supplyOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		if (supplyOrder.isSupplyFromStock() && supplyOrder.getStockId() != null) {
			Result<Stock> getStockResult = stockService.getStockById(supplyOrder.getStockId());
			// 不影响主流程。
			if (!getStockResult.isSuccess()) {
				logger.error("cancelSupplyOrder getStockById error stockId:"
						+ supplyOrder.getStockId() + " msg : " + getStockResult.getResultMsg());
			} else {
				Stock stock = getStockResult.getModule();
				if (stock != null && supplyOrder.isFlagCardValid()) {
					stock.setErrorInfo(supplyOrder.getUpstreamMemo());
					stockService.sequestrationStorage(stock);
				} else if (stock != null && !supplyOrder.isFlagCardValid()) {
					stockService.returnToStorage(stock);
				}

			}
		}

		supplyOrder.setDealOperId(null); // 这里不记录处理人。
		supplyOrder.setSupplyStatus(Constants.SupplyOrder.STATUS_FAILED);

		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(Constants.SupplyOrder.STATUS_CHARGING);
		statusList.add(Constants.SupplyOrder.STATUS_UNCONFIRMED);
		statusList.add(Constants.SupplyOrder.STATUS_INIT);
		statusList.add(Constants.SupplyOrder.STATUS_EXCEPTION);
		result = updateSupplyOrderCheckStatus(supplyOrder, statusList);

		if (!result.isSuccess()) {
			return result;
		}
		
		if(result.getModule()){
			// 处理中限制 减减
			chargingLimitService.decCount(supplyOrder.getSupplyTraderId());
		}

		return result;
	}

	@Override
	public Result<Boolean> unComfirmSupplyOrder(SupplyOrder supplyOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (supplyOrder == null || supplyOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		supplyOrder.setSupplyStatus(Constants.SupplyOrder.STATUS_UNCONFIRMED);
		result = updateSupplyOrder(supplyOrder);

		return result;
	}

	@Override
	public Result<Boolean> chargingSupplyOrderAndCounting(SupplyOrder supplyOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (supplyOrder == null || supplyOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		supplyOrder.setSupplyStatus(Constants.SupplyOrder.STATUS_CHARGING);
		result = updateSupplyOrder(supplyOrder);

		if (!result.isSuccess()) {
			return result;
		}

		if(result.getModule()){
			// 处理中限制 加加
			chargingLimitService.incCount(supplyOrder.getSupplyTraderId());
		}
		return result;
	}

	@Override
	public Result<SupplyOrder> getSupplyOrderById(Long supplyOrderId) {
		Result<SupplyOrder> result = new Result<SupplyOrder>();
		if (supplyOrderId == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			SupplyOrder supplyOrder = supplyOrderDAO.selectSupplyOrderById(supplyOrderId);
			if (supplyOrder != null) {
				result.setSuccess(true);
				result.setModule(supplyOrder);
			} else {
				result.setResultMsg("没有该供货单。供货单id : " + supplyOrderId);
			}
		} catch (SQLException e) {
			result.setResultMsg("供货单查询失败  id : " + supplyOrderId + " msg: " + e.getMessage());
			logger.error("getSupplyOrderById error", e);
		}
		return result;
	}

	@Override
	public Result<Integer> getCountInChargingByTraderId(Long traderId) {
		Result<Integer> result = new Result<Integer>();
		if (traderId == null) {
			result.setResultMsg("入参错误");
			return result;
		}
		SupplyOrderQuery supplyOrderQuery = new SupplyOrderQuery();
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(Constants.SupplyOrder.STATUS_CHARGING);
		statusList.add(Constants.SupplyOrder.STATUS_UNCONFIRMED);
		statusList.add(Constants.SupplyOrder.STATUS_INIT);
		statusList.add(Constants.SupplyOrder.STATUS_LOCK);
		supplyOrderQuery.setSupplyTraderId(traderId);
		supplyOrderQuery.setStatusList(statusList);
		supplyOrderQuery.setStartGmtCreate(DateTool.beforeDay(new Date(), 2));

		Integer count = null;
		try {
			count = supplyOrderDAO.queryCount(supplyOrderQuery);
		} catch (SQLException e) {
			result.setResultMsg("getCountInChargingByTraderId error 查询数据库异常");
			logger.error("getCountInChargingByTraderId error", e);
			return result;
		}
		result.setModule(count);
		result.setSuccess(true);
		return result;
	}
	

	@Override
	public Result<SupplyOrder> getSupplyOrderByIdAndTraderId(Long id, Long traderId) {
		Result<SupplyOrder> result = new Result<SupplyOrder>();
		if (traderId == null || id == null) {
			result.setResultMsg("入参错误");
			return result;
		}
		SupplyOrderQuery supplyOrderQuery = new SupplyOrderQuery();
		supplyOrderQuery.setSupplyTraderId(traderId);
		supplyOrderQuery.setId(id);

		try {
			List<SupplyOrder> list = supplyOrderDAO.queryByPage(supplyOrderQuery);
			result.setSuccess(true);
			if (list != null && !list.isEmpty()) {
				result.setModule(list.get(0));
			}
		} catch (SQLException e) {
			result.setResultMsg("getSupplyOrderByIdAndTraderId error 查询数据库异常");
			logger.error("getSupplyOrderByIdAndTraderId error", e);
			return result;
		}
		return result;
	}

	@Override
	public Result<SupplyOrder> getSupplyOrderByUpstreamSerialno(String upstreamSerialno,
			Long traderId) {
		Result<SupplyOrder> result = new Result<SupplyOrder>();
		if (traderId == null || upstreamSerialno == null) {
			result.setResultMsg("入参错误");
			return result;
		}
		SupplyOrderQuery supplyOrderQuery = new SupplyOrderQuery();
		supplyOrderQuery.setSupplyTraderId(traderId);
		supplyOrderQuery.setUpstreamSerialno(upstreamSerialno);

		try {
			List<SupplyOrder> list = supplyOrderDAO.queryByPage(supplyOrderQuery);
			result.setSuccess(true);
			if (list != null && !list.isEmpty()) {
				result.setModule(list.get(0));
			}
		} catch (SQLException e) {
			result.setResultMsg("getSupplyOrderByUpstreamSerialno error 查询数据库异常");
			logger.error("getSupplyOrderByUpstreamSerialno error", e);
			return result;
		}
		return result;
	}

	@Override
	public Result<List<SupplyOrder>> querySupplyOrderByBizOrder(Long bizOrderId) {
		Result<List<SupplyOrder>> result = new Result<List<SupplyOrder>>();
		if (bizOrderId == null) {
			result.setResultMsg("入参错误");
			return result;
		}
		SupplyOrderQuery supplyOrderQuery = new SupplyOrderQuery();
		supplyOrderQuery.setBizOrderId(bizOrderId);
		supplyOrderQuery.setPageSize(100);

		try {
			List<SupplyOrder> list = supplyOrderDAO.queryByPage(supplyOrderQuery);
			result.setSuccess(true);
			result.setModule(list);
		} catch (SQLException e) {
			result.setResultMsg("querySupplyOrderByBizOrder error 查询数据库异常");
			logger.error("querySupplyOrderByBizOrder error", e);
			return result;
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
			List<SupplyOrder> queryResult = supplyOrderDAO.queryByExport(supplyOrderQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("供货查询失败    msg: " + e.getMessage());
			logger.error("querySupplyOrderPage error ", e);
		}

		return result;
	}

	@Override
	public Result<Boolean> manualLockSupplyOrder(SupplyOrder supplyOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (supplyOrder == null || supplyOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			int row = supplyOrderDAO.lockSupplyOrder(supplyOrder);
			if (row > 0) {
				result.setSuccess(true);
				result.setModule(true);
			} else {
				result.setResultMsg("锁定失败 ,可能被其他操作员抢先啦。");
			}
		} catch (SQLException e) {
			result.setResultMsg("锁定失败 supplyOrderId : " + supplyOrder.getId() + "数据库异常");
			logger.error("lockBizOrder error", e);
		}
		return result;
	}

	@Override
	public Result<Boolean> manualUnLockSupplyOrder(SupplyOrder supplyOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (supplyOrder == null || supplyOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			int row = supplyOrderDAO.unLockSupplyOrder(supplyOrder);
			if (row > 0) {
				result.setSuccess(true);
				result.setModule(true);
			} else {
				result.setResultMsg("解锁失败,可能被其他操作员抢先啦");
			}
		} catch (SQLException e) {
			result.setResultMsg("解锁失败 supplyOrderId : " + supplyOrder.getId() + "数据库异常");
			logger.error("unLockSupplyOrder error", e);
		}
		return result;
	}

	@Override
	public Result<Boolean> manualConfirmSupplyOrder(SupplyOrder supplyOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (supplyOrder == null || supplyOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			// 记录供货时间。
			if (supplyOrder.getGmtCreate() == null) {
				Result<SupplyOrder> queryResult = getSupplyOrder(supplyOrder.getId());
				if (queryResult.getModule() == null) {
					result.setResultMsg("找不到该供货单");
					return result;
				}
				supplyOrder.setSupplyCostTime(supplyOrder.computCostTime(queryResult.getModule()
						.getGmtCreate()));
			} else {
				supplyOrder
						.setSupplyCostTime(supplyOrder.computCostTime(supplyOrder.getGmtCreate()));
			}
			int row = supplyOrderDAO.confirmSupplyOrder(supplyOrder);
			if (row > 0) {
				result.setSuccess(true);
				result.setModule(true);

				// 处理中限制 减减
				chargingLimitService.decCount(supplyOrder.getSupplyTraderId());
			} else {
				result.setResultMsg("确认失败,供货单已经被确认,或者该供货单不是你锁定的");
			}
		} catch (SQLException e) {
			result.setResultMsg("确认失败 supplyOrderId : " + supplyOrder.getId() + "数据库异常");
			logger.error("confirmSupplyOrder error", e);
		}
		return result;
	}

	@Override
	public Result<Boolean> manualCancelSupplyOrder(SupplyOrder supplyOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (supplyOrder == null || supplyOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			int row = supplyOrderDAO.cancelSupplyOrder(supplyOrder);
			if (row > 0) {
				result.setSuccess(true);
				result.setModule(true);

				// 处理中限制 减减
				chargingLimitService.decCount(supplyOrder.getSupplyTraderId());

			} else {
				result.setResultMsg("人工锁定失败 ,可能被其他操作员抢先啦。订单id : " + supplyOrder.getId());
			}
		} catch (SQLException e) {
			result.setResultMsg("人工锁定失败 supplyOrderId : " + supplyOrder.getId() + "数据库异常");
			logger.error("cancelSupplyOrder error", e);
		}
		return result;
	}

	@Override
	public Result<Boolean> manualUnConfirmSupplyOrder(SupplyOrder supplyOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (supplyOrder == null || supplyOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			int row = supplyOrderDAO.unConfirmSupplyOrder(supplyOrder);
			if (row > 0) {
				result.setSuccess(true);
				result.setModule(true);
			} else {
				result.setResultMsg("未确认供货单失败，供货单已经被未确认操作,或者该供货单不是你锁定的");
			}
		} catch (SQLException e) {
			result.setResultMsg("未确认供货单失败 supplyOrderId : " + supplyOrder.getId() + "数据库异常");
			logger.error("unConfirmSupplyOrder error", e);
		}
		return result;
	}

}
