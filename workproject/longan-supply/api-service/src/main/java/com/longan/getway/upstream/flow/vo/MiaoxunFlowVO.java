package com.longan.getway.upstream.flow.vo;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.MultiThreadedHttpConnection;

/**
 * 妙讯流量
 * @author Administrator
 *
 */
public class MiaoxunFlowVO {
	private static final Logger logger = LoggerFactory.getLogger(MiaoxunFlowVO.class);
	public final static Long traderId = Long.parseLong(Utils.getProperty("MiaoxunFlow.traderId"));
	private final static String merid = Utils.getProperty("MiaoxunFlow.merid");
	public final static String secretkey = Utils.getProperty("MiaoxunFlow.secretkey");
	private final static String notifyUrl = Utils.getProperty("MiaoxunFlow.notifyUrl");
	private final static String baseUrl = Utils.getProperty("MiaoxunFlow.baseUrl");
	private final static String chargeUrl = baseUrl+"FlowAgent.aspx";
	private final static String queryBalanceUrl = baseUrl+"FlowBalance.aspx";
	public static final Map<String, String> codeResultMap = new HashMap<String, String>();
	static{
		codeResultMap.put("10", "缺少必填参数");
		codeResultMap.put("11", "商户号不存在");
		codeResultMap.put("12", "参数签名错误");
		codeResultMap.put("13", "无匹配流量产品");
		codeResultMap.put("14", "订单号重复");
		codeResultMap.put("15", "扣款失败类");
		codeResultMap.put("98", "其它错误类");
		codeResultMap.put("99", "系统错误");
	}
	
	/**
	 * 
	 * @param orderId 订单号
	 * @param mobile 手机号
	 * @param flowValue 流量值 单位 M
	 * @param range 流量范围
	 * @param otime 操作时间yyyyMMddHHmmss
	 * @return
	 */
	public static SupplyResult<String> charge(String orderId,String mobile,String flowvalue,String range,String otime){
		try{
			String signStr = String.format("merid=%s&orderid=%s&mobile=%s&flowvalue=%s&range=%s&otime=%s&notifyurl=%s&key=%s",
					merid,orderId,mobile,flowvalue,range,otime,notifyUrl,Md5Encrypt.md5(secretkey));
			String sign = Md5Encrypt.md5(signStr);
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("merid", merid);
			paramMap.put("orderid", orderId);
			paramMap.put("mobile", mobile);
			paramMap.put("flowvalue", flowvalue);
			paramMap.put("range", range);
			paramMap.put("otime", otime);
			paramMap.put("notifyurl", URLEncoder.encode(notifyUrl, "utf-8"));
			paramMap.put("sign", sign);
			return MultiThreadedHttpConnection.getInstance().sendPostByMap(chargeUrl, paramMap);
		}catch(Exception e){
			logger.error("妙讯充值异常",e);
			SupplyResult<String> result = new SupplyResult<String>();
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("妙讯充值异常");
			return result ;
		}
	}
	/**
	 * 余额查询
	 * @return
	 */
	public static SupplyResult<String> balanceQuery(String time){ 
		String signStr = String.format("merid=%s&time=%s&key=%s", merid,time,Md5Encrypt.md5(secretkey));
		String sign = Md5Encrypt.md5(signStr);
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("merid", merid);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		return MultiThreadedHttpConnection.getInstance().sendPostByMap(queryBalanceUrl, paramMap);
	}
}
