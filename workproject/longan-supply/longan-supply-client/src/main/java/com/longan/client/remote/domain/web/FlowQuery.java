package com.longan.client.remote.domain.web;

import java.io.Serializable;
import java.util.Map;

import com.longan.biz.dataobject.MobileBelong;

public class FlowQuery implements Serializable {
	private static final long serialVersionUID = 1L;

	private MobileBelong mobileBelong;

	private Map<String, ItemForQuery> itemMap;

	public MobileBelong getMobileBelong() {
		return mobileBelong;
	}

	public void setMobileBelong(MobileBelong mobileBelong) {
		this.mobileBelong = mobileBelong;
	}

	public Map<String, ItemForQuery> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, ItemForQuery> itemMap) {
		this.itemMap = itemMap;
	}
}
