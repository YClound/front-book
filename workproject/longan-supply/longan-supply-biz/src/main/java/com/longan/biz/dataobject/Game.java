package com.longan.biz.dataobject;

import java.util.Date;

import com.longan.biz.utils.Constants;

public class Game {
	private Long id;
	private Date gmtCreate;
	private Date gmtModify;
	private Long carrierId;
	private String officialUrl;
	private String gameName;
	private String logo;
	private String gameDesc;
	private Integer status;
	private String keyIndex;
	private Integer bizId;
	private String carrierName;

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

	public Long getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Long carrierId) {
		this.carrierId = carrierId;
	}

	public String getOfficialUrl() {
		return officialUrl;
	}

	public void setOfficialUrl(String officialUrl) {
		this.officialUrl = officialUrl;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getGameDesc() {
		return gameDesc;
	}

	public void setGameDesc(String gameDesc) {
		this.gameDesc = gameDesc;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getKeyIndex() {
		return keyIndex;
	}

	public void setKeyIndex(String keyIndex) {
		this.keyIndex = keyIndex;
	}

	public Integer getBizId() {
		return bizId;
	}

	public void setBizId(Integer bizId) {
		this.bizId = bizId;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public boolean showRed() {
		if (status == null) {
			return false;
		}
		return Constants.Game.STATUS_CANCEL == status;
	}

	public boolean showGreen() {
		if (status == null) {
			return false;
		}
		return Constants.Game.STATUS_NORMAL == status;
	}

	public String getStatusDesc() {
		String result = null;
		if (status == null) {
			return null;
		}
		if (Constants.Game.STATUS_NORMAL == status) {
			result = Constants.Game.STATUS_NORMAL_DESC;
		} else if (Constants.Game.STATUS_CANCEL == status) {
			result = Constants.Game.STATUS_CANCEL_DESC;
		}
		return result;
	}
}
