package com.longan.web.core.form;

import com.longan.biz.utils.Constants;

public class MultiFlowRechargeForm extends BasePayForm {
	private String flow;
	private String usableArea;

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public String getUsableArea() {
		return usableArea;
	}

	public void setUsableArea(String usableArea) {
		this.usableArea = usableArea;
	}

	public String getUsableAreaDesc() {
		if ((Constants.BizFlow.ITEM_USABLE_NATIONWIDE + "").equals(usableArea)) {
			return Constants.BizFlow.ITEM_USABLE_NATIONWIDE_DESC;
		} else if ((Constants.BizFlow.ITEM_USABLE_AREA + "").equals(usableArea)) {
			return Constants.BizFlow.ITEM_USABLE_AREA_DESC;
		}
		return null;
	}
}
