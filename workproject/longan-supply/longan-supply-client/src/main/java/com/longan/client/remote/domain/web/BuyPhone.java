package com.longan.client.remote.domain.web;

import java.io.Serializable;

public class BuyPhone extends BaseBuy implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long itemFacePrice;

	public Long getItemFacePrice() {
		return itemFacePrice;
	}

	public void setItemFacePrice(Long itemFacePrice) {
		this.itemFacePrice = itemFacePrice;
	}

}
