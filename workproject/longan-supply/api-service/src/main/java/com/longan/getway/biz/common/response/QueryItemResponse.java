package com.longan.getway.biz.common.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class QueryItemResponse extends Response {
	private ItemData data;

	public ItemData getData() {
		return data;
	}

	public void setData(ItemData data) {
		this.data = data;
	}

}
