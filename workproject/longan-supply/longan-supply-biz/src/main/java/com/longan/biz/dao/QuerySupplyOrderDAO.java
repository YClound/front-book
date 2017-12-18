package com.longan.biz.dao;

import java.sql.SQLException;
import java.util.List;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.dataobject.SupplyOrderQuery;

public interface QuerySupplyOrderDAO {

	List<SupplyOrder> queryByPage(SupplyOrderQuery supplyOrderQuery) throws SQLException;

	public List<SupplyOrder> queryByExport(SupplyOrderQuery supplyOrderQuery) throws SQLException;

	public Integer queryCount(SupplyOrderQuery supplyOrderQuery) throws SQLException;

}
