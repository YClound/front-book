package com.longan.getway.upstream.flow.domain;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class BaoziFlow {
	public final static String url= Utils.getProperty("baoziFlow.url");
	public final static String chargeAction=Utils.getProperty("baoziFlow.chargeAction");
	public final static String queryAction=Utils.getProperty("baoziFlow.queryAction");
	public final static String queryBalance=Utils.getProperty("baoziFlow.queryAction");
	public final static String userId=Utils.getProperty("baoziFlow.userId");
	public final static String key=Utils.getProperty("baoziFlow.key");
	private final static String back_url = Utils.getProperty("suKaTelecomFlow.back_url");
	
	private String number;
	private String goods;
	private String price;
	private final static String amount="1";
	private String order_no;
	private String create_time;
	private String attach1;
	private String attach2;
	private String sign;
	


	private Map<String, String> tempMap = new TreeMap<String, String>();

	
	public String createChargeUrl() {
		StringBuffer sb=createChargeParam();
		createChargeSign();
		sb.append("sign="+sign);
		String geturl = url+chargeAction+"?"+sb.toString();
		return geturl;
	}

	public Map<String, String> createQueryParamMap() {
		tempMap.put("userId", userId);
		if (StringUtils.isNotEmpty(order_no)) {
			tempMap.put("order_no", order_no);
		}
		tempMap.put("sign", sign);
		return tempMap;
	}

	private void createChargeSign() {
		StringBuffer sb = new StringBuffer();
		sb.append(amount);
		if(StringUtils.isNotEmpty("attach1")) sb.append(attach1);
		if(StringUtils.isNotEmpty("attach2")) sb.append(attach2);
		if(StringUtils.isNotEmpty("create_time")) sb.append(create_time);
		if(StringUtils.isNotEmpty("goods")) sb.append(goods);
		if(StringUtils.isNotEmpty("number")) sb.append(number);
		if(StringUtils.isNotEmpty("order_no")) sb.append(order_no);
		if(StringUtils.isNotEmpty("price")) sb.append(price);
		if(StringUtils.isNotEmpty("userId")) sb.append(userId);
		if(StringUtils.isNotEmpty("key")) sb.append(key);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}
	private StringBuffer createChargeParam(){
		StringBuffer sb = new StringBuffer();
		sb.append("amount="+amount);
		if(StringUtils.isNotEmpty("attach1")) sb.append("attach1="+attach1);
		if(StringUtils.isNotEmpty("attach2")) sb.append("attach2="+attach2);
		if(StringUtils.isNotEmpty("create_time")) sb.append("create_time="+create_time);
		if(StringUtils.isNotEmpty("goods")) sb.append("goods="+goods);
		if(StringUtils.isNotEmpty("number")) sb.append("number="+number);
		if(StringUtils.isNotEmpty("order_no")) sb.append("order_no="+order_no);
		if(StringUtils.isNotEmpty("price")) sb.append("price="+price);
		if(StringUtils.isNotEmpty("userId")) sb.append("userId="+userId);
		return sb;
	}
	public void createQuerySign() {
		StringBuffer sb = new StringBuffer();
		sb.append("userId=" + userId).append("&order_no=" + order_no).append("&key=" + key);// userid=10001704&order_no=123&key=jldjlefjljfdlnfslnsdljfweojfewndlfsdljf8w23u0u2r032usdfjslfdfdjflsdjfw8775
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}

	public String createChargeAction() {
		return url + chargeAction;
	}

	public String createQueryAction() {
		return url + queryAction;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getAttach1() {
		return attach1;
	}

	public void setAttach1(String attach1) {
		this.attach1 = attach1;
	}

	public String getAttach2() {
		return attach2;
	}

	public void setAttach2(String attach2) {
		this.attach2 = attach2;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public static String getAmount() {
		return amount;
	}
}