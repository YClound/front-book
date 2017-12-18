package com.longan.getway.biz.common.response;

public class GameCarrierData extends BaseData {
	private Long id;
	private String carrierName;
	private String carrierDesc;
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
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
