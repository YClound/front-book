package com.longan.getway.upstream.flow.vo;

import java.util.Random;

import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.jce.provider.JCEMac.SHA1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.utils.SHAUtils;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.MultiThreadedHttpConnection;

/**
 * 创蓝流链充值
 * @author Administrator
 *
 */
public class ChuangLanFlowVO {
	private static final Logger logger = LoggerFactory.getLogger(ChuangLanFlowVO.class);
	public final static Long traderId = Long.parseLong(Utils.getProperty("chuangLanFlow.traderId"));
	private final static String account = Utils.getProperty("chuangLanFlow.account");
	private final static String key = Utils.getProperty("chuangLanFlow.key");
	private final static String baseUrl = Utils.getProperty("chuangLanFlow.baseUrl");
	
	/**
	 * 充值
	 * @param mobile
	 * @param packageCode
	 * @param orderId
	 * @return
	 */
	public static SupplyResult<String> charge(String mobile,String packageCode,String orderId){
		
		String timestamp = String.valueOf(System.currentTimeMillis()/1000);
		String noncestr = ramStr();
		String signStr = "account=%s&ext_id=%s&mobile=%s&noncestr=%s&package=%s&timestamp=%s&key=%s";
		signStr = String.format(signStr,account,orderId,mobile,noncestr,packageCode,timestamp,key);
		String sign = SHAUtils.SHA1(signStr);
		String paramStr = "account=%s&ext_id=%s&mobile=%s&noncestr=%s&timestamp=%s&package=%s&signature=%s";
		paramStr = String.format(paramStr, account,orderId,mobile,noncestr,timestamp,packageCode,sign);
		String url = baseUrl +"send?"+paramStr;
		logger.warn("创蓝流链充值接口请求:"+url);
		SupplyResult<String> result = MultiThreadedHttpConnection.getInstance().sendDataByGetReturnString(url);
		logger.warn("创蓝流链充值接口返回:"+result.getModule());
		return result ;
	}
	
	public static SupplyResult<String> chargeQuery(String orderId){
		
		String signStr = String.format("account=%s&ext_id=%s&key=%s",account,orderId,key);
		String sign = SHAUtils.SHA1(signStr);
		String paramStr = "account=%s&ext_id=%s&signature=%s";
		paramStr = String.format(paramStr,account,orderId,sign);
		String url = baseUrl +"checkStatus?"+paramStr;
		logger.warn("创蓝流链查询接口请求:"+paramStr);
		SupplyResult<String> result = MultiThreadedHttpConnection.getInstance().sendDataByGetReturnString(url);
		logger.warn("创蓝流链查询接口返回:"+result.getModule());
		return result ;
	}
	
	public static String ramStr(){
		Random ran = new Random();
		int i = ran.nextInt(9999999);
		return String.valueOf(i);
	}
	
	
}
