package com.longan.web.core.form;

import com.longan.biz.utils.Constants;

public class FlowRechargePaymentForm extends BasePayForm {
	private String uid;
	private String itemId;
	private String price;
	private String moblieCarrieName;
	private String mobileProvinceName;
	private String itemFacePrice;
	private String priceDesc;
	private String useableTypeDesc;
	private String flow;
	private String usableArea;

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

	public String getUseableTypeDesc() {
		return useableTypeDesc;
	}

	public void setUseableTypeDesc(String useableTypeDesc) {
		this.useableTypeDesc = useableTypeDesc;
	}

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public String getUsableArea() {
		return usableArea;
	}

	public void setUsableArea(String usableArea) {
		this.usableArea = usableArea;
	}

	public String getUsableAreaDesc() {
		if ((Constants.BizFlow.ITEM_USABLE_NATIONWIDE + "").equals(usableArea)) {
			return Constants.BizFlow.ITEM_USABLE_NATIONWIDE_DESC;
		} else if ((Constants.BizFlow.ITEM_USABLE_AREA + "").equals(usableArea)) {
			return Constants.BizFlow.ITEM_USABLE_AREA_DESC;
		}
		return null;
	}

}
