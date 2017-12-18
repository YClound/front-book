package com.longan.biz.dataobject;

import java.util.Date;
import java.util.List;

import com.longan.biz.domain.QueryBase;

public class SupplyOrderQuery extends QueryBase {
	private static final long serialVersionUID = 1L;
	private Date startGmtCreate;
	private Date endGmtCreate;
	private Integer supplyStatus;
	private Integer itemId;
	private String itemUid;
	private Long userId;
	private Integer bizId;
	private Long id;
	private Long supplyTraderId;
	private Integer lessCostTime;
	private Integer moreCostTime;
	private Long bizOrderId;
	private String upstreamSerialno;
	private Long lockOperId;
	private Long dealOperId;
	private Integer supplyType;
	private List<Integer> statusList;

	public Date getStartGmtCreate() {
		return startGmtCreate;
	}

	public void setStartGmtCreate(Date startGmtCreate) {
		this.startGmtCreate = startGmtCreate;
	}

	public Date getEndGmtCreate() {
		return endGmtCreate;
	}

	public void setEndGmtCreate(Date endGmtCreate) {
		this.endGmtCreate = endGmtCreate;
	}

	public Integer getSupplyStatus() {
		return supplyStatus;
	}

	public void setSupplyStatus(Integer supplyStatus) {
		this.supplyStatus = supplyStatus;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemUid() {
		return itemUid;
	}

	public void setItemUid(String itemUid) {
		this.itemUid = itemUid;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getBizId() {
		return bizId;
	}

	public void setBizId(Integer bizId) {
		this.bizId = bizId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSupplyTraderId() {
		return supplyTraderId;
	}

	public void setSupplyTraderId(Long supplyTraderId) {
		this.supplyTraderId = supplyTraderId;
	}

	public Integer getLessCostTime() {
		return lessCostTime;
	}

	public void setLessCostTime(Integer lessCostTime) {
		this.lessCostTime = lessCostTime;
	}

	public Integer getMoreCostTime() {
		return moreCostTime;
	}

	public void setMoreCostTime(Integer moreCostTime) {
		this.moreCostTime = moreCostTime;
	}

	public Long getBizOrderId() {
		return bizOrderId;
	}

	public void setBizOrderId(Long bizOrderId) {
		this.bizOrderId = bizOrderId;
	}

	public String getUpstreamSerialno() {
		return upstreamSerialno;
	}

	public void setUpstreamSerialno(String upstreamSerialno) {
		this.upstreamSerialno = upstreamSerialno;
	}

	public Long getLockOperId() {
		return lockOperId;
	}

	public void setLockOperId(Long lockOperId) {
		this.lockOperId = lockOperId;
	}

	public Long getDealOperId() {
		return dealOperId;
	}

	public void setDealOperId(Long dealOperId) {
		this.dealOperId = dealOperId;
	}

	public Integer getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(Integer supplyType) {
		this.supplyType = supplyType;
	}

	public List<Integer> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<Integer> statusList) {
		this.statusList = statusList;
	}

}
