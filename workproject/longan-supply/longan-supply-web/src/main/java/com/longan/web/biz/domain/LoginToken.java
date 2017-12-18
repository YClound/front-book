package com.longan.web.biz.domain;

import java.io.Serializable;
import java.util.UUID;

import com.longan.biz.utils.AESUtils;

public class LoginToken implements Serializable {
	private static final long serialVersionUID = 1L;

	private String ttUid;
	private String token = AESUtils.createKey();
	private Long ts = System.currentTimeMillis(); // salt给登录密码加点盐，保证每次加密后不一样。

	public Long resetTs() {
		ts = System.currentTimeMillis();
		return ts;
	}

	public String resetToken() {
		token = AESUtils.createKey();
		return token;
	}

	public void createTtUid() {
		ttUid = UUID.randomUUID().toString().replace("-", "");
	}

	public String getTtUid() {
		return ttUid;
	}

	public void setTtUid(String ttUid) {
		this.ttUid = ttUid;
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

}
