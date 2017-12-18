package com.longan.getway.upstream.flow.vo;


import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.utils.AESUtils2;
import com.longan.biz.utils.Md5Encrypt;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.MultiThreadedHttpConnection;

import net.sf.json.JSONObject;

/**
 * 吉虎大禹流量充值交易类
 * @author Administrator
 *
 */
public class JihuDayuFlowTransVO {
	private static final Logger logger = LoggerFactory.getLogger(JihuDayuFlowTransVO.class);
	public final static Long traderId = Long.parseLong(Utils.getProperty("JihuDayuFlow.traderId"));
	public final static String serviceIp = Utils.getProperty("JihuDayuFlow.serviceIp");
	public final static String serviceAccount  = Utils.getProperty("JihuDayuFlow.serviceAccount");
	public final static String servicePwd  = Utils.getProperty("JihuDayuFlow.servicePwd");
	public final static String serviceKey   = Utils.getProperty("JihuDayuFlow.serviceKey");
	
	public static String chargeUrl = "http://"+serviceIp+"/service/flow/order.shtml?";
	public static String queryOrderUrl = "http://"+serviceIp+"/service/flow/query.shtml?";
	public static String queryBalanceUrl = "http://"+serviceIp+"/service/flow/balance.shtml?";
	
	public static String PARAM_SERVICEACCOUNT = "serviceAccount";
	public static String PARAM_ORDERNO = "orderNo";
	public static String PARAM_SERVICEPWD = "servicePwd";
	public static String PARAM_SERVICEKEY = "serviceKey";
	public static String PARAM_TELEPHONE = "telephone";
	public static String PARAM_FLOWTYPE = "flowType";
	public static String PARAM_PRODUCTID = "productId";
	public static String PARAM_SIGN = "sign";
	public static String PARAM_TIMESTAMP = "timestamp";
	public static String PARAM_REQUESTCONTENT = "requestContent";
	/**
	 * 订单号
	 * 由于上游需要传送17到22位的订单号，而我方的订单号有可能会小于17位,所以左补0,此订单号将作为上游订单号存放在供货订单中
	 * 所以此订单号并非是真正的上游订单号
	 */
	private String orderNo;
	/**
	 * 充值手机
	 */
	private String telephone;
	/**
	 * 流量类型   0-全国   1-省内
	 */
	private String flowType; 
	/**
	 * 供货方产品id
	 */
	private String productId;
	
	private String timestamp  = Long.toString(new Date().getTime());
	public JihuDayuFlowTransVO(){
		
	}
	public JihuDayuFlowTransVO(String orderNo){
		this.orderNo = orderNo;
	}
	
	
	public JihuDayuFlowTransVO(String orderNo,String telephone,String flowType,String productId){
		this.orderNo = orderNo;
		this.telephone = telephone;
		this.flowType = flowType;
		this.productId = productId;
	}
	
