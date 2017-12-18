package com.longan.biz.dao.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.longan.biz.dao.QueryBizOrderDAO;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.BizOrderQuery;

public class QueryBizOrderDAOImpl implements QueryBizOrderDAO {
	@Resource
	private SqlMapClient querySqlMapClient;

	@SuppressWarnings("unchecked")
	@Override
	public List<BizOrder> queryByPage(BizOrderQuery bizOrderQuery) throws SQLException {
		int count = (Integer) querySqlMapClient.queryForObject("biz_order.queryByPageCount",
				bizOrderQuery);
		bizOrderQuery.setTotalItem(count);
		return (List<BizOrder>) querySqlMapClient.queryForList("biz_order.queryByPage",
				bizOrderQuery);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BizOrder> queryByExport(BizOrderQuery bizOrderQuery) throws SQLException {
		return (List<BizOrder>) querySqlMapClient.queryForList("biz_order.queryByExport",
				bizOrderQuery);
	}

	@Override
	public Integer queryCount(BizOrderQuery bizOrderQuery) throws SQLException {
		Integer count = (Integer) querySqlMapClient.queryForObject("biz_order.queryByPageCount",
				bizOrderQuery);
		return count;
	}

	@Override
	public Long queryCostPriceCount(BizOrderQuery bizOrderQuery) throws SQLException {
		Long count = (Long) querySqlMapClient.queryForObject("biz_order.queryCostPriceCount",
				bizOrderQuery);
		if (count == null) {
			count = 0L;
		}
		return count;
	}
}