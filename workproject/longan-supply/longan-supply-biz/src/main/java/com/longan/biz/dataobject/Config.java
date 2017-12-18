package com.longan.biz.dataobject;

import java.util.Date;

public class Config {
	private Long id;
	private Date gmtCreate;
	private Date gmtModify;
	private String configKey;
	private String configValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModify() {
		return gmtModify;
	}

	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public boolean isSwitchOn() {
		if (configValue == null) {
			return false;
		}
		return configValue.equalsIgnoreCase("on") || configValue.equalsIgnoreCase("true");
	}

	public boolean isSwitchOff() {
		if (configValue == null) {
			return true;
		}
		return !configValue.equalsIgnoreCase("on") && !configValue.equalsIgnoreCase("true");
	}
}
