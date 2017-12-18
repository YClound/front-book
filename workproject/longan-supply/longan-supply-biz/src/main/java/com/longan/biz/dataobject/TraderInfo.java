package com.longan.biz.dataobject;

import com.longan.biz.utils.Constants;

public class TraderInfo {
	private Long id;

	private Long userId;

	private Integer supplyType;

	private Boolean isAsyncSupply;

	private String callbackUrl;

	private String downstreamKey;

	private Integer traderType;

	private Integer status;

	private Integer chargingLimit;

	private Integer maxDay;

	private Integer maxMounth;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(Integer supplyType) {
		this.supplyType = supplyType;
	}

	public Boolean getIsAsyncSupply() {
		return isAsyncSupply;
	}

	public void setIsAsyncSupply(Boolean isAsyncSupply) {
		this.isAsyncSupply = isAsyncSupply;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getDownstreamKey() {
		return downstreamKey;
	}

	public void setDownstreamKey(String downstreamKey) {
		this.downstreamKey = downstreamKey;
	}

	public Integer getTraderType() {
		return traderType;
	}

	public void setTraderType(Integer traderType) {
		this.traderType = traderType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getChargingLimit() {
		return chargingLimit;
	}

	public void setChargingLimit(Integer chargingLimit) {
		this.chargingLimit = chargingLimit;
	}

	public Integer getMaxDay() {
		return maxDay;
	}

	public void setMaxDay(Integer maxDay) {
		this.maxDay = maxDay;
	}

	public Integer getMaxMounth() {
		return maxMounth;
	}

	public void setMaxMounth(Integer maxMounth) {
		this.maxMounth = maxMounth;
	}

	public String getSupplyWayDesc() {
		String result = null;
		if (isAsyncSupply == null) {
			return null;
		}
		if (isAsyncSupply) {
			result = Constants.TraderInfo.SUPPLY_WAY_ASYNC_DESC;
		} else {
			result = Constants.TraderInfo.SUPPLY_WAY_SYNC_DESC;
		}
		return result;
	}

	public boolean isManualSupply() {
		if (supplyType == null) {
			return false;
		}
		return Constants.ItemSupply.TYPE_MAN == supplyType;
	}

	public String getSupplyTypeDesc() {
		String result = null;
		if (supplyType == null) {
			return null;
		}
		if (Constants.ItemSupply.TYPE_CARD_FORWARD_CHARGE == supplyType) {
			result = Constants.ItemSupply.TYPE_CARD_FORWARD_CHARGE_DESC;
		} else if (Constants.ItemSupply.TYPE_DIRECT_CHARGE == supplyType) {
			result = Constants.ItemSupply.TYPE_DIRECT_CHARGE_DESC;
		} else if (Constants.ItemSupply.TYPE_MAN == supplyType) {
			result = Constants.ItemSupply.TYPE_MAN_DESC;
		} else if (Constants.ItemSupply.TYPE_CARD == supplyType) {
			result = Constants.ItemSupply.TYPE_CARD_DESC;
		} else if (Constants.ItemSupply.TYPE_CARD_CHARGE == supplyType) {
			result = Constants.ItemSupply.TYPE_CARD_CHARGE_DESC;
		}
		return result;
	}

}