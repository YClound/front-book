package com.longan.biz.domain;

public class SupplyOrderStatisic {
	private Integer chargingCount = 0;
	private Integer unComfirmCount = 0;
	private Integer successCount = 0;
	private Integer failedCount = 0;
	private Integer exceptionCount = 0;
	
	private Integer lockedCount = 0;

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

	public Integer getLockedCount() {
		return lockedCount;
	}

	public void setLockedCount(Integer lockedCount) {
		this.lockedCount = lockedCount;
	}
	
	
}
