package com.longan.biz.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class Utils {
	private static final String filename = "/config.properties";

	private static Properties properties = null;

	static {
		InputStream is = null;
		try {
			is = Utils.class.getResourceAsStream(filename);
			properties = new Properties();
			properties.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		if (properties != null) {
			return (String) properties.get(key);
		}
		return null;
	}

	public static boolean isPhone(String phone) {
		if (StringUtils.isBlank(phone)) {
			return false;
		}
		return Pattern.compile("^1[3578]\\d{9}$").matcher(phone).matches();
	}
}