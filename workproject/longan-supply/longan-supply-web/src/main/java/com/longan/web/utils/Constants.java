package com.longan.web.utils;

import java.util.HashSet;

import com.longan.biz.utils.Utils;

public class Constants {
	public final static String ASSETS_SERVER_KEY = "assetsServer";

	public final static String ASSETS_SERVER = Utils.getProperty(ASSETS_SERVER_KEY);

	public final static String CONTEXT_PATH_KEY = "contextPath";

	public final static String CONTEXT_PATH = Utils.getProperty(CONTEXT_PATH_KEY);

	public final static String ENV = Utils.getProperty("env");

	public final static String UPLOAD_PATH = Utils.getProperty("upload.url");

	public final static HashSet<String> NO_NEED_LOGIN_URL_SET = new HashSet<String>();

	static {
		NO_NEED_LOGIN_URL_SET.add("/refuse.htm");
		NO_NEED_LOGIN_URL_SET.add("/error404.htm");
		NO_NEED_LOGIN_URL_SET.add("/");
		NO_NEED_LOGIN_URL_SET.add("/index.htm");
		NO_NEED_LOGIN_URL_SET.add("/login.htm");
		NO_NEED_LOGIN_URL_SET.add("/api/checkCode.htm");
		NO_NEED_LOGIN_URL_SET.add("/api/check.htm");
		NO_NEED_LOGIN_URL_SET.add("/user/register.htm");
		NO_NEED_LOGIN_URL_SET.add("/user/registerCommit.htm");
		NO_NEED_LOGIN_URL_SET.add("/checkCode.htm");
		NO_NEED_LOGIN_URL_SET.add("/api/getLoginToken.htm");
		NO_NEED_LOGIN_URL_SET.add("/api/getRegAuthCode.htm");
		NO_NEED_LOGIN_URL_SET.add("/api/app/testJsonp.htm");
		NO_NEED_LOGIN_URL_SET.add("/api/app/test.htm");
	}

	public final static String SESSION_USER = "userInfo";

	public interface Login {
		public final static String CODE_NEED_CHECKCODE = "need_checkcode";
		public final static int PWD_ERROR_COUNT_MAX = 6;
	}

	public interface Cookie {
		public final static String KEY_TT_UID = "TTUID";
		public final static int EXP_TT_UID = Integer.MAX_VALUE;

		public final static String KEY_USER_ID = "ID";
		public final static String KEY_SESSION_ID = "SID";
	}

	public interface Memcached {
		public final static String KEY_LOGIN_TOKEN = "LOGIN_TOKEN_";
		public final static int EXP_LOGIN_TOKEN = 15 * 60;

		public final static String KEY_LOGIN_SESSION = "LOGIN_SESSION_";
		public final static int EXP_LOGIN_SESSION = 1 * 60 * 60;

		public final static String KEY_PAY_TOKEN = "PAY_TOKEN_";
		public final static int EXP_PAY_TOKEN = 24 * 60 * 60;

		public final static String KEY_LOGIN_CHECKCODE = "LOGIN_CHECKCODE_";
		public final static int EXP_LOGIN_CHECKCODE = 10 * 60;

		public final static String KEY_LOGIN_PWD_ERROR_COUNT = "LOGIN_PWD_ERROR_COUNT_";
		public final static int EXP_LOGIN_PWD_ERROR_COUNT = 10 * 60;

		public final static String KEY_REG_AUTHCODE = "REG_AUTHCODE_";
		public final static int EXP_REG_AUTHCODE = 10 * 60;

		public final static int EXP_REG_AUTHCODE_MUTEX = 55; // 设置少于前台的60秒

	}

	public interface PageSize {
		public final static int SMALL = 5;
		public final static int MIDDLE = 10;
		public final static int LARGE = 20;
	}

	public interface Bank {
		// public final static String BANK_1 = "农业银行";
		// public final static String BANK_2 = "工商银行";
		// public final static String BANK_3 = "建设银行";
		// public final static String BANK_4 = "支付宝";
		// public final static String BANK_1_DESC =
		// "农业银行  浙江杭州农业银行杭三路支行  62284 80328 74471 8276  季猛林";
		// public final static String BANK_2_DESC =
		// "工商银行  浙江杭州工商银行中河支行  62122 61202 01434 9907 季猛林";
		// public final static String BANK_3_DESC =
		// "建设银行  浙江杭州建设银行文新支行  62366 81540 00126 4980  季猛林";
		// public final static String BANK_4_DESC =
		// "支付宝账户 : 13957113283   收款人： 季猛林";

		public final static String BANK_1 = "建设银行";
		public final static String BANK_2 = "农业银行";
		public final static String BANK_1_DESC = "浙江杭州建设银行文新支行 6227 0732 0086 2523  王延民";
		public final static String BANK_2_DESC = "浙江杭州农业银行城西支行  6228 4603 2001 0746 416 王延民";
	}
}
