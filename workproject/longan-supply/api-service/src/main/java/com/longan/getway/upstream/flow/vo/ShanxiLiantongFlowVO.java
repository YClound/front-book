package com.longan.getway.upstream.flow.vo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.HMacMD5;
import com.longan.getway.utils.MultiThreadedHttpConnection;

/**
 * 陕西联通流量充值
 * @author Administrator
 *
 */
public class ShanxiLiantongFlowVO {
	private static final Logger logger = LoggerFactory.getLogger(TianyiFlowVO.class);
	public final static Long traderId = Long.parseLong(Utils.getProperty("shanxiLiantong.traderId"));
	public final static String systemId = Utils.getProperty("shanxiLiantong.systemId");
	public final static String orderFlowUrl = Utils.getProperty("shanxiLiantong.orderFlowUrl");
	public final static String client = Utils.getProperty("shanxiLiantong.client");
	public final static String signKey = Utils.getProperty("shanxiLiantong.signKey");
	
	/**
	 * 
	 * @param type 使用余额类型（1 流量池，2 流量包）
	 * @param limit 充值额度
	 * @param phone 直充手机号码 
	 * @param bank 直充失败是否将流量充入用户流量银行 （0 否，1 是。默认否）
	 * @return
	 */
	public static SupplyResult<String> orderFlow(String type,String limit,String phone,String bank){
		String timeStap = String.valueOf(System.currentTimeMillis());
//		Map<String,String> paramMap = new HashMap<String,String>();
//		paramMap.put("HOUXIANG_SYSTEMID", systemId);
//		paramMap.put("HOUXIANG_CLIENT", client);
//		paramMap.put("HOUXIANG_TS ", timeStap);
//		paramMap.put("HOUXIANG_TYPE", type);
//		paramMap.put("HOUXIANG_LIMIT", limit);
//		paramMap.put("HOUXIANG_PHONE", phone);
//		paramMap.put("HOUXIANG_BANK", bank);
		String signStr = String.format("%s:%s:%s:%s:%s:%s:%s", systemId,client,timeStap,type,limit,phone,bank);
		String mac = HMacMD5.sign(signStr, signKey);
//		paramMap.put("HOUXIANG_MAC", mac);
		
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
//		String req = String.format("HOUXIANG_SYSTEMID=%s&HOUXIANG_CLIENT=%s&HOUXIANG_TS=%s&HOUXIANG_TYPE=%s&HOUXIANG_LIMIT=%s&HOUXIANG_PHONE=%s&HOUXIANG_MAC=%s&HOUXIANG_BANK=%s", systemId,client,timeStap, type,limit,phone,mac,bank);
		String req = String.format(orderFlowUrl+"?HOUXIANG_SYSTEMID=%s&HOUXIANG_CLIENT=%s&HOUXIANG_TS=%s&HOUXIANG_TYPE=%s&HOUXIANG_LIMIT=%s&HOUXIANG_PHONE=%s&HOUXIANG_MAC=%s&HOUXIANG_BANK=%s", systemId,client,timeStap, type,limit,phone,mac,bank);
		logger.warn("陝西联通后向流量充值请求参数:"+req);
//		return MultiThreadedHttpConnection.getInstance().sendDataByPost(orderFlowUrl, req);
		String result = getResult(req);
		logger.warn("陝西联通后向流量充值返回:"+result);
		SupplyResult<String> supplyResult = new SupplyResult<String>();
		if(result != null){
		    supplyResult.setStatus(supplyResult.STATUS_SUCCESS);
		    supplyResult.setModule(result);
		    return supplyResult;
		}
		supplyResult.setStatus(supplyResult.STATUS_FAILED);
		supplyResult.setResultMsg("提交失败");
		return supplyResult;
		
	}
	public static String getResult(String url) {
        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(20000);
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = reader.readLine();
                while (null != line) {
                    sb.append(line);
                    line = reader.readLine();
                }
                return sb.toString();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
