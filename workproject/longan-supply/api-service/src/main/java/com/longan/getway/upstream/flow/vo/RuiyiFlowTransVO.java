package com.longan.getway.upstream.flow.vo;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.MultiThreadedHttpConnection;
import com.longan.getway.utils.RSAUtils;

/**
 * 瑞翼信息流量充值交易类
 * @author Administrator
 */
public class RuiyiFlowTransVO {
	/**
	 * 参数名定义
	 */
	public static String PARAM_AUTHTIMESPAN = "authTimespan";//时间戳
	public static String PARAM_AUTHAPPKEY = "authAppkey";//
	public static String PARAM_APPSECRET = "appSecret";//
	public static String PARAM_PARTNERORDERNO = "partnerOrderNo";//订单号
	public static String PARAM_AUTHSIGN = "authSign";//签名
	public static String PARAM_PUBLICKEY = "publickey";//公钥
	public static String PARAM_MOBILE = "mobile";//手机号
	public static String PARAM_RPODUCTID = "productId";//产品编号
	public static String PARAM_NOTIFYURL = "notifyUrl";//产品编号
	
	
	
	/**
	 * 配置参数读取
	 */
	public final static Long traderId = Long.parseLong(Utils.getProperty("ruiyiFlow.traderId"));
	
	/*回调路径*/
	private final  static String notifyUrl = Utils.getProperty("ruiyiFlow.notifyUrl");//时间戳
	/*分配的appkey*/
	private final static String authAppkey = Utils.getProperty("ruiyiFlow.authAppkey");
	private final static String appSecret = Utils.getProperty("ruiyiFlow.appSecret");
	private final static String partnerId = Utils.getProperty("ruiyiFlow.partnerId");
	/*交易的url根地址*/
	private final static String transUrl = Utils.getProperty("ruiyiFlow.transUrl");
	
	private static String publicUrl = transUrl+"/public/"+partnerId;
	private static String privateUrl = transUrl+"/private/"+partnerId;
	/*4.1.公钥获取接口*/
	private final static String getPublicKeyUrl = publicUrl+"/common/getPublicKey";
	/*4.2.充值流量接口(后向)*/
	private final static String buyFlowUrl = privateUrl+"/order/buyFlow?";
	/*根据合作方的订单号查询订单信息*/
	private final static String queryOrderUrl = privateUrl+"/order/queryOrderByPartnerOrderNo?";
	/*获取合作方的可售商品列表*/
	private final static String productListUrl = privateUrl+"/product/productList?";
	/*4.5.根据手机号获取可购买商品列表*/
	private final static String productListByMobileUrl = privateUrl+"/product/productListByMobile?";
	
	
	
	
	/**
	 * 创造时间
	 */
	private Date now ;
	/**
	 * 时间戳
	 */
	private String authTimespan;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 产品id
	 */
	private String productId;
	/**
	 * 订单号
	 */
	private String partnerOrderNo;
	/**
	 * 公钥(构造对象后需要设置这个值,先从缓存取,取不到在发交易查询,缓存有效期1天)
	 */
	private String publicKey ;
	public RuiyiFlowTransVO(){
		
	}
	public RuiyiFlowTransVO(String mobile,String productId,String partnerOrderNo){
		this.mobile = mobile;
		this.productId = productId;
		this.partnerOrderNo = partnerOrderNo;
		now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		authTimespan = sdf.format(now);
	}
	/**
	 * 创建查询订单的交易类
	 * @param partnerOrderNo
	 * @return
	 */
	public static RuiyiFlowTransVO createQueryOrderVO(String partnerOrderNo){
		RuiyiFlowTransVO vo = new RuiyiFlowTransVO();
		vo.setPartnerOrderNo(partnerOrderNo);
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		vo.setNow(now);
		vo.setAuthTimespan(sdf.format(now));
		return vo ;
	}
	
