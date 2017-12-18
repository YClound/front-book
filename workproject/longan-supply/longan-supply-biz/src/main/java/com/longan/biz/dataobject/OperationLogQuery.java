package com.longan.biz.dataobject;

import java.util.Date;

import com.longan.biz.domain.QueryBase;

public class OperationLogQuery extends QueryBase{
	private static final long serialVersionUID = 1L;
	private Date startGmtCreate;
	private Date endGmtCreate;
	private String moduleName;
	private Long userId;
	private Integer bizId;
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
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getBizId() {
		return bizId;
	}
	public void setBizId(Integer bizId) {
		this.bizId = bizId;
	}
	
}
