package com.longan.web.core.form;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

public class UserRegisterForm extends BaseUserForm {
	@NotBlank(message = "登录名不能为空")
	@RegExp(value = "1[3578]\\d{9}", message = "登录名必须是正确的手机号")
	private String loginId;
	@RegExp(value = "(\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?)?", message = "邮箱格式不正确")
	private String email;
	@NotBlank(message = "用户昵称不能为空")
	@RegExp(value = "[\u4e00-\u9fa5\\w]{2,20}", message = "用户昵称必须是2到20位非特殊字符")
	private String userName;
	@NotBlank(message = "登录密码不能为空")
	private String pwd;
	@NotBlank(message = "门店或公司名不能为空")
	@RegExp(value = "[\u4e00-\u9fa5\\w]{3,30}", message = "门店或公司名必须是3到30位非特殊字符")
	private String compayInfo;
	@NotBlank(message = "联系地址不能为空")
	private String addr;

	@NotBlank(message = "短信验证码不能为空")
	@RegExp(value = "^\\d{6}$", message = "短信验证码必须是6位数字")
	private String authCode;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getCompayInfo() {
		return compayInfo;
	}

	public void setCompayInfo(String compayInfo) {
		this.compayInfo = compayInfo;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

}
