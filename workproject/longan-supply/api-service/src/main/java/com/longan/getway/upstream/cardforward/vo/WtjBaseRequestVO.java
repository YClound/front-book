package com.longan.getway.upstream.cardforward.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.utils.Utils;

public class WtjBaseRequestVO {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected static final String DESKey = Utils.getProperty("wtjCardForward.desKey");
	protected static final String URL = Utils.getProperty("wtjCardForward.URL");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private static String secKey; // 登录后生成
	private String sign;
	private String clientIp = Utils.getProperty("service.ip");
	private String timeStamp = sdf.format(new Date());

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getDESKey() {
		return DESKey;
	}

	public String getSecKey() {
		return secKey;
	}

	public void setSecKey(String secKey) {
		WtjBaseRequestVO.secKey = secKey;
	}

}
