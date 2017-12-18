package com.longan.getway.upstream.telephone.vo;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class DuanFengRequestVO {
	private final static String url = Utils.getProperty("duanFengSupply.URL");
	private final static String mykey = Utils.getProperty("duanFengSupply.mykey");

	private String SkType;
	private final static String SCono = Utils.getProperty("duanFengSupply.SCono");
	private String SOutTrans;
	private final static String SBackUrl = Utils.getProperty("duanFengSupply.SBackUrl");
	private String SPhoneNum;
	private String SPhoneMoney;
	private final static String STypeID = "0";
	private String SEnCode;

	private Map<String, String> tempMap = new TreeMap<String, String>();

	public String getSkType() {
		return SkType;
	}

	public void setSkType(String skType) {
		SkType = skType;
	}

	public String getSOutTrans() {
		return SOutTrans;
	}

	public void setSOutTrans(String sOutTrans) {
		SOutTrans = sOutTrans;
	}

	public String getSPhoneNum() {
		return SPhoneNum;
	}

	public void setSPhoneNum(String sPhoneNum) {
		SPhoneNum = sPhoneNum;
	}

	public String getSPhoneMoney() {
		return SPhoneMoney;
	}

	public void setSPhoneMoney(String sPhoneMoney) {
		SPhoneMoney = sPhoneMoney;
	}

	public String getSEnCode() {
		return SEnCode;
	}

	public void setSEnCode(String sEnCode) {
		SEnCode = sEnCode;
	}

	public Map<String, String> createChargeParam() {
		setSkType("1");
		setSEnCode(Md5Encrypt.md5(SCono + SOutTrans + STypeID + SPhoneNum + SPhoneMoney + mykey)
				.toUpperCase());

		if (StringUtils.isNotEmpty(SkType)) {
			tempMap.put("SkType", SkType);
		}
		tempMap.put("SCono", SCono);
		if (StringUtils.isNotEmpty(SOutTrans)) {
			tempMap.put("SOutTrans", SOutTrans);
		}
		tempMap.put("SBackUrl", SBackUrl);
		if (StringUtils.isNotEmpty(SPhoneNum)) {
			tempMap.put("SPhoneNum", SPhoneNum);
		}
		if (StringUtils.isNotEmpty(SPhoneMoney)) {
			tempMap.put("SPhoneMoney", SPhoneMoney);
		}
		tempMap.put("STypeID", STypeID);
		if (StringUtils.isNotEmpty(SEnCode)) {
			tempMap.put("SEnCode", SEnCode);
		}
		return tempMap;
	}

	public Map<String, String> createQueryParam() {
		setSkType("2");
		setSEnCode(Md5Encrypt.md5(SCono + SOutTrans + mykey).toUpperCase());
		if (StringUtils.isNotEmpty(SkType)) {
			tempMap.put("SkType", SkType);
		}
		tempMap.put("SCono", SCono);
		if (StringUtils.isNotEmpty(SOutTrans)) {
			tempMap.put("SOutTrans", SOutTrans);
		}
		if (StringUtils.isNotEmpty(SEnCode)) {
			tempMap.put("SEnCode", SEnCode);
		}
		return tempMap;
	}

	public Map<String, String> createQueryBalanceParam() {
		setSkType("3");
		setSEnCode(Md5Encrypt.md5(SCono + SOutTrans + mykey).toUpperCase());
		if (StringUtils.isNotEmpty(SkType)) {
			tempMap.put("SkType", SkType);
		}
		tempMap.put("SCono", SCono);
		if (StringUtils.isNotEmpty(SOutTrans)) {
			tempMap.put("SOutTrans", SOutTrans);
		}
		if (StringUtils.isNotEmpty(SEnCode)) {
			tempMap.put("SEnCode", SEnCode);
		}
		return tempMap;
	}

	public String createRequestUrl() {
		return url;
	}
}
