package com.longan.getway.upstream.telephone.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class SlcxCallbackVO {
	private String retcode;
	@XStreamAlias("oid_reguser")
	private String oidReguser;

	@XStreamAlias("jno_cli")
	private String jnoCli;

	@XStreamAlias("oid_goodsorder")
	private String oidGoodsorder;

	@XStreamAlias("succ_amount")
	private String succAmount;

	@XStreamAlias("fill_time")
	private String fillTime;

	private String sign;

	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public String getOidReguser() {
		return oidReguser;
	}

	public void setOidReguser(String oidReguser) {
		this.oidReguser = oidReguser;
	}

	public String getJnoCli() {
		return jnoCli;
	}

	public void setJnoCli(String jnoCli) {
		this.jnoCli = jnoCli;
	}

	public String getOidGoodsorder() {
		return oidGoodsorder;
	}

	public void setOidGoodsorder(String oidGoodsorder) {
		this.oidGoodsorder = oidGoodsorder;
	}

	public String getSuccAmount() {
		return succAmount;
	}

	public void setSuccAmount(String succAmount) {
		this.succAmount = succAmount;
	}

	public String getFillTime() {
		return fillTime;
	}

	public void setFillTime(String fillTime) {
		this.fillTime = fillTime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public boolean checkSign() {
//		StringBuffer sb = new StringBuffer("");
//		if (StringUtils.isNotEmpty(retcode)) {
//			sb.append(retcode);
//		}
//		if (StringUtils.isNotEmpty(oidReguser)) {
//			sb.append(oidReguser);
//		}
//		if (StringUtils.isNotEmpty(jnoCli)) {
//			sb.append(jnoCli);
//		}
//		if (StringUtils.isNotEmpty(oidGoodsorder)) {
//			sb.append(oidGoodsorder);
//		}
//		if (StringUtils.isNotEmpty(succAmount)) {
//			sb.append(succAmount);
//		}
//		if (StringUtils.isNotEmpty(fillTime)) {
//			sb.append(fillTime);
//		}
//		//sb.append(SlcxChargeRequestVO.privateKey);
//		String signStr = Md5Encrypt.md5(sb.toString());
//		System.out.println(signStr);
//		System.out.println(sign);
//		return signStr.equals(sign);
		return true;
	}

	public static void main(String[] args) {
		SlcxCallbackVO vo = new SlcxCallbackVO();
		vo.setRetcode("000000");
		vo.setOidReguser("880096");
		vo.setJnoCli("148057");
		vo.setOidGoodsorder("1015185365");
		vo.setSuccAmount("20.000");
		vo.setFillTime("20140718025840");
		vo.setSign("3910cf5ca8c6291a06cf757f950db1b4");
		System.out.println(vo.checkSign());
	}

}
