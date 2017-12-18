package com.longan.getway.upstream.flow.domain;

import java.util.HashMap;
import java.util.Map;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class MaiyuanFlow {
	private final static String url = Utils.getProperty("maiyuanFlow.url");
	private final static String chargeAction = "charge";
	// private final static String queryAction = "";
	private final static String balanceAction = "getBalance";
	private final static String apiKey = Utils.getProperty("maiyuanFlow.key");
	private final static String v = "1.1";
	private final static String account = Utils.getProperty("maiyuanFlow.account");
	

	private String range;
	
	private String mobile;
	private String productId;
	private String sign;
	private String outTradeNo;

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getAccount() {
		return account;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public static String getUrl() {
		return url;
	}

	public static String getChargeaction() {
		return chargeAction;
	}

	public static String getBalanceaction() {
		return balanceAction;
	}

	public static String getApikey() {
		return apiKey;
	}

	public static String getV() {
		return v;
	}

	public void setChargeSign() {
		StringBuffer sb = new StringBuffer("");
		sb.append("account=").append(account).append("&mobile=").append(mobile).append("&package=").append(productId).append("&key=").append(apiKey);
		this.setSign(Md5Encrypt.md5(sb.toString()));
	}

	public Map<String, String> getCommonParams() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("v", v);
		map.put("account", account);
		return map;
	}

	public SupplyResult<String> charge() {
		setChargeSign();
		Map<String, String> map = getCommonParams();
		map.put("action", chargeAction);
		map.put("outTradeNo", outTradeNo);
		map.put("range", range);
		map.put("mobile", mobile);
		map.put("package", productId);
		map.put("sign", sign);

		SupplyResult<String> result = MultiThreadedHttpConnection.getInstance().sendPostByMap(url, map);
		return result;
	}

	public void setBalanceSign() {
		StringBuffer sb = new StringBuffer("");
		sb.append("account=").append(account).append("&key=").append(apiKey);
		this.setSign(Md5Encrypt.md5(sb.toString()));
	}

	public SupplyResult<String> queryBalance() {
		setBalanceSign();
		Map<String, String> map = getCommonParams();
		map.put("action", balanceAction);
		map.put("sign", sign);
		SupplyResult<String> result = MultiThreadedHttpConnection.getInstance().sendPostByMap(url, map);
		return result;
	}

}
