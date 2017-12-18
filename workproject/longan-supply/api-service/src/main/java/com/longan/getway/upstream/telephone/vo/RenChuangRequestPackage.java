package com.longan.getway.upstream.telephone.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class RenChuangRequestPackage {
	private static final Logger logger = LoggerFactory.getLogger(RenChuangRequestPackage.class);
	private String packageStr;

	public RenChuangRequestPackage(String phoneNum, String facePrice) {
		BodyInfo bodyInfo = new BodyInfo(rightPad(phoneNum, 13), leftZero(facePrice, 16));
		String bodyStr = bodyInfo.toString().toUpperCase();
		HeadInfo headInfo = new HeadInfo(leftZero(bodyStr.length() + "", 4), Md5Encrypt.md5(bodyStr
				+ Utils.getProperty("renChuangSupply.key")));
		String headStr = headInfo.toString().toUpperCase();
		this.packageStr = headStr + bodyStr + "\n\r";
	}

	// 包头
	class HeadInfo {
		// 报文体长度
		private String bodyLength;
		// 业务类型
		private final String tradeType = "RCHF";
		// 交易代码
		private final String tradeCode = "800001";
		// 操作员号码
		private final String operateId = rightPad(Utils.getProperty("renChuangSupply.operateId"), 4);
		// 操作员密码
		private final String operatePwd = leftZero(Utils.getProperty("renChuangSupply.operatePwd"),
				32);
		// 报文签名
		private String sign;

		public HeadInfo(String bodyLength, String sign) {
			this.bodyLength = bodyLength;
			this.sign = sign;
		}

		@Override
		public String toString() {
			return bodyLength + tradeType + tradeCode + operateId + operatePwd + sign;
		}

	}

	// 包体
	class BodyInfo {
		// 渠道账号
		private final String channelId = rightPad(Utils.getProperty("renChuangSupply.channelId"),
				13);
		// 渠道密码
		private final String channelPwd = Md5Encrypt.md5(Utils
				.getProperty("renChuangSupply.channelPwd"));
		// 运营商类型
		private final String operType = "02";
		// 业务类型
		private final String bizType = "01";
		// 客户区号
		private final String areaCode = rightPad(" ", 4);
		// 客户手机号
		private String phoneNum;
		// 充值面额
		private String facePrice;

		public BodyInfo(String phoneNum, String facePrice) {
			this.phoneNum = phoneNum;
			this.facePrice = facePrice;
		}

		@Override
		public String toString() {
			return channelId + channelPwd + operType + bizType + areaCode + phoneNum + facePrice;
		}

	}

	public static String leftZero(String num, int length) {// 数字前补0
		try {
			return com.longan.getway.utils.Utils.FormatStringAddZero(num, length);
		} catch (Exception e) {
			logger.error("leftPad error" + e.getMessage());
		}
		return null;
	}

	public static String rightPad(String text, int length) {// 右补" "
		try {
			return com.longan.getway.utils.Utils.FormatStringAddBlank(text, length);
		} catch (Exception e) {
			logger.error("rightPad error" + e.getMessage());
		}
		return null;
	}

	@Override
	public String toString() {// 要发送的报文
		return packageStr;
	}

}
