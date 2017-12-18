package com.longan.getway.upstream.telecomflow.vo;

public class SuKaTelecomFlowCallbackVO {
	private String userid;
	private String orderid;
	private String sporderid;
	private String merchantsubmittime;
	private String resultno;
	private String sign;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getSporderid() {
		return sporderid;
	}

	public void setSporderid(String sporderid) {
		this.sporderid = sporderid;
	}

	public String getMerchantsubmittime() {
		return merchantsubmittime;
	}

	public void setMerchantsubmittime(String merchantsubmittime) {
		this.merchantsubmittime = merchantsubmittime;
	}

	public String getResultno() {
		return resultno;
	}

	public void setResultno(String resultno) {
		this.resultno = resultno;
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
