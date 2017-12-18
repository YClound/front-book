package com.longan.getway.upstream.telephone.vo;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class SanTongRequestVO {
	public final static String secretKey = Utils.getProperty("sanTongSupply.secretKey");
	private final static String url = Utils.getProperty("sanTongSupply.URL");
	private final static String chargeAction = Utils.getProperty("sanTongSupply.chargeAction");
	private final static String queryAction = Utils.getProperty("sanTongSupply.queryAction");
	private final static String queryBalanceAction = Utils
			.getProperty("sanTongSupply.queryBalanceAction");

	private final static String userid = Utils.getProperty("sanTongSupply.userid");
	private final static String coopId = Utils.getProperty("sanTongSupply.coopId");
	private final static String notifyUrl = Utils.getProperty("sanTongSupply.notifyUrl");
	private String merchantOrderNo;
	private String chargeNumber;
	private String chargeMoney;
	private String sign;

	public String getMerchantOrderNo() {
		return merchantOrderNo;
	}

	public void setMerchantOrderNo(String merchantOrderNo) {
		this.merchantOrderNo = merchantOrderNo;
	}

	public String getChargeNumber() {
		return chargeNumber;
	}

	public void setChargeNumber(String chargeNumber) {
		this.chargeNumber = chargeNumber;
	}

	public String getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(String chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String createChargeParam() {
		String reqStr = "coopId=" + coopId + "&merchantOrderNo=" + merchantOrderNo
				+ "&chargeNumber=" + chargeNumber + "&chargeMoney=" + chargeMoney + "&notifyUrl="
				+ notifyUrl + "&sign=" + sign;
		return url + chargeAction + reqStr;
	}

	public void createChargeSign() {
		StringBuffer sb = new StringBuffer();
		sb.append(coopId).append(merchantOrderNo).append(chargeNumber).append(chargeMoney)
				.append(notifyUrl).append(secretKey);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}

	public String createQueryParam() {
		String reqStr = "coopId=" + coopId + "&merchantOrderNo=" + merchantOrderNo + "&sign="
				+ sign;
		return url + queryAction + reqStr;
	}

	public void createQuerySign() {
		StringBuffer sb = new StringBuffer();
		sb.append(coopId).append(merchantOrderNo).append(secretKey);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}

	public String createQueryBalanceParam() {
		String reqStr = "userid=" + userid + "&sign=" + sign;
		return url + queryBalanceAction + reqStr;
	}

	public void createQueryBalanceSign() {
		StringBuffer sb = new StringBuffer();
		sb.append(userid).append(secretKey);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}
}
