package com.longan.getway.biz.common.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class QueryGameItemListResponse extends Response {

	private ListData datas;

	public ListData getDatas() {
		return datas;
	}

	public void setDatas(ListData datas) {
		this.datas = datas;
	}
}
