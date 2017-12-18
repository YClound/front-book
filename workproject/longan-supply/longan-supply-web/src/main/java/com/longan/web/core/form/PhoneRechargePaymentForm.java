package com.longan.web.core.form;

public class PhoneRechargePaymentForm extends BasePayForm {
	private String uid;
	private String itemId;
	private String price;
	private String moblieCarrieName;
	private String mobileProvinceName;
	private String itemFacePrice;
	private String priceDesc;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getMoblieCarrieName() {
		return moblieCarrieName;
	}

	public void setMoblieCarrieName(String moblieCarrieName) {
		this.moblieCarrieName = moblieCarrieName;
	}

	public String getMobileProvinceName() {
		return mobileProvinceName;
	}

	public void setMobileProvinceName(String mobileProvinceName) {
		this.mobileProvinceName = mobileProvinceName;
	}

	public String getItemFacePrice() {
		return itemFacePrice;
	}

	public void setItemFacePrice(String itemFacePrice) {
		this.itemFacePrice = itemFacePrice;
	}

	public String getPriceDesc() {
		return priceDesc;
	}

	public void setPriceDesc(String priceDesc) {
		this.priceDesc = priceDesc;
	}

}
