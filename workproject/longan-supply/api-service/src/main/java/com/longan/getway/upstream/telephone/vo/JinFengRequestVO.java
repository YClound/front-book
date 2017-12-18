package com.longan.getway.upstream.telephone.vo;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.utils.HmacEncrypt;
import com.longan.biz.utils.Utils;

public class JinFengRequestVO {
	private final static String key = Utils.getProperty("jinFengSupply.key");
	private final static String chargeAction = Utils.getProperty("jinFengSupply.chargeAction");
	private final static String queryAction = Utils.getProperty("jinFengSupply.queryAction");
	private final static String queryBalanceAction = Utils
			.getProperty("jinFengSupply.queryBalanceAction");
	private final static String url = Utils.getProperty("jinFengSupply.URL");
	
	private final static String queryUrl = Utils.getProperty("jinFengSupply.queryURL");

	private final static String P0_biztype = "mobiletopup";
	private final static String P1_agentcode = Utils.getProperty("jinFengSupply.agentcode");
	private String P2_mobile;
	private String P3_parvalue;
	private String P4_productcode;
	private String P5_requestid;
	private final static String P6_callbackurl = Utils.getProperty("jinFengSupply.callbackurl");
	private final static String P7_extendinfo = "";
	private String hmac;

	private Map<String, String> tempMap = new TreeMap<String, String>();

	public String getP2_mobile() {
		return P2_mobile;
	}

	public void setP2_mobile(String p2_mobile) {
		P2_mobile = p2_mobile;
	}

	public String getP3_parvalue() {
		return P3_parvalue;
	}

	public void setP3_parvalue(String p3_parvalue) {
		P3_parvalue = p3_parvalue;
	}

	public String getP4_productcode() {
		return P4_productcode;
	}

	public void setP4_productcode(String p4_productcode) {
		P4_productcode = p4_productcode;
	}

	public String getP5_requestid() {
		return P5_requestid;
	}

	public void setP5_requestid(String p5_requestid) {
		P5_requestid = p5_requestid;
	}

	public String getHmac() {
		return hmac;
	}

	public void setHmac(String hmac) {
		this.hmac = hmac;
	}

	public Map<String, String> createChargeParam() {
		tempMap.put("P0_biztype", P0_biztype);
		tempMap.put("P1_agentcode", P1_agentcode);
		if (StringUtils.isNotEmpty(P2_mobile)) {
			tempMap.put("P2_mobile", P2_mobile);
		}
		if (StringUtils.isNotEmpty(P3_parvalue)) {
			tempMap.put("P3_parvalue", P3_parvalue);
		}
		if (StringUtils.isNotEmpty(P4_productcode)) {
			tempMap.put("P4_productcode", P4_productcode);
		}
		if (StringUtils.isNotEmpty(P5_requestid)) {
			tempMap.put("P5_requestid", P5_requestid);
		}
		tempMap.put("P6_callbackurl", P6_callbackurl);
		tempMap.put("P7_extendinfo", P7_extendinfo);
		if (StringUtils.isNotEmpty(hmac)) {
			tempMap.put("hmac", hmac);
		}
		return tempMap;
	}

	public void createChargeHmac() {

		StringBuffer sb = new StringBuffer();
		sb.append(P0_biztype).append(P1_agentcode).append(P2_mobile).append(P3_parvalue)
				.append(P4_productcode).append(P5_requestid).append(P6_callbackurl)
				.append(P7_extendinfo);
		String hmacStr = HmacEncrypt.hmacSign(sb.toString(), key);
		setHmac(hmacStr);
	}

	public String createChargeAction() {
		return url + chargeAction;
	}

	public Map<String, String> createQueryParam() {
		tempMap.put("P1_agentcode", P1_agentcode);
		if (StringUtils.isNotEmpty(P5_requestid)) {
			tempMap.put("P5_requestid", P5_requestid);
		}
		if (StringUtils.isNotEmpty(hmac)) {
			tempMap.put("hmac", hmac);
		}
		return tempMap;
	}

	public void createQueryHmac() {

		StringBuffer sb = new StringBuffer();
		sb.append(P1_agentcode).append(P5_requestid);
		String hmacStr = HmacEncrypt.hmacSign(sb.toString(), key);
		setHmac(hmacStr);
	}

	public String createQueryAction() {
		return queryUrl + queryAction;
	}

	public Map<String, String> createQueryBalanceParam() {
		tempMap.put("P1_agentcode", P1_agentcode);
		if (StringUtils.isNotEmpty(hmac)) {
			tempMap.put("hmac", hmac);
		}
		return tempMap;
	}

	public void createQueryBalanceHmac() {

		StringBuffer sb = new StringBuffer();
		sb.append(P1_agentcode);
		String hmacStr = HmacEncrypt.hmacSign(sb.toString(), key);
		setHmac(hmacStr);
	}

	public String createQueryBalanceAction() {
		return url + queryBalanceAction;
	}
}
