package com.longan.getway.upstream.flow.vo;

import java.util.TreeSet;

import com.longan.biz.utils.AESUtil;
import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.SHAUtils;
import com.longan.biz.utils.Utils;

public class MoPinFlowRequestVO {
	private final static String url = Utils.getProperty("moPinFlow.URL");
	private final static String chargeAction = Utils.getProperty("moPinFlow.chargeAction");
	private final static String tokenAction = Utils.getProperty("moPinFlow.tokenAction");
	private final static String apiKey = Utils.getProperty("moPinFlow.API_KEY");
	private final static String secretKey = Utils.getProperty("moPinFlow.SECRET_KEY");

	private String cpUser = Utils.getProperty("moPinFlow.CP_USER");;
	private String channelOrderId;
	private String content;
	private String createTime;
	private String type;
	private String amount;
	private String range;
	private String mobile;
	private String notifyUrl = Utils.getProperty("moPinFlow.notifyUrl");

	public String getCpUser() {
		return cpUser;
	}

	public void setCpUser(String cpUser) {
		this.cpUser = cpUser;
	}

	public String getChannelOrderId() {
		return channelOrderId;
	}

	public void setChannelOrderId(String channelOrderId) {
		this.channelOrderId = channelOrderId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String createChargeAction(String reqParam){
		String reqBody = AESUtil.encrypt(reqParam, apiKey);
		String d = Md5Encrypt.md5(reqBody);
		String t = System.currentTimeMillis() + "";

		TreeSet<Object> set = new TreeSet<Object>();
		set.add(cpUser);
		set.add(secretKey);
		set.add(d);
		set.add(t);
		StringBuffer sb = new StringBuffer();
		for (Object obj : set) {
			sb.insert(0, obj);
		}
		String s = SHAUtils.SHA1(sb.toString());
		return url + chargeAction + "d=" + d + "&t=" + t + "&s=" + s + "&a="
				+ cpUser;
	}

	public String createReqBody(String reqParam){
		return AESUtil.encrypt(reqParam, apiKey);
	}

	public String createTokenAction() {
		return url + tokenAction;
	}
}
