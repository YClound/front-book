package com.longan.biz.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

	public static String encrypt(String sSrc, String sKey) throws Exception {
		if (sKey == null || sSrc == null) {
			throw new RuntimeException("encrypt error params is null");
		}
		byte[] raw = sKey.getBytes("utf-8");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");// AES/ECB/PKCS5Padding
													// 算法/模式/补码方式
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

		return Base64.encode(encrypted);// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
	}

	public static String decrypt(String sSrc, String sKey) throws Exception {
		if (sKey == null || sSrc == null) {
			throw new RuntimeException("encrypt error params is null");
		}
		byte[] raw = sKey.getBytes("utf-8");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES"); // 默认就是
													// AES/ECB/PKCS5Padding
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] encrypted1 = Base64.decode(sSrc);// 先用base64解密
		byte[] original = cipher.doFinal(encrypted1);
		return new String(original, "utf-8");
	}

	private static final String chars = "abcdefghijklmnopqrstuvwxyz1234567890";
	private static int length = 16;

	public static String createKey() {
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt((int) (Math.random() * 36)));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(createKey());
	}

}
