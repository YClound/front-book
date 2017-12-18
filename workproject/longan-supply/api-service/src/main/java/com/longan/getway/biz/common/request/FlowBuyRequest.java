package com.longan.getway.biz.common.request;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

public class FlowBuyRequest extends Request {
	@NotBlank(message = "充值流量不能为空")
	@NotNull(message = "充值流量不能为空")
	private String flow;

	@NotBlank(message = "充值流量类型不能为空")
	@NotNull(message = "充值流量类型不能为空")
	private String flowType;

	@RegExp(value = "^\\d{1,4}$", message = "购买数量必须是1到4位数字")
	private String amt;

	private String ext1;
	private String ext2;

	@NotBlank(message = "客户手机号不能为空")
	@NotNull(message = "客户手机号不能为空")
	@RegExp(value = "^\\d{1,11}$", message = "手机号不正确")
	private String uid;

	@NotBlank(message = "合作方商户的流水号不能为空")
	@NotNull(message = "合作方商户的流水号不能为空")
	private String serialno;

	private String dtCreate;

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getDtCreate() {
		return dtCreate;
	}

	public void setDtCreate(String dtCreate) {
		this.dtCreate = dtCreate;
	}

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public String getFlowType() {
		return flowType;
	}

	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}

}
