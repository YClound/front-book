package com.longan.getway.upstream.flow.vo;

import java.util.Map;
import java.util.TreeMap;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class YuanMaiFlowRequestVO {
	private final static String url = Utils.getProperty("yuanMaiFlow.URL");
	private final static String chargeAction = Utils.getProperty("yuanMaiFlow.chargeAction");
	private final static String queryAction = Utils.getProperty("yuanMaiFlow.queryAction");
	private final static String key = Utils.getProperty("yuanMaiFlow.key");
	private final static String queryBalanceAction = Utils
			.getProperty("yuanMaiFlow.queryBalanceAction");

	private final static String dealerid = Utils.getProperty("yuanMaiFlow.dealerid");
	private String orderid;
	private final static String chargetype = "1";
	private String photonum;
	private String flowsize;
	private String ordertime;
	private final static String mark = "";
	private String sign;

	private Map<String, String> tempMap = new TreeMap<String, String>();

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

	public String getFlowsize() {
		return flowsize;
	}

	public void setFlowsize(String flowsize) {
		this.flowsize = flowsize;
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

	public Map<String, String> createChargeParamMap() {
		tempMap.put("dealerid", dealerid);
		tempMap.put("orderid", orderid);
		tempMap.put("chargetype", chargetype);
		tempMap.put("photonum", photonum);
		tempMap.put("flowsize", flowsize);
		tempMap.put("ordertime", ordertime);
		tempMap.put("mark", mark);
		StringBuffer sb = new StringBuffer("");
		for (Map.Entry<String, String> e : tempMap.entrySet()) {
			sb.append("&" + e.getKey() + "=");
			sb.append(e.getValue());
		}
		sb.append(key);
		String signStr = sb.toString().substring(1);
		setSign(Md5Encrypt.md5(signStr));
		tempMap.put("sign", sign);
		return tempMap;
	}

	public String createChargeAction() {
		return url + chargeAction;
	}

	public Map<String, String> createQueryParamMap() {
		tempMap.put("dealerid", dealerid);
		tempMap.put("orderid", orderid);
		StringBuffer sb = new StringBuffer("");
		for (Map.Entry<String, String> e : tempMap.entrySet()) {
			sb.append("&" + e.getKey() + "=");
			sb.append(e.getValue());
		}
		sb.append(key);
		String signStr = sb.toString().substring(1);
		setSign(Md5Encrypt.md5(signStr));
		tempMap.put("sign", sign);
		return tempMap;
	}

	public String createQueryAction() {
		return url + queryAction;
	}

	public Map<String, String> createBalanceParamMap() {
		StringBuffer sb = new StringBuffer();
		sb.append("dealerid=" + dealerid).append(key);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);

		tempMap.put("dealerid", dealerid);
		tempMap.put("sign", sign);
		return tempMap;
	}

	public String createQueryBalanceAction() {
		return url + queryBalanceAction;
	}
}
