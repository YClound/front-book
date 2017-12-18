package com.longan.getway.upstream.shortmsg.vo;

import java.util.Map;
import java.util.TreeMap;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class BaiWuShortMsgRequestVO {
	private final static String url = Utils.getProperty("baiWuGateway.URL");
	private final static String sendAction = Utils.getProperty("baiWuGateway.sendAction");
	private final static String queryBalanceAction = Utils
			.getProperty("baiWuGateway.queryBalanceAction");
	private final static String id = Utils.getProperty("baiWuGateway.id");
	private final static String pwd = Utils.getProperty("baiWuGateway.pwd");
	private final static String serviceCode = Utils.getProperty("baiWuGateway.serviceCode");

	private String MD5_td_code;
	private String mobile;
	private String msg_content;
	private String msg_id;
	private String ext;

	private Map<String, String> tempMap = new TreeMap<String, String>();

	public String getMD5_td_code() {
		return MD5_td_code;
	}

	public void setMD5_td_code(String mD5_td_code) {
		MD5_td_code = mD5_td_code;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMsg_content() {
		return msg_content;
	}

	public void setMsg_content(String msg_content) {
		this.msg_content = msg_content;
	}

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public Map<String, String> createSendParam() {
		MD5_td_code = Md5Encrypt.md5(pwd + serviceCode);
		tempMap.put("id", id);
		tempMap.put("MD5_td_code", MD5_td_code);
		tempMap.put("mobile", mobile);
		tempMap.put("msg_content", msg_content);
		tempMap.put("msg_id", msg_id);
		tempMap.put("ext", ext);
		return tempMap;
	}

	public String createSendAction() {

		return url + sendAction;
	}

	public Map<String, String> createBalanceParam() {
		tempMap.put("id", id);
		tempMap.put("pwd", pwd);
		return tempMap;
	}

	public String createQueryBalanceAction() {
		return url + queryBalanceAction;
	}
}
