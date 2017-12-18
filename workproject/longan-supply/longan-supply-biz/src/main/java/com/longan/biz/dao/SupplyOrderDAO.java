package com.longan.biz.dao;

import java.sql.SQLException;
import java.util.List;

import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.dataobject.SupplyOrderQuery;

public interface SupplyOrderDAO {
	Long insert(SupplyOrder supplyOrder) throws SQLException;

	List<SupplyOrder> queryByPage(SupplyOrderQuery SupplyOrderQuery) throws SQLException;

	int updateSupplyOrder(SupplyOrder supplyOrder) throws SQLException;

	SupplyOrder getSupplyOrderById(Long supplyOrderId) throws SQLException;

	SupplyOrder selectSupplyOrderById(Long id) throws SQLException;

	Integer queryCount(SupplyOrderQuery supplyOrderQuery) throws SQLException;

	boolean updateSupplyOrderCheckStatus(SupplyOrder supplyOrder, List<Integer> statusList)
			throws SQLException;

	public List<SupplyOrder> queryByExport(SupplyOrderQuery supplyOrderQuery) throws SQLException;

	public int lockSupplyOrder(SupplyOrder supplyOrder) throws SQLException;

	public int unLockSupplyOrder(SupplyOrder supplyOrder) throws SQLException;

	public int cancelSupplyOrder(SupplyOrder supplyOrder) throws SQLException;

	public int confirmSupplyOrder(SupplyOrder supplyOrder) throws SQLException;

	public int unConfirmSupplyOrder(SupplyOrder supplyOrder) throws SQLException;

}
