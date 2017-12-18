package com.longan.biz.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlEncoding {
	private final static Logger logger = LoggerFactory.getLogger(UrlEncoding.class);

	public static String urlGB2312(String str) {
		try {
			return URLEncoder.encode(str, "gb2312");
		} catch (UnsupportedEncodingException e) {
			logger.error("UrlEncoding urlGB2312 error ", e);
			return null;
		}
	}
}
