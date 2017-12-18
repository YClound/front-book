package com.longan.getway.biz.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.longan.getway.biz.common.BaseService;

public class DownStreamInfoContext {
	public volatile Map<String, String> KEY_MAP = null;

	public volatile Map<String, String> CALLBACK_URL_MAP = null;

	private static final String filename = "/downStreamInfo.xml";

	void init() {
		reload();
	}

	public String getDownStreamKey(String userId) {
		return KEY_MAP.get(userId);
	}

	public String getDownStreamCallBackUrl(String userId) {
		return CALLBACK_URL_MAP.get(userId);
	}

	@SuppressWarnings("unchecked")
	public void reload() {
		InputStream in = BaseService.class.getResourceAsStream(filename);
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(in);
		} catch (DocumentException e) {
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		Element root = doc.getRootElement();
		List<Element> userInfoList = (List<Element>) root.elements();
		Map<String, String> newKeyMap = new HashMap<String, String>();
		Map<String, String> newURLMap = new HashMap<String, String>();
		for (Element element : userInfoList) {
			if (element.elementText("key") != null) {
				newKeyMap.put(element.elementText("id"), element.elementText("key").toString());
			}
			if (element.elementText("callBackUrl") != null) {
				newURLMap.put(element.elementText("id"), element.elementText("callBackUrl")
						.toString());
			}

		}
		CALLBACK_URL_MAP = newURLMap;
		KEY_MAP = newKeyMap;
	}

}
