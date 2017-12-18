package com.longan.biz.dataobject;

import java.util.Date;
import java.util.List;

import com.longan.biz.domain.QueryBase;

public class BizOrderQuery extends QueryBase {
	private static final long serialVersionUID = 1L;
	private Date startGmtCreate;
	private Date endGmtCreate;
	private Integer status;
	private Integer channel;
	private Integer itemId;
	private String itemUid;
	private Long payOrderId;
	private Long userId;
	private Integer bizId;
	private Long id;
	private Long itemSupplyId;
	private Long stockId;
	private Long lockOperId;
	private Long dealOperId;
	private String downstreamSerialno;
	private String itemExt1;
	private Long upstreamId;
	private String itemExt2;
	private String provinceCode;
	private Integer lessCostTime;
	private Integer moreCostTime;
	private Integer supplyType;
	private Integer carrierType;
	private List<Integer> statusList;

	public Long getItemSupplyId() {
		return itemSupplyId;
	}

	public void setItemSupplyId(Long itemSupplyId) {
		this.itemSupplyId = itemSupplyId;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getBizId() {
		return bizId;
	}

	public void setBizId(Integer bizId) {
		this.bizId = bizId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
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

	public Long getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(Long payOrderId) {
		this.payOrderId = payOrderId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

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

	public String getDownstreamSerialno() {
		return downstreamSerialno;
	}

	public void setDownstreamSerialno(String downstreamSerialno) {
		this.downstreamSerialno = downstreamSerialno;
	}

	public String getItemExt1() {
		return itemExt1;
	}

	public void setItemExt1(String itemExt1) {
		this.itemExt1 = itemExt1;
	}

	public Long getUpstreamId() {
		return upstreamId;
	}

	public void setUpstreamId(Long upstreamId) {
		this.upstreamId = upstreamId;
	}

	public String getItemExt2() {
		return itemExt2;
	}

	public void setItemExt2(String itemExt2) {
		this.itemExt2 = itemExt2;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
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

	public Integer getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(Integer supplyType) {
		this.supplyType = supplyType;
	}

	public Integer getCarrierType() {
		return carrierType;
	}

	public void setCarrierType(Integer carrierType) {
		this.carrierType = carrierType;
	}

	public List<Integer> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<Integer> statusList) {
		this.statusList = statusList;
	}

}
