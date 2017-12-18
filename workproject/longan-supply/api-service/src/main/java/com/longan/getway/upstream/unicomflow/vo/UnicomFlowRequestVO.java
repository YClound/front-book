package com.longan.getway.upstream.unicomflow.vo;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;
import com.longan.getway.utils.Constants;

public class UnicomFlowRequestVO {
	Logger logger = LoggerFactory.getLogger(UnicomFlowRequestVO.class);

	private static Long supplierId = Long.parseLong(Utils.getProperty("unicomFlow.supplierId"));
	private Long serviceNum;
	private String cardNum;
	private String password;
	private Integer money;
	private String tId;
	private Long ts;
	private String sign;

	private Map<String, Object> tempMap = new TreeMap<String, Object>();

	public UnicomFlowRequestVO() {
		ts = System.currentTimeMillis();
	}

	public Long getServiceNum() {
		return serviceNum;
	}

	public void setServiceNum(Long serviceNum) {
		this.serviceNum = serviceNum;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public String gettId() {
		return tId;
	}

	public void settId(String tId) {
		this.tId = tId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public Long getTs() {
		return ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	private void putToTreeMap() {
		if (serviceNum != null) {
			tempMap.put("serviceNum", serviceNum);
		}
		if (StringUtils.isNotEmpty(cardNum)) {
			tempMap.put("cardNum", cardNum);
		}
		if (StringUtils.isNotEmpty(password)) {
			tempMap.put("password", password);
		}
		if (supplierId != null) {
			tempMap.put("supplierId", supplierId);
		}
		if (StringUtils.isNotEmpty(tId)) {
			tempMap.put("tId", tId);
		}
		if (ts != null) {
			tempMap.put("ts", ts);
		}

		if (money != null) {
			tempMap.put("money", money);
		}
	}

	public String getRequesytParam() {
		putToTreeMap();
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Object> e : tempMap.entrySet()) {
			sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
		}
		sb.append("sign=").append(createSignStr());
		// logger.warn("request : " + sb.toString());
		return sb.toString();
	}

	public String getChargeQueryURL() {
		return Constants.UnicomFlow.CHARGEQUERY_ACTION + getRequesytParam();
	}

	public String getChargeURL() {
		return Constants.UnicomFlow.CHARGE_ACTION + getRequesytParam();
	}

	public String getCardQueryURL() {
		return Constants.UnicomFlow.CARDQUERY_ACTION + getRequesytParam();
	}

	public String getCardCheckURL() {
		return Constants.UnicomFlow.CARDCHECK_ACTION + getRequesytParam();
	}

	private String createSignStr() {
		StringBuffer sb = new StringBuffer();
		String signStr = null;
		for (Entry<String, Object> e : tempMap.entrySet()) {
			sb.append(e.getKey()).append(e.getValue());
		}
		sb.append(Constants.UnicomFlow.SIGN_KEY);
		// System.out.println("sourceStr: " + sb.toString());
		// signStr = MD5Util.encode(sb.toString(), "GBK");
		signStr = Md5Encrypt.md5(sb.toString());
		// System.out.println("signStr: " + signStr);
		return signStr;
	}

}
