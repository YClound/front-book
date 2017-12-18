package com.longan.biz.dao.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.longan.biz.dao.CashOrderDAO;
import com.longan.biz.dataobject.CashOrder;
import com.longan.biz.dataobject.CashOrderQuery;

public class CashOrderDAOImpl implements CashOrderDAO {
	@Resource
	private SqlMapClient sqlMapClient;

	@SuppressWarnings("unchecked")
	@Override
	public List<CashOrder> queryByPage(CashOrderQuery cashOrderQuery) throws SQLException {
		int count = (Integer) sqlMapClient.queryForObject("cash_order.queryByPageCount",
				cashOrderQuery);
		cashOrderQuery.setTotalItem(count);
		return (List<CashOrder>) sqlMapClient
				.queryForList("cash_order.queryByPage", cashOrderQuery);
	}

	@Override
	public Long insert(CashOrder record) throws SQLException {
		return (Long) sqlMapClient.insert("cash_order.insert", record);
	}

	@Override
	public CashOrder selectByPrimaryKey(Long id) throws SQLException {
		CashOrder key = new CashOrder();
		key.setId(id);
		CashOrder record = (CashOrder) sqlMapClient.queryForObject("cash_order.selectByPrimaryKey",
				key);
		return record;
	}

	@Override
	public int updateByPrimaryKeySelective(CashOrder record) throws SQLException {
		int rows = sqlMapClient.update("cash_order.updateByPrimaryKeySelective", record);
		return rows;
	}

	@Override
	public int updateByPrimaryKeyAndStatus(CashOrder record) throws SQLException {
		int rows = sqlMapClient.update("cash_order.updateByPrimaryKeyAndStatus", record);
		return rows;
	}

}
