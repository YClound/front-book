package com.longan.getway.upstream.unicomflow.vo;

import org.codehaus.jackson.annotate.JsonIgnore;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class HunanUnicomFlowChargeRequestVO {
	private static String appkey = Utils.getProperty("unicomFlowHunanOff.appKey");
	private static String contractid = Utils.getProperty("unicomFlowHunanOff.contractId");
	private static String secretKey = Utils.getProperty("unicomFlowHunanOff.secretKey");
	private static String chargeAction = Utils.getProperty("unicomFlowHunanOff.chargeAction");
	private static String url = Utils.getProperty("unicomFlowHunanOff.URL");

	private static final JsonConfig jc = new JsonConfig();
	private static String transStr = "UNI010731";
	private String productid;
	private String mobile;
	private String transid;
	private String sign;

	static {
		jc.addIgnoreFieldAnnotation(JsonIgnore.class);
	}

	public String getAppkey() {
		return appkey;
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

	@JsonIgnore
	public String getSecretKey() {
		return secretKey;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public void createTransId(String count) {
		this.setTransid(transStr + System.currentTimeMillis() / 1000 + count);
	}

	public String createChargeAciton() {
		return url + chargeAction;
	}

	public void createSign() {
		StringBuffer sb = new StringBuffer();
		sb.append("appkey=").append(appkey).append("&transid=").append(transid)
				.append("&contractid=").append(contractid).append("&productid=").append(productid)
				.append("&mobile=").append(mobile).append("&key=").append(secretKey);
		String signStr = Md5Encrypt.md5(sb.toString());
		setSign(signStr);
	}

	public String createRequestParam() {
		return JSONObject.fromObject(this, jc).toString();
	}

	public static void main(String[] args) {
		HunanUnicomFlowChargeRequestVO vo = new HunanUnicomFlowChargeRequestVO();
		vo.setMobile("13757146578");
		vo.createTransId("00000001");
		vo.createSign();
		System.out.println(vo.createRequestParam());
	}

}
