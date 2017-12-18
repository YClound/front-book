package com.longan.getway.upstream.flow.vo;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.utils.SHAUtils;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.Base64Utils;
import com.longan.getway.utils.MultiThreadedHttpConnection;

/**
 * 联通宽带流量
 * @author Administrator
 *
 */
public class LTKuandaiFlowVO {
	private static final Logger logger = LoggerFactory.getLogger(LTKuandaiFlowVO.class);
	public final static Long traderId = Long.parseLong(Utils.getProperty("LTKuandaiFlow.traderId"));
	private final static String appKey = Utils.getProperty("LTKuandaiFlow.appKey");
	public final static String appSecret = Utils.getProperty("LTKuandaiFlow.appSecret");
	private final static String notifyUrl = Utils.getProperty("LTKuandaiFlow.notifyUrl");
	private final static String baseUrl = Utils.getProperty("LTKuandaiFlow.baseUrl");
	public static final Map<String, String> codeResultMap = new HashMap<String, String>();
	static{
		codeResultMap.put("0000", "订购成功");
		codeResultMap.put("0001", "订购请求已接受");
		codeResultMap.put("1000", "系统异常，请尽快联系相关运营人员");
		codeResultMap.put("1001", "参数错误");
		codeResultMap.put("1002", "数字签名验证失败");
		codeResultMap.put("1003", "请求服务不存在");
		codeResultMap.put("1004", "此地区产品未开通");
		codeResultMap.put("1005", "IP地址错误");
		codeResultMap.put("1006", "请求时间错误");
		codeResultMap.put("1007", "订购请求重复提交");
		codeResultMap.put("1008", "访问受限");
		codeResultMap.put("1009", "达到阀值，访问受限");
		codeResultMap.put("2001", "用户信息未知");
		codeResultMap.put("2002", "用户已停机");
		codeResultMap.put("2003", "用户状态异常(欠费)");
		codeResultMap.put("2004", "用户是黑名单");
		codeResultMap.put("2005", "该笔订单已完成订购");
		codeResultMap.put("2006", "订购关系不存在");
		codeResultMap.put("2007", "与已订购的其他产品冲突");
		codeResultMap.put("2008", "不允许变更的产品");
		codeResultMap.put("2009", "参数错误");
		codeResultMap.put("2010", "用户套餐不能订购该业务");
		codeResultMap.put("2011", "其他原因");
		codeResultMap.put("2012", "订购失败");
		codeResultMap.put("2013", "该地区暂不支持 2G 用户订购");
		codeResultMap.put("2014", "无更多的产品订购(用户当月订购流量包已到达叠加上限)");
		codeResultMap.put("2015", "暂时无产品订购，针对预付用户");
		codeResultMap.put("2016", "2G/3G 融合用户不允许订购");
		codeResultMap.put("2017", "用户状态异常(资料不全)");
		codeResultMap.put("2018", "用户状态异常(不在有效期)");
		codeResultMap.put("2019", "用户主套餐变更，当月不能订购");
		codeResultMap.put("2020", "用户服务密码为初始密码");
		codeResultMap.put("2021", "用户有正在处理订单");
		codeResultMap.put("2022", "该地区暂不支持 4G 用户订购");
		codeResultMap.put("2023", "用户状态异常(未查到用户类型)");
		codeResultMap.put("2024", "该地区暂不支持 3G 用户订购");
		codeResultMap.put("2025", "用户网别变更，不允许订购");
		codeResultMap.put("2026", "产品不允许重复订购");
		codeResultMap.put("2027", "省份系统出错");
		codeResultMap.put("2028", "省份月末月初进出帐");
		codeResultMap.put("2029", "省份系统超时，请稍后再试");
		codeResultMap.put("2030", "用户可用额度不足");
		codeResultMap.put("2031", "省分台账异常");
		codeResultMap.put("3001", "appkey 验证无效");
		codeResultMap.put("3002", "账户余额不足，请尽快联系相关运营人员");
		codeResultMap.put("3003", "账户余额不足，请尽快联系相关运营人员");
		codeResultMap.put("3004", "产品编号无效");
		codeResultMap.put("3005", "剩余的订购关系数目不足");
		codeResultMap.put("3006", "此地区产品未开通");
		codeResultMap.put("3007", "活动信息不存在");
		codeResultMap.put("3008", "活动已过期");
		codeResultMap.put("3009", "活动不包含此产品");
		codeResultMap.put("3010", "没有 pkgNo 的订购权限");
		codeResultMap.put("3011", "订单信息已存在，但未提交订购");
		codeResultMap.put("3012", "账户异常");
		codeResultMap.put("4001", "客户端流水号请求重复订购");
		codeResultMap.put("9001", "等待响应消息超时");
		codeResultMap.put("9002", "网络异常");
		codeResultMap.put("9003", "数据库连接异常");
		codeResultMap.put("9101", "系统错误");
		codeResultMap.put("9102", "网络异常");
		codeResultMap.put("9999", "其他原因");
	}
	/**
	 * 充值
	 * @param transNo
	 * @param phoneNo
	 * @param pkgNo
	 * @param saleNo
	 * @param timeStamp
	 * @return
	 */
	public static SupplyResult<String> charge(String transNo,String phoneNo,String pkgNo,String saleNo,String timeStamp){
		try{
			TreeMap<String,String> paramMap = new TreeMap<String, String>();
			paramMap.put("action", "orderPkg");
			paramMap.put("appKey", appKey);
			paramMap.put("pkgNo",pkgNo);
			paramMap.put("phoneNo",phoneNo);
			paramMap.put("backUrl",Base64Utils.encode(notifyUrl.getBytes()));
			paramMap.put("transNo",transNo);
			paramMap.put("timeStamp",timeStamp);
			if(saleNo != null)paramMap.put("saleNo",saleNo);
			paramMap.put("sign",sign(paramMap));
			return MultiThreadedHttpConnection.getInstance().sendPostByMap(baseUrl, paramMap);
		}catch(Exception e){
			logger.error("联通宽带流量充值异常",e);
			SupplyResult<String> result = new SupplyResult<String>();
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("联通宽带流量充值异常");
			return result ;
		}
	}
	/**
	 * 订单查询
	 * @param orderId 上游订单号
	 * @param timeStamp 时间戳
	 * @return
	 */
	public static SupplyResult<String> queryOrder(String orderId,String timeStamp){
		try{
			TreeMap<String,String> paramMap = new TreeMap<String, String>();
			paramMap.put("action", "queryOrder");
			paramMap.put("appKey", appKey);
			paramMap.put("orderId",orderId);
			paramMap.put("timeStamp",timeStamp);
			paramMap.put("sign",sign(paramMap));
			return MultiThreadedHttpConnection.getInstance().sendPostByMap(baseUrl, paramMap);
		}catch(Exception e){
			logger.error("联通宽带流量订单查询异常",e);
			SupplyResult<String> result = new SupplyResult<String>();
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("联通宽带流量订单查询异常");
			return result ;
		}
	}
	
	private static String sign(TreeMap<String,String> paramMap){
		StringBuffer sb = new StringBuffer();
		sb.append(appSecret);
		for(String key : paramMap.keySet()){
			sb.append(key).append(paramMap.get(key));
		}
		sb.append(appSecret);
		return SHAUtils.SHA1(sb.toString()).toUpperCase();
	}
}
