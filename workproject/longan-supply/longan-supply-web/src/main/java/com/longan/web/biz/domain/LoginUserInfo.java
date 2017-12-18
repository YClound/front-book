package com.longan.web.biz.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.longan.biz.dataobject.UserInfo;

public class LoginUserInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private UserInfo userInfo;

	private Map<String, Object> attributes = new HashMap<String, Object>();

	private String sessionId;

	private String remoteIp;

	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	public Object getAttribut(String key) {
		return attributes.get(key);
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

}
