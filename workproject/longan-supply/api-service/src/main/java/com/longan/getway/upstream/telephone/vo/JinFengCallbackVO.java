package com.longan.getway.upstream.telephone.vo;

public class JinFengCallbackVO {
	private String R4_trxamount;
	private String R6_requestid;
	private String R8_returncode;
	private String R10_trxDate;
	private String hmac;

	public String getR4_trxamount() {
		return R4_trxamount;
	}

	public void setR4_trxamount(String r4_trxamount) {
		R4_trxamount = r4_trxamount;
	}

	public String getR6_requestid() {
		return R6_requestid;
	}

	public void setR6_requestid(String r6_requestid) {
		R6_requestid = r6_requestid;
	}

	public String getR8_returncode() {
		return R8_returncode;
	}

	public void setR8_returncode(String r8_returncode) {
		R8_returncode = r8_returncode;
	}

	public String getR10_trxDate() {
		return R10_trxDate;
	}

	public void setR10_trxDate(String r10_trxDate) {
		R10_trxDate = r10_trxDate;
	}

	public String getHmac() {
		return hmac;
	}

	public void setHmac(String hmac) {
		this.hmac = hmac;
	}

	public boolean checkSign() {
		return true;
	}
}
