package com.longan.getway.upstream.telephone.vo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class YiSaiQueryRequestVO {
	public final static String key = Utils.getProperty("yiSaiSupply.key");
	private final static String url = Utils.getProperty("yiSaiSupply.URL");
	private final static String queryAction = Utils.getProperty("yiSaiSupply.queryAction");
	private final static String queryBalanceAction = Utils
			.getProperty("yiSaiSupply.queryBalanceAction");
	private final static String userNumber = Utils.getProperty("yiSaiSupply.UserNumber");
	private String inOrderNumber;
	private final static String outOrderNumber = "None";
	private final static String queryType = "P";
	private String recordKey;
	private final static String remark = "HELLO WORLD!";

	private Map<String, String> tempMap = new TreeMap<String, String>();

	public String getInOrderNumber() {
		return inOrderNumber;
	}

	public void setInOrderNumber(String inOrderNumber) {
		this.inOrderNumber = inOrderNumber;
	}

	public String getRecordKey() {
		return recordKey;
	}

	public void setRecordKey(String recordKey) {
		this.recordKey = recordKey;
	}

	public void createQuerySign(String ustreamSerialno) {
		setInOrderNumber(ustreamSerialno);
		String keyStr = userNumber + inOrderNumber + outOrderNumber + queryType + key;
		String str = Md5Encrypt.md5(keyStr);
		String recordKeyStr = str.substring(0, 16).toUpperCase();// 取前16位转大写
		setRecordKey(recordKeyStr);
	}

	public Map<String, String> createParamMap() {
		tempMap.put("UserNumber", userNumber);
		if (StringUtils.isNotEmpty(inOrderNumber)) {
			tempMap.put("InOrderNumber", inOrderNumber);
		}
		tempMap.put("OutOrderNumber", outOrderNumber);
		tempMap.put("QueryType", queryType);
		if (StringUtils.isNotEmpty(recordKey)) {
			tempMap.put("RecordKey", recordKey);
		}
		tempMap.put("Remark", remark);
		return tempMap;
	}

	public Map<String, String> createBalanceParamMap() {
		SimpleDateFormat formdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss)");
		Date date = new Date();
		String queryTime = formdate.format(date);
		String keyStr = userNumber + queryTime + key;
		String str = Md5Encrypt.md5(keyStr);
		String recordKeyStr = str.substring(0, 16).toUpperCase();

		tempMap.put("UserNumber", userNumber);
		tempMap.put("QueryTime", queryTime);
		tempMap.put("RecordKey", recordKeyStr);
		return tempMap;
	}

	public String createQueryAciton() {
		return url + queryAction;
	}

	public String createQueryBalanceAction() {
		return url + queryBalanceAction;
	}
}
