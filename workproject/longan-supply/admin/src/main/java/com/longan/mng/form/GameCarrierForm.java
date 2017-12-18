package com.longan.mng.form;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

public class GameCarrierForm {
	private String id;
	@NotBlank(message = "厂商名不能为空")
	@RegExp(value = "[\u4e00-\u9fa5\\w]{1,20}", message = "厂商名不能含特殊字符")
	private String carrierName;
	private String bizId;
	private String carrierDesc;
	private Integer status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
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

}
