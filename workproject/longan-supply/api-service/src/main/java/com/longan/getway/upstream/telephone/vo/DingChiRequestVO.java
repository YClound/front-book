package com.longan.getway.upstream.telephone.vo;

import java.util.Map;
import java.util.TreeMap;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class DingChiRequestVO {
	public final static String privateKey = Utils.getProperty("dingChiSupply.privateKey");
	private final static String url = Utils.getProperty("dingChiSupply.URL");
	private final static String chargeAction = Utils.getProperty("dingChiSupply.chargeAction");
	private final static String queryAction = Utils.getProperty("dingChiSupply.queryAction");
	private final static String queryBalanceAction = Utils
			.getProperty("dingChiSupply.queryBalanceAction");

	private final static String userId = Utils.getProperty("dingChiSupply.userId");
	private String itemId;
	private String serialno;
	private String dtCreate;
	private String uid;
	private String sign;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getDtCreate() {
		return dtCreate;
	}

	public void setDtCreate(String dtCreate) {
		this.dtCreate = dtCreate;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String createChargeParam() {
		String reqStr = "userId=" + userId + "&uid=" + uid + "&sign=" + sign + "&itemId=" + itemId
				+ "&serialno=" + serialno + "&dtCreate=" + dtCreate;
		return url + chargeAction + reqStr;
	}

	public void createChargeSign() {
		TreeMap<String, String> map = new TreeMap<String, String>();
		map.put("userId", userId);
		map.put("uid", uid);
		map.put("serialno", serialno);
		map.put("itemId", itemId);
		map.put("dtCreate", dtCreate);
		StringBuffer sb = new StringBuffer("");
		for (Map.Entry<String, String> e : map.entrySet()) {
			sb.append(e.getValue());
		}
		sb.append(privateKey);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}

	public String createQueryParam() {
		String reqStr = "userId=" + userId + "&sign=" + sign + "&serialno=" + serialno;
		return url + queryAction + reqStr;
	}

	public void createQuerySign() {
		TreeMap<String, String> map = new TreeMap<String, String>();
		map.put("userId", userId);
		map.put("serialno", serialno);
		StringBuffer sb = new StringBuffer("");
		for (Map.Entry<String, String> e : map.entrySet()) {
			sb.append(e.getValue());
		}
		sb.append(privateKey);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}

	public String createQueryBalanceParam() {
		String reqStr = "userId=" + userId + "&sign=" + sign;
		return url + queryBalanceAction + reqStr;
	}

	public void createQueryBalanceSign() {
		StringBuffer sb = new StringBuffer("");
		sb.append(userId).append(privateKey);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}
}
