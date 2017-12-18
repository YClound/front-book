package com.longan.getway.biz.common.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class QueryBizOrderResponse extends Response {
	private BizOrderData data;

	public BizOrderData getData() {
		return data;
	}

	public void setData(BizOrderData data) {
		this.data = data;
	}

}