	/**
	 * 充值
	 * @return
	 */
	public SupplyResult<String> charge(){
		LinkedHashMap<String, String> params = new LinkedHashMap<String,String>();
		params.put(PARAM_SERVICEACCOUNT, serviceAccount);
		params.put(PARAM_REQUESTCONTENT, createRequestContent());
		
//		LinkedHashMap<String, String> returnMap = new LinkedHashMap<String,String>();
//		returnMap.put("requestNo", "201500000000001");
//		returnMap.put("orderStatus", "UNDERWAY");
//		returnMap.put("orderNo", orderNo);
//		returnMap.put("errCode", "0910");
//		returnMap.put("errDesc", "充值中");
//		JSONObject jsonObject = JSONObject.fromObject(returnMap);
//		SupplyResult<String> result = new SupplyResult<String>();
//		result.setModule(jsonObject.toString());
//		result.setStatus(SupplyResult.STATUS_SUCCESS);
//		return result ;
		
		String url = createUrlAndParams(chargeUrl,params);
		return MultiThreadedHttpConnection.getInstance().sendDataByGetReturnString(url);
	} 
	/**
	 * 订单查询
	 * @return
	 */
	public SupplyResult<String> orderQuery(){ 
		LinkedHashMap<String, String> params = new LinkedHashMap<String,String>();
		params.put(PARAM_ORDERNO, orderNo);
		params.put(PARAM_SERVICEACCOUNT, serviceAccount);
		params.put(PARAM_SERVICEPWD, servicePwd);
		params.put(PARAM_SERVICEKEY, serviceKey);
		params.put(PARAM_TIMESTAMP, timestamp);
		params.put(PARAM_SIGN, createSign(params));
		String url = createUrlAndParams(queryOrderUrl,params);
    	return MultiThreadedHttpConnection.getInstance().sendDataByGetReturnString(url);
	}
	/**
	 * 余额查询
	 * @return
	 */
	public SupplyResult<String> balanceQuery(){ 
		LinkedHashMap<String, String> params = new LinkedHashMap<String,String>();
		params.put(PARAM_SERVICEACCOUNT, serviceAccount);
		params.put(PARAM_SERVICEPWD, servicePwd);
		params.put(PARAM_SERVICEKEY, serviceKey);
		params.put(PARAM_TIMESTAMP, timestamp);
		params.put(PARAM_SIGN, createSign(params));
		String url = createUrlAndParams(queryBalanceUrl,params);
    	return MultiThreadedHttpConnection.getInstance().sendDataByGetReturnString(url);
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
	
	public String createRequestContent(){
		LinkedHashMap<String,String> signMap = new LinkedHashMap<String,String>();
		signMap.put(PARAM_ORDERNO, orderNo);
		signMap.put(PARAM_SERVICEACCOUNT, serviceAccount);
		signMap.put(PARAM_SERVICEPWD, servicePwd);
		signMap.put(PARAM_SERVICEKEY,serviceKey);
		signMap.put(PARAM_TELEPHONE, telephone);
		signMap.put(PARAM_FLOWTYPE, flowType);
		signMap.put(PARAM_PRODUCTID, productId);
		signMap.put(PARAM_TIMESTAMP,timestamp);
		String sign = createSign(signMap);
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		map.put(PARAM_ORDERNO, orderNo);
		map.put(PARAM_SERVICEPWD, servicePwd);
		map.put(PARAM_SERVICEKEY,serviceKey);
		map.put(PARAM_TELEPHONE, telephone);
		map.put(PARAM_FLOWTYPE, flowType);
		map.put(PARAM_PRODUCTID, productId);
		map.put(PARAM_SIGN,sign);
		map.put(PARAM_TIMESTAMP,timestamp);
		JSONObject jsonObject = JSONObject.fromObject(map);
		try {
			logger.warn("大禹流量充值加密json:"+jsonObject.toString());
			return AESUtils2.encrypt(jsonObject.toString(), "E98A43285C035DB7").toUpperCase();
		} catch (Exception e) {
			logger.warn("大禹加密失败",e);
		}
		return null ;
	}
	
	/**
	 * 创建签名
	 * @param params
	 * @return
	 */
	public String createSign(LinkedHashMap<String,String> params){
		StringBuilder sb = new StringBuilder();
		for(String key : params.keySet()){
			sb.append(key).append(params.get(key));
		}
		sb.append("E98A43285C035DB7");
		return Md5Encrypt.md5(sb.toString());
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFlowType() {
		return flowType;
	}
	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public static void main(String[] args) throws Exception {
//		JihuDayuFlowTransVO vo = new JihuDayuFlowTransVO("12345678901234567", "15168318002", "0", "43214314343");
//		vo.charge();
//		JihuDayuFlowTransVO vo1 = new JihuDayuFlowTransVO("12345678901234567");
//		vo1.orderQuery();
//		JihuDayuFlowTransVO vo2 = new JihuDayuFlowTransVO();
//		vo2.balanceQuery();
		
	}
}
