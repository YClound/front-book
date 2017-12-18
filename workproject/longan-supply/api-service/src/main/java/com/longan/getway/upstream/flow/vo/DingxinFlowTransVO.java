package com.longan.getway.upstream.flow.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;

public class DingxinFlowTransVO {
	public final static Long traderId = Long.parseLong(Utils.getProperty("dingxinGprs.traderId"));
	public final static String userId = Utils.getProperty("dingxinGprs.userid");
	public final static String pwd = Utils.getProperty("dingxinGprs.pwd");
	public final static String privateKey = Utils.getProperty("dingxinGprs.key");
	public final static String url = Utils.getProperty("dingxinGprs.url");
	public final static String chargeUrl = url+"gprsChongzhiAdvance.do?";
	public final static String queryBalanceUrl = url+"money_jkuser.do?";
	public final static String queryOrderUrl = url+"query_jkorders.do?";
	
	/*http参数名配置*/
	public final static String PARAM_USERID="userid";
	public final static String PARAM_PWD="pwd";
	public final static String PARAM_ORDERID="orderid";
	public final static String PARAM_ACCOUNT="account";
	public final static String PARAM_GPRS="gprs";
	public final static String PARAM_AREA="area";
	public final static String PARAM_EFFECTTIME="effecttime";
	public final static String PARAM_VALIDITY="validity";
	public final static String PARAM_TIMES="times";
	public final static String PARAM_USERKEY="userkey";
	public final static String PARAM_STATE="state";
	
	
	
