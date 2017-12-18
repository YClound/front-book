package com.longan.mng.form;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

public class UserInfoForm {

	private String id;

	@NotBlank(message = "登录名不能为空")
	@RegExp(value = "[0-9a-zA-Z]{3,14}", message = "登录名必须是3到14位数字或者字母")
	private String loginId;

	@NotBlank(message = "用户名不能为空")
	@RegExp(value = "[\u4e00-\u9fa5\\w]{2,20}", message = "用户名必须是2到20位非特殊字符")
	private String userName;
	@NotBlank(message = "公司信息不能为空")
	@RegExp(value = "[\u4e00-\u9fa5\\w]{3,30}", message = "公司信息必须是3到30位非特殊字符")
	private String compayInfo;

	@RegExp(value = "(\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?)?", message = "非法邮箱")
	private String email;
	@NotBlank(message = "绑定手机号不能为空")
	@RegExp(value = "[0-9a-zA-Z]{11,12}", message = "非法手机号")
	private String mobile;

	private String addr;

	private String area;

	@RegExp(value = "[0-9a-zA-Z]{6,16}", message = "密码必须是6到16位")
	private String pwd;

	private String referer;
	private Integer status;
	@NotBlank(message = "用户类型不能为空")
	private String type;
	// 商户信息
	private String callbackUrl;

	private String supplyType;

	private Boolean isAsyncSupply;

	@RegExp(value = "([1-9][\\d]{0,8})?", message = "阀值限定应该为数字")
	private String chargingLimit;

	private Integer traderInoStatus;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(String supplyType) {
		this.supplyType = supplyType;
	}

	public Boolean getIsAsyncSupply() {
		return isAsyncSupply;
	}

	public void setIsAsyncSupply(Boolean isAsyncSupply) {
		this.isAsyncSupply = isAsyncSupply;
	}

	public String getChargingLimit() {
		return chargingLimit;
	}

	public void setChargingLimit(String chargingLimit) {
		this.chargingLimit = chargingLimit;
	}

	public Integer getTraderInoStatus() {
		return traderInoStatus;
	}

	public void setTraderInoStatus(Integer traderInoStatus) {
		this.traderInoStatus = traderInoStatus;
	}

}
