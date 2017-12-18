package com.longan.biz.dao.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.longan.biz.dao.QuerySupplyOrderDAO;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.dataobject.SupplyOrderQuery;

public class QuerySupplyOrderDAOImpl implements QuerySupplyOrderDAO {

	@Resource
	private SqlMapClient querySqlMapClient;

	@SuppressWarnings("unchecked")
	@Override
	public List<SupplyOrder> queryByPage(SupplyOrderQuery supplyOrderQuery) throws SQLException {
		int count = (Integer) querySqlMapClient.queryForObject("supply_order.queryByPageCount",
				supplyOrderQuery);
		supplyOrderQuery.setTotalItem(count);
		return (List<SupplyOrder>) querySqlMapClient.queryForList("supply_order.queryByPage",
				supplyOrderQuery);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SupplyOrder> queryByExport(SupplyOrderQuery supplyOrderQuery) throws SQLException {
		return (List<SupplyOrder>) querySqlMapClient.queryForList("supply_order.queryByExport",
				supplyOrderQuery);
	}

	@Override
	public Integer queryCount(SupplyOrderQuery supplyOrderQuery) throws SQLException {
		Integer count = (Integer) querySqlMapClient.queryForObject("supply_order.queryByPageCount",
				supplyOrderQuery);
		return count;
	}

}
