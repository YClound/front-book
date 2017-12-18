package com.longan.getway.upstream.cardforward.vo;

import java.io.StringReader;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class WtjHeartBeatRequestVO extends WtjBaseRequestVO {
	private final static String heartBeatUrl = URL
			+ Utils.getProperty("wtjCardForward.heartBeatAction");

	public SupplyResult<String> queryBalance() {
		SupplyResult<String> result = new SupplyResult<String>();
		SupplyResult<Document> requestResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByGet(heartBeatUrl);
		result.setStatus(requestResult.getStatus());

		if (!requestResult.isSuccess()) {
			logger.error(" WtjCardForward heartBeat error msg : " + requestResult.getResultMsg());
			result.setResultMsg(requestResult.getResultMsg());
			return result;
		}
		Document doc = requestResult.getModule();
		if (doc == null) {
			logger.error(" WtjCardForward heartBeat error result is null ");
			result.setResultMsg("result is null");
			return result;
		}

		Element root = doc.getRootElement();
		logger.warn("response: " + root.asXML());
		String usrBlance = null;
		if (root != null) {
			usrBlance = root.elementText("usr_blance");
		}
		result.setModule(usrBlance);
		return result;
	}

	public void heartBeat() {
		SupplyResult<String> result = MultiThreadedHttpConnection.getInstance().sendPostByMapLogin(
				heartBeatUrl, null);
		if (!result.isSuccess()) {
			logger.error(" WtjCardForward heartBeat error msg : " + result.getResultMsg());
			return;
		}

		String body = result.getModule();

		if (StringUtils.isEmpty(body)) {
			logger.error("WtjCardForward heartBeat error msg result is null");
			return;
		}

		SAXReader reader = new SAXReader();
		Document doc;
		try {
			doc = reader.read(new StringReader(result.getModule()));
			if (doc == null) {
				logger.error(" WtjCardForward heartBeat error result is null ");
				return;
			}
			Element root = doc.getRootElement();
			String code = root.elementText("error_code");
			if ("0".equals(code)) {
				// 已经登录，不做任何操作
				return;
			} else if ("100".equals(code) || "111".equals(code) || "120".equals(code)) {
				// 未登录等等
				// login
				WtjLoginRequestVO wtjLoginRequestVO = new WtjLoginRequestVO();
				wtjLoginRequestVO.login();
				return;
			}

			logger.error("WtjCardForward heartBeat error ,code : " + code);
		} catch (DocumentException e) {
			logger.error("WtjCardForward heartBeat error parse error  ", e);
		}
	}
}
