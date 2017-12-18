package com.longan.getway.upstream.telephone.vo;

public class YuanMaiCallbackVO {
	private String orderid;
	private String status;
	private String ordermoney;
	private String sign;

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrdermoney() {
		return ordermoney;
	}

	public void setOrdermoney(String ordermoney) {
		this.ordermoney = ordermoney;
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
