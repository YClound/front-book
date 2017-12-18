package com.longan.getway.upstream.telecomflow.vo;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class HcTelecomFlowRequestVO {
	private final static String URL = Utils.getProperty("hcTelecomFlow.URL");
	private final static String serviceTypeCharge = Utils.getProperty("hcTelecomFlow.chargeAction");
	private final static String serviceTypeQuery = Utils.getProperty("hcTelecomFlow.queryAction");
	private final static String privateKey = Utils.getProperty("hcTelecomFlow.privateKey");
	private final static String coopId = Utils.getProperty("hcTelecomFlow.coopId");

	private String serviceType;
	private String orderNo;
	private String phone;
	private String productId;
	private String ts = System.currentTimeMillis() + "";
	private String sign;

	private TreeMap<String, String> paramMap = new TreeMap<String, String>();

	public TreeMap<String, String> getParamMap() {
		return paramMap;
	}

	public void setParamMap(TreeMap<String, String> paramMap) {
		this.paramMap = paramMap;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getCoopId() {
		return coopId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void putToTempMap() {
		if (StringUtils.isNotEmpty(serviceType)) {
			paramMap.put("serviceType", serviceType);
		}
		if (StringUtils.isNotEmpty(coopId)) {
			paramMap.put("coopId", coopId);
		}

		if (StringUtils.isNotEmpty(orderNo)) {
			paramMap.put("orderNo", orderNo);
		}
		if (StringUtils.isNotEmpty(phone)) {
			paramMap.put("phone", phone);
		}
		if (StringUtils.isNotEmpty(productId)) {
			paramMap.put("productId", productId);
		}
		if (StringUtils.isNotEmpty(ts)) {
			paramMap.put("ts", ts);
		}
	}

	public void createSign() {
		putToTempMap();
		StringBuffer sb = new StringBuffer("");
		for (Entry<String, String> e : paramMap.entrySet()) {
			sb.append(e.getKey()).append(e.getValue());
		}
		sb.append(privateKey);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
		paramMap.put("sign", signStr);
	}

	public String getRequestURL() {
		return URL;
	}

	public String createChargeParams() {
		this.setServiceType(serviceTypeCharge);
		createSign();
		StringBuffer sb = new StringBuffer("");
		for (Entry<String, String> e : paramMap.entrySet()) {
			sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
		}
		return sb.toString();
	}

	public String createQueryParams() {
		this.setServiceType(serviceTypeQuery);
		createSign();
		StringBuffer sb = new StringBuffer("");
		for (Entry<String, String> e : paramMap.entrySet()) {
			sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
		}
		return sb.toString();
	}

}
