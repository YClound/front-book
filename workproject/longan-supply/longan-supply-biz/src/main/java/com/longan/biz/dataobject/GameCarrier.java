package com.longan.biz.dataobject;

import java.util.Date;

import com.longan.biz.utils.Constants;

public class GameCarrier {
	private Long id;
	private Date gmtCreate;
	private Date gmtModify;
	private String carrierName;
	private Integer bizId;
	private String carrierDesc;
	private Integer status;

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

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public Integer getBizId() {
		return bizId;
	}

	public void setBizId(Integer bizId) {
		this.bizId = bizId;
	}

	public String getCarrierDesc() {
		return carrierDesc;
	}

	public void setCarrierDesc(String carrierDesc) {
		this.carrierDesc = carrierDesc;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public boolean showRed() {
		if (status == null) {
			return false;
		}
		return Constants.GameCarrier.STATUS_CANCEL == status;
	}

	public boolean showGreen() {
		if (status == null) {
			return false;
		}
		return Constants.GameCarrier.STATUS_NORMAL == status;
	}

	public String getStatusDesc() {
		String result = null;
		if (status == null) {
			return null;
		}
		if (Constants.GameCarrier.STATUS_NORMAL == status) {
			result = Constants.GameCarrier.STATUS_NORMAL_DESC;
		} else if (Constants.GameCarrier.STATUS_CANCEL == status) {
			result = Constants.GameCarrier.STATUS_CANCEL_DESC;
		}
		return result;
	}
}
