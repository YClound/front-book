package com.longan.getway.upstream.flow.vo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.MultiThreadedHttpConnection;

/**
 * 麦穗流量充值
 * @author Administrator
 *
 */
public class MaisuiFlowVO {
	public static final Long traderId = Long.valueOf(Utils.getProperty("maisuiFlow.traderId"));
    private static  Logger logger = LoggerFactory.getLogger(MaisuiFlowVO.class);
    public static final String account = Utils.getProperty("maisuiFlow.account");
    public static final String apiKey = Utils.getProperty("maisuiFlow.apiKey");
    public static final String baseUrl = Utils.getProperty("maisuiFlow.baseUrl");
    
    public static HashMap<String, String> resultMap = new HashMap<String,String>(){
    	{
    		put("001", "参数错误");
    		put("002", "充值号码不合法");
    		put("003", "帐号密码错误");
    		put("004", "余额不足");
    		put("005", "不存在指定流量包");
    		put("006", "不支持该地区");
    		put("007", "卡号或者密码错误");
    		put("008", "该卡已使用过");
    		put("009", "该卡不支持(移动/电信/联通)号码");
    		put("010", "协议版本错误");
    		put("100", "签名验证错误");
    		put("999", "其他错误");
    	}
    };
    
    public static SupplyResult<String> charge(String range,String orderId,String mobile,String flowPackage){
    	String urlStr = baseUrl+"?action=%s&v=%s&range=%s&outTradeNo=%s&account=%s&mobile=%s&package=%s&sign=%s";
    	Map<String,String> signMap = new TreeMap<String,String>();
    	signMap.put("account", account);
    	signMap.put("mobile", mobile);
    	signMap.put("package", flowPackage);
    	String sign = sign(signMap);
    	System.out.println("麦穗流量签名后数据:"+sign);
    	String url = String.format(urlStr,"charge","1.1",range,orderId,account,mobile,flowPackage,sign);
    	logger.warn("麦穗流量充值请求:"+url);
    	System.out.println("麦穗流量充值请求:"+url);
    	SupplyResult<String> result =  MultiThreadedHttpConnection.getInstance().sendDataByGetReturnString(url);
    	System.out.println("麦穗流量充值返回:"+result.getModule());
    	logger.warn("麦穗流量充值返回:"+result.getModule());
    	return result;
    }
    public static SupplyResult<String> queryBalance(){
    	String urlStr = baseUrl+"?action=%s&v=%s&account=%s&sign=%s";
    	Map<String,String> signMap = new TreeMap<String,String>();
    	signMap.put("account", account);
    	String sign = sign(signMap);
    	String url = String.format(urlStr,"getBalance","1.1",account,sign);
    	SupplyResult<String> result =  MultiThreadedHttpConnection.getInstance().sendDataByGetReturnString(url);
    	return result;
    }
    public static String sign(Map<String,String> paramMap){
		StringBuffer sb = new StringBuffer("");
		for (Entry<String, String> e : paramMap.entrySet()) {
			sb.append(e.getKey()).append("=").append(e.getValue() == null ? "":e.getValue()).append("&");
		}
		sb.append("key").append("=").append(apiKey);
		System.out.println("麦穗流量签名前数据:"+sb.toString());
		return Md5Encrypt.md5(sb.toString()).toLowerCase();
	}
}
