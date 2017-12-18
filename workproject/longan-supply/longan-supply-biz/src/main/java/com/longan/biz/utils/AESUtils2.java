package com.longan.biz.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils2 {

	private static final String AES = "AES";

	public static byte[] encrypt(byte[] src, String key) throws Exception {
		Cipher cipher = Cipher.getInstance(AES);
		SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);
		cipher.init(Cipher.ENCRYPT_MODE, securekey);// 设置密钥和加密形式
		return cipher.doFinal(src);
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	public final static String encrypt(String data, String key) {
		try {
			return byte2hex(encrypt(data.getBytes(), key));
		} catch (Exception e) {
		}
		return null;
	}

}