	/* 订单号*/
	private String orderId ;
	/*用户手机号 */
	private String uid ;
	/*充值流量  */
	private int gprs ;
	/**充值区域
	 * 0 全国流量，1 省内流量   
	 * 暂且填 0 
	 */
	private int  area ; 
	/**
	 *生效日期  0 即时生效，1次日生效，2 次月生效   
	 *暂且填 0  
	 */
	private int effectTime ;
	/**流量有效期
	 * 传入月数，0为当月有效
	 * 暂且填  0 
     */
	private int validity;
	/*时间戳  yyyyMMddhhmmss*/
	private String times;
	/*签名*/
	private String sign ;
	public DingxinFlowTransVO(String orderId,String uid,int gprs){
		this.orderId = orderId;
		this.uid = uid;
		this.gprs = gprs;
		SimpleDateFormat formdate = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date();
		times = formdate.format(curDate);
		createChargeSign();
	}
//	sb.append(PARAM_USERID).append(userId)
//	.append(PARAM_PWD).append(pwd)
//	.append(PARAM_ORDERID).append(orderId)
//	.append(PARAM_ACCOUNT).append(uid)
//	.append(PARAM_GPRS).append(gprs)
//	.append(PARAM_AREA).append(area)
//	.append(PARAM_EFFECTTIME).append(effectTime)
//	.append(PARAM_VALIDITY).append(validity)
//	.append(PARAM_TIMES).append(times)
//	.append(privateKey);
//	public DingxinGprsRequestVO(String orderId,String uid,int gprs,int area,int effectTime,int validity){
//		this.orderId = orderId;
//		this.uid = uid;
//		this.gprs = gprs;
//		this.area = area ;
//		this.effectTime = effectTime;
//		this.validity = validity;
//		createChargeSign();
//	}
	/**
	 * 组装参数串
	 * @return
	 */
	public String createChargeParam() {
		StringBuffer sb = new StringBuffer(chargeUrl);
		sb.append(PARAM_USERID).append("=").append(userId).append("&")
		.append(PARAM_PWD).append("=").append(pwd).append("&")
		.append(PARAM_ORDERID).append("=").append(orderId).append("&")
		.append(PARAM_ACCOUNT).append("=").append(uid).append("&")
		.append(PARAM_GPRS).append("=").append(gprs).append("&")
		.append(PARAM_AREA).append("=").append(area).append("&")
		.append(PARAM_EFFECTTIME).append("=").append(effectTime).append("&")
		.append(PARAM_VALIDITY).append("=").append(validity).append("&")
		.append(PARAM_TIMES).append("=").append(times).append("&")
		.append(PARAM_USERKEY).append("=").append(sign);
		return sb.toString();
	}
	/**
	 * 生成签名
	 * （userid+“用户编号”+ pwd+“由鼎信商务提供”+ orderid+“用户提交的订单号”
	 * + account+“手机号码”+gprs+”流量值”+ area+”流量范围”+effecttime
	 * +”生效日期”+validity+”流量有效期”+times+”时间戳”+”加密密钥”）MD5加密
	 */
	public void createChargeSign() {
		StringBuffer sb = new StringBuffer();
		sb.append(PARAM_USERID).append(userId)
			.append(PARAM_PWD).append(pwd)
			.append(PARAM_ORDERID).append(orderId)
			.append(PARAM_ACCOUNT).append(uid)
			.append(PARAM_GPRS).append(gprs)
			.append(PARAM_AREA).append(area)
			.append(PARAM_EFFECTTIME).append(effectTime)
			.append(PARAM_VALIDITY).append(validity)
			.append(PARAM_TIMES).append(times)
			.append(privateKey);
		String signStr = Md5Encrypt.md5(sb.toString()).toUpperCase();
		setSign(signStr);
	}
	/**
	 * 创建余额查询url
	 * @return
	 */
	public static String createQueryBalanceUrl(){
		//生成userkey
		StringBuffer sb = new StringBuffer();
		sb.append(PARAM_USERID).append(userId)
			.append(PARAM_PWD).append(pwd)
			.append(privateKey);
		String signStr = Md5Encrypt.md5(sb.toString()).toUpperCase();
		//生成url
		StringBuffer usb = new StringBuffer(queryBalanceUrl);
		usb.append(PARAM_USERID).append("=").append(userId).append("&")
		.append(PARAM_PWD).append("=").append(pwd).append("&")
		.append(PARAM_USERKEY).append("=").append(signStr);
		return usb.toString();
	}
	/**
	 * 创建订单查询url
	 * @return
	 */
	public static String createQueryOrderUrl(String orderId){
		//生成userkey
		StringBuffer sb = new StringBuffer();
		sb.append(PARAM_USERID).append(userId)
			.append(PARAM_PWD).append(pwd)
			.append(PARAM_ORDERID).append(orderId)
			.append(privateKey);
		String signStr = Md5Encrypt.md5(sb.toString()).toUpperCase();
		//生成url
		StringBuffer usb = new StringBuffer(queryOrderUrl);
		usb.append(PARAM_USERID).append("=").append(userId).append("&")
		.append(PARAM_PWD).append("=").append(pwd).append("&")
		.append(PARAM_ORDERID).append("=").append(orderId).append("&")
		.append(PARAM_USERKEY).append("=").append(signStr);
		return usb.toString();
	}
	/**
	 * 是否充值中
	 * @param state
	 * @return
	 */
	public static boolean isChargeing(String state){
		if("0".equals(state)){
			return true ;
		}
		return false ;
	}
	/**
	 * 是否等待扣款
	 * @param state
	 * @return
	 */
	public static boolean isWaitPay(String state){
		if("8".equals(state)){
			return true ;
		}
		return false ;
	}
	/**
	 * 是否充值成功
	 * @param state
	 * @return
	 */
	public static boolean isChargeSuccess(String state){
		if("1".equals(state)){
			return true ;
		}
		return false ;
	}
	/**
	 * 是否充值失败
	 * @param state
	 * @return
	 */
	public static boolean isChargeFailed(String state){
		if("2".equals(state)){
			return true ;
		}
		return false ;
	}
	
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getGprs() {
		return gprs;
	}

	public void setGprs(int gprs) {
		this.gprs = gprs;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public int getEffectTime() {
		return effectTime;
	}

	public void setEffectTime(int effectTime) {
		this.effectTime = effectTime;
	}

	public int getValidity() {
		return validity;
	}

	public void setValidity(int validity) {
		this.validity = validity;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public static String getUserid() {
		return userId;
	}

	public static String getPwd() {
		return pwd;
	}

	public static String getUrl() {
		return url;
	}
}
