package com.longan.biz.dataobject;

import java.io.Serializable;
import java.util.Date;

import com.longan.biz.utils.Constants;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String loginId;

	private Long acctId;

	private String userName;

	private String compayInfo;

	private String email;

	private String userInfo;

	private String mobile;

	private String addr;

	private String area;

	private String pwd;

	private String referer;

	private Integer status;

	private Date lastLoginTime;

	private String lastLoginIp;

	private Date gmtCreate;

	private Date gmtModify;

	private Integer type;

	private Integer salesPrice;

	private Integer roleId;

	private String roleDesc;

	private String payPwd;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Long getAcctId() {
		return acctId;
	}

	public void setAcctId(Long acctId) {
		this.acctId = acctId;
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

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModify() {
		return gmtModify;
	}

	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(Integer salesPrice) {
		this.salesPrice = salesPrice;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public boolean isDownStreamUser() {
		if (type == null) {
			return false;
		}
		return type == Constants.UserInfo.TYPE_DOWNSTREAM;
	}

	public boolean isCustomUser() {
		if (type == null) {
			return false;
		}
		return type == Constants.UserInfo.TYPE_CUSTOM;
	}

	public boolean isBusinessUser() {
		if (type == null) {
			return false;
		}
		return type == Constants.UserInfo.TYPE_BUSINESS;
	}

	public boolean canUserTrade() {
		if (type == null) {
			return false;
		}
		return type == Constants.UserInfo.TYPE_DOWNSTREAM
				|| type == Constants.UserInfo.TYPE_BUSINESS
				|| type == Constants.UserInfo.TYPE_CUSTOM;
	}

	public boolean isUpStreamUser() {
		return type == Constants.UserInfo.TYPE_UPSTREAM;
	}

	public boolean isAdmin() {
		return type == Constants.UserInfo.TYPE_ADMIN;
	}

	public boolean showRed() {
		if (status == null) {
			return false;
		}
		return Constants.UserInfo.STATUS_CANCEL == status
				|| Constants.UserInfo.STATUS_DEL == status;
	}

	public boolean showGreen() {
		if (status == null) {
			return false;
		}
		return Constants.UserInfo.STATUS_NORMAL == status;
	}

	public String getStatusDesc() {
		String result = null;
		if (status == null) {
			return null;
		}
		if (Constants.UserInfo.STATUS_NORMAL == status) {
			result = Constants.UserInfo.STATUS_NORMAL_DESC;
		} else if (Constants.UserInfo.STATUS_CANCEL == status) {
			result = Constants.UserInfo.STATUS_CANCEL_DESC;
		} else if (Constants.UserInfo.STATUS_DEL == status) {
			result = Constants.UserInfo.STATUS_DEL_DESC;
		}
		return result;
	}

	public String getTypeDesc() {
		String result = null;
		if (type == null) {
			return null;
		}
		if (Constants.UserInfo.TYPE_ADMIN == type) {
			result = Constants.UserInfo.TYPE_ADMIN_DESC;
		} else if (Constants.UserInfo.TYPE_DOWNSTREAM == type) {
			result = Constants.UserInfo.TYPE_DOWNSTREAM_DESC;
		} else if (Constants.UserInfo.TYPE_UPSTREAM == type) {
			result = Constants.UserInfo.TYPE_UPSTREAM_DESC;
		} else if (Constants.UserInfo.TYPE_BUSINESS == type) {
			result = Constants.UserInfo.TYPE_BUSINESS_DESC;
		} else if (Constants.UserInfo.TYPE_CUSTOM == type) {
			result = Constants.UserInfo.TYPE_CUSTOM_DESC;
		}
		return result;
	}

	public String getSalePriceDesc() {
		if (salesPrice == null) {
			return null;
		}
		if (Constants.AcctInfo.SALES_PRICE_1 == salesPrice) {
			return Constants.AcctInfo.SALES_PRICE_1_DESC;
		} else if (Constants.AcctInfo.SALES_PRICE_2 == salesPrice) {
			return Constants.AcctInfo.SALES_PRICE_2_DESC;
		} else if (Constants.AcctInfo.SALES_PRICE_3 == salesPrice) {
			return Constants.AcctInfo.SALES_PRICE_3_DESC;
		} else if (Constants.AcctInfo.SALES_PRICE_4 == salesPrice) {
			return Constants.AcctInfo.SALES_PRICE_4_DESC;
		} else if (Constants.AcctInfo.SALES_PRICE_5 == salesPrice) {
			return Constants.AcctInfo.SALES_PRICE_5_DESC;
		} else if (Constants.AcctInfo.SALES_PRICE_6 == salesPrice) {
			return Constants.AcctInfo.SALES_PRICE_6_DESC;
		}
		return null;
	}
}