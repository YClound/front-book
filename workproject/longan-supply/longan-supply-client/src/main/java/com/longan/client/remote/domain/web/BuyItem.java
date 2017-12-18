package com.longan.client.remote.domain.web;

import java.io.Serializable;

public class BuyItem extends BaseBuy implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer itemId;
	private Integer amt;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getAmt() {
		return amt;
	}

	public void setAmt(Integer amt) {
		this.amt = amt;
	}

}
