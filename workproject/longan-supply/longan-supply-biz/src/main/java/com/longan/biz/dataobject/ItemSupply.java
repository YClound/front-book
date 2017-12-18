package com.longan.biz.dataobject;

import java.util.Date;

import com.longan.biz.utils.BigDecimalUtils;
import com.longan.biz.utils.Constants;

public class ItemSupply {
	private Long id;

	private Date gmtCreate;

	private Date gmtModify;

	private Long supplyTraderId;

	private Integer bizId;

	private Integer itemId;

	private Integer itemCostPrice;

	private String itemSupplyExt1;

	private String itemSupplyExt2;

	private String itemSupplyExt3;

	private String itemSupplyExt4;

	private String itemSupplyExt5;

	private Integer priority;

	private Integer status;

	private Integer quantity;

	private Integer warnQuantity;

	private Integer maxDay;

	private Integer numDay;

	private Integer maxMounth;

	private Integer numMounth;

	private String itemName;

	private Integer itemType;

	private Integer itemFacePrice;

	private String salesArea;

	private String supplyTraderName;

	private String supplyProductCode;

	private Integer itemSupplyType;

	private Boolean isAsyncSupply;

	private Integer lossType;

	private Integer lossTime;

	private Integer supplyFilterIndex;

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

	public Long getSupplyTraderId() {
		return supplyTraderId;
	}

