package com.longan.getway.upstream.telephone.vo;

public class SanTongCallbackVO {
	private String orderId;
	private String esaleOrderId;
	private String chargeStatus;
	private String finishMoney;
	private String finishTime;
	private String SIGN;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getEsaleOrderId() {
		return esaleOrderId;
	}

	public void setEsaleOrderId(String esaleOrderId) {
		this.esaleOrderId = esaleOrderId;
	}

	public String getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(String chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

	public String getFinishMoney() {
		return finishMoney;
	}

	public void setFinishMoney(String finishMoney) {
		this.finishMoney = finishMoney;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getSIGN() {
		return SIGN;
	}

	public void setSIGN(String sIGN) {
		SIGN = sIGN;
	}

	public boolean checkSign() {
		return true;
	}
}
