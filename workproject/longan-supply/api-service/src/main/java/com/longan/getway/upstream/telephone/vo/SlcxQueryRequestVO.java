package com.longan.getway.upstream.telephone.vo;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("root")
public class SlcxQueryRequestVO {
	private final static String preFix = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	private final static String privateKey = Utils.getProperty("slcxSupply.privateKey");
	private final static String url = Utils.getProperty("slcxSupply.URL");
	private final static String queryAction = Utils.getProperty("slcxSupply.queryAction");
	private final static String queryBalanceAction = Utils
			.getProperty("slcxSupply.queryBalanceAction");

	@XStreamAlias("jno_cli")
	private String jnoCli;
	@XStreamAlias("oid_reguser")
	private String oidReguser = Utils.getProperty("slcxSupply.userId");
	private String sign;

	public String getOidReguser() {
		return oidReguser;
	}

	public void setOidReguser(String oidReguser) {
		this.oidReguser = oidReguser;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getJnoCli() {
		return jnoCli;
	}

	public void setJnoCli(String jnoCli) {
		this.jnoCli = jnoCli;
	}

	public void createQuerySign() {
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotEmpty(jnoCli)) {
			sb.append(jnoCli);
		}
		if (StringUtils.isNotEmpty(oidReguser)) {
			sb.append(oidReguser);
		}
		if (StringUtils.isNotEmpty(privateKey)) {
			sb.append(privateKey);
		}
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}

	public String createQueryParam() {
		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		return preFix + xstream.toXML(this).replaceAll("__", "_");
	}

	public String createQueryAciton() {
		return url + queryAction;
	}

	public String createQueryBalanceAction() {
		return url + queryBalanceAction;
	}

	public String createQureyBalanceParam() {
		String signStr = Md5Encrypt.md5(oidReguser + privateKey);
		return preFix + "<root><oid_reguser>" + oidReguser + "</oid_reguser><sign>" + signStr
				+ "</sign></root>";
	}

	public static void main(String[] args) {
		SlcxQueryRequestVO vo = new SlcxQueryRequestVO();
		vo.createQuerySign();
		System.out.println(vo.createQueryAciton());
		System.out.println(vo.createQueryParam());
	}
}
