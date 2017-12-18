package com.longan.getway.upstream.telephone.vo;

public class DingChiCallbackVO {
	private String userId;
	private String bizId;
	private String id;
	private String downstreamSerialno;
	private String status;
	private String sign;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDownstreamSerialno() {
		return downstreamSerialno;
	}

	public void setDownstreamSerialno(String downstreamSerialno) {
		this.downstreamSerialno = downstreamSerialno;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
