package com.longan.getway.upstream.cardforward.vo;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.longan.biz.utils.DesedeUtils;
import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class WtjLoginRequestVO extends WtjBaseRequestVO {
	private final static String loginUrl = URL + Utils.getProperty("wtjCardForward.loginAction");

	private static String userCode = Utils.getProperty("wtjCardForward.userId");
	private static String password = Md5Encrypt.md5(Utils.getProperty("wtjCardForward.password"));

	public String getUserCode() {
		return userCode;
	}

	public String getPassword() {
		return password;
	}

	public void createSign() {
		// "usercode=***&password=***&client_ip=***&time_stamp=***|||3DES"
		String source = "usercode=" + userCode + "&password=" + password + "&client_ip="
				+ getClientIp() + "&time_stamp=" + getTimeStamp() + "|||" + DESKey;
		String signStr = Md5Encrypt.md5(source);
		setSign(signStr);
	}

	public void login() {
		SupplyResult<String> result = MultiThreadedHttpConnection.getInstance().sendPostByMapLogin(
				createUrl(), createParams());
		if (!result.isSuccess()) {
			logger.error("WtjCardForward login error msg : " + result.getResultMsg());
			return;
		}

		if (StringUtils.isEmpty(result.getModule())) {
			logger.error("WtjCardForward login error msg result is null");
			return;
		}

		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(new StringReader(result.getModule()));
			Element root = document.getRootElement();
			String code = root.elementText("error_code");
			if ("0".equals(code)) {
				// 登录后设置 私钥
				String secKey = root.elementText("secKey");
				secKey = DesedeUtils.decryptTripDes(DESKey, secKey);
				super.setSecKey(secKey);
				logger.warn("WtjCardForward login success secKey = " + secKey);
			} else {
				logger.error("WtjCardForward login error,code : " + code);
			}
		} catch (DocumentException e) {
			logger.error("WtjCardForward login error ,parse documt error", e);
			return;
		}
	}

	private String createUrl() {
		return loginUrl;
	}

	private Map<String, String> createParams() {
		Map<String, String> result = new HashMap<String, String>();
		// 设置签名
		createSign();

		result.put("usercode", userCode);
		result.put("password", password);
		result.put("client_ip", getClientIp());
		result.put("time_stamp", getTimeStamp());
		result.put("sign", getSign());
		return result;
	}

}
