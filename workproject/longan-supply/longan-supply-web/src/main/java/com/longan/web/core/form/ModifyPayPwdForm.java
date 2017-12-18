package com.longan.web.core.form;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

public class ModifyPayPwdForm {
	@NotNull(message = "旧支付密码不能为空")
	@NotBlank(message = "旧支付密码不能为空")
	private String oldPayPwd;

	@NotNull(message = "新支付密码不能为空")
	@NotBlank(message = "新支付密码不能为空")
	private String newPayPwd;

	private String payPwd;// 这里不做校验,action里做

	public String getOldPayPwd() {
		return oldPayPwd;
	}

	public void setOldPayPwd(String oldPayPwd) {
		this.oldPayPwd = oldPayPwd;
	}

	public String getNewPayPwd() {
		return newPayPwd;
	}

	public void setNewPayPwd(String newPayPwd) {
		this.newPayPwd = newPayPwd;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

}
