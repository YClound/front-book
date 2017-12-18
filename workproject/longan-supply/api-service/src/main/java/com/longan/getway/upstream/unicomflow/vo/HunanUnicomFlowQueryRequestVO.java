package com.longan.getway.upstream.unicomflow.vo;

import net.sf.json.JSONObject;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class HunanUnicomFlowQueryRequestVO {
	private static String appkey = Utils.getProperty("unicomFlowHunanOff.appKey");
	private static String secretKey = Utils.getProperty("unicomFlowHunanOff.secretKey");
	private static String queryAction = Utils.getProperty("unicomFlowHunanOff.queryAction");
	private static String url = Utils.getProperty("unicomFlowHunanOff.URL");

	private String transid;
	private String sign;

	public String getAppkey() {
		return appkey;
	}

	public String getTransid() {
		return transid;
	}

	public void setTransid(String transid) {
		this.transid = transid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String createQueryAction() {
		return url + queryAction;
	}

	public void createSign() {
		StringBuffer sb = new StringBuffer();
		sb.append("appkey=").append(appkey).append("&transid=").append(transid).append("&key=")
				.append(secretKey);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}

	public String createRequestParam() {
		return JSONObject.fromObject(this).toString();
	}

}
