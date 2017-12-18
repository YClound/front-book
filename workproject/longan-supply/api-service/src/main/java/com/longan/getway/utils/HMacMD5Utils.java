package com.longan.getway.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.longan.biz.utils.Base64;

public class HMacMD5Utils {

		public static final String KEY_SHA="SHA";
		  public static final String KEY_MD5="MD5";
		  public static final String KEY_MAC="HmacMD5";
		    
		public static byte[] encryptMD5(byte[] data) throws Exception
		{
		MessageDigest md5=MessageDigest.getInstance(KEY_MD5);
		md5.update(data);
		return md5.digest();
		}
		public static byte[] encryptSHA(byte[] data) throws Exception
		{
		MessageDigest sha=MessageDigest.getInstance(KEY_SHA);
		sha.update(data);
		return sha.digest();
		}
		public static byte[] encryptHMAC(byte[] data,String key) throws Exception
		{
		SecretKey sk=new SecretKeySpec(key.getBytes(),KEY_MAC);
		Mac mac=Mac.getInstance(sk.getAlgorithm());
		mac.init(sk);
		return mac.doFinal(data);
		}
		public static String HMACMD5(String data,String key){
			try {
				return Base64.encode(encryptHMAC(data.getBytes("utf-8"), key));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null ;
		}

}
