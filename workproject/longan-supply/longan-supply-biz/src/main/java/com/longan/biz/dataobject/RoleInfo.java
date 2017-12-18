package com.longan.biz.dataobject;

import com.longan.biz.utils.Constants;

public class RoleInfo {
	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column role_info.id
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	private Integer id;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column role_info.role_name
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	private String roleName;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column role_info.role_desc
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	private String roleDesc;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column role_info.status
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	private Integer status;

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column role_info.id
	 * 
	 * @return the value of role_info.id
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column role_info.id
	 * 
	 * @param id
	 *            the value for role_info.id
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column role_info.role_name
	 * 
	 * @return the value of role_info.role_name
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column role_info.role_name
	 * 
	 * @param roleName
	 *            the value for role_info.role_name
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column role_info.role_desc
	 * 
	 * @return the value of role_info.role_desc
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public String getRoleDesc() {
		return roleDesc;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column role_info.role_desc
	 * 
	 * @param roleDesc
	 *            the value for role_info.role_desc
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column role_info.status
	 * 
	 * @return the value of role_info.status
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column role_info.status
	 * 
	 * @param status
	 *            the value for role_info.status
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	public boolean showRed() {
		if (status == null) {
			return false;
		}
		return Constants.RoleInfo.STATUS_CANCEL == status
				|| Constants.RoleInfo.STATUS_DEL == status;
	}

	public boolean showGreen() {
		if (status == null) {
			return false;
		}
		return Constants.RoleInfo.STATUS_NORMAL == status;
	}

	public String getStatusDesc() {
		String result = null;
		if (status == null) {
			return null;
		}
		if (Constants.RoleInfo.STATUS_NORMAL == status) {
			result = Constants.RoleInfo.STATUS_NORMAL_DESC;
		} else if (Constants.RoleInfo.STATUS_CANCEL == status) {
			result = Constants.RoleInfo.STATUS_CANCEL_DESC;
		} else if (Constants.RoleInfo.STATUS_DEL == status) {
			result = Constants.RoleInfo.STATUS_DEL_DESC;
		}
		return result;
	}
}