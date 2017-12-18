package com.longan.getway.biz.common.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class QueryGameResponse extends Response {

	private GameData data;

	public GameData getData() {
		return data;
	}

	public void setData(GameData data) {
		this.data = data;
	}

}
