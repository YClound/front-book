package com.longan.getway.biz.common.response;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class ListData extends BaseData {
	private List<Object> data;

	@XmlElements({ @XmlElement(name = "gameCarrier", type = GameCarrierData.class),
			@XmlElement(name = "game", type = GameData.class) ,
			@XmlElement(name = "gameItem", type = GameItemData.class) })
	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}

}
