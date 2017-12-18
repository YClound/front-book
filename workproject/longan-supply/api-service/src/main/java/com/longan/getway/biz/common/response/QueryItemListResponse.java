package com.longan.getway.biz.common.response;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class QueryItemListResponse extends Response {
	private List<ItemData> data;

	public List<ItemData> getData() {
		return data;
	}

	public void setData(List<ItemData> data) {
		this.data = data;
	}

}
