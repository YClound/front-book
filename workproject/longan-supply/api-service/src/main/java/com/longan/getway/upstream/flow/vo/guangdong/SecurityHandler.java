package com.longan.getway.upstream.flow.vo.guangdong;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.utils.Base64;
import com.longan.biz.utils.Utils;
import com.longan.getway.utils.Base64Utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public final class SecurityHandler {
	private static final Logger logger = LoggerFactory.getLogger(SecurityHandler.class);

	public static final String url = Utils.getProperty("guangdongFlow.url");
	// public static final String url = "http://127.0.0.1:8080/Cta-Pt";
	public static final String accountPublicKey = Utils.getProperty("guangdongFlow.accountPublicKey");
	// 其实也包含公钥在内
	public static final String accountPrivateKey = Utils.getProperty("guangdongFlow.accountPrivateKey");
	public static final String accountPrivatePwd = Utils.getProperty("guangdongFlow.accountPrivatePwd");
	public static final String accountPrivateName = Utils.getProperty("guangdongFlow.accountPrivateName");
	public static final String threePublicKey = Utils.getProperty("guangdongFlow.threePublicKey");
	/**
	 * 商户编号是商户在我们后台开户后分配的编号，要一致
	 */
	public static final String accountNum = Utils.getProperty("guangdongFlow.accountNum");

	/**
	 * 这个接口标记由开发前置时标记，指定商户具体调用哪个接口
	 */
	public static final String charge_serviceType = "FLOW_RECHARGE";
	public static final String queryOrder_serviceType = "FLOW_RECHARGE_QUERY";

	/**
	 * 发送请求.
	 * 
	 * @param httpUrl
	 *            请求的地址
	 * @param paramStr
	 *            请求的数据
	 * @throws Exception
	 */
	public static String postHttp(String httpUrl, String methodName, String paramStr) throws Exception {
		// paramStr = URLEncoder.encode(paramStr,"utf-8");
		HttpURLConnection urlCon = null;
		StringBuffer result = new StringBuffer();
		try {
			urlCon = (HttpURLConnection) (new URL(httpUrl + "/" + methodName)).openConnection();
			urlCon.setReadTimeout(120000);
			urlCon.setConnectTimeout(10000);

			urlCon.setDoInput(true);
			urlCon.setDoOutput(true);
			urlCon.setRequestMethod("POST");
			urlCon.getOutputStream().write(paramStr.getBytes("UTF-8"));
			urlCon.getOutputStream().flush();
			urlCon.getOutputStream().close();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != urlCon) {
				urlCon.disconnect();
			}

		}
		return result.toString();
	}

	public static byte[] encrypt(byte[] datasource, String password) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			return cipher.doFinal(datasource);
		} catch (Throwable e) {
			logger.error("", e);
		}
		return null;
	}

	public static byte[] decryptAES(byte[] input, String key) {
		byte[] output = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skey);
			output = cipher.doFinal(input);
		} catch (Exception e) {
			logger.error("", e);
		}
		return output;
	}

	public static byte[] encryptAES(byte[] input, String key) {
		byte[] crypted = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			crypted = cipher.doFinal(input);
		} catch (Exception e) {
			logger.error("", e);
		}
		return crypted;
	}

	public static byte[] decodeBase64(String s) {
		try {
			byte[] b = Base64Utils.decode(s);
			return b;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}

	}

	public static String encodeBase64(byte[] data) {

		try {
			return Base64Utils.encode(data).replace("\n", "").replace("\r", "");
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;

	}

	public static String genRandomNum(int pwd_len) {
		final int maxNum = 36;
		int i;
		int count = 0;
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			i = Math.abs(r.nextInt(maxNum));
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	public static byte[] decryptByPrivateKey(byte[] data) throws Exception {
		PrivateKey privateKey = getPrivateKey();
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	public static String generateEnvelope(String key) throws Exception {
		try {
			PublicKey publicKey = getPublicKey(2);
			Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] encodeKey = cipher.doFinal(key.getBytes());
			String b64 = Base64.encode(encodeKey);
			return b64;
		} catch (Exception e) {
			logger.error("", e);
			throw new Exception(e.getMessage());
		}
	}

	public static byte[] decrypt(byte[] src, byte[] password) throws Exception {
		SecureRandom random = new SecureRandom();
		DESKeySpec desKey = new DESKeySpec(password);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		return cipher.doFinal(src);
	}

	/**
	 * type为1获取自己证书，其它值 获取别人证书
	 * 
	 * @param type
	 * @return
	 */
	public static PublicKey getPublicKey(int type) {
		try {
			Certificate certificate = null;
			if (type == 1) {
				certificate = getCertificate(accountPublicKey);
			} else {
				certificate = getCertificate(threePublicKey);
			}
			PublicKey key = certificate.getPublicKey();
			return key;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	public static byte[] sign(byte[] data) throws Exception {
		try {
			X509Certificate x509Certificate = (X509Certificate) getCertificate(accountPrivateKey);
			PrivateKey privateKey = getPrivateKey();
			Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
			signature.initSign(privateKey);
			signature.update(data);
			return signature.sign();
		} catch (Exception e) {
			logger.error("", e);
			throw new Exception(e.getMessage());
		}
	}

	public static boolean verify(byte[] data, String sign, String certificatePath) throws Exception {
		X509Certificate x509Certificate = (X509Certificate) getCertificate(certificatePath);
		PublicKey publicKey = getPublicKey(2);
		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		signature.initVerify(publicKey);
		signature.update(data);

		return signature.verify(Base64.decode(sign));

	}

	public static PrivateKey getPrivateKey() {
		try {
			KeyStore ks = getKeyStore(accountPrivateKey, accountPrivatePwd);
			PrivateKey key = (PrivateKey) ks.getKey(accountPrivateName, accountPrivatePwd.toCharArray());
			return key;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	static KeyStore getKeyStore(String keyStorePath, String password) throws Exception {
		FileInputStream is = new FileInputStream(keyStorePath);
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(is, password.toCharArray());
		is.close();
		return ks;
	}

	static Certificate certificate;

	static Certificate getCertificate(String certificatePath) {
		if (certificate != null)
			return certificate;
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			FileInputStream fis = new FileInputStream(certificatePath);
			BufferedInputStream bis = new BufferedInputStream(fis);
			certificate = certificateFactory.generateCertificate(bis);
			fis.close();
			bis.close();
			return certificate;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseJSON2Map(String jsonStr) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		// 最外层解析
		JSONObject json = JSONObject.fromObject(jsonStr);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			// 如果内层还是数组的话，继续解析
			if (v instanceof JSONArray) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Iterator<JSONObject> it = ((JSONArray) v).iterator();
				while (it.hasNext()) {
					JSONObject json2 = it.next();
					list.add(parseJSON2Map(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}
}
