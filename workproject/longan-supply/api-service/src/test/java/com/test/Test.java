package com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import com.longan.biz.utils.Md5Encrypt;


public class Test {
	static String key="f3bf0120f611e1360aefd09903ab181c9b1fcabbe4a1e7787a75a4c1ce13ae79";
	static String userId="162";
	static String type="*/*";
	public static void main(String[] args){
//		type="application/json;charset=UTF-8";
		test11Client();
	}
	//充值接口测试数据
	private static void test11Client(){
		String goods="13480";
		String number="13410000000";
		String order_no="aaaaaaaaa19";
		String create_time="20160705114001";
//		//根据变量名自然排序
		String sign = create_time+goods+number+order_no+userId+key;
		sign=Md5Encrypt.md5(sign);
		String url = "http://localhost/api-service/buy/aync.do?userId="+userId+"&goods="+goods+"&number="+number+"&order_no="+order_no+"&create_time="+create_time+"&sign="+sign;
		clientGet(url);
	}
	
	private static void test22Client(){
		String order_no="aaaaaaaaaa8";
		String sign=order_no+userId+key;
		sign=Md5Encrypt.md5(sign);
		String url="http://localhost/api-service/query/order.do?userId="+userId+"&order_no="+order_no+"&sign="+sign;
		clientGet(url);
	}
	
	private static void test33Client(){
		String sign=userId+key;
		sign=Md5Encrypt.md5(sign);
		String url="http://localhost/api-service/query/balance.do?userId="+userId+"&sign="+sign;
		clientGet(url);
	}
	
	//充值接口测试数据
	private static void test1(){
		String itemId="13516";
		String uid="13410000000";
		String serialno="lsadjfsdioj2l3";
//		String serialno="ldsjflkdsnf43";
		String dtCreate="20160705114001";
//		//根据变量名自然排序
//		String sign = dtCreate+itemId+serialno+uid+userId+key;
//		System.out.println(sign);
//		sign=Md5Encrypt.md5(sign);
//		System.out.println(sign);
		//测试连接url：http://localhost/longan-supply-getway/unicomAync/buy.do?userId=154&itemId=13516&uid=13410000000&serialno=lsadjfsdioj2l3&dtCreate=20160705114001&sign=d7cb92c8104eab538e8b038affee7cd4
		//测试连接url：http://localhost/longan-supply-getway/unicomAync/buy.do?userId=168&itemId=13516&uid=13410000000&serialno=ldsjflkdsnf43&dtCreate=20160705114001&sign=646a6c02dd9d272a5b751aadb8838bf4
	}
	//新购买接口测试数据
	private static void test11(){
		String goods="13516";
		String number="13410000000";
		String order_no="aaaaaaaaaa6";
		String create_time="20160705114001";
//		//根据变量名自然排序
		String sign = create_time+goods+number+order_no+userId+key;
		System.out.println(sign);
		sign=Md5Encrypt.md5(sign);
		System.out.println(sign);
		//测试连接url：http://121.40.92.102/api-service/buy/aync.do?userId=168&goods=13516&number=13410000000&order_no=aaaaaaaaaa6&create_time=20160705114001&sign=d5b8bc981088ff8d55fcacb322731cf6

	}
	//交易查询接口测试数据
	public static void test2(){
		String serialno="lsadjfsdioj2l3";//交易信息商户系统流水号
		String sign = serialno+userId+key;
		System.out.println(sign);
		sign=Md5Encrypt.md5(sign);
//		System.out.println(sign);
		//测试连接url：http://localhost/longan-supply-getway/unicomAync/queryBizOrder.do?userId=168&serialno=lsadjfsdioj2l3&sign=e1a91b49ae87bf483f26f096a71b9180
	}
	//新交易接口测试数据
	public static void test22(){
		String order_no="lsadjfsdioj2l3";
		String sign=order_no+userId+key;
		sign=Md5Encrypt.md5(sign);
		System.out.println(sign);
		//测试连接url：http://localhost/longan-supply-getway/query/order.do?userId=168&order_no=lsadjfsdioj2l3&sign=e1a91b49ae87bf483f26f096a71b9180
	}
	
	//余额查询测试接口
	public static void test3(){
		String sign=userId+key;
		System.out.println(sign);
		sign=Md5Encrypt.md5(sign);
		System.out.println(sign);
		//测试连接url：http://localhost/longan-supply-getway/unicomAync/queryBalance.do?userId=168&sign=1bfe07daf9b9de2ebe2def5a5e0eb3a6
	}
	//新余额查询接口
	public static void test33(){
		String sign=userId+key;
		sign=Md5Encrypt.md5(sign);
		System.out.println(sign);
		//测试连接：http://localhost/longan-supply-getway/query/balance.do?userId=168&sign=1bfe07daf9b9de2ebe2def5a5e0eb3a6
	}
	public static void test4(){
		//对账接口
		String acctDate="20160705";
		String sign=acctDate+userId+key;
		System.out.println(sign);
		sign=Md5Encrypt.md5(sign);
		System.out.println(sign);
		// http://localhost/longan-supply-getway/unicomAync/queryCb.do?userId=168&acctDate=20160705&sign=bcf65931c8a364d034394bc01ba79068
	}
	
	private static void clientGet(String url){
		System.out.println("url:"+url);
		URL realUrl;
		try {
			realUrl = new URL(url);
	        // 打开和URL之间的连接
	        URLConnection connection = realUrl.openConnection();
	        // 设置通用的请求属性
	        connection.setRequestProperty("accept", type);
	        connection.setRequestProperty("connection", "Keep-Alive");
	        connection.setRequestProperty("user-agent",
	                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	        // 建立实际的连接
	        connection.connect();
	        // 获取所有响应头字段
	        Map<String, List<String>> map = connection.getHeaderFields();
	        // 遍历所有的响应头字段
	        for (String key : map.keySet()) {
	            System.out.println(key + "--->" + map.get(key));
	        }
	        // 定义 BufferedReader输入流来读取URL的响应
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	                connection.getInputStream()));
	        String line,result="";
	        while ((line = in.readLine()) != null) {
	            result += line;
	        }
	        System.out.println(result);
	        if (in != null) {
	            in.close();
	        }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
