package com.longan.biz.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.longan.biz.dao.SupplyOrderDAO;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.dataobject.SupplyOrderQuery;

public class SupplyOrderDAOImpl implements SupplyOrderDAO {
	@Resource
	private SqlMapClient sqlMapClient;

	@Override
	public Long insert(SupplyOrder supplyOrder) throws SQLException {
		return (Long) sqlMapClient.insert("supply_order.insert", supplyOrder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SupplyOrder> queryByPage(SupplyOrderQuery supplyOrderQuery) throws SQLException {
		int count = (Integer) sqlMapClient.queryForObject("supply_order.queryByPageCount",
				supplyOrderQuery);
		supplyOrderQuery.setTotalItem(count);
		return (List<SupplyOrder>) sqlMapClient.queryForList("supply_order.queryByPage",
				supplyOrderQuery);
	}

	@Override
	public int updateSupplyOrder(SupplyOrder supplyOrder) throws SQLException {
		return sqlMapClient.update("supply_order.updateSupplyOrder", supplyOrder);
	}

	@Override
	public SupplyOrder getSupplyOrderById(Long supplyOrderId) throws SQLException {
		return (SupplyOrder) sqlMapClient.queryForObject("supply_order.getSupplyOrderById",
				supplyOrderId);
	}

	@Override
	public SupplyOrder selectSupplyOrderById(Long id) throws SQLException {
		SupplyOrder key = new SupplyOrder();
		key.setId(id);
		SupplyOrder record = (SupplyOrder) sqlMapClient.queryForObject(
				"supply_order.selectByPrimaryKey", key);
		return record;
	}

	@Override
	public Integer queryCount(SupplyOrderQuery supplyOrderQuery) throws SQLException {
		Integer count = (Integer) sqlMapClient.queryForObject("supply_order.queryByPageCount",
				supplyOrderQuery);
		return count;
	}

	@Override
	public boolean updateSupplyOrderCheckStatus(SupplyOrder supplyOrder, List<Integer> statusList)
			throws SQLException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("supplyOrder", supplyOrder);
		params.put("statusList", statusList);
		return sqlMapClient.update("supply_order.updateSupplyOrderCheckStatus", params) > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SupplyOrder> queryByExport(SupplyOrderQuery supplyOrderQuery) throws SQLException {
		return (List<SupplyOrder>) sqlMapClient.queryForList("supply_order.queryByExport",
				supplyOrderQuery);
	}

	@Override
	public int lockSupplyOrder(SupplyOrder supplyOrder) throws SQLException {
		int rows = sqlMapClient.update("supply_order.lockSupplyOrder", supplyOrder);
		return rows;
	}

	@Override
	public int unLockSupplyOrder(SupplyOrder supplyOrder) throws SQLException {
		int rows = sqlMapClient.update("supply_order.unLockSupplyOrder", supplyOrder);
		return rows;
	}

	@Override
	public int cancelSupplyOrder(SupplyOrder supplyOrder) throws SQLException {
		int rows = sqlMapClient.update("supply_order.cancelSupplyOrder", supplyOrder);
		return rows;
	}

	@Override
	public int confirmSupplyOrder(SupplyOrder supplyOrder) throws SQLException {
		int rows = sqlMapClient.update("supply_order.confirmSupplyOrder", supplyOrder);
		return rows;
	}

	@Override
	public int unConfirmSupplyOrder(SupplyOrder supplyOrder) throws SQLException {
		int rows = sqlMapClient.update("supply_order.unConfirmSupplyOrder", supplyOrder);
		return rows;
	}

}
