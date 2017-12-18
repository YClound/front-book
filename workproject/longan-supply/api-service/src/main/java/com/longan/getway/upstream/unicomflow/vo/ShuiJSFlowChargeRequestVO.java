package com.longan.getway.upstream.unicomflow.vo;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.utils.ShuiJSMd5;
import com.longan.biz.utils.Utils;

public class ShuiJSFlowChargeRequestVO {
	private static String key = Utils.getProperty("unicomFlowShuiJS.key");
	private static String chargeAction = Utils.getProperty("unicomFlowShuiJS.chargeAction");
	private static String queryAction = Utils.getProperty("unicomFlowShuiJS.queryAction");
	private static String url = Utils.getProperty("unicomFlowShuiJS.URL");
	private static String activityId = Utils.getProperty("unicomFlowShuiJS.activityId");

	// private static final JsonConfig jc = new JsonConfig();
	private String productId;
	private String phoneNum;
	private String realPrice;
	private String token;

	private Map<String, Object> tempMap = new TreeMap<String, Object>();

	/*
	 * static { jc.addIgnoreFieldAnnotation(JsonIgnore.class); }
	 */

	public String getActivityId() {
		return activityId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(String realPrice) {
		this.realPrice = realPrice;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String createChargeAciton() {
		return url + chargeAction;
	}

	public String createToken() {
		StringBuffer sb = new StringBuffer();
		sb.append(key).append(phoneNum).append(productId).append(activityId).append(realPrice);
		String tokenStr = ShuiJSMd5.md5(sb.toString());
		return tokenStr;
	}

	private void putToTreeMap() {

		if (productId != null) {
			tempMap.put("productId", productId);
		}
		if (StringUtils.isNotEmpty(activityId)) {
			tempMap.put("activityId", activityId);
		}
		if (StringUtils.isNotEmpty(phoneNum)) {
			tempMap.put("phoneNum", phoneNum);
		}
		if (StringUtils.isNotEmpty(realPrice)) {
			tempMap.put("realPrice", realPrice);
		}
	}

	public String getRequesytParam() {
		putToTreeMap();
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Object> e : tempMap.entrySet()) {
			sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
		}
		createToken();
		sb.append("token=").append(createToken());
		return sb.toString();
	}

	public String getChargeURL() {
		return url + chargeAction + getRequesytParam();
	}

	public String getQueryURL(String UpstreamSerialno) {
		return url + queryAction + "&orderNo=" + UpstreamSerialno;
	}

	public static void main(String[] args) {
		ShuiJSFlowChargeRequestVO vo = new ShuiJSFlowChargeRequestVO();

		System.out.println(vo.getQueryURL("2014072916384421"));
	}
}
