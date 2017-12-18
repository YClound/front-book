package com.longan.biz.domain;

import com.longan.biz.utils.BigDecimalUtils;

public class BizOrderStatisic {
	private Integer chargingCount = 0;
	private Integer unComfirmCount = 0;
	private Integer successCount = 0;
	private Integer failedCount = 0;
	private Integer exceptionCount = 0;
	private Long costPriceCount = 0L;

	public Integer getChargingCount() {
		return chargingCount;
	}

	public void setChargingCount(Integer chargingCount) {
		this.chargingCount = chargingCount;
	}

	public Integer getUnComfirmCount() {
		return unComfirmCount;
	}

	public void setUnComfirmCount(Integer unComfirmCount) {
		this.unComfirmCount = unComfirmCount;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Integer getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(Integer failedCount) {
		this.failedCount = failedCount;
	}

	public Integer getExceptionCount() {
		return exceptionCount;
	}

	public void setExceptionCount(Integer exceptionCount) {
		this.exceptionCount = exceptionCount;
	}

	public Long getCostPriceCount() {
		return costPriceCount;
	}

	public void setCostPriceCount(Long costPriceCount) {
		this.costPriceCount = costPriceCount;
	}

	public Double getCostPriceCountDesc() {
		if (costPriceCount == null) {
			return null;
		}
		return BigDecimalUtils.doubleDiveid(String.valueOf(costPriceCount));
	}
}
