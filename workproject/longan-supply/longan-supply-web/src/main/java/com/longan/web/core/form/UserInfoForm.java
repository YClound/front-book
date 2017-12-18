package com.longan.web.core.form;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

public class UserInfoForm {
	@NotBlank(message = "用户名不能为空")
	@RegExp(value = "[\u4e00-\u9fa5\\w]{2,20}", message = "用户名必须是2到20位非特殊字符")
	private String userName;
	@NotBlank(message = "门店或公司名不能为空")
	@RegExp(value = "[\u4e00-\u9fa5\\w]{3,30}", message = "门店或公司名必须是3到30位非特殊字符")
	private String compayInfo;

	@RegExp(value = "(\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?)?", message = "非法邮箱")
	private String email;

	@RegExp(value = "[0-9a-zA-Z]{6,16}", message = "密码必须是6到16位")
	private String pwd;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCompayInfo() {
		return compayInfo;
	}

	public void setCompayInfo(String compayInfo) {
		this.compayInfo = compayInfo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
