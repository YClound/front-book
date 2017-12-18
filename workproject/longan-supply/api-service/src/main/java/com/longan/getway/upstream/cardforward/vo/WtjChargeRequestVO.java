package com.longan.getway.upstream.cardforward.vo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.longan.biz.utils.DesedeUtils;
import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class WtjChargeRequestVO extends WtjBaseRequestVO {
	private static final String chargeUrl = URL + Utils.getProperty("wtjCardForward.chargeAction");

	private String billUsrid;
	private String billAmount;
	private String billCrdno;
	private String billCrdpwd;
	private String billReq;
	private String clientBillId;
	private String billReqEncode;

	public String getBillUsrid() {
		return billUsrid;
	}

	public void setBillUsrid(String billUsrid) {
		this.billUsrid = billUsrid;
	}

	public String getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}

	public String getBillCrdno() {
		return billCrdno;
	}

	public void setBillCrdno(String billCrdno) {
		this.billCrdno = billCrdno;
	}

	public String getBillCrdpwd() {
		return billCrdpwd;
	}

	public void setBillCrdpwd(String billCrdpwd) {
		this.billCrdpwd = billCrdpwd;
	}

	public String getBillReq() {
		return billReq;
	}

	public void setBillReq(String billReq) {
		this.billReq = billReq;
	}

	public String getClientBillId() {
		return clientBillId;
	}

	public void setClientBillId(String clientBillId) {
		this.clientBillId = clientBillId;
	}

	public String getBillReqEncode() {
		return billReqEncode;
	}

	public void setBillReqEncode(String billReqEncode) {
		this.billReqEncode = billReqEncode;
	}

	public void createBillReq() {
		// bill_usrid=13813800001&bill_amount=50&bill_crdno=123456789123456789&bill_crdpwd=123456789123456789
		String source = "bill_usrid=" + billUsrid + "&bill_amount=" + billAmount + "&bill_crdno="
				+ billCrdno + "&bill_crdpwd=" + billCrdpwd;
		String encryptStr = DesedeUtils.encryptTripDes(getDESKey(), source);
		String end = null;
		try {
			end = URLEncoder.encode(encryptStr, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("createBillReq encode error", e);
		}

		setBillReq(end);
	}

	public void createSign() {
		String source = "bill_req=" + billReq + "&client_bill_id=" + clientBillId + "&client_ip="
				+ getClientIp() + "&time_stamp=" + getTimeStamp() + "|||" + getSecKey();
		String signStr = Md5Encrypt.md5(source);
		setSign(signStr);
	}

	public String createUrl() {
		return chargeUrl;
	}

	public Map<String, String> createParams() {
		Map<String, String> result = new HashMap<String, String>();
		// 设置bill
		createBillReq();

		// 设置签名
		createSign();

		result.put("bill_req", billReq);
		result.put("client_bill_id", clientBillId);
		result.put("client_ip", getClientIp());
		result.put("time_stamp", getTimeStamp());
		result.put("sign", getSign());
		return result;
	}

}
