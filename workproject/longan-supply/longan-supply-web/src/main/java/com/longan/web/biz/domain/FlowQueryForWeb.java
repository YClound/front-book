package com.longan.web.biz.domain;

import java.util.Map;

import com.longan.biz.dataobject.MobileBelong;
import com.longan.client.remote.domain.web.ItemForQuery;

public class FlowQueryForWeb {
	private MobileBelong mobileBelong;

	private Map<Integer, Map<String, ItemForQuery>> itemMap;

	public MobileBelong getMobileBelong() {
		return mobileBelong;
	}

	public void setMobileBelong(MobileBelong mobileBelong) {
		this.mobileBelong = mobileBelong;
	}

	public Map<Integer, Map<String, ItemForQuery>> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<Integer, Map<String, ItemForQuery>> itemMap) {
		this.itemMap = itemMap;
	}

}
