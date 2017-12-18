package com.longan.biz.utils;

import java.net.URLEncoder;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DesedeUtils {
	private final static Logger logger = LoggerFactory.getLogger(DesedeUtils.class);

	public static String encryptBASE64(byte[] key) throws Exception {
		return Base64.encode(key);
	}

	public static byte[] decryptBASE64(String key) throws Exception {
		return Base64.decode(key);
	}

	public static String encryptTripDes(String keyStr, String mainStr) {
		String encodeString_ECB = null;
		try {
			// 密钥
			byte[] key = decryptBASE64(keyStr);
			byte[] data = mainStr.getBytes("UTF-8");
			byte[] encodeByte_ECB = des3EncodeECB(key, data);
			// 加密后的String 需把加密的byte[]转base64
			encodeString_ECB = encryptBASE64(encodeByte_ECB);
		} catch (Exception e) {
			logger.error("DESUtils encryptTripDes error ", e);
		}
		return encodeString_ECB;
	}

	public static String decryptTripDes(String keyStr, String mainStr) {
		String decodeString_ECB = null;
		try {
			// 密钥
			byte[] key = decryptBASE64(keyStr);
			byte[] decodeByteMain_ECB = decryptBASE64(mainStr);
			byte[] decodeByte_ECB = ees3DecodeECB(key, decodeByteMain_ECB);
			decodeString_ECB = new String(decodeByte_ECB, "UTF-8");

		} catch (Exception e) {
			logger.error("DESUtils decryptTripDes error ", e);
		}
		return decodeString_ECB;
	}

	public static byte[] des3EncodeECB(byte[] key, byte[] data) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] bOut = cipher.doFinal(data);
		return bOut;
	}

	public static byte[] ees3DecodeECB(byte[] key, byte[] data) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, deskey);
		byte[] bOut = cipher.doFinal(data);
		return bOut;

	}

	public static String createKey() {
		String s = null;
		try {
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			kg.init(128);// 要生成多少位，只需要修改这里即可128, 192或256
			SecretKey sk = kg.generateKey();
			byte[] b = sk.getEncoded();
			s = byteToHexString(b);
			return s;
		} catch (NoSuchAlgorithmException e) {
			logger.error("DESUtils createKey error ", e);
		}
		return null;
	}

	public static String byteToHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String strHex = Integer.toHexString(bytes[i]);
			if (strHex.length() > 3) {
				sb.append(strHex.substring(6));
			} else {
				if (strHex.length() < 2) {
					sb.append("0" + strHex);
				} else {
					sb.append(strHex);
				}
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {

		try {
			String key = createKey();
			System.out.println(key);
			System.out.println("十六进制密钥长度为" + key.length());
			System.out.println("二进制密钥的长度为" + key.length() * 4);
			String jiaMi1 = encryptTripDes(
					key,
					"bill_usrid=13813800001&bill_amount=50&bill_crdno=123456789123456789&bill_crdpwd=123456789123456789");
			String jiaMi1_URLEncoder = URLEncoder.encode(jiaMi1, "utf-8");
			print(jiaMi1);
			print(jiaMi1_URLEncoder);
			print(decryptTripDes(key, jiaMi1));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static final void print(String string) {
		System.out.println(string);
	}

	public static final void failRed(String string) {
		System.err.println(string);
	}
}