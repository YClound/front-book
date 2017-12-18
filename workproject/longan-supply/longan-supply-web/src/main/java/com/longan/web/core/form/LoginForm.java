package com.longan.web.core.form;

public class LoginForm extends BaseUserForm {
	private String loginId;
	private String pwd;
	private String checkCode;
	private String phoneCheckCode;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getPhoneCheckCode() {
		return phoneCheckCode;
	}

	public void setPhoneCheckCode(String phoneCheckCode) {
		this.phoneCheckCode = phoneCheckCode;
	}

}
