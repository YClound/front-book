package com.longan.getway.upstream.flow.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class KuyichongFlowVO {
	private static final Logger logger = LoggerFactory.getLogger(KuyichongFlowVO.class);
	public final static Long traderId = Long.parseLong(Utils.getProperty("KuyichongFlow.traderId"));
	public final static String chargeUrl = Utils.getProperty("KuyichongFlow.chargeUrl");
	public final static String account  = Utils.getProperty("KuyichongFlow.account");
	public final static String password  = Utils.getProperty("KuyichongFlow.password");
	
	public static SupplyResult<String> charge(String orderno,String mobile,String productId){
		String paramStr = String.format("orderno=%s&account=%s&password=%s&mobile=%s&productid=%s",orderno,account,password,mobile,productId);
		String preStr = String.format("orderno=%s&account=%s&password=%s&mobile=%s&productid=%s",orderno,account,password,mobile,productId);
		String sign = Md5Encrypt.md5(preStr);
		String url = chargeUrl+"?"+paramStr+"&sign="+sign;
		logger.warn("酷易充充值请求:"+url);
		System.out.println("酷易充充值请求:"+url);
		SupplyResult<String> result =  MultiThreadedHttpConnection.getInstance().sendDataByGetLongTimeout(url);
		logger.warn("酷易充充值返回:"+result.getModule());
		System.out.println("酷易充充值返回:"+result.getModule());
		return result ;
	} 
	public static void main(String[] args) {
		String orginalString = String.format("orderNo=%s&account=%s&password=%s&mobile=%s&productId=%s", "testno", "kywxmerchant", "kywxmerchant", "13000000000", "1");
		System.out.println(orginalString);
		System.out.println(Md5Encrypt.md5(orginalString));
		String SS = "orderno=testno&account=kywxmerchant&password=kywxmerchant&mobile=13000000000&productid=1";
		System.out.println(SS);
		System.out.println(Md5Encrypt.md5(SS));

	}
}
