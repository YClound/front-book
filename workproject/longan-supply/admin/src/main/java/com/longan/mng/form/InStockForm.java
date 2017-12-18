package com.longan.mng.form;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

public class InStockForm {
	@NotBlank(message = "供货商品不能为空")
	private String itemSupplyId;

	@RegExp(value = "[1-9]\\d+", message = "金额格式不对")
	private String itemCostPrice;

	private String supplyTraderId;
	
	@NotBlank(message = "业务编号不能为空")
	private String bizId;

	public String getItemSupplyId() {
		return itemSupplyId;
	}

	public void setItemSupplyId(String itemSupplyId) {
		this.itemSupplyId = itemSupplyId;
	}

	public String getItemCostPrice() {
		return itemCostPrice;
	}

	public void setItemCostPrice(String itemCostPrice) {
		this.itemCostPrice = itemCostPrice;
	}

	public String getSupplyTraderId() {
		return supplyTraderId;
	}

	public void setSupplyTraderId(String supplyTraderId) {
		this.supplyTraderId = supplyTraderId;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	
	

}
