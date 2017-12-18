package com.longan.getway.upstream.flow.vo.guangdong;

import java.util.Map;

import net.sf.json.JSONObject;

public class CtController {

	/**
	 * 提交并接收
	 * 
	 * @param input
	 * @throws Exception
	 */
	public String submit(Map<String, Object> input) throws Exception {
		// try {
		// 生成数字信封（DIGITAL_ENVELOPE）
		String desKey = SecurityHandler.genRandomNum(16);
		String envelope = SecurityHandler.generateEnvelope(desKey);

		// 生成数字签名（DEGITAL_SIGNATURE）
		String signature = SecurityHandler.encodeBase64(SecurityHandler
				.sign(JSONObject.fromObject(input).toString().getBytes()));

		// 生成密文（DATA_JSON）
		byte[] data = SecurityHandler.encryptAES(JSONObject.fromObject(input)
				.toString().getBytes("UTF-8"), desKey);
		String mw = SecurityHandler.encodeBase64(data);
		/**
		 * 构造提交前置参数，必须是post提交
		 */
		String param = "SERVICE_TYPE=" + input.get("SERVICE_TYPE")
				+ "&SAFE_MODE=" + input.get("SAFE_MODE") + "&DIGITAL_ENVELOPE"
				+ "=" + envelope + "&DEGITAL_SIGNATURE" + "=" + signature
				+ "&MERCH_CODE=" + input.get("MERCH_CODE") + "&DATA_JSON" + "="
				+ mw;
		/**
		 * 提交并获取响应
		 */
		return SecurityHandler.postHttp(SecurityHandler.url, "service.do",
				param);
		/*
		 * } catch (Exception e) { e.printStackTrace(); return
		 * CTErrorCode._40002.getReturnObj("提交异常 ：" +
		 * e.getCause().getMessage()); }
		 */
	}

	public Map<String, String> receive(String input) {
		try {
			Map<String, Object> ret = SecurityHandler.parseJSON2Map(input);
			Object rc = ret.get("RESULT_CODE");
			if (rc != null) {
				// 只有成功后才能调用下面的代码
				if (rc.toString().equals("00000")) {
					// 获取签名
					String sign = ret.get("DEGITAL_SIGNATURE").toString();
					// 获取信封
					String enve = ret.get("DIGITAL_ENVELOPE").toString();
					// 获取密文
					String enData = ret.get("DATA_JSON").toString();
					// 解开信封
					byte[] d = SecurityHandler.decodeBase64(enve);
					byte[] b = SecurityHandler.decryptByPrivateKey(d);
					// 解开密文
					byte[] srcData = SecurityHandler
							.decryptAES(SecurityHandler.decodeBase64(enData),
									new String(b));
					// 验证签名
					if (SecurityHandler.verify(srcData, sign,
							SecurityHandler.threePublicKey)) {
						Map<String, String> ll = CTErrorCode._00000.reply();
						ll.put("DATA_JSON", new String(srcData));
						return ll;
					} else {
						return CTErrorCode._40023.reply("验签失败");
					}
				} else {
					return CTErrorCode._40023.reply(ret.get("RESULT_INFO")
							.toString());
				}
			} else {
				return CTErrorCode._40023.reply("如果商户端接收到为空则前置接口有问题，联系相关技术人员");
			}
		} catch (Exception e) {
			return CTErrorCode._40023.reply("验证异常 ："
					+ e.getCause().getMessage());
		}
	}
}
