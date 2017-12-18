package com.longan.getway.biz.common.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class QueryGameItemResponse extends Response {

	private GameItemData data;

	public GameItemData getData() {
		return data;
	}

	public void setData(GameItemData data) {
		this.data = data;
	}

}
