package com.longan.getway.upstream.game.vo;

public class DianDianCallbackVO {
	private String coopId;
	private String tranId;
	private String orderNo;
	private String orderStatus;
	private String orderSuccessTime;
	private String failedCode;
	private String failedReason;
	private String sign;

	public String getCoopId() {
		return coopId;
	}

	public void setCoopId(String coopId) {
		this.coopId = coopId;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderSuccessTime() {
		return orderSuccessTime;
	}

	public void setOrderSuccessTime(String orderSuccessTime) {
		this.orderSuccessTime = orderSuccessTime;
	}

	public String getFailedCode() {
		return failedCode;
	}

	public void setFailedCode(String failedCode) {
		this.failedCode = failedCode;
	}

	public String getFailedReason() {
		return failedReason;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public boolean checkSign() {
		return true;
	}
}
