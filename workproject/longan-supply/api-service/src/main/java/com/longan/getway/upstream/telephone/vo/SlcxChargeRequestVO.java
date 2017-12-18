package com.longan.getway.upstream.telephone.vo;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class SlcxChargeRequestVO {
	private final static String preFix = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	public final static String privateKey = Utils.getProperty("slcxSupply.privateKey");
	private final static String url = Utils.getProperty("slcxSupply.URL");
	private final static String chargeAction = Utils.getProperty("slcxSupply.chargeAction");

	@XStreamAlias("oid_biz")
	private String oidBiz = Utils.getProperty("slcxSupply.bizId");
	@XStreamAlias("jno_cli")
	private String jnoCli;
	@XStreamAlias("oid_reguser")
	private String oidReguser = Utils.getProperty("slcxSupply.userId");
	@XStreamAlias("uid_cli")
	private String uidCli;
	private String price;
	private String sign;

	public String getOidReguser() {
		return oidReguser;
	}

	public void setOidReguser(String oidReguser) {
		this.oidReguser = oidReguser;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOidBiz() {
		return oidBiz;
	}

	public void setOidBiz(String oidBiz) {
		this.oidBiz = oidBiz;
	}

	public String getJnoCli() {
		return jnoCli;
	}

	public void setJnoCli(String jnoCli) {
		this.jnoCli = jnoCli;
	}

	public String getUidCli() {
		return uidCli;
	}

	public void setUidCli(String uidCli) {
		this.uidCli = uidCli;
	}

	public void createChargeSign() {
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotEmpty(oidBiz)) {
			sb.append(oidBiz);
		}
		if (StringUtils.isNotEmpty(jnoCli)) {
			sb.append(jnoCli);
		}
		if (StringUtils.isNotEmpty(oidReguser)) {
			sb.append(oidReguser);
		}
		if (StringUtils.isNotEmpty(uidCli)) {
			sb.append(uidCli);
		}
		if (StringUtils.isNotEmpty(price)) {
			sb.append(price);
		}
		if (StringUtils.isNotEmpty(privateKey)) {
			sb.append(privateKey);
		}
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}

	public String createChargeAciton() {
		return url + chargeAction;
	}

	public String createChargeParam() {
		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		xstream.alias("root", SlcxChargeRequestVO.class);
		return preFix + xstream.toXML(this).replaceAll("__", "_");
	}

	public static void main(String[] args) {
		SlcxChargeRequestVO vo = new SlcxChargeRequestVO();
		vo.setOidBiz("100");
		vo.createChargeSign();
		System.out.println(vo.createChargeParam());
	}
}
