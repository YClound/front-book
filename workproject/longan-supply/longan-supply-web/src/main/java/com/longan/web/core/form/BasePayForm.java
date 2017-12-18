package com.longan.web.core.form;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

public class BasePayForm {
	@NotBlank(message = "必须输入支付密码")
	private String payPwd;

	@NotBlank(message = "缺少必要参数")
	private String token;

	@NotBlank(message = "缺少必要参数")
	private String ts;

	private String otherSerialNO;

	public String getOtherSerialNO() {
		return otherSerialNO;
	}

	public void setOtherSerialNO(String otherSerialNO) {
		this.otherSerialNO = otherSerialNO;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

}
