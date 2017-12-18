package com.longan.getway.upstream.telephone.vo;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class JuYouQueryRequestVO {
	private final static String preFix = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	private final static String url = Utils.getProperty("juYouSupply.URL");

	@SuppressWarnings("unused")
	private String ob = "odstate";// 订单查询
	private String uid = Utils.getProperty("juYouSupply.userId");// 账号
	private String od;// 订单号
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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void createQureySign() {
		StringBuffer sb = new StringBuffer();
		sb.append("uid=" + uid);
		if (StringUtils.isNotEmpty(od)) {
			sb.append("&od=" + od);
		}
		sb.append("&sn=" + sn);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}

	public String createQueryParam() {
		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		xstream.alias("items", JuYouQueryRequestVO.class);
		return preFix + xstream.toXML(this);
	}

	public String createQureyBalanceParam() {
		StringBuffer sb = new StringBuffer();
		sb.append("uid=" + uid).append("&sn=" + sn);
		String signStr = Md5Encrypt.md5(sb.toString());
		String xmlStr = preFix + "<items><ob>balance</ob><uid>" + uid + "</uid><key>" + signStr
				+ "</key></items>";
		return xmlStr;
	}
}
