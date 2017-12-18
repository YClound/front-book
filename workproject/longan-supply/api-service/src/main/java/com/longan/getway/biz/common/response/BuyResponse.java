package com.longan.getway.biz.common.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class BuyResponse extends Response {
	private String serialno;
	private String bizOrderId;
	private String itemId;
	private String itemName;
	private String itemFacePrice;
	private String price;
	private String amount;
	private String carrierType;
	private String areaCode;

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getBizOrderId() {
		return bizOrderId;
	}

	public void setBizOrderId(String bizOrderId) {
		this.bizOrderId = bizOrderId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemFacePrice() {
		return itemFacePrice;
	}

	public void setItemFacePrice(String itemFacePrice) {
		this.itemFacePrice = itemFacePrice;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCarrierType() {
		return carrierType;
	}

	public void setCarrierType(String carrierType) {
		this.carrierType = carrierType;
	}
	
	

}
