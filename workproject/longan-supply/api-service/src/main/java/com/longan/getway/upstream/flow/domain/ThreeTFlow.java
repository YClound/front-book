package com.longan.getway.upstream.flow.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.MultiThreadedHttpConnection;

import net.sf.json.JSONObject;

public class ThreeTFlow {
	private final static Logger logger = org.slf4j.LoggerFactory.getLogger(ThreeTFlow.class);

	private final static String url = Utils.getProperty("threeTFlow.url");
	private final static String appSecret = Utils.getProperty("threeTFlow.appSecret");
	private final static String appKey = Utils.getProperty("threeTFlow.appKey");
	private final static String notifyUrl = Utils.getProperty("threeTFlow.notifyUrl");

	private String prodValue;
	private String customer;
	private int prodPayType = 0;
	private String channelOrderNo;
	private Long timestamp = System.currentTimeMillis() / 1000;
	private String sign;

	private String orderNo;

	public String getProdValue() {
		return prodValue;
	}

	public void setProdValue(String prodValue) {
		this.prodValue = prodValue;
	}

	public String getCustomer() {
		return customer;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public int getProdPayType() {
		return prodPayType;
	}

	public void setProdPayType(int prodPayType) {
		this.prodPayType = prodPayType;
	}

	public String getChannelOrderNo() {
		return channelOrderNo;
	}

	public void setChannelOrderNo(String channelOrderNo) {
		this.channelOrderNo = channelOrderNo;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public static String getUrl() {
		return url;
	}

	public static String getAppsecret() {
		return appSecret;
	}

	public static String getAppkey() {
		return appKey;
	}

	private void setCreateOrderSign() {
		StringBuffer sb = new StringBuffer("");
		sb.append("appkey").append(appKey).append("channelOrderNo").append(channelOrderNo).append("customer").append(customer).append("notifyUrl").append(notifyUrl).append("prodPayType").append(prodPayType).append("prodValue").append(prodValue).append("timestamp").append(timestamp).append(appSecret);
		System.out.println(sb.toString());
		try {
			System.out.println(Md5Encrypt.md5(URLEncoder.encode(sb.toString(), "utf-8")));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println(Md5Encrypt.md5(sb.toString()));
		try {
			this.setSign(Md5Encrypt.md5(URLEncoder.encode(sb.toString(), "utf-8")));
		} catch (UnsupportedEncodingException e) {
			logger.error("encode error", e);
		}
	}

	private void setSubmitOrderSign() {
		StringBuffer sb = new StringBuffer("");
		sb.append("appkey").append(appKey).append("orderNo").append(orderNo).append("timestamp").append(timestamp).append(appSecret);
		try {
			this.setSign(Md5Encrypt.md5(URLEncoder.encode(sb.toString(), "utf-8")));
		} catch (UnsupportedEncodingException e) {
			logger.error("encode error", e);
		}
	}

	private void setQuerySign() {
		StringBuffer sb = new StringBuffer("");
		sb.append("appkey").append(appKey).append("orderNo").append(orderNo).append("timestamp").append(timestamp).append(appSecret);
		System.out.println(sb.toString());
		try {
			this.setSign(Md5Encrypt.md5(URLEncoder.encode(sb.toString(), "utf-8")));
		} catch (UnsupportedEncodingException e) {
			logger.error("encode error", e);
		}
	}

	private void setBalanceSign() {
		StringBuffer sb = new StringBuffer("");
		sb.append("appkey").append(appKey).append("timestamp").append(timestamp).append(appSecret);
		this.setSign(Md5Encrypt.md5(sb.toString()));
	}

	public SupplyResult<String> supply() {
		SupplyResult<String> result = new SupplyResult<String>();
		setCreateOrderSign();
		String notifyEncode = null;
		try {
			notifyEncode = URLEncoder.encode(notifyUrl, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("", e1);
		}

		StringBuffer createOrderUrl = new StringBuffer(url);
		createOrderUrl.append("r/Channel/createOrderWithProdValue?");
		createOrderUrl.append("appkey=").append(appKey).append("&prodValue=").append(prodValue);
		createOrderUrl.append("&customer=").append(customer).append("&prodPayType=").append(prodPayType).append("&notifyUrl=").append(notifyEncode);
		createOrderUrl.append("&channelOrderNo=").append(channelOrderNo).append("&timestamp=").append(timestamp).append("&sign=").append(sign);

		SupplyResult<String> createOrderResult = MultiThreadedHttpConnection.getInstance().sendDataByGetReturnString(createOrderUrl.toString());
		if (!createOrderResult.isSuccess()) {
			result.setResultMsg(createOrderResult.getResultMsg());
			return result;
		}

		try {
			JSONObject createOrderJO = JSONObject.fromObject(createOrderResult.getModule());
			if (createOrderJO.get("resultCode") == null || !"1000".equals(createOrderJO.get("resultCode").toString())) {
				result.setResultMsg(createOrderJO.get("resultReason").toString());
				return result;
			}

			orderNo = createOrderJO.get("orderNo").toString();
			if (StringUtils.isEmpty(orderNo)) {
				result.setResultMsg("上游订单号为空");
				return result;
			}
			this.setOrderNo(orderNo);
		} catch (Exception e) {
			logger.error("订单创建解析失败", e);
			result.setResultMsg("订单创建解析失败");
			return result;
		}

		// 重新创建timestamp
		this.setTimestamp(System.currentTimeMillis() / 1000);
		setSubmitOrderSign();
		StringBuffer submitOrderUrl = new StringBuffer(url);
		submitOrderUrl.append("r/Channel/submitOrder?");
		submitOrderUrl.append("orderNo=").append(orderNo).append("&appkey=").append(appKey);
		submitOrderUrl.append("&timestamp=").append(timestamp).append("&sign=").append(sign);

		result = MultiThreadedHttpConnection.getInstance().sendDataByGetReturnString(submitOrderUrl.toString());

		return result;
	}

	public SupplyResult<String> query() {
		SupplyResult<String> result = new SupplyResult<String>();
		setQuerySign();
		StringBuffer queryUrl = new StringBuffer(url);
		queryUrl.append("r/Channel/queryOrder?");
		queryUrl.append("appkey=").append(appKey);
		queryUrl.append("&orderNo=").append(orderNo).append("&timestamp=").append(timestamp).append("&sign=").append(sign);
		result = MultiThreadedHttpConnection.getInstance().sendDataByGetReturnString(queryUrl.toString());
		return result;
	}

	public SupplyResult<String> queryBalance() {
		SupplyResult<String> result = new SupplyResult<String>();
		setBalanceSign();
		StringBuffer queryBalanceUrl = new StringBuffer(url);
		queryBalanceUrl.append("r/Channel/queryBalance?");
		queryBalanceUrl.append("appkey=").append(appKey);
		queryBalanceUrl.append("&timestamp=").append(timestamp).append("&sign=").append(sign);
		System.out.println(queryBalanceUrl.toString());
		result = MultiThreadedHttpConnection.getInstance().sendDataByGetReturnString(queryBalanceUrl.toString());
		return result;
	}

}
