package com.longan.getway.biz.common.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class NewBuyResponse extends Response{
	private String orderno;//serialno
	private String serviceorder;//bizOrderId
	private String goods;//itemId
	private String goodsname;//itemName
	private String goodsfaceprice;//itemFacePrice
	private String goodsprice;//price
	private String amount;//amount
	private String carrier;//carrierType
	private String areacode;//areaCode
	
	public NewBuyResponse(){
		super();
	}
	
	public NewBuyResponse(Response response){
		super();
		super.setCode(response.getCode());
		super.setStatus(response.getStatus());
		super.setDesc(response.getDesc());
		if(response instanceof BuyResponse){
			BuyResponse buyResponse = (BuyResponse)response;
			this.orderno=buyResponse.getSerialno();
			this.serviceorder=buyResponse.getBizOrderId();
			this.goods=buyResponse.getItemId();
			this.goodsfaceprice=buyResponse.getItemFacePrice();
			this.goodsprice=buyResponse.getPrice();
			this.amount=buyResponse.getAmount();
			this.carrier=buyResponse.getCarrierType();
			this.areacode=buyResponse.getAreaCode();
		}
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getServiceorder() {
		return serviceorder;
	}

	public void setServiceorder(String serviceorder) {
		this.serviceorder = serviceorder;
	}

	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public String getGoodsfaceprice() {
		return goodsfaceprice;
	}

	public void setGoodsfaceprice(String goodsfaceprice) {
		this.goodsfaceprice = goodsfaceprice;
	}

	public String getGoodsprice() {
		return goodsprice;
	}

	public void setGoodsprice(String goodsprice) {
		this.goodsprice = goodsprice;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
}
