package com.longan.getway.upstream.telephone.vo;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class YuanMaiChargeRequestVO {
	public final static String dealerkey = Utils.getProperty("yuanMaiSupply.dealerkey");
	private final static String url = Utils.getProperty("yuanMaiSupply.URL");
	private final static String chargeAction = Utils.getProperty("yuanMaiSupply.chargeAction");

	private final static String ispid = "";
	private String amount;
	private final static String dealerid = Utils.getProperty("yuanMaiSupply.dealerid");
	private String orderid;
	private String photonum;
	private String ordertime;
	private final static String mark = "";
	private String sign;

	private Map<String, String> tempMap = new TreeMap<String, String>();

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDealerid() {
		return dealerid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getPhotonum() {
		return photonum;
	}

	public void setPhotonum(String photonum) {
		this.photonum = photonum;
	}

	public String getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public static String getIspid() {
		return ispid;
	}

	public static String getMark() {
		return mark;
	}

	public Map<String, String> createParamMap() {
		tempMap.put("ispid", ispid);
		if (StringUtils.isNotEmpty(amount)) {
			tempMap.put("amount", amount);
		}
		tempMap.put("dealerid", dealerid);
		if (StringUtils.isNotEmpty(orderid)) {
			tempMap.put("orderid", orderid);
		}
		if (StringUtils.isNotEmpty(photonum)) {
			tempMap.put("photonum", photonum);
		}
		if (StringUtils.isNotEmpty(ordertime)) {
			tempMap.put("ordertime", ordertime);
		}
		tempMap.put("mark", mark);
		tempMap.put("sign", sign);
		return tempMap;
	}

	public void createChargeSign() {

		StringBuffer sb = new StringBuffer();
		sb.append("ispid=" + ispid).append("&amount=" + amount).append("&dealerid=" + dealerid)
				.append("&orderid=" + orderid).append("&photonum=" + photonum)
				.append("&ordertime=" + ordertime).append("&mark=" + mark)
				.append("&dealerkey=" + dealerkey);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}

	public String createChargeAciton() {
		return url + chargeAction;
	}
}
