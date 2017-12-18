package com.longan.getway.upstream.telephone.vo;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class YuanMaiQueryRequestVO {
	public final static String dealerkey = Utils.getProperty("yuanMaiSupply.dealerkey");
	public final static String key = Utils.getProperty("yuanMaiSupply.key");
	private final static String url = Utils.getProperty("yuanMaiSupply.URL");
	private final static String queryAction = Utils.getProperty("yuanMaiSupply.queryAction");
	private final static String queryBalanceAction = Utils
			.getProperty("yuanMaiSupply.queryBalanceAction");

	private final static String dealerid = Utils.getProperty("yuanMaiSupply.dealerid");
	private String orderid;
	private String sign;

	private Map<String, String> tempMap = new TreeMap<String, String>();

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Map<String, String> createParamMap() {
		tempMap.put("dealerid", dealerid);
		if (StringUtils.isNotEmpty(orderid)) {
			tempMap.put("orderid", orderid);
		}
		tempMap.put("sign", sign);
		return tempMap;
	}

	public Map<String, String> createBalanceParamMap() {
		tempMap.put("dealerid", dealerid);
		tempMap.put("sign", sign);
		return tempMap;
	}

	public void createQureySign() {
		StringBuffer sb = new StringBuffer();
		sb.append("dealerid=" + dealerid).append("&orderid=" + orderid)
				.append("&dealerkey=" + dealerkey);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}

	public void createQureyBalanceSign() {
		StringBuffer sb = new StringBuffer();
		sb.append("dealerid=" + dealerid).append(key);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}

	public String createQueryAction() {
		return url + queryAction;
	}

	public String createQueryBalanceAction() {
		return queryBalanceAction;
	}
}
