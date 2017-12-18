package com.longan.app.utils;

import com.longan.biz.utils.Utils;

public class Constants {

	public final static String ENV = Utils.getProperty("env");

	// public final static HashSet<String> NO_NEED_LOGIN_URL_SET = new
	// HashSet<String>();
	//
	// static {
	// NO_NEED_LOGIN_URL_SET.add("/refuse.htm");
	// NO_NEED_LOGIN_URL_SET.add("/error404.htm");
	// NO_NEED_LOGIN_URL_SET.add("/");
	// NO_NEED_LOGIN_URL_SET.add("/index.htm");
	// NO_NEED_LOGIN_URL_SET.add("/login.htm");
	// NO_NEED_LOGIN_URL_SET.add("/api/checkCode.htm");
	// NO_NEED_LOGIN_URL_SET.add("/api/check.htm");
	// NO_NEED_LOGIN_URL_SET.add("/user/register.htm");
	// NO_NEED_LOGIN_URL_SET.add("/user/registerCommit.htm");
	// NO_NEED_LOGIN_URL_SET.add("/checkCode.htm");
	// NO_NEED_LOGIN_URL_SET.add("/api/getLoginToken.htm");
	// NO_NEED_LOGIN_URL_SET.add("/api/getRegAuthCode.htm");
	// }

	// public final static String SESSION_USER = "userInfo";

	public interface Login {
		// public final static String CODE_NEED_CHECKCODE = "need_checkcode";
		// public final static int PWD_ERROR_COUNT_MAX = 6;
	}

	public interface Cookie {
		// public final static String KEY_TT_UID = "TTUID";
		// public final static int EXP_TT_UID = Integer.MAX_VALUE;
		//
		// public final static String KEY_USER_ID = "ID";
		// public final static String KEY_SESSION_ID = "SID";
	}

	public interface Memcached {
		// public final static String KEY_LOGIN_TOKEN = "LOGIN_TOKEN_";
		// public final static int EXP_LOGIN_TOKEN = 15 * 60;
		//
		// public final static String KEY_LOGIN_SESSION = "LOGIN_SESSION_";
		// public final static int EXP_LOGIN_SESSION = 1 * 60 * 60;
		//
		// public final static String KEY_PAY_TOKEN = "PAY_TOKEN_";
		// public final static int EXP_PAY_TOKEN = 24 * 60 * 60;
		//
		// public final static String KEY_LOGIN_CHECKCODE = "LOGIN_CHECKCODE_";
		// public final static int EXP_LOGIN_CHECKCODE = 10 * 60;
		//
		// public final static String KEY_LOGIN_PWD_ERROR_COUNT =
		// "LOGIN_PWD_ERROR_COUNT_";
		// public final static int EXP_LOGIN_PWD_ERROR_COUNT = 10 * 60;
		//
		// public final static String KEY_REG_AUTHCODE = "REG_AUTHCODE_";
		// public final static int EXP_REG_AUTHCODE = 10 * 60;
		//
		// public final static int EXP_REG_AUTHCODE_MUTEX = 55; // 设置少于前台的60秒

	}

	public interface PageSize {
		public final static int SMALL = 5;
		public final static int MIDDLE = 10;
		public final static int LARGE = 20;
	}

}
