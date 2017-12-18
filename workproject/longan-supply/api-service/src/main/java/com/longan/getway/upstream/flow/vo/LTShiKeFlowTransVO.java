package com.longan.getway.upstream.flow.vo;

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

/**
 * 联通时科流量
 * @author Administrator
 *
 */
public class LTShiKeFlowTransVO {
	public final static Long traderId = Long.parseLong(Utils.getProperty("LTShikeFlow.traderId"));
	private final static String baseUrl = Utils.getProperty("LTShikeFlow.baseUrl");
	private final static String chargeUrl = baseUrl+"order?";
	private final static String secretkey = Utils.getProperty("LTShikeFlow.secretkey");
	private final static String interfaceid = Utils.getProperty("LTShikeFlow.interfaceid");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public  final static String PARAM_INTERFACEID = "interfaceid";
	public  final static String PARAM_PROVINCECODE = "provincecode";
	public  final static String PARAM_PRODUCTCODE = "productcode";
	public  final static String PARAM_USERNUMBER = "usernumber";
	public  final static String PARAM_TIMESTAMP = "timestamp";
	public  final static String PARAM_SIGN = "sign";
	
	/*省份编号*/
	private String provinceCode ; 
	/*产品编号*/
	private String productCode ;
	/*用户号码*/
	private String userNumber;
	/*创建时间*/
	private Date now = new Date ();
	/*时间戳*/
	private String timeStamp = sdf.format(now);
	
	public LTShiKeFlowTransVO(String provinceCode,String productCode,String userNumber){
		this.provinceCode = provinceCode ;
		this.productCode = productCode ;
		this.userNumber = userNumber ;
	}
	/**
	 * 充值
	 * @return
	 */
	public SupplyResult<String> sendCharge(){
		Map<String,String> params = new HashMap<String,String>();
		params.put(PARAM_INTERFACEID, interfaceid);
    	params.put(PARAM_TIMESTAMP, timeStamp);
    	params.put(PARAM_PROVINCECODE, provinceCode);
    	params.put(PARAM_PRODUCTCODE, productCode);
    	params.put(PARAM_USERNUMBER,userNumber);
    	params.put(PARAM_SIGN, createSign(params));
    	String url = createUrlAndParams(chargeUrl, params);
    	return MultiThreadedHttpConnection.getInstance().sendDataByGetReturnJson(url,"GBK");
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
     *创建签名
     *
     */
    public  String createSign(Map<String, String> params) {
    	StringBuffer sb = new StringBuffer();
    	sb.append(secretkey);
    	if(params != null && !params.isEmpty()){
    		 List<String> keys = new ArrayList<String>(params.keySet());
             Collections.sort(keys);
             for (int i = 0; i < keys.size(); i++) {
                 String key = keys.get(i);
                 String value = params.get(key);
                 sb.append(key).append(value);
             }
    	}
    	sb.append(secretkey);
        return Md5Encrypt.md5(sb.toString()).toUpperCase();
    }
    
}
/**
 * 返回实体类
 * @author Administrator
 *
 */
class LTShiKeResponse{
	private String code ;
	private String desc ;
	private String orderNo ;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
}
