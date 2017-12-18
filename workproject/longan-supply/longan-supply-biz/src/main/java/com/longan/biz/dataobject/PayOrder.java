package com.longan.biz.dataobject;

import java.util.Date;

import com.longan.biz.utils.BigDecimalUtils;
import com.longan.biz.utils.Constants;

public class PayOrder {
	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private Long id;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.biz_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private Integer bizId;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.user_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private Long userId;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.acct_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private Long acctId;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.amount
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private Long amount;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.acct_date
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private String acctDate;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.gmt_create
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private Date gmtCreate;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.gmt_modify
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private Date gmtModify;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.pay_mode
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private Integer payMode;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.status
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private Integer status;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.error_msg
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private String errorMsg;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.pay_type
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private Integer payType;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.channle
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private Integer channle;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.biz_order_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private Long bizOrderId;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.item_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private Integer itemId;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.supply_item_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private Integer supplyItemId;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.supply_trader_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private Integer supplyTraderId;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.acct_log_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private Long acctLogId;

	/**
	 * This field was generated by Abator for iBATIS. This field corresponds to
	 * the database column pay_order.bank_no
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	private String bankNo;

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.id
	 * 
	 * @return the value of pay_order.id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public Long getId() {
		return id;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.id
	 * 
	 * @param id
	 *            the value for pay_order.id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.biz_id
	 * 
	 * @return the value of pay_order.biz_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public Integer getBizId() {
		return bizId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.biz_id
	 * 
	 * @param bizId
	 *            the value for pay_order.biz_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setBizId(Integer bizId) {
		this.bizId = bizId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.user_id
	 * 
	 * @return the value of pay_order.user_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.user_id
	 * 
	 * @param userId
	 *            the value for pay_order.user_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.acct_id
	 * 
	 * @return the value of pay_order.acct_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public Long getAcctId() {
		return acctId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.acct_id
	 * 
	 * @param acctId
	 *            the value for pay_order.acct_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setAcctId(Long acctId) {
		this.acctId = acctId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.amount
	 * 
	 * @return the value of pay_order.amount
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public Long getAmount() {
		return amount;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.amount
	 * 
	 * @param amount
	 *            the value for pay_order.amount
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setAmount(Long amount) {
		this.amount = amount;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.acct_date
	 * 
	 * @return the value of pay_order.acct_date
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public String getAcctDate() {
		return acctDate;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.acct_date
	 * 
	 * @param acctDate
	 *            the value for pay_order.acct_date
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setAcctDate(String acctDate) {
		this.acctDate = acctDate;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.gmt_create
	 * 
	 * @return the value of pay_order.gmt_create
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public Date getGmtCreate() {
		return gmtCreate;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.gmt_create
	 * 
	 * @param gmtCreate
	 *            the value for pay_order.gmt_create
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.gmt_modify
	 * 
	 * @return the value of pay_order.gmt_modify
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public Date getGmtModify() {
		return gmtModify;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.gmt_modify
	 * 
	 * @param gmtModify
	 *            the value for pay_order.gmt_modify
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.pay_mode
	 * 
	 * @return the value of pay_order.pay_mode
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public Integer getPayMode() {
		return payMode;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.pay_mode
	 * 
	 * @param payMode
	 *            the value for pay_order.pay_mode
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.status
	 * 
	 * @return the value of pay_order.status
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.status
	 * 
	 * @param status
	 *            the value for pay_order.status
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.error_msg
	 * 
	 * @return the value of pay_order.error_msg
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.error_msg
	 * 
	 * @param errorMsg
	 *            the value for pay_order.error_msg
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.pay_type
	 * 
	 * @return the value of pay_order.pay_type
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public Integer getPayType() {
		return payType;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.pay_type
	 * 
	 * @param payType
	 *            the value for pay_order.pay_type
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.channle
	 * 
	 * @return the value of pay_order.channle
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public Integer getChannle() {
		return channle;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.channle
	 * 
	 * @param channle
	 *            the value for pay_order.channle
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setChannle(Integer channle) {
		this.channle = channle;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.biz_order_id
	 * 
	 * @return the value of pay_order.biz_order_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public Long getBizOrderId() {
		return bizOrderId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.biz_order_id
	 * 
	 * @param bizOrderId
	 *            the value for pay_order.biz_order_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setBizOrderId(Long bizOrderId) {
		this.bizOrderId = bizOrderId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.item_id
	 * 
	 * @return the value of pay_order.item_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public Integer getItemId() {
		return itemId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.item_id
	 * 
	 * @param itemId
	 *            the value for pay_order.item_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.supply_item_id
	 * 
	 * @return the value of pay_order.supply_item_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public Integer getSupplyItemId() {
		return supplyItemId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.supply_item_id
	 * 
	 * @param supplyItemId
	 *            the value for pay_order.supply_item_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setSupplyItemId(Integer supplyItemId) {
		this.supplyItemId = supplyItemId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.supply_trader_id
	 * 
	 * @return the value of pay_order.supply_trader_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public Integer getSupplyTraderId() {
		return supplyTraderId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.supply_trader_id
	 * 
	 * @param supplyTraderId
	 *            the value for pay_order.supply_trader_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setSupplyTraderId(Integer supplyTraderId) {
		this.supplyTraderId = supplyTraderId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.acct_log_id
	 * 
	 * @return the value of pay_order.acct_log_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public Long getAcctLogId() {
		return acctLogId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.acct_log_id
	 * 
	 * @param acctLogId
	 *            the value for pay_order.acct_log_id
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setAcctLogId(Long acctLogId) {
		this.acctLogId = acctLogId;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method returns the
	 * value of the database column pay_order.bank_no
	 * 
	 * @return the value of pay_order.bank_no
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public String getBankNo() {
		return bankNo;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method sets the
	 * value of the database column pay_order.bank_no
	 * 
	 * @param bankNo
	 *            the value for pay_order.bank_no
	 * 
	 * @abatorgenerated Sun Feb 23 21:30:36 CST 2014
	 */
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getAmountDesc() {
		Double result = BigDecimalUtils.doubleDiveid(String.valueOf(amount));
		return result.toString();
	}

	public boolean showRed() {
		if (status == null) {
			return false;
		}
		return Constants.PayOrder.STATUS_UNCONFIRMED == status
				|| Constants.PayOrder.STATUS_INIT == status
				|| Constants.PayOrder.STATUS_FAILED == status;
	}

	public boolean showGreen() {
		if (status == null) {
			return false;
		}
		return Constants.PayOrder.STATUS_SUCCESS == status;
	}

	public String getStatusDesc() {
		String result = null;
		if (status == null) {
			return null;
		}
		if (Constants.PayOrder.STATUS_INIT == status) {
			result = Constants.PayOrder.STATUS_INIT_DESC;
		} else if (Constants.PayOrder.STATUS_SUCCESS == status) {
			result = Constants.PayOrder.STATUS_SUCCESS_DESC;
		} else if (Constants.PayOrder.STATUS_FAILED == status) {
			result = Constants.PayOrder.STATUS_FAILED_DESC;
		} else if (Constants.PayOrder.STATUS_UNCONFIRMED == status) {
			result = Constants.PayOrder.STATUS_UNCONFIRMED_DESC;
		} else if (Constants.PayOrder.STATUS_REFUND == status) {
			result = Constants.PayOrder.STATUS_REFUND_DESC;
		}
		return result;
	}

	public boolean canAdjust() {
		if (status == null) {
			return false;
		}
		return Constants.PayOrder.STATUS_SUCCESS == status;
	}
}