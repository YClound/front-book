package com.longan.biz.dataobject;

import java.io.Serializable;
import java.util.Date;

import com.longan.biz.utils.BigDecimalUtils;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;

public class SupplyOrder implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;

	private Date gmtCreate;

	private Date gmtModify;

	private Long bizOrderId;

	private Integer bizId;

	private Long itemSupplyId;

	private Long stockId;

	private Long userId;

	private Integer itemId;

	private String itemName;

	private Integer itemFacePrice;

	private String itemUid;

	private String itemExt1;

	private String itemExt2;

	private String itemExt3;

	private Long amount;

	private Integer amt;

	private Long lockOperId;

	private Long dealOperId;

	private Long supplyTraderId;

	private String supplyTraderName;

	private Integer supplyCostTime;

	private Integer supplyType;

	private Integer supplyFacePrice;

	private Integer supplyCostPrice;

	private Integer supplyActualCost;

	private Integer supplyStatus;

	private Integer finalType;

	private Integer repeatType;

	private Integer combineType;

	private Integer manualType;

	private String upstreamSerialno;

	private Date upstreamDate;

	private String upstreamMemo;

	private boolean flagCardValid;

	public String lockOperName;

	public String dealOperName;

	private String downstreamName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getBizOrderId() {
		return bizOrderId;
	}

	public void setBizOrderId(Long bizOrderId) {
		this.bizOrderId = bizOrderId;
	}

	public Integer getBizId() {
		return bizId;
	}

	public void setBizId(Integer bizId) {
		this.bizId = bizId;
	}

	public Long getItemSupplyId() {
		return itemSupplyId;
	}

	public void setItemSupplyId(Long itemSupplyId) {
		this.itemSupplyId = itemSupplyId;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemFacePrice() {
		return itemFacePrice;
	}

	public void setItemFacePrice(Integer itemFacePrice) {
		this.itemFacePrice = itemFacePrice;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Integer getAmt() {
		return amt;
	}

	public void setAmt(Integer amt) {
		this.amt = amt;
	}

	public Long getLockOperId() {
		return lockOperId;
	}

	public void setLockOperId(Long lockOperId) {
		this.lockOperId = lockOperId;
	}

	public Long getDealOperId() {
		return dealOperId;
	}

	public void setDealOperId(Long dealOperId) {
		this.dealOperId = dealOperId;
	}

	public Long getSupplyTraderId() {
		return supplyTraderId;
	}

	public void setSupplyTraderId(Long supplyTraderId) {
		this.supplyTraderId = supplyTraderId;
	}

	public String getSupplyTraderName() {
		return supplyTraderName;
	}

	public void setSupplyTraderName(String supplyTraderName) {
		this.supplyTraderName = supplyTraderName;
	}

	public Integer getSupplyCostTime() {
		return supplyCostTime;
	}

	public void setSupplyCostTime(Integer supplyCostTime) {
		this.supplyCostTime = supplyCostTime;
	}

	public Integer getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(Integer supplyType) {
		this.supplyType = supplyType;
	}

	public Integer getSupplyFacePrice() {
		return supplyFacePrice;
	}

	public void setSupplyFacePrice(Integer supplyFacePrice) {
		this.supplyFacePrice = supplyFacePrice;
	}

	public Integer getSupplyCostPrice() {
		return supplyCostPrice;
	}

	public void setSupplyCostPrice(Integer supplyCostPrice) {
		this.supplyCostPrice = supplyCostPrice;
	}

	public Integer getSupplyActualCost() {
		return supplyActualCost;
	}

	public void setSupplyActualCost(Integer supplyActualCost) {
		this.supplyActualCost = supplyActualCost;
	}

	public Integer getSupplyStatus() {
		return supplyStatus;
	}

	public void setSupplyStatus(Integer supplyStatus) {
		this.supplyStatus = supplyStatus;
	}

	public Integer getFinalType() {
		return finalType;
	}

	public void setFinalType(Integer finalType) {
		this.finalType = finalType;
	}

	public Integer getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(Integer repeatType) {
		this.repeatType = repeatType;
	}

	public Integer getCombineType() {
		return combineType;
	}

	public void setCombineType(Integer combineType) {
		this.combineType = combineType;
	}

	public Integer getManualType() {
		return manualType;
	}

	public void setManualType(Integer manualType) {
		this.manualType = manualType;
	}

	public String getUpstreamSerialno() {
		return upstreamSerialno;
	}

	public void setUpstreamSerialno(String upstreamSerialno) {
		this.upstreamSerialno = upstreamSerialno;
	}

	public Date getUpstreamDate() {
		return upstreamDate;
	}

	public void setUpstreamDate(Date upstreamDate) {
		this.upstreamDate = upstreamDate;
	}

	public String getUpstreamMemo() {
		return upstreamMemo;
	}

	public void setUpstreamMemo(String upstreamMemo) {
		this.upstreamMemo = upstreamMemo;
	}

	public String getItemUid() {
		return itemUid;
	}

	public void setItemUid(String itemUid) {
		this.itemUid = itemUid;
	}

	public String getItemExt1() {
		return itemExt1;
	}

	public void setItemExt1(String itemExt1) {
		this.itemExt1 = itemExt1;
	}

	public String getItemExt2() {
		return itemExt2;
	}

	public void setItemExt2(String itemExt2) {
		this.itemExt2 = itemExt2;
	}

	public String getItemExt3() {
		return itemExt3;
	}

	public void setItemExt3(String itemExt3) {
		this.itemExt3 = itemExt3;
	}

	public boolean isOver() {
		if (supplyStatus == null) {
			return false;
		}
		return supplyStatus == Constants.SupplyOrder.STATUS_SUCCESS
				|| supplyStatus == Constants.SupplyOrder.STATUS_FAILED;
	}

	public boolean isTypeCardForwardCharge() {
		return supplyType != null && supplyType == Constants.ItemSupply.TYPE_CARD_FORWARD_CHARGE;
	}

	public boolean isTypeDirectCharge() {
		return supplyType != null && supplyType == Constants.ItemSupply.TYPE_DIRECT_CHARGE;
	}

	public boolean isTypeMan() {
		return supplyType != null && supplyType == Constants.ItemSupply.TYPE_MAN;
	}

	public boolean isTypeCard() {
		return supplyType != null && supplyType == Constants.ItemSupply.TYPE_CARD;
	}

	public boolean isTypeCardCharge() {
		return supplyType != null && supplyType == Constants.ItemSupply.TYPE_CARD_CHARGE;
	}

	public boolean isManCharge() {
		if (supplyType == null) {
			return false;
		}
		return supplyType == Constants.ItemSupply.TYPE_MAN;
	}

	public boolean isSupplyFromStock() {
		if (supplyType == null) {
			return false;
		}
		return supplyType == Constants.ItemSupply.TYPE_CARD_FORWARD_CHARGE
				|| supplyType == Constants.ItemSupply.TYPE_CARD;
	}

	public boolean isFlagCardValid() {
		return flagCardValid;
	}

	public void setFlagCardValid(boolean flagCardValid) {
		this.flagCardValid = flagCardValid;
	}

	public Integer computCostTime(Date date) {
		if (date != null) {
			Long costTime = (System.currentTimeMillis() - date.getTime()) / 1000;
			if (costTime == 0) {
				costTime = 1L;
			}
			return costTime.intValue();
		}
		return null;
	}

	public String getAmountDesc() {
		Double result = BigDecimalUtils.doubleDiveid(String.valueOf(amount));
		return result.toString();
	}

	public String getSupplyStatusDesc() {
		String result = null;
		if (supplyStatus == null) {
			return null;
		}
		if (Constants.SupplyOrder.STATUS_INIT == supplyStatus) {
			result = Constants.SupplyOrder.STATUS_INIT_DESC;
		} else if (Constants.SupplyOrder.STATUS_CHARGING == supplyStatus) {
			result = Constants.SupplyOrder.STATUS_CHARGING_DESC;
		} else if (Constants.SupplyOrder.STATUS_SUCCESS == supplyStatus) {
			result = Constants.SupplyOrder.STATUS_SUCCESS_DESC;
		} else if (Constants.SupplyOrder.STATUS_FAILED == supplyStatus) {
			result = Constants.SupplyOrder.STATUS_FAILED_DESC;
		} else if (Constants.SupplyOrder.STATUS_LOCK == supplyStatus) {
			result = Constants.SupplyOrder.STATUS_LOCK_DESC;
		} else if (Constants.SupplyOrder.STATUS_UNCONFIRMED == supplyStatus) {
			result = Constants.SupplyOrder.STATUS_UNCONFIRMED_DESC;
		} else if (Constants.SupplyOrder.STATUS_EXCEPTION == supplyStatus) {
			result = Constants.SupplyOrder.STATUS_EXCEPTION_DESC;
		}
		return result;
	}

	public String getSupplyCostTimeDesc() {
		if (supplyCostTime == null) {
			return null;
		}
		return DateTool.secondToDate(supplyCostTime);
	}

	public String getLockOperName() {
		return lockOperName;
	}

	public void setLockOperName(String lockOperName) {
		this.lockOperName = lockOperName;
	}

	public String getDealOperName() {
		return dealOperName;
	}

	public void setDealOperName(String dealOperName) {
		this.dealOperName = dealOperName;
	}

	public String getDownstreamName() {
		return downstreamName;
	}

	public void setDownstreamName(String downstreamName) {
		this.downstreamName = downstreamName;
	}

	public boolean showRed() {
		if (supplyStatus == null) {
			return false;
		}
		return Constants.SupplyOrder.STATUS_UNCONFIRMED == supplyStatus
				|| Constants.SupplyOrder.STATUS_LOCK == supplyStatus
				|| Constants.SupplyOrder.STATUS_FAILED == supplyStatus;
	}

	public boolean showGreen() {
		if (supplyStatus == null) {
			return false;
		}
		return Constants.SupplyOrder.STATUS_SUCCESS == supplyStatus;
	}

	public Double getSupplyActualCostDesc() {
		if (supplyActualCost == null) {
			return null;
		}
		return BigDecimalUtils.doubleDiveid(String.valueOf(supplyActualCost));
	}

	public Double getSupplyCostPriceDesc() {
		if (supplyCostPrice == null) {
			return null;
		}
		return BigDecimalUtils.doubleDiveid(String.valueOf(supplyCostPrice));
	}

	public boolean canDeal() {
		if (supplyStatus == null || gmtCreate == null) {
			return false;
		}
		Long now = System.currentTimeMillis();
		return (Constants.SupplyOrder.STATUS_UNCONFIRMED == supplyStatus || Constants.SupplyOrder.STATUS_CHARGING == supplyStatus)
				&& !isManCharge() && now - gmtCreate.getTime() > 60 * 1000 * 2;
	}

	public boolean canAdjust() {
		if (supplyStatus == null) {
			return false;
		}
		return Constants.SupplyOrder.STATUS_SUCCESS == supplyStatus
				|| Constants.SupplyOrder.STATUS_INIT == supplyStatus;
	}

	public boolean isManualRepeatType() {
		if (manualType == null) {
			return false;
		}
		return manualType == Constants.SupplyOrder.MANUAL_TYPE_YES;
	}

	public String getFinalTypeDesc() {
		String result = null;
		if (finalType == null) {
			return result;
		}
		if (finalType == Constants.SupplyOrder.FINAL_TYPE_YES) {
			result = Constants.SupplyOrder.FINAL_TYPE_YES_DESC;
		} else if (finalType == Constants.SupplyOrder.FINAL_TYPE_NO) {
			result = Constants.SupplyOrder.FINAL_TYPE_NO_DESC;
		}
		return result;
	}

	public String getRepeatTypeDesc() {
		String result = null;
		if (repeatType == null) {
			return result;
		}
		if (repeatType == Constants.SupplyOrder.REPEAT_TYPE_YES) {
			result = Constants.SupplyOrder.REPEAT_TYPE_YES_DESC;
		} else if (repeatType == Constants.SupplyOrder.REPEAT_TYPE_NO) {
			result = Constants.SupplyOrder.REPEAT_TYPE_NO_DESC;
		}
		return result;
	}

	public String getManualRepeatTypeDesc() {
		String result = null;
		if (manualType == null) {
			return result;
		}
		if (manualType == Constants.SupplyOrder.MANUAL_TYPE_YES) {
			result = Constants.SupplyOrder.MANUAL_TYPE_YES_DESC;
		} else if (manualType == Constants.SupplyOrder.MANUAL_TYPE_NO) {
			result = Constants.SupplyOrder.MANUAL_TYPE_NO_DESC;
		}
		return result;
	}

	public double getItemFacePriceDouble() {
		Double result = BigDecimalUtils.doubleDiveid(String.valueOf(itemFacePrice));
		return result;
	}

	public double getAmountDouble() {
		Double result = BigDecimalUtils.doubleDiveid(String.valueOf(amount));
		return result;
	}

	/**
	 * 能否人工取消
	 * 
	 * @return
	 */
	public boolean canManCancel() {
		if (supplyStatus == null) {
			return false;
		}
		return isManCharge()
				&& (supplyStatus == Constants.SupplyOrder.STATUS_CHARGING
						|| supplyStatus == Constants.SupplyOrder.STATUS_LOCK || Constants.SupplyOrder.STATUS_UNCONFIRMED == supplyStatus);
	}

	/**
	 * 能否人工锁定
	 * 
	 * @return
	 */
	public boolean canManLock() {
		if (supplyStatus == null) {
			return false;
		}
		return isManCharge() && supplyStatus == Constants.SupplyOrder.STATUS_CHARGING;
	}

	/**
	 * 是否能人工解锁
	 * 
	 * @return
	 */
	public boolean canManUnLock() {
		if (supplyStatus == null) {
			return false;
		}
		return isManCharge() && supplyStatus == Constants.SupplyOrder.STATUS_LOCK;
	}

	/**
	 * 是否能人工充值
	 * 
	 * @return
	 */
	public boolean canManConfirm() {
		if (supplyStatus == null) {
			return false;
		}
		return isManCharge()
				&& (supplyStatus == Constants.SupplyOrder.STATUS_LOCK || Constants.SupplyOrder.STATUS_UNCONFIRMED == supplyStatus);
	}

	/**
	 * 是否能人工充值
	 * 
	 * @return
	 */
	public boolean canManUnConfirm() {
		if (supplyStatus == null) {
			return false;
		}
		return isManCharge() && (supplyStatus == Constants.SupplyOrder.STATUS_LOCK);
	}
}