	/**
	 * 获取公钥
	 * @return
	 */
	public SupplyResult<String>  sendGetPublicKey(){
//		String url = createGetPublicKeyUrl();
		return MultiThreadedHttpConnection.getInstance().sendDataByGetReturnJson(getPublicKeyUrl);
	}
	/**
	 * 充值
	 * @return
	 */
	public SupplyResult<String> sendCharge(){
//		/*充值模拟返回*/
//		SupplyResult<String> result = new SupplyResult<String>();
//		String s =  "{\"code\":\"2001\",\"data\":{\"orderNo\":\"123\",\"status\":\"1\"}}";
//		result.setModule(s);
//		result.setStatus(SupplyResult.STATUS_SUCCESS);
//		return result ;
//		/*充值模拟结束*/
		String url = createChargeUrl();
		return MultiThreadedHttpConnection.getInstance().sendDataByGetReturnJson(url);
	}
	/**
	 * 查询订单
	 * @return
	 */
	public SupplyResult<String> sendQueryOrder(){
		String url = createQueryOrderUrl();
		return MultiThreadedHttpConnection.getInstance().sendDataByGetReturnJson(url);
	}
	/**
	 * 获取所有商品
	 * @return
	 */
	public SupplyResult<String> sendGetProducts(){
		String url = createProductListUrl();
		return MultiThreadedHttpConnection.getInstance().sendDataByGetReturnJson(url);
	}
	/**
	 * 根据手机号获取商品
	 * @return
	 */
	public SupplyResult<String> sendGetProductsByMobile(){
		String url = createProductListByMobileUrl();
		return MultiThreadedHttpConnection.getInstance().sendDataByGetReturnJson(url);
	}
	
//	/**
//	 * 创建获取公钥的请求地址
//	 * @return
//	 */
//	public String createGetPublicKeyUrl(){
//		Map<String,String> params = new HashMap<String,String>();
//		String sign = createSign(params);
//		params.put(PARAM_AUTHAPPKEY, authAppkey);
//		params.put(PARAM_AUTHTIMESPAN, authTimespan);
//		params.put(PARAM_AUTHSIGN, sign);
//		return createUrlAndParams(getPublicKeyUrl, params);
//	}
	
	
	/**
	 * 创建充值请求地址
	 * @return
	 */
	public String createChargeUrl(){
		try{
			String mobileRSA = RSAUtils.encryptByPublicKey(mobile, publicKey);
			String mobileRSAEN = URLEncoder.encode(mobileRSA, "UTF-8");
			Map<String,String> params = new HashMap<String,String>();
			params.put(PARAM_MOBILE, mobileRSA);
			params.put(PARAM_RPODUCTID, productId);
			params.put(PARAM_PARTNERORDERNO, partnerOrderNo);
			params.put(PARAM_NOTIFYURL, notifyUrl);
			String sign = createSign(params);
			params.put(PARAM_MOBILE, mobileRSAEN);
			params.put(PARAM_AUTHAPPKEY, authAppkey);
			params.put(PARAM_AUTHTIMESPAN, authTimespan);
			params.put(PARAM_AUTHSIGN, sign);
			return createUrlAndParams(buyFlowUrl, params);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null ;
	}
	/**
	 * 创建订单查询url
	 * @return
	 */
	public String createQueryOrderUrl(){
		Map<String,String> params = new HashMap<String,String>();
		params.put(PARAM_PARTNERORDERNO, partnerOrderNo);
		String sign = createSign(params);
		params.put(PARAM_AUTHAPPKEY, authAppkey);
		params.put(PARAM_AUTHTIMESPAN, authTimespan);
		params.put(PARAM_AUTHSIGN, sign);
		return createUrlAndParams(queryOrderUrl, params);
	}
	/**
	 * 创建获取全部商品url
	 * @return
	 */
	public String createProductListUrl(){
		Map<String,String> params = new HashMap<String,String>();
		String sign = createSign(params);
		params.put(PARAM_AUTHAPPKEY, authAppkey);
		params.put(PARAM_AUTHTIMESPAN, authTimespan);
		params.put(PARAM_AUTHSIGN, sign);
		return createUrlAndParams(productListUrl, params);
	}
	/**
	 * 创建根据手机号获取全部商品url
	 * @return
	 */
	public String createProductListByMobileUrl(){
		try{
			String mobileRSA = RSAUtils.encryptByPublicKey(mobile, publicKey);
			String mobileRSAEN = URLEncoder.encode(mobileRSA, "UTF-8");
			Map<String,String> params = new HashMap<String,String>();
			params.put(PARAM_MOBILE, mobileRSA);
			String sign = createSign(params);
			params.put(PARAM_MOBILE, mobileRSA);
			params.put(PARAM_AUTHAPPKEY, authAppkey);
			params.put(PARAM_AUTHTIMESPAN, authTimespan);
			params.put(PARAM_AUTHSIGN, sign);
			return createUrlAndParams(productListByMobileUrl, params);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null ;
	}
	
	
	
	
	/**
     *创建请求url
     * @param params
     * @return
     */
    public String createUrlAndParams(String url,Map<String, String> params){
    	 List<String> keys = new ArrayList<String>(params.keySet());
         StringBuffer sb = new StringBuffer();
         sb.append(url);
         for (int i = 0; i < keys.size(); i++) {
             String key = keys.get(i);
             String value = params.get(key);
             sb.append(key).append("=").append(value).append("&");
         }
         sb.deleteCharAt(sb.length()-1);
         return sb.toString();
    }
	/** 
     *创建签名,不包括 authAppkey,authTimespan,appSecret
     *
     */
    public  String createSign(Map<String, String> params) {
    	 StringBuffer sb = new StringBuffer();
    	if(params != null && !params.isEmpty()){
    		 List<String> keys = new ArrayList<String>(params.keySet());
             Collections.sort(keys);
            
             for (int i = 0; i < keys.size(); i++) {
                 String key = keys.get(i);
                 String value = params.get(key);
                 sb.append(key).append("=").append(value);
             }
    	}
        StringBuffer preStr = new StringBuffer();
        preStr.append(authAppkey).append(PARAM_AUTHTIMESPAN).append("=")
        .append(authTimespan).append(sb.toString()).append(appSecret);
        return Md5Encrypt.md5(preStr.toString()).toLowerCase();
    }
	public String getAuthTimespan() {
		return authTimespan;
	}
	public void setAuthTimespan(String authTimespan) {
		this.authTimespan = authTimespan;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getPartnerOrderNo() {
		return partnerOrderNo;
	}
	public void setPartnerOrderNo(String partnerOrderNo) {
		this.partnerOrderNo = partnerOrderNo;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public Date getNow() {
		return now;
	}
	public void setNow(Date now) {
		this.now = now;
	}
    
}
