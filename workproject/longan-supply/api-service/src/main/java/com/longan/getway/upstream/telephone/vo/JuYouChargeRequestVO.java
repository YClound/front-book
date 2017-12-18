package com.longan.getway.upstream.telephone.vo;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class JuYouChargeRequestVO {
	private final static String preFix = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	private final static String url = Utils.getProperty("juYouSupply.URL");

	@SuppressWarnings("unused")
	private String ob = "pay";// 购买
	private String uid = Utils.getProperty("juYouSupply.userId");// 账号
	private String od;// 唯一性的订单号
	private String hm;// 手机号
	private String money;// 金额
	@XStreamAlias("key")
	private String sign;
	private final static String sn = Utils.getProperty("juYouSupply.key");// key

	public String getUrl() {
		return url;
	}

	public String getOd() {
		return od;
	}

	public void setOd(String od) {
		this.od = od;
	}

	public String getHm() {
		return hm;
	}

	public void setHm(String hm) {
		this.hm = hm;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void createChargeSign() {
		StringBuffer sb = new StringBuffer();
		sb.append("uid=" + uid);
		if (StringUtils.isNotEmpty(od)) {
			sb.append("&od=" + od);
		}
		if (StringUtils.isNotEmpty(od)) {
			sb.append("&hm=" + hm);
		}
		if (StringUtils.isNotEmpty(od)) {
			sb.append("&money=" + money);
		}
		sb.append("&sn=" + sn);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}

	public String createChargeParam() {
		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		xstream.alias("items", JuYouChargeRequestVO.class);
		return preFix + xstream.toXML(this);
	}
}
