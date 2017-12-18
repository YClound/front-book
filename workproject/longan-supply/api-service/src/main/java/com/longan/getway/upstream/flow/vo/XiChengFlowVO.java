package com.longan.getway.upstream.flow.vo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.Base64Utils;
import com.longan.getway.utils.MultiThreadedHttpConnection;

/**
 * 西城网络流量充值
 * 
 * @author Administrator
 *
 */
public class XiChengFlowVO {
	private static final Logger logger = LoggerFactory.getLogger(XiChengFlowVO.class);
	public final static Long traderId = Long.parseLong(Utils.getProperty("xiChengFlow.traderId"));
	private final static String custInteId = Utils.getProperty("xiChengFlow.custInteId");
	private final static String secretKey = Utils.getProperty("xiChengFlow.secretKey");
	private final static String baseUrl = Utils.getProperty("xiChengFlow.baseUrl");

	public static SupplyResult<Document> charge(String mobile, String orderId, String packCode, String effectType) {
		try {
			Random r = new Random();
			String echo = String.valueOf(r.nextInt(99999999));
			String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String url = baseUrl + "order.do";
			String signStr = createStr(custInteId, orderId, secretKey, echo, timestamp);
			String sign = Base64Utils.encode(Md5Encrypt.md5(signStr).getBytes());
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("utf-8");
			Element root = doc.addElement("request");
			Element head = root.addElement("head");
			head.addElement("custInteId").setText(custInteId);
			head.addElement("orderId").setText(orderId);
			head.addElement("orderType").setText("1");
			head.addElement("echo").setText(echo);
			head.addElement("timestamp").setText(timestamp);
			head.addElement("version").setText("1");
			head.addElement("chargeSign").setText(sign);
			Element body = root.addElement("body");
			Element item = body.addElement("item");
			item.addElement("packCode").setText(packCode);
			item.addElement("mobile").setText(mobile);
			item.addElement("effectType").setText(effectType);
			logger.warn("西城流量充值接口请求:" + doc.asXML());
			SupplyResult<Document> result = MultiThreadedHttpConnection.getInstance().sendDataByPostReturnXml(url, doc.asXML(), null);
			logger.warn("西城流量充值接口返回:" + result.getModule() == null ? "未返回数据" : result.getModule().asXML());
			return result;
		} catch (Exception e) {
			logger.warn("西城流量充值异常");
			e.printStackTrace();
			SupplyResult<Document> result = new SupplyResult<Document>();
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("西城流量充值异常");
			return result;
		}
	}

	public static SupplyResult<Document> queryBalance() {
		try {
			Random r = new Random();
			String echo = String.valueOf(r.nextInt(99999999));
			String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String url = baseUrl + "query/balance.do";
			String signStr = createStr(custInteId, secretKey, echo, timestamp);
			String sign = Base64Utils.encode(Md5Encrypt.md5(signStr).getBytes());
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("utf-8");
			Element root = doc.addElement("request");
			Element head = root.addElement("head");
			head.addElement("custInteId").setText(custInteId);
			head.addElement("echo").setText(echo);
			head.addElement("timestamp").setText(timestamp);
			head.addElement("version").setText("1");
			head.addElement("chargeSign").setText(sign);
			logger.warn("西城余额查询接口请求:" + doc.asXML());
			SupplyResult<Document> result = MultiThreadedHttpConnection.getInstance().sendDataByPostReturnXml(url, doc.asXML(), null);
			logger.warn("西城余额查询接口返回:" + result.getModule() == null ? "未返回数据" : result.getModule().asXML());
			return result;
		} catch (Exception e) {
			logger.warn("西城余额查询异常");
			e.printStackTrace();
			SupplyResult<Document> result = new SupplyResult<Document>();
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("交易异常");
			return result;
		}
	}

	public static SupplyResult<Document> queryOrder(String orderId, Date orderDate) {
		try {
			Random r = new Random();
			String echo = String.valueOf(r.nextInt(99999999));
			String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String url = baseUrl + "query/queryOrder.do";
			String signStr = createStr(custInteId, secretKey, echo, timestamp);
			String sign = Base64Utils.encode(Md5Encrypt.md5(signStr).getBytes());
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("utf-8");
			Element root = doc.addElement("request");
			Element head = root.addElement("head");
			head.addElement("custInteId").setText(custInteId);
			head.addElement("echo").setText(echo);
			head.addElement("timestamp").setText(timestamp);
			head.addElement("version").setText("1");
			head.addElement("chargeSign").setText(sign);
			Element body = root.addElement("body");
			Element item = body.addElement("item");
			item.addElement("month").setText(new SimpleDateFormat("yyyyMM").format(orderDate));
			item.addElement("orderIds").setText(orderId);
			logger.warn("西城订单查询接口请求:" + doc.asXML());
			SupplyResult<Document> result = MultiThreadedHttpConnection.getInstance().sendDataByPostReturnXml(url, doc.asXML(), null);
			logger.warn("西城订单查询接口返回:" + result.getModule() == null ? "未返回数据" : result.getModule().asXML());
			return result;
		} catch (Exception e) {
			logger.warn("西城订单查询异常");
			e.printStackTrace();
			SupplyResult<Document> result = new SupplyResult<Document>();
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("西城订单查询异常");
			return result;
		}
	}

	private static String createStr(String... ss) {
		StringBuffer sb = new StringBuffer();
		for (String s : ss) {
			sb.append(s);
		}
		return sb.toString();
	}
}
