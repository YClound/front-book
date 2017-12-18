package com.longan.getway.upstream.telecomflow.vo;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class SuKaTelecomFlowRequestVO {
	private final static String url = Utils.getProperty("suKaTelecomFlow.URL");
	private final static String chargeAction = Utils.getProperty("suKaTelecomFlow.chargeAction");
	private final static String queryAction = Utils.getProperty("suKaTelecomFlow.queryAction");

	private final static String key = Utils.getProperty("suKaTelecomFlow.key");
	private final static String userid = Utils.getProperty("suKaTelecomFlow.userid");
	private final static String back_url = Utils.getProperty("suKaTelecomFlow.back_url");
	private String productid;
	private String price;
	private final static String num = "1";
	private String mobile;
	private String spordertime;
	private String sporderid;
	private String sign;

	private Map<String, String> tempMap = new TreeMap<String, String>();

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSpordertime() {
		return spordertime;
	}

	public void setSpordertime(String spordertime) {
		this.spordertime = spordertime;
	}

	public String getSporderid() {
		return sporderid;
	}

	public void setSporderid(String sporderid) {
		this.sporderid = sporderid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Map<String, String> createChargeParamMap() {
		tempMap.put("userid", userid);
		if (StringUtils.isNotEmpty(productid)) {
			tempMap.put("productid", productid);
		}
		if (StringUtils.isNotEmpty(price)) {
			tempMap.put("price", price);
		}
		tempMap.put("num", num);
		if (StringUtils.isNotEmpty(mobile)) {
			tempMap.put("mobile", mobile);
		}
		if (StringUtils.isNotEmpty(spordertime)) {
			tempMap.put("spordertime", spordertime);
		}
		if (StringUtils.isNotEmpty(sporderid)) {
			tempMap.put("sporderid", sporderid);
		}
		tempMap.put("sign", sign);
		tempMap.put("back_url", back_url);
		return tempMap;
	}

	public Map<String, String> createQueryParamMap() {
		tempMap.put("userid", userid);
		if (StringUtils.isNotEmpty(sporderid)) {
			tempMap.put("sporderid", sporderid);
		}
		tempMap.put("sign", sign);
		return tempMap;
	}

	public void createChargeSign() {

		StringBuffer sb = new StringBuffer();
		sb.append("userid=" + userid).append("&productid=" + productid).append("&price=" + price)
				.append("&num=" + num).append("&mobile=" + mobile)
				.append("&spordertime=" + spordertime).append("&sporderid=" + sporderid)
				.append("&key=" + key);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}

	public void createQuerySign() {
		StringBuffer sb = new StringBuffer();
		sb.append("userid=" + userid).append("&sporderid=" + sporderid).append("&key=" + key);// userid=10001704&sporderid=123&key=jldjlefjljfdlnfslnsdljfweojfewndlfsdljf8w23u0u2r032usdfjslfdfdjflsdjfw8775
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}

	public String createChargeAction() {
		return url + chargeAction;
	}

	public String createQueryAction() {
		return url + queryAction;
	}
}
