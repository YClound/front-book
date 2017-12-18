package com.longan.web.core.form;

import java.util.Date;

import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

import com.longan.biz.domain.QueryBase;

public class AcctLogForm extends QueryBase {
	private static final long serialVersionUID = 1L;
	private Date startGmtCreate;
	private Date endGmtCreate;
	private Long userId;
	private Integer billType;
	@RegExp(value = "(\\d+)?", message = "交易号输入错误")
	private String billId;
	private Integer tradeType;
	@RegExp(value = "(\\d+)?", message = "资金流水号输入错误")
	private String id;
	@RegExp(value = "(\\d+)?", message = "订单号输入错误")
	private String bizOrderId;

	public Date getStartGmtCreate() {
		return startGmtCreate;
	}

	public void setStartGmtCreate(Date startGmtCreate) {
		this.startGmtCreate = startGmtCreate;
	}

	public Date getEndGmtCreate() {
		return endGmtCreate;
	}

	public void setEndGmtCreate(Date endGmtCreate) {
		this.endGmtCreate = endGmtCreate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBizOrderId() {
		return bizOrderId;
	}

	public void setBizOrderId(String bizOrderId) {
		this.bizOrderId = bizOrderId;
	}

}
