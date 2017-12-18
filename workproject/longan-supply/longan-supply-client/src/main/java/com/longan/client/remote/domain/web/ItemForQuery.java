package com.longan.client.remote.domain.web;

import java.io.Serializable;

import com.longan.biz.utils.BigDecimalUtils;

public class ItemForQuery implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String itemName;

	private Integer itemFacePrice;

	private String itemFacePriceDesc;

	private String itemUnit;

	private Integer itemSalesPrice;

	private String itemSalesPriceDesc;

	private Integer bizId;

	private Integer itemType;

	private Integer status;

	private Integer carrierType;

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

	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	public Integer getItemSalesPrice() {
		return itemSalesPrice;
	}

	public void setItemSalesPrice(Integer itemSalesPrice) {
		this.itemSalesPrice = itemSalesPrice;
	}

	public Integer getBizId() {
		return bizId;
	}

	public void setBizId(Integer bizId) {
		this.bizId = bizId;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCarrierType() {
		return carrierType;
	}

	public void setCarrierType(Integer carrierType) {
		this.carrierType = carrierType;
	}

	public String getItemFacePriceDesc() {
		return itemFacePriceDesc;
	}

	public void setItemFacePriceDesc(String itemFacePriceDesc) {
		this.itemFacePriceDesc = itemFacePriceDesc;
	}

	public String getItemSalesPriceDesc() {
		return itemSalesPriceDesc;
	}

	public void setItemSalesPriceDesc(String itemSalesPriceDesc) {
		this.itemSalesPriceDesc = itemSalesPriceDesc;
	}

	public void initPriceDesc() {
		if (itemSalesPrice != null) {
			Double price = BigDecimalUtils.doubleDiveid(String.valueOf(itemSalesPrice));
			setItemSalesPriceDesc(price.toString());
		}

		if (itemFacePrice != null) {
			Double price = BigDecimalUtils.doubleDiveid(String.valueOf(itemFacePrice));
			setItemFacePriceDesc(price.toString());
		}
	}

}
