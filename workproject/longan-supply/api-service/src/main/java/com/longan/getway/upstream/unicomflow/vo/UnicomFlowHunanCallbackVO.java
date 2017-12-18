package com.longan.getway.upstream.unicomflow.vo;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class UnicomFlowHunanCallbackVO {
	private static String appkey = Utils.getProperty("unicomFlowHunanOff.appKey");
	private static String contractid = Utils.getProperty("unicomFlowHunanOff.contractId");
	private static String secretKey = Utils.getProperty("unicomFlowHunanOff.secretKey");
	private String productid;
	private String transid;
	private String mobile;
	private String status;
	private String sign;

	public boolean checkSign() {
		String sign = createSign();
		return sign.equals(getSign());
	}

	public String getAppkey() {
		return appkey;
	}

	public String getTransid() {
		return transid;
	}

	public void setTransid(String transid) {
		this.transid = transid;
	}

	public String getContractid() {
		return contractid;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String createSign() {
		StringBuffer sb = new StringBuffer();
		sb.append("appkey=").append(appkey).append("&transid=").append(transid)
				.append("&contractid=").append(contractid).append("&productid=").append(productid)
				.append("&mobile=").append(mobile).append("&status=").append(status)
				.append("&key=").append(secretKey);
		String signStr = Md5Encrypt.md5(sb.toString());
		return signStr;
	}

}
