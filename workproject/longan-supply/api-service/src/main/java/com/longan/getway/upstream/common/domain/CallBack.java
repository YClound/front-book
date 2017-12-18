package com.longan.getway.upstream.common.domain;

import com.longan.getway.core.domain.SupplyResult;

public class CallBack {
	private Long supplyOrderId; // 统一转化成我方供货单编号;
	private Integer status; // 状态 已转化成我方的订单状态
	private String failedCode; // 上游失败编号
	private String failedMsg; // 上游失败原因
	private String tradeTime; // 交易成功时间
	private Integer actualCost; // 上游实际扣款金额 单位厘 这里不给下游的

	public Long getSupplyOrderId() {
		return supplyOrderId;
	}

	public void setSupplyOrderId(Long supplyOrderId) {
		this.supplyOrderId = supplyOrderId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFailedCode() {
		return failedCode;
	}

	public void setFailedCode(String failedCode) {
		this.failedCode = failedCode;
	}

	public String getFailedMsg() {
		return failedMsg;
	}

	public void setFailedMsg(String failedMsg) {
		this.failedMsg = failedMsg;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public Integer getActualCost() {
		return actualCost;
	}

	public void setActualCost(Integer actualCost) {
		this.actualCost = actualCost;
	}

	public boolean isSuccess() {
		if (status == null) {
			return false;
		}
		return status == SupplyResult.STATUS_SUCCESS;
	}

	public boolean isFaild() {
		if (status == null) {
			return false;
		}
		return status == SupplyResult.STATUS_FAILED;
	}

	public boolean isCardValied() {
		return status == SupplyResult.STATUS_CARD_INVALID;
	}

	public boolean isUnconfirm() {
		if (status == null) {
			return true;
		}
		return status != SupplyResult.STATUS_SUCCESS && status != SupplyResult.STATUS_FAILED
				&& status != SupplyResult.STATUS_CARD_INVALID;
	}

	/**
	 * 生命周期是否结束
	 * 
	 * @return
	 */
	public boolean isOver() {
		if (status == null) {
			return false;
		}
		return status == SupplyResult.STATUS_CARD_INVALID || status == SupplyResult.STATUS_FAILED
				|| status == SupplyResult.STATUS_SUCCESS;
	}

}
