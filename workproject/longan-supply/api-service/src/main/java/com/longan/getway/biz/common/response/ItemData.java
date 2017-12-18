package com.longan.getway.biz.common.response;

public class ItemData extends BaseData {
	private Integer id;
	private String itemName;
	private Integer itemFacePrice;
	private Integer itemSalesPrice;
	private Integer bizId;
	private Integer status;
	private String statusDesc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemFacePrice() {
		return itemFacePrice;
	}

	public void setItemFacePrice(Integer itemFacePrice) {
		this.itemFacePrice = itemFacePrice;
	}

	public Integer getItemSalesPrice() {
		return itemSalesPrice;
	}

	public void setItemSalesPrice(Integer itemSalesPrice) {
		this.itemSalesPrice = itemSalesPrice;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getBizId() {
		return bizId;
	}

	public void setBizId(Integer bizId) {
		this.bizId = bizId;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	
}
