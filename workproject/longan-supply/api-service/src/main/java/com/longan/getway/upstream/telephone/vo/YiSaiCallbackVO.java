package com.longan.getway.upstream.telephone.vo;

public class YiSaiCallbackVO {
	private String userNumber;
	private String inOrderNumber;
	private String outOrderNumber;
	private String payResult;
	private String customInfo;
	private String recordKey;
	private String orderType;

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public String getInOrderNumber() {
		return inOrderNumber;
	}

	public void setInOrderNumber(String inOrderNumber) {
		this.inOrderNumber = inOrderNumber;
	}

	public String getOutOrderNumber() {
		return outOrderNumber;
	}

	public void setOutOrderNumber(String outOrderNumber) {
		this.outOrderNumber = outOrderNumber;
	}

	public String getPayResult() {
		return payResult;
	}

	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}

	public String getCustomInfo() {
		return customInfo;
	}

	public void setCustomInfo(String customInfo) {
		this.customInfo = customInfo;
	}

	public String getRecordKey() {
		return recordKey;
	}

	public void setRecordKey(String recordKey) {
		this.recordKey = recordKey;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public boolean checkSign() {
		return true;
	}
}
