package com.longan.client.remote.domain.web;

public class BaseBuy {
	private Long userId;

	private Integer supplyWay;

	private String uid;

	private String otherSerialNO;

	public String getOtherSerialNO() {
		return otherSerialNO;
	}

	public void setOtherSerialNO(String otherSerialNO) {
		this.otherSerialNO = otherSerialNO;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getSupplyWay() {
		return supplyWay;
	}

	public void setSupplyWay(Integer supplyWay) {
		this.supplyWay = supplyWay;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