	public void setSupplyTraderId(Long supplyTraderId) {
		this.supplyTraderId = supplyTraderId;
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

	public Integer getItemCostPrice() {
		return itemCostPrice;
	}

	public void setItemCostPrice(Integer itemCostPrice) {
		this.itemCostPrice = itemCostPrice;
	}

	public String getItemSupplyExt1() {
		return itemSupplyExt1;
	}

	public void setItemSupplyExt1(String itemSupplyExt1) {
		this.itemSupplyExt1 = itemSupplyExt1;
	}

	public String getItemSupplyExt2() {
		return itemSupplyExt2;
	}

	public void setItemSupplyExt2(String itemSupplyExt2) {
		this.itemSupplyExt2 = itemSupplyExt2;
	}

	public String getItemSupplyExt3() {
		return itemSupplyExt3;
	}

	public void setItemSupplyExt3(String itemSupplyExt3) {
		this.itemSupplyExt3 = itemSupplyExt3;
	}

	public String getItemSupplyExt4() {
		return itemSupplyExt4;
	}

	public void setItemSupplyExt4(String itemSupplyExt4) {
		this.itemSupplyExt4 = itemSupplyExt4;
	}

	public String getItemSupplyExt5() {
		return itemSupplyExt5;
	}

	public void setItemSupplyExt5(String itemSupplyExt5) {
		this.itemSupplyExt5 = itemSupplyExt5;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getWarnQuantity() {
		return warnQuantity;
	}

	public void setWarnQuantity(Integer warnQuantity) {
		this.warnQuantity = warnQuantity;
	}

	public Integer getMaxDay() {
		return maxDay;
	}

	public void setMaxDay(Integer maxDay) {
		this.maxDay = maxDay;
	}

	public Integer getNumDay() {
		return numDay;
	}

	public void setNumDay(Integer numDay) {
		this.numDay = numDay;
	}

	public Integer getMaxMounth() {
		return maxMounth;
	}

	public void setMaxMounth(Integer maxMounth) {
		this.maxMounth = maxMounth;
	}

	public Integer getNumMounth() {
		return numMounth;
	}

	public void setNumMounth(Integer numMounth) {
		this.numMounth = numMounth;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public Integer getItemFacePrice() {
		return itemFacePrice;
	}

	public void setItemFacePrice(Integer itemFacePrice) {
		this.itemFacePrice = itemFacePrice;
	}

	public String getSalesArea() {
		return salesArea;
	}

	public void setSalesArea(String salesArea) {
		this.salesArea = salesArea;
	}

	public String getSupplyTraderName() {
		return supplyTraderName;
	}

	public void setSupplyTraderName(String supplyTraderName) {
		this.supplyTraderName = supplyTraderName;
	}

	public Integer getItemSupplyType() {
		return itemSupplyType;
	}

	public void setItemSupplyType(Integer itemSupplyType) {
		this.itemSupplyType = itemSupplyType;
	}

	public Boolean getIsAsyncSupply() {
		return isAsyncSupply;
	}

	public void setIsAsyncSupply(Boolean isAsyncSupply) {
		this.isAsyncSupply = isAsyncSupply;
	}

	public String getSupplyProductCode() {
		return supplyProductCode;
	}

	public void setSupplyProductCode(String supplyProductCode) {
		this.supplyProductCode = supplyProductCode;
	}

	public Integer getLossType() {
		return lossType;
	}

	public void setLossType(Integer lossType) {
		this.lossType = lossType;
	}

	public Integer getLossTime() {
		return lossTime;
	}

	public void setLossTime(Integer lossTime) {
		this.lossTime = lossTime;
	}

	public String getPriceDesc(Integer price) {
		if (price == null) {
			return null;
		}
		Double result = BigDecimalUtils.doubleDiveid(String.valueOf(price));
		return result.toString();
	}

	public String getItemTypeDesc() {
		String result = null;
		if (itemType == null) {
			return null;
		}
		if (Constants.Item.TYPE_ITEM_CARD == itemType) {
			result = Constants.Item.TYPE_ITEM_CARD_DESC;
		} else if (Constants.Item.TYPE_ITEM_CHARGE == itemType) {
			result = Constants.Item.TYPE_ITEM_CHARGE_DESC;
		}
		return result;
	}

	public boolean canUpOperation() {
		if (status == null) {
			return false;
		}
		return status != Constants.ItemSupply.STATUS_UP;
	}

	public boolean canDownOperation() {
		if (status == null) {
			return false;
		}
		return status == Constants.ItemSupply.STATUS_UP;
	}

	public boolean showRed() {
		if (status == null) {
			return false;
		}
		return status == Constants.Item.STATUS_DOWN;
	}

	public boolean showGreen() {
		if (status == null) {
			return false;
		}
		return status == Constants.Item.STATUS_UP;
	}

	public String getStatusDesc() {
		String result = null;
		if (status == null) {
			return null;
		}
		if (status == Constants.ItemSupply.STATUS_DEL) {
			result = Constants.ItemSupply.STATUS_DEL_DESC;
		} else if (status == Constants.ItemSupply.STATUS_DOWN) {
			result = Constants.ItemSupply.STATUS_DOWN_DESC;
		} else if (status == Constants.ItemSupply.STATUS_INIT) {
			result = Constants.Item.STATUS_INIT_DESC;
		} else if (status == Constants.ItemSupply.STATUS_UP) {
			result = Constants.ItemSupply.STATUS_UP_DESC;
		}
		return result;
	}

	public String getSupplyTypeDesc() {
		String result = null;
		if (itemSupplyType == null) {
			return null;
		}
		if (Constants.ItemSupply.TYPE_CARD_FORWARD_CHARGE == itemSupplyType) {
			result = Constants.ItemSupply.TYPE_CARD_FORWARD_CHARGE_DESC;
		} else if (Constants.ItemSupply.TYPE_DIRECT_CHARGE == itemSupplyType) {
			result = Constants.ItemSupply.TYPE_DIRECT_CHARGE_DESC;
		} else if (Constants.ItemSupply.TYPE_MAN == itemSupplyType) {
			result = Constants.ItemSupply.TYPE_MAN_DESC;
		} else if (Constants.ItemSupply.TYPE_CARD == itemSupplyType) {
			result = Constants.ItemSupply.TYPE_CARD_DESC;
		} else if (Constants.ItemSupply.TYPE_CARD_CHARGE == itemSupplyType) {
			result = Constants.ItemSupply.TYPE_CARD_CHARGE_DESC;
		}
		return result;
	}

	public String getSupplyWayDesc() {
		String result = null;
		if (isAsyncSupply == null) {
			return null;
		}
		if (isAsyncSupply) {
			result = Constants.TraderInfo.SUPPLY_WAY_ASYNC_DESC;
		} else {
			result = Constants.TraderInfo.SUPPLY_WAY_SYNC_DESC;
		}
		return result;
	}

	public boolean hasStock() {
		return itemSupplyType != null
				&& (itemSupplyType == Constants.ItemSupply.TYPE_CARD || itemSupplyType == Constants.ItemSupply.TYPE_CARD_FORWARD_CHARGE);
	}

	public boolean isTypeCardForwardCharge() {
		return itemSupplyType != null
				&& itemSupplyType == Constants.ItemSupply.TYPE_CARD_FORWARD_CHARGE;
	}

	public boolean isTypeDirectCharge() {
		return itemSupplyType != null && itemSupplyType == Constants.ItemSupply.TYPE_DIRECT_CHARGE;
	}

	public boolean isTypeMan() {
		return itemSupplyType != null && itemSupplyType == Constants.ItemSupply.TYPE_MAN;
	}

	public boolean isTypeCard() {
		return itemSupplyType != null && itemSupplyType == Constants.ItemSupply.TYPE_CARD;
	}

	public boolean isTypeCardCharge() {
		return itemSupplyType != null && itemSupplyType == Constants.ItemSupply.TYPE_CARD_CHARGE;
	}

	public boolean canOutStorage() {
		return itemSupplyType != null
				&& (itemSupplyType == Constants.ItemSupply.TYPE_CARD || itemSupplyType == Constants.ItemSupply.TYPE_CARD_FORWARD_CHARGE);
	}

	public Integer getSupplyFilterIndex() {
		return supplyFilterIndex;
	}

	public void setSupplyFilterIndex(Integer supplyFilterIndex) {
		this.supplyFilterIndex = supplyFilterIndex;
	}

	public boolean canLoseSupply() {
		if (lossType == null) {
			return false;
		}
		return Constants.ItemSupply.LOSSTYPE_CAN == lossType;
	}

}