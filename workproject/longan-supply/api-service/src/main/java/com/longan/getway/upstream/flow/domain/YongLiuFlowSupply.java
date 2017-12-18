package com.longan.getway.upstream.flow.domain;

import java.util.HashMap;
import java.util.Map;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class YongLiuFlowSupply {
	private static final String url = Utils.getProperty("yongliu.url.supply");
	private static final String privateKey = Utils.getProperty("yongliu.privateKey");
	private static final String notifyUrl = Utils.getProperty("yongliu.callback");
	private static final String traderNo = Utils.getProperty("yongliu.userId");

	private String transNo;
	private String phone;
	private String goodsId;
	private String sign;

	public String getTraderNo() {
		return traderNo;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	private void createSign() {
		StringBuffer sb = new StringBuffer("");
		sb.append(traderNo).append(transNo).append(phone).append(goodsId).append(privateKey);
		System.out.println(sb);
		this.sign = Md5Encrypt.md5(sb.toString());
	}

	public SupplyResult<String> supply() {
		createSign();
		Map<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("traderNo", traderNo);
		paramMap.put("transNo", transNo);
		paramMap.put("phone", phone);
		paramMap.put("goodsId", goodsId);
		paramMap.put("notifyUrl", notifyUrl);
		paramMap.put("sign", sign);
		return MultiThreadedHttpConnection.getInstance().sendPostByMap(url, paramMap);
	}

}
