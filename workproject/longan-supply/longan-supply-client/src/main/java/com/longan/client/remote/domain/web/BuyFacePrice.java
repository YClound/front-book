package com.longan.client.remote.domain.web;

import java.io.Serializable;

public class BuyFacePrice extends BaseBuy implements Serializable {
	private static final long serialVersionUID = 1L;
	private String itemFacePrice;
	private Integer type;
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getItemFacePrice() {
		return itemFacePrice;
	}

	public void setItemFacePrice(String itemFacePrice) {
		this.itemFacePrice = itemFacePrice;
	}

}
