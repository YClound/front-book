package com.longan.getway.upstream.flow.vo;

import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.utils.SHAUtils;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.MultiThreadedHttpConnection;

/**
 * 国信联通流量充值
 * @author Administrator
 *
 */
public class GuoxinLiantongFlowVO {
	private static final Logger logger = LoggerFactory.getLogger(GuoxinLiantongFlowVO.class);
	public final static Long traderId = Long.parseLong(Utils.getProperty("guoxinLiantongFlow.traderId"));
	private final static String appkey = Utils.getProperty("guoxinLiantongFlow.appKey");
	public final static String securityKey = Utils.getProperty("guoxinLiantongFlow.securityKey");
	private final static String notifyUrl = Utils.getProperty("guoxinLiantongFlow.notifyUrl");
	private final static String baseUrl = Utils.getProperty("guoxinLiantongFlow.baseUrl");
	/**
	 * 充值
	 * @param productId 产品id
	 * @param phoneNo  手机号
	 * @param cstmOrderNo 我方订单号
	 * @param region 使用区域  0-国内 1-省内
	 * @return
	 */
	public static SupplyResult<String> charge(String productId,String phoneNo,String cstmOrderNo,String region){
		Map<String,String> map = new TreeMap<String,String>();
		map.put("appkey", appkey);
		map.put("v", "1.2");
		map.put("method", "backward.order");
		map.put("callbackUrl", notifyUrl);
		map.put("productId", productId);
		map.put("phoneNo", phoneNo);
		map.put("region", region);
		map.put("cstmOrderNo", cstmOrderNo);
		map.put("sig", sign(map));
		JSONObject jo = JSONObject.fromObject(map);
		return MultiThreadedHttpConnection.getInstance().sendDataByPost(baseUrl, jo.toString());
	}
	/**
	 * 余额查询
	 * @return
	 */
	public static SupplyResult<String> queryOrder(String orderId,String cstmOrderNo){
		Map<String,String> map = new TreeMap<String,String>();
		map.put("appkey", appkey);
		map.put("v", "1.2");
		map.put("method", "backward.orderStatus");
		map.put("cstmOrderNo", cstmOrderNo);
		map.put("orderId", orderId);
		map.put("sig", sign(map));
		JSONObject jo = JSONObject.fromObject(map);
		return MultiThreadedHttpConnection.getInstance().sendDataByPost(baseUrl, jo.toString());
	} 
	/**
	 * 余额查询
	 * @return
	 */
	public static SupplyResult<String> queryBalance(){
		Map<String,String> map = new TreeMap<String,String>();
		map.put("appkey", appkey);
		map.put("v", "1.2");
		map.put("method", "backward.queryBalance");
		map.put("sig", sign(map));
		JSONObject jo = JSONObject.fromObject(map);
		return MultiThreadedHttpConnection.getInstance().sendDataByPost(baseUrl, jo.toString());
	} 
	
	private static String sign(Map<String,String> paramMap){
		StringBuffer sb = new StringBuffer();
		for(String key : paramMap.keySet()){
			sb.append(key).append(paramMap.get(key));
		}
		sb.append(securityKey);
		return SHAUtils.SHA1(sb.toString());
	}
	
}
