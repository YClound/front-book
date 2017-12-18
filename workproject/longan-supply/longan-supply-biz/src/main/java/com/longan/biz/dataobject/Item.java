package com.longan.biz.dataobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.utils.BigDecimalUtils;
import com.longan.biz.utils.Constants;

public class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(Item.class);

	private Integer id;

	private Date gmtCreate;

	private Date gmtModify;

	private String itemName;

	private Integer itemFacePrice;

	private String itemUnit;

	private Integer itemSalesPrice;

	private Integer itemSalesPrice2;

	private Integer itemSalesPrice3;

	private Integer itemSalesPrice4;

	private Integer itemCostPrice;

	private Integer bizId;

	private Integer itemCategoryId;

	private Integer itemType;

	private String itemExt1;

	private String itemExt2;

	private String itemExt3;

	private String itemExt4;

	private String itemExt5;

	private Integer status;

	private Integer carrierType;

	private String salesArea;

	private List<String> salesAreaList;

	private Integer supplyFilterType;

	private String itemSalesAreaDesc;

	private Boolean associated;

	private Boolean canUp;

	private Boolean canSync;

	private Integer faceType;

	private String numberList;

	private Integer minNumber;

	private Integer maxNumber;

	private Integer exchargeRatio;

	private Integer repeatType;

	private Integer repeatCount;

	private Integer repeatTime;

	private Integer combineType;

	private String gameName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	public Integer getItemSalesPrice() {
		return itemSalesPrice;
	}

	public void setItemSalesPrice(Integer itemSalesPrice) {
		this.itemSalesPrice = itemSalesPrice;
	}

	public Integer getItemSalesPrice2() {
		return itemSalesPrice2;
	}

	public void setItemSalesPrice2(Integer itemSalesPrice2) {
		this.itemSalesPrice2 = itemSalesPrice2;
	}

	public Integer getItemSalesPrice3() {
		return itemSalesPrice3;
	}

	public void setItemSalesPrice3(Integer itemSalesPrice3) {
		this.itemSalesPrice3 = itemSalesPrice3;
	}

	public Integer getItemSalesPrice4() {
		return itemSalesPrice4;
	}

	public void setItemSalesPrice4(Integer itemSalesPrice4) {
		this.itemSalesPrice4 = itemSalesPrice4;
	}

	public Integer getItemCostPrice() {
		return itemCostPrice;
	}

	public void setItemCostPrice(Integer itemCostPrice) {
		this.itemCostPrice = itemCostPrice;
	}

	public Integer getBizId() {
		return bizId;
	}

	public void setBizId(Integer bizId) {
		this.bizId = bizId;
	}

	public Integer getItemCategoryId() {
		return itemCategoryId;
	}

	public void setItemCategoryId(Integer itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCarrierType() {
		return carrierType;
	}

	public void setCarrierType(Integer carrierType) {
		this.carrierType = carrierType;
	}

	public String getSalesArea() {
		return salesArea;
	}

	public void setSalesArea(String salesArea) {
		this.salesArea = salesArea;
	}

	public Boolean getCanSync() {
		return canSync;
	}

	public void setCanSync(Boolean canSync) {
		this.canSync = canSync;
	}

	public Integer getFaceType() {
		return faceType;
	}

	public void setFaceType(Integer faceType) {
		this.faceType = faceType;
	}

	public String getNumberList() {
		return numberList;
	}

	public void setNumberList(String numberList) {
		this.numberList = numberList;
	}

	public Integer getMinNumber() {
		return minNumber;
	}

	public void setMinNumber(Integer minNumber) {
		this.minNumber = minNumber;
	}

	public Integer getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(Integer maxNumber) {
		this.maxNumber = maxNumber;
	}

	public Integer getExchargeRatio() {
		return exchargeRatio;
	}

	public void setExchargeRatio(Integer exchargeRatio) {
		this.exchargeRatio = exchargeRatio;
	}

	public Integer getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(Integer repeatType) {
		this.repeatType = repeatType;
	}

	public Integer getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(Integer repeatCount) {
		this.repeatCount = repeatCount;
	}

	public Integer getRepeatTime() {
		return repeatTime;
	}

	public void setRepeatTime(Integer repeatTime) {
		this.repeatTime = repeatTime;
	}

	public Integer getCombineType() {
		return combineType;
	}

	public void setCombineType(Integer combineType) {
		this.combineType = combineType;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setSalesAreaList(List<String> salesAreaList) {
		this.salesAreaList = salesAreaList;
	}

	public List<String> getSalesAreaList() {
		if (salesAreaList == null) {
			setSalesAreaList();
		}
		return salesAreaList;
	}

	public void setSalesAreaList() {
		salesAreaList = new ArrayList<String>();
		if (isNationwide()) {
			return;
		}
		try {
			String[] strs = salesArea.split(Constants.Item.SALES_AREA_SPLIT);
			for (String str : strs) {
				salesAreaList.add(str);
			}
		} catch (Exception e) {
			logger.error("setSalesAreaList error", e);
		}
	}

	public Integer getSupplyFilterType() {
		return supplyFilterType;
	}

	public void setSupplyFilterType(Integer supplyFilterType) {
		this.supplyFilterType = supplyFilterType;
	}

	public Boolean getAssociated() {
		return associated;
	}

	public void setAssociated(Boolean associated) {
		this.associated = associated;
	}

	public Boolean getCanUp() {
		return canUp;
	}

	public void setCanUp(Boolean canUp) {
		this.canUp = canUp;
	}

	public Integer getSalesAreaType() {
		if (isNationwide()) {
			return Constants.Item.SALE_TYPE_NATIONWIDE;
		}
		return Constants.Item.SALE_TYPE_AREA;
	}

	public String getItemSalesAreaDesc() {
		return itemSalesAreaDesc;
	}

	public void setItemSalesAreaDesc(String itemSalesAreaDesc) {
		this.itemSalesAreaDesc = itemSalesAreaDesc;
	}

	public String getStatusDesc() {
		String result = null;
		if (status == null) {
			return null;
		}
		if (status == Constants.Item.STATUS_DEL) {
			result = Constants.Item.STATUS_DEL_DESC;
		} else if (status == Constants.Item.STATUS_DOWN) {
			result = Constants.Item.STATUS_DOWN_DESC;
		} else if (status == Constants.Item.STATUS_INIT) {
			result = Constants.Item.STATUS_INIT_DESC;
		} else if (status == Constants.Item.STATUS_UP) {
			result = Constants.Item.STATUS_UP_DESC;
		}
		return result;
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

	public boolean canUpOperation() {
		if (status == null) {
			return false;
		}
		return status != Constants.Item.STATUS_UP;
	}

	public boolean canDownOperation() {
		if (status == null) {
			return false;
		}
		return status == Constants.Item.STATUS_UP;
	}

	public String getPriceDesc(Integer price) {
		if (price == null) {
			return null;
		}
		Double result = BigDecimalUtils.doubleDiveid(String.valueOf(price));
		return result.toString();
	}

	public String getBizDesc() {
		return Constants.BIZ_MAP.get(bizId);
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

	public String getCarrierTypeDesc() {
		String result = null;
		if (carrierType == null) {
			return null;
		}
		if (Constants.Item.CARRIER_TYPE_UNICOM == carrierType) {
			result = Constants.Item.CARRIER_TYPE_UNICOM_DESC;
		} else if (Constants.Item.CARRIER_TYPE_TELECOM == carrierType) {
			result = Constants.Item.CARRIER_TYPE_TELECOM_DESC;
		} else if (Constants.Item.CARRIER_TYPE_MOBILE == carrierType) {
			result = Constants.Item.CARRIER_TYPE_MOBILE_DESC;
		} else if (Constants.Item.CARRIER_TYPE_OTHER == carrierType) {
			result = Constants.Item.CARRIER_TYPE_OTHER_DESC;
		}
		return result;
	}

	public boolean isNationwide() {
		return StringUtils.isBlank(salesArea);
	}

	/**
	 * 商品是否供货给手机
	 * 
	 * @return
	 */
	public boolean isUidNumber() {
		if (carrierType == null) {
			return false;
		}
		return carrierType != Constants.Item.CARRIER_TYPE_OTHER;
	}

	public boolean isFlowItemUsableArea() {
		if (StringUtils.isBlank(itemExt2) || !StringUtils.isNumeric(itemExt2)) {
			return false;
		}
		return Constants.BizFlow.ITEM_USABLE_AREA == Integer.parseInt(itemExt2);
	}
}