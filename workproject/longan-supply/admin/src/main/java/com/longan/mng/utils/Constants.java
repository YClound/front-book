package com.longan.mng.utils;

import com.longan.biz.utils.Utils;

public class Constants {
	public final static String SESSION_USER = "LoginUser";
	public final static String NO_NEED_LOGIN_URL = "index.do|login.do|checkCode.do|authError.do|loginOut.do|lockBizOrder.do|callbackBizOrder.do|admin/initCharingLimit.do";

	public final static String STATIC_SERVER_KEY = "staticServer";

	public final static String STATIC_SERVER = Utils.getProperty(STATIC_SERVER_KEY);

	public final static String UPLOAD_PATH = Utils.getProperty("upload.url");

	public final static String ENV = Utils.getProperty("env");

	public static boolean isProduct() {
		return "product".equals(ENV);
	}

}
