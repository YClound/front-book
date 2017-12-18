package com.longan.web.biz.domain;

import java.io.Serializable;

import com.longan.biz.utils.AESUtils;

public class PayToken implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userId;
	private String token = AESUtils.createKey();
	private Long ts;
	private Integer bizId;
	private Long oldTs;

	public void resetTs() {
		oldTs = ts;
		ts = System.currentTimeMillis();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getTs() {
		return ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}

	public Integer getBizId() {
		return bizId;
	}

	public void setBizId(Integer bizId) {
		this.bizId = bizId;
	}

	public Long getOldTs() {
		return oldTs;
	}

	public void setOldTs(Long oldTs) {
		this.oldTs = oldTs;
	}

}
