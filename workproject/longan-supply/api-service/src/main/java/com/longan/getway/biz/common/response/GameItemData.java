package com.longan.getway.biz.common.response;

public class GameItemData extends BaseData {
	private Integer id;
	private Integer gameId;
	private String itemName;
	private Integer itemFacePrice;
	private String itemUnit;
	private String exchargeRatio;
	private String numberList;
	private Integer itemSalesPrice;
	private Integer bizId;
	private Integer status;

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

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	public String getExchargeRatio() {
		return exchargeRatio;
	}

	public void setExchargeRatio(String exchargeRatio) {
		this.exchargeRatio = exchargeRatio;
	}

	public String getNumberList() {
		return numberList;
	}

	public void setNumberList(String numberList) {
		this.numberList = numberList;
	}

}
