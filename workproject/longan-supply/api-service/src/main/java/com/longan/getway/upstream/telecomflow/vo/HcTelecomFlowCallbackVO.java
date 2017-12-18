package com.longan.getway.upstream.telecomflow.vo;

import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class HcTelecomFlowCallbackVO {
	private final static String privateKey = Utils.getProperty("hcTelecomFlow.privateKey");
	private String coopId;
	private String serviceType;
	private String orderNo;
	private String orderStatus;
	private String orderSuccessTime;
	private String failedCode;
	private String failedReason;
	private String sign;

	private TreeMap<String, String> tempMap = new TreeMap<String, String>();

	public String getCoopId() {
		return coopId;
	}

	public void setCoopId(String coopId) {
		this.coopId = coopId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOrderSuccessTime() {
		return orderSuccessTime;
	}

	public void setOrderSuccessTime(String orderSuccessTime) {
		this.orderSuccessTime = orderSuccessTime;
	}

	public String getFailedCode() {
		return failedCode;
	}

	public void setFailedCode(String failedCode) {
		this.failedCode = failedCode;
	}

	public String getFailedReason() {
		return failedReason;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}

	public void putToTempMap() {
		if (StringUtils.isNotEmpty(serviceType)) {
			tempMap.put("serviceType", serviceType);
		}
		if (StringUtils.isNotEmpty(coopId)) {
			tempMap.put("coopId", coopId);
		}

		if (StringUtils.isNotEmpty(orderNo)) {
			tempMap.put("orderNo", orderNo);
		}
		if (StringUtils.isNotEmpty(orderStatus)) {
			tempMap.put("orderStatus", orderStatus);
		}
	}

	public boolean checkSign() {
		String sign = createSign();
		return sign.equals(getSign());
	}

	public String createSign() {
		StringBuffer sb = new StringBuffer();
		putToTempMap();
		for (Entry<String, String> e : tempMap.entrySet()) {
			sb.append(e.getKey()).append(e.getValue());
		}
		sb.append(privateKey);
		String signStr = Md5Encrypt.md5(sb.toString());
		return signStr;
	}

}
