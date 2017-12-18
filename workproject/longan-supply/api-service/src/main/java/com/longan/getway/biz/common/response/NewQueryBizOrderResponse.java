package com.longan.getway.biz.common.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class NewQueryBizOrderResponse extends Response{
	private NewBizOrderData data;

	public NewQueryBizOrderResponse(){
		super();
	}
	public NewQueryBizOrderResponse(Response response){
		super();
		super.setCode(response.getCode());
		super.setDesc(response.getDesc());
		super.setStatus(response.getStatus());
		if(response instanceof QueryBizOrderResponse){
			QueryBizOrderResponse qbor = (QueryBizOrderResponse)response;
			data=new NewBizOrderData(qbor.getData());
		}
	}
	public NewBizOrderData getData() {
		return data;
	}
	public void setData(NewBizOrderData data) {
		this.data = data;
	}
	
}
