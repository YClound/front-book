package com.longan.getway.upstream.flow.vo;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.AESUtil;
import com.longan.getway.utils.MultiThreadedHttpConnection;

/**
 * 天翼流量充值
 * @author Administrator
 *
 */
public class TianyiFlowVO {
	private static final Logger logger = LoggerFactory.getLogger(TianyiFlowVO.class);
	public final static Long traderId = Long.parseLong(Utils.getProperty("tianyiFlow.traderId"));
	public final static String partner_no = Utils.getProperty("tianyiFlow.partner_no");
	public final static String service_code = Utils.getProperty("tianyiFlow.service_code");
	public final static String contract_id = Utils.getProperty("tianyiFlow.contract_id");
	public final static String activity_id = Utils.getProperty("tianyiFlow.activity_id");
	public final static String secreKey = Utils.getProperty("tianyiFlow.secreKey");
	public final static String vector = Utils.getProperty("tianyiFlow.vector");
	public final static String orderFlow_url = Utils.getProperty("tianyiFlow.orderFlow_url");
	public static Map<String, String> resultMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("10229", "订购中,等待结果");
			put("10001", "空号/号码不存在,退款处理");
			put("10010", "欠费/停机,退款处理");
			put("10012", "号码已冻结或注销,退款处理");
			put("10013", "黑名单客户,退款处理");
			put("10018", "不能重复订购,退款处理");
			put("10024", "业务互斥,退款处理");
			put("10033", "在途工单,退款处理");
			put("10057", "号码归属地信息不正确,退款处理");
			put("10058", "客户业务受限,退款处理");
			put("10063", "用户状态异常,退款处理");
			put("10074", "用户信息不存在,退款处理");
			put("10225", "无主套餐,退款处理");
			put("80004", "解析接收报文异常,退款处理");
			put("90001", "系统异常,退款处理");
			put("90003", "模拟异常报竣,退款处理");
			put("10003", "非法参数,研发跟进");
			put("10006", "非法客户,研发跟进");
			put("10007", "非法销售品,研发跟进");
			put("10008", "非法请求流水号,研发跟进");
			put("10030", "非法合同编号,研发跟进");
			put("10031", "销售品未配置,研发跟进");
			put("10040", "服务无权访问,研发跟进");
			put("10054", "销售品配置异常,研发跟进");
			put("10081", "销售品不存在,研发跟进");
			put("10091", "回调地址未配置,研发跟进");
			put("10109", "活动省份不存在,研发跟进");
			put("10230", "重复请求流水号,研发跟进");
			put("80002", "网络异常,研发跟进");
			put("99999", "系统未知错误,研发跟进");
			put("10026", "合同授权金额不足,商务跟进");
			put("10028", "合同尚未开始,商务跟进");
			put("10029", "合同已经过期,商务跟进");

		}
	};
	public static  SupplyResult<String> orderFlow(String request_no,String order_type,String phone_id,
			String plat_offer_id,String effect_type,String old_msg_id){
		JSONObject json = new JSONObject();
		json.put("partner_no",partner_no);
		json.put("request_no", request_no);
		json.put("service_code", service_code);
		json.put("contract_id", contract_id);
		json.put("activity_id", activity_id);
		json.put("order_type", order_type);
		json.put("phone_id", phone_id);
		json.put("plat_offer_id", plat_offer_id);
		json.put("effect_type", effect_type);
		logger.warn("天翼流量800流量充值请求:"+json.toString());
		String code ="";
		try {
			code = AESUtil.Encrypt(json.toString(),secreKey,vector);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		json.clear();
		json.put("partner_no",partner_no);
		json.put("code", code);
		logger.warn("天翼流量800流量充值请求加密后数据:"+json.toString());
		return MultiThreadedHttpConnection.getInstance().sendDataByPost(orderFlow_url,json.toString());
	}
}
