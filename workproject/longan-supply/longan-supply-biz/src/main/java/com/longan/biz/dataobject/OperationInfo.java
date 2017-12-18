package com.longan.biz.dataobject;

import com.longan.biz.utils.Constants;

public class OperationInfo {
	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column operation_info.id
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	private Integer id;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column operation_info.operation_code
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	private String operationCode;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column operation_info.operation_name
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	private String operationName;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column operation_info.operation_url
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	private String operationUrl;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column operation_info.biz_id
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	private Integer bizId;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column operation_info.type
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	private Integer type;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column operation_info.status
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	private Integer status;
	private Integer belongMenu;
	private String belongMenuDesc;

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column operation_info.id
	 * 
	 * @return the value of operation_info.id
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column operation_info.id
	 * 
	 * @param id
	 *            the value for operation_info.id
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column operation_info.operation_code
	 * 
	 * @return the value of operation_info.operation_code
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public String getOperationCode() {
		return operationCode;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column operation_info.operation_code
	 * 
	 * @param operationCode
	 *            the value for operation_info.operation_code
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column operation_info.operation_name
	 * 
	 * @return the value of operation_info.operation_name
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public String getOperationName() {
		return operationName;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column operation_info.operation_name
	 * 
	 * @param operationName
	 *            the value for operation_info.operation_name
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column operation_info.operation_url
	 * 
	 * @return the value of operation_info.operation_url
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public String getOperationUrl() {
		return operationUrl;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column operation_info.operation_url
	 * 
	 * @param operationUrl
	 *            the value for operation_info.operation_url
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public void setOperationUrl(String operationUrl) {
		this.operationUrl = operationUrl;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column operation_info.biz_id
	 * 
	 * @return the value of operation_info.biz_id
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public Integer getBizId() {
		return bizId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column operation_info.biz_id
	 * 
	 * @param bizId
	 *            the value for operation_info.biz_id
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public void setBizId(Integer bizId) {
		this.bizId = bizId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column operation_info.type
	 * 
	 * @return the value of operation_info.type
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column operation_info.type
	 * 
	 * @param type
	 *            the value for operation_info.type
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column operation_info.status
	 * 
	 * @return the value of operation_info.status
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column operation_info.status
	 * 
	 * @param status
	 *            the value for operation_info.status
	 * 
	 * @abatorgenerated Thu Mar 06 15:12:58 CST 2014
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getBelongMenu() {
		return belongMenu;
	}

	public void setBelongMenu(Integer belongMenu) {
		this.belongMenu = belongMenu;
	}

	public String getBelongMenuDesc() {
		return belongMenuDesc;
	}

	public void setBelongMenuDesc(String belongMenuDesc) {
		this.belongMenuDesc = belongMenuDesc;
	}

	public boolean showRed() {
		if (status == null) {
			return false;
		}
		return Constants.OperationInfo.STATUS_CANCEL == status
				|| Constants.OperationInfo.STATUS_DEL == status;
	}

	public boolean showGreen() {
		if (status == null) {
			return false;
		}
		return Constants.OperationInfo.STATUS_NORMAL == status;
	}

	public String getStatusDesc() {
		String result = null;
		if (status == null) {
			return null;
		}
		if (Constants.OperationInfo.STATUS_NORMAL == status) {
			result = Constants.OperationInfo.STATUS_NORMAL_DESC;
		} else if (Constants.OperationInfo.STATUS_CANCEL == status) {
			result = Constants.OperationInfo.STATUS_CANCEL_DESC;
		} else if (Constants.OperationInfo.STATUS_DEL == status) {
			result = Constants.OperationInfo.STATUS_DEL_DESC;
		}
		return result;
	}

	public String getTypeDesc() {
		String result = null;
		if (type == null) {
			return null;
		}
		if (Constants.OperationInfo.TYPE_CATALOG == type) {
			result = Constants.OperationInfo.TYPE_CATALOG_DESC;
		} else if (Constants.OperationInfo.TYPE_URL == type) {
			result = Constants.OperationInfo.TYPE_URL_DESC;
		} else if (Constants.OperationInfo.TYPE_BIZ == type) {
			result = Constants.OperationInfo.TYPE_BIZ_DESC;
		}
		return result;
	}
}