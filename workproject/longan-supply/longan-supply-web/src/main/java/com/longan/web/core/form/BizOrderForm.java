package com.longan.web.core.form;

import java.util.Date;

import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

import com.longan.biz.domain.QueryBase;

public class BizOrderForm extends QueryBase {
	private static final long serialVersionUID = 1L;
	private Date startGmtCreate;
	private Date endGmtCreate;
	private Integer status;
	@RegExp(value = "([1-9]\\d+)?", message = "手机号输入错误")
	private String itemUid;
	private Long userId;
	private Integer bizId;
	@RegExp(value = "(\\d+)?", message = "订单号输入错误")
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getBizId() {
		return bizId;
	}

	public void setBizId(Integer bizId) {
		this.bizId = bizId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getItemUid() {
		return itemUid;
	}

	public void setItemUid(String itemUid) {
		this.itemUid = itemUid;
	}

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

}
