package com.longan.biz.dataobject;

import java.io.Serializable;
import java.util.Date;

import com.longan.biz.utils.BigDecimalUtils;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;

public class BizOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private Date gmtCreate;

	private Date gmtModify;

	private Integer status;

	private Long payOrderId;

	private Long userId;

	private Long amount;

	private Integer amt;

	private Integer bizId;

	private Integer itemId;

	private String itemName;

	private Integer itemPrice;

	private Integer itemFacePrice;

	private Integer itemCategoryId;

	private String itemUid;

	private String itemCard;

	private String itemExt1;

	private String itemExt2;

	private String itemExt3;

	private String itemExt4;

	private String itemExt5;

	private Long itemSupplyId;

	private Long stockId;

	private Integer channel;

	private String upstreamId;

	private String upstreamName;

	private String upstreamSerialno;

	private Date upstreamDate;

	private String upstreamMemo;

	private String downstreamId;

	private String downstreamName;

	private Date downstreamDate;

	private String downstreamSerialno;

	private String downstreamNotes;

	private String memo;

	private Long lockOperId;

	private Long dealOperId;

	private String uidAreaInfo;

	public String lockOperName;

	public String dealOperName;

	private Integer costTime;

	private Integer supplyType;

	private Integer downstreamSupplyWay;

	private Integer itemCostPrice;

	private Integer carrierType;

	private String provinceCode;

	private Integer actualCost;

	private Integer combineType;

	// repeat
	private Boolean isRepeat;

	private Integer supplyCount;

	private Integer supplyFilterIndex;

	private Integer manualType;

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(Long payOrderId) {
		this.payOrderId = payOrderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Integer getBizId() {
		return bizId;
	}

	public void setBizId(Integer bizId) {
		this.bizId = bizId;
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

	public Integer getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Integer itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Integer getItemFacePrice() {
		return itemFacePrice;
	}

	public void setItemFacePrice(Integer itemFacePrice) {
		this.itemFacePrice = itemFacePrice;
	}

	public Integer getItemCategoryId() {
		return itemCategoryId;
	}

	public void setItemCategoryId(Integer itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
	}

	public String getItemUid() {
		return itemUid;
	}

	public void setItemUid(String itemUid) {
		this.itemUid = itemUid;
	}

	public String getItemCard() {
		return itemCard;
	}

	public void setItemCard(String itemCard) {
		this.itemCard = itemCard;
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

	public String getItemExt4() {
		return itemExt4;
	}

	public void setItemExt4(String itemExt4) {
		this.itemExt4 = itemExt4;
	}

	public String getItemExt5() {
		return itemExt5;
	}

	public void setItemExt5(String itemExt5) {
		this.itemExt5 = itemExt5;
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

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public String getUpstreamId() {
		return upstreamId;
	}

	public void setUpstreamId(String upstreamId) {
		this.upstreamId = upstreamId;
	}

	public String getUpstreamName() {
		return upstreamName;
	}

	public void setUpstreamName(String upstreamName) {
		this.upstreamName = upstreamName;
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

	public String getDownstreamId() {
		return downstreamId;
	}

	public void setDownstreamId(String downstreamId) {
		this.downstreamId = downstreamId;
	}

	public String getDownstreamName() {
		return downstreamName;
	}

	public void setDownstreamName(String downstreamName) {
		this.downstreamName = downstreamName;
	}

	public Date getDownstreamDate() {
		return downstreamDate;
	}

	public void setDownstreamDate(Date downstreamDate) {
		this.downstreamDate = downstreamDate;
	}

	public String getDownstreamSerialno() {
		return downstreamSerialno;
	}

	public void setDownstreamSerialno(String downstreamSerialno) {
		this.downstreamSerialno = downstreamSerialno;
	}

	public String getDownstreamNotes() {
		return downstreamNotes;
	}

	public void setDownstreamNotes(String downstreamNotes) {
		this.downstreamNotes = downstreamNotes;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getUidAreaInfo() {
		return uidAreaInfo;
	}

	public void setUidAreaInfo(String uidAreaInfo) {
		this.uidAreaInfo = uidAreaInfo;
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

	public Integer getCostTime() {
		return costTime;
	}

	public void setCostTime(Integer costTime) {
		this.costTime = costTime;
	}

	public Integer getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(Integer supplyType) {
		this.supplyType = supplyType;
	}

	public Integer getDownstreamSupplyWay() {
		return downstreamSupplyWay;
	}

	public void setDownstreamSupplyWay(Integer downstreamSupplyWay) {
		this.downstreamSupplyWay = downstreamSupplyWay;
	}

	public Integer getItemCostPrice() {
		return itemCostPrice;
	}

	public void setItemCostPrice(Integer itemCostPrice) {
		this.itemCostPrice = itemCostPrice;
	}

	public Integer getCarrierType() {
		return carrierType;
	}

	public void setCarrierType(Integer carrierType) {
		this.carrierType = carrierType;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public Integer getActualCost() {
		return actualCost;
	}

	public void setActualCost(Integer actualCost) {
		this.actualCost = actualCost;
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

	public String getStatusDesc() {
		String result = null;
		if (status == null) {
			return null;
		}
		if (Constants.BizOrder.STATUS_INIT == status) {
			result = Constants.BizOrder.STATUS_INIT_DESC;
		} else if (Constants.BizOrder.STATUS_CHARGING == status) {
			result = Constants.BizOrder.STATUS_CHARGING_DESC;
		} else if (Constants.BizOrder.STATUS_SUCCESS == status) {
			result = Constants.BizOrder.STATUS_SUCCESS_DESC;
		} else if (Constants.BizOrder.STATUS_FAILED == status) {
			result = Constants.BizOrder.STATUS_FAILED_DESC;
		} else if (Constants.BizOrder.STATUS_LOCK == status) {
			result = Constants.BizOrder.STATUS_LOCK_DESC;
		} else if (Constants.BizOrder.STATUS_UNCONFIRMED == status) {
			result = Constants.BizOrder.STATUS_UNCONFIRMED_DESC;
		} else if (Constants.BizOrder.STATUS_EXCEPTION == status) {
			result = Constants.BizOrder.STATUS_EXCEPTION_DESC;
		}
		return result;
	}

	public String getAmountDesc() {
		Double result = BigDecimalUtils.doubleDiveid(String.valueOf(amount));
		return result.toString();
	}

	public double getAmountDouble() {
		Double result = BigDecimalUtils.doubleDiveid(String.valueOf(amount));
		return result;
	}

	public double getItemFacePriceDouble() {
		Double result = BigDecimalUtils.doubleDiveid(String.valueOf(itemFacePrice));
		return result;
	}

	public boolean canDeal() {
		if (status == null || gmtCreate == null) {
			return false;
		}
		Long now = System.currentTimeMillis();
		return (Constants.BizOrder.STATUS_UNCONFIRMED == status || Constants.BizOrder.STATUS_CHARGING == status)
				&& !isManCharge() && now - gmtCreate.getTime() > 60 * 1000 * 2;
	}

	/**
	 * 是不是人工充值业务
	 * 
	 * @return
	 */
	public boolean isManCharge() {
		// 目的暂时不做人工业务了。 这里做 false处理
		return false;
		// if (supplyType == null) {
		// return false;
		// }
		// return supplyType == Constants.ItemSupply.TYPE_MAN;
	}

	/**
	 * 能否人工取消
	 * 
	 * @return
	 */
	public boolean canManCancel() {
		if (status == null) {
			return false;
		}
		return isManCharge()
				&& (status == Constants.BizOrder.STATUS_CHARGING
						|| status == Constants.BizOrder.STATUS_LOCK || Constants.BizOrder.STATUS_UNCONFIRMED == status);
	}

	/**
	 * 能否人工锁定
	 * 
	 * @return
	 */
	public boolean canManLock() {
		if (status == null) {
			return false;
		}
		return isManCharge() && status == Constants.BizOrder.STATUS_CHARGING;
	}

	/**
	 * 是否能人工解锁
	 * 
	 * @return
	 */
	public boolean canManUnLock() {
		if (status == null) {
			return false;
		}
		return isManCharge() && status == Constants.BizOrder.STATUS_LOCK;
	}

	/**
	 * 是否能人工充值
	 * 
	 * @return
	 */
	public boolean canManConfirm() {
		if (status == null) {
			return false;
		}
		return isManCharge()
				&& (status == Constants.BizOrder.STATUS_LOCK || Constants.BizOrder.STATUS_UNCONFIRMED == status);
	}

	/**
	 * 是否能人工充值
	 * 
	 * @return
	 */
	public boolean canManUnConfirm() {
		if (status == null) {
			return false;
		}
		return isManCharge() && (status == Constants.BizOrder.STATUS_LOCK);
	}

	public boolean showRed() {
		if (status == null) {
			return false;
		}
		return Constants.BizOrder.STATUS_UNCONFIRMED == status
				|| Constants.BizOrder.STATUS_LOCK == status
				|| Constants.BizOrder.STATUS_FAILED == status;
	}

	public boolean showGreen() {
		if (status == null) {
			return false;
		}
		return Constants.BizOrder.STATUS_SUCCESS == status;
	}

	public boolean canAdjust() {
		if (status == null) {
			return false;
		}
		return Constants.BizOrder.STATUS_SUCCESS == status
				|| Constants.BizOrder.STATUS_INIT == status;
	}

	public Boolean isSupplyFromStock() {
		if (supplyType == null) {
			return null;
		}
		return supplyType == Constants.ItemSupply.TYPE_CARD_FORWARD_CHARGE
				|| supplyType == Constants.ItemSupply.TYPE_CARD;
	}

	public Boolean isDirectCharge() {
		if (supplyType == null) {
			return null;
		}
		return supplyType == Constants.ItemSupply.TYPE_DIRECT_CHARGE;
	}

	public Boolean isCardCharge() {
		if (supplyType == null) {
			return null;
		}
		return supplyType == Constants.ItemSupply.TYPE_CARD_CHARGE;
	}

	public Boolean isCardForwardCharge() {
		if (supplyType == null) {
			return null;
		}
		return supplyType == Constants.ItemSupply.TYPE_CARD_FORWARD_CHARGE;
	}

	public boolean isSyncSupply() {
		if (downstreamSupplyWay == null) {
			return false;
		}
		return downstreamSupplyWay == Constants.BizOrder.DOWNSTREAM_SUPPLY_WAY_SYNC;
	}

	public String getCarrierDesc() {
		String result = null;
		if (carrierType == null) {
			return result;
		}

		if (Constants.Item.CARRIER_TYPE_MOBILE == carrierType) {
			result = Constants.Item.CARRIER_TYPE_MOBILE_DESC;
		} else if (Constants.Item.CARRIER_TYPE_TELECOM == carrierType) {
			result = Constants.Item.CARRIER_TYPE_TELECOM_DESC;
		} else if (Constants.Item.CARRIER_TYPE_UNICOM == carrierType) {
			result = Constants.Item.CARRIER_TYPE_UNICOM_DESC;
		} else if (Constants.Item.CARRIER_TYPE_OTHER == carrierType) {
			result = Constants.Item.CARRIER_TYPE_OTHER_DESC;
		}
		return result;
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

	public String getCostTimeDesc() {
		if (costTime == null) {
			return null;
		}
		return DateTool.secondToDate(costTime);
	}

	public Double getItemCostPriceDesc() {
		if (itemCostPrice == null) {
			return null;
		}
		return BigDecimalUtils.doubleDiveid(String.valueOf(itemCostPrice));
	}

	public Double getItemFacePriceDesc() {
		if (itemFacePrice == null) {
			return null;
		}
		return BigDecimalUtils.doubleDiveid(String.valueOf(itemFacePrice));
	}

	public Double getActualCostDesc() {
		if (actualCost == null) {
			return null;
		}
		return BigDecimalUtils.doubleDiveid(String.valueOf(actualCost));
	}

	public Boolean getIsRepeat() {
		return isRepeat;
	}

	public void setIsRepeat(Boolean isRepeat) {
		this.isRepeat = isRepeat;
	}

	public Integer getSupplyCount() {
		return supplyCount;
	}

	public void setSupplyCount(Integer supplyCount) {
		this.supplyCount = supplyCount;
	}

	public Integer getSupplyFilterIndex() {
		return supplyFilterIndex;
	}

	public void setSupplyFilterIndex(Integer supplyFilterIndex) {
		this.supplyFilterIndex = supplyFilterIndex;
	}

	public boolean isManualRepeatType() {
		if (manualType == null) {
			return false;
		}
		return manualType == Constants.SupplyOrder.MANUAL_TYPE_YES;
	}

	public boolean isOver() {
		if (status == null) {
			return false;
		}
		return status == Constants.BizOrder.STATUS_SUCCESS
				|| status == Constants.BizOrder.STATUS_FAILED;
	}

	public String getBizName() {
		return Constants.BIZ_MAP.get(bizId);
	}

	public boolean isBizPhone() {
		if (bizId == null) {
			return false;
		}

		return bizId == Constants.BizInfo.CODE_PHONE_MOBILE
				|| bizId == Constants.BizInfo.CODE_PHONE_TELECOM
				|| bizId == Constants.BizInfo.CODE_PHONE_UNICOM;
	}

	public boolean isBizFlow() {
		if (bizId == null) {
			return false;
		}

		return bizId == Constants.BizInfo.CODE_FLOW_MOBILE
				|| bizId == Constants.BizInfo.CODE_FLOW_TELECOM
				|| bizId == Constants.BizInfo.CODE_FLOW_UNICOM;
	}

	public boolean isFromSupply() {
		if (channel == null) {
			return false;
		}

		return channel == Constants.BizOrder.CHANNEL_SUPPLY;
	}

	public String getChannelDesc() {
		String result = null;
		if (channel == null) {
			return null;
		}
		if (Constants.BizOrder.CHANNEL_SUPPLY == channel) {
			result = Constants.BizOrder.CHANNEL_SUPPLY_DESC;
		} else if (Constants.BizOrder.CHANNEL_WEB == channel) {
			result = Constants.BizOrder.CHANNEL_WEB_DESC;
		}
		return result;
	}
}