package com.longan.getway.upstream.flow.vo;

public class YuanMaiFlowCallbackVO {
	private String orderid;
	private String status;
	private String serialid;
	private String finishflowsize;
	private String orderamount;
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

	public String getSerialid() {
		return serialid;
	}

	public void setSerialid(String serialid) {
		this.serialid = serialid;
	}

	public String getFinishflowsize() {
		return finishflowsize;
	}

	public void setFinishflowsize(String finishflowsize) {
		this.finishflowsize = finishflowsize;
	}

	public String getOrderamount() {
		return orderamount;
	}

	public void setOrderamount(String orderamount) {
		this.orderamount = orderamount;
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
