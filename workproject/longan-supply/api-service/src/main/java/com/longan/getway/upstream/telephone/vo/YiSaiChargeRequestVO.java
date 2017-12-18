package com.longan.getway.upstream.telephone.vo;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class YiSaiChargeRequestVO {
	public final static String key = Utils.getProperty("yiSaiSupply.key");
	private final static String url = Utils.getProperty("yiSaiSupply.URL");
	private final static String chargeAction = Utils.getProperty("yiSaiSupply.chargeAction");

	private final static String userNumber = Utils.getProperty("yiSaiSupply.UserNumber");
	private String inOrderNumber;
	private final static String outOrderNumber = "None";
	private String phoneNumber;
	private final static String province = "None";
	private final static String city = "None";
	private final static String phoneClass = "None";
	private String phoneMoney;
	private final static String sellPrice = "None";
	private String startTime;
	private final static String timeOut = "7000";
	private String recordKey;
	private final static String remark = "HELLO WORLD!";

	private Map<String, String> tempMap = new TreeMap<String, String>();

	public String getInOrderNumber() {
		return inOrderNumber;
	}

	public void setInOrderNumber(String inOrderNumber) {
		this.inOrderNumber = inOrderNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneMoney() {
		return phoneMoney;
	}

	public void setPhoneMoney(String phoneMoney) {
		this.phoneMoney = phoneMoney;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getRecordKey() {
		return recordKey;
	}

	public void setRecordKey(String recordKey) {
		this.recordKey = recordKey;
	}

	public void createParam(String phoneNumber, String phoneMoney) {
		@SuppressWarnings("static-access")
		String time1 = new DateFormatUtils().format(new Date(), "yyyyMMddHHmmss");
		@SuppressWarnings("static-access")
		String time2 = new DateFormatUtils().format(new Date(), "yyyy-MM-dd HH:mm:ss");
		// 随机4位数
		int rd = (int) (Math.random() * 9000 + 1000);

		setInOrderNumber("IP" + userNumber + time1 + rd);
		setPhoneMoney(phoneMoney);
		setPhoneNumber(phoneNumber);
		setStartTime(time2);
		setRecordKey(createChargeSign());
	}

	public String createChargeSign() {
		String keyStr = userNumber + inOrderNumber + outOrderNumber + phoneNumber + province + city
				+ phoneClass + phoneMoney + sellPrice + startTime + timeOut + key;
		String str = Md5Encrypt.md5(keyStr);
		String recordKeyStr = str.substring(0, 16).toUpperCase();// 取前16位转大写
		return recordKeyStr;
	}

	public Map<String, String> createParamMap() {
		tempMap.put("UserNumber", userNumber);
		if (StringUtils.isNotEmpty(inOrderNumber)) {
			tempMap.put("InOrderNumber", inOrderNumber);
		}
		tempMap.put("OutOrderNumber", outOrderNumber);
		if (StringUtils.isNotEmpty(phoneNumber)) {
			tempMap.put("PhoneNumber", phoneNumber);
		}
		tempMap.put("Province", province);
		tempMap.put("City", city);
		tempMap.put("PhoneClass", phoneClass);
		if (StringUtils.isNotEmpty(phoneMoney)) {
			tempMap.put("PhoneMoney", phoneMoney);
		}
		tempMap.put("SellPrice", sellPrice);
		if (StringUtils.isNotEmpty(startTime)) {
			tempMap.put("StartTime", startTime);
		}
		tempMap.put("TimeOut", timeOut);
		if (StringUtils.isNotEmpty(recordKey)) {
			tempMap.put("RecordKey", recordKey);
		}
		tempMap.put("Remark", remark);
		return tempMap;
	}

	public String createChargeAciton() {
		return url + chargeAction;
	}
}
