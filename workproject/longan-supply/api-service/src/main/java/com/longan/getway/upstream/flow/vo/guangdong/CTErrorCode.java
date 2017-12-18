package com.longan.getway.upstream.flow.vo.guangdong;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 后台和前置对应错误码
 * 
 * @author ZhongYF
 * 
 */
public enum CTErrorCode {
	_00000("00000", "成功"),
	//
	_00001("00001", "没有登录或者会话过期,请登录 ！"),
	//
	_40001("40001", "参数合法性校验失败"),
	//
	_40002("40002", "商户没有注册，请联系后台管理人员."),
	//
	_40003("40003", "商户金额不足，请充值."),
	//
	_40004("40004", "商户[资金使用规则]设置不正确."),
	//
	_40005("40005", "[商户记账总表]和[商户记账明细表]数据不一致，请联系系统管理员 ."),
	//
	_40006(
			"40006",
			"构造商户消费明细并扣减商户记账总表时异常，需要跟踪类[CtTransactionService]的方法[getConsuDetail(?,?,?,?,?)]"),
	//
	_40007("40007",
			"根据“商户消费明细”数据对消“商户记账明细”数据时，出现“记账”无法对消“消费”的数据，这种情况表明“商户记账总表”和“商户记账明细表”数据不一致"),
	//
	_40008("40008", "程序运行时异常"),
	//
	_40009("40009", "查询号码归属地失败"),
	//
	_40010("40010", "保存号码归属于失败"),
	//
	_40011("40011", "商户登录失败"),
	//
	_40012("40012", "产品/套餐设置错误"),
	//
	_40013("40013", "充值流量更新消费状态失败"),
	//
	_40017("40017", "没有设置商户对接运营商限制"),
	//
	_40018("40018", "商户对运营商资金使用规则设置错误"),
	//
	_40019("40019", "商户消费对销商户记账明细失败"),
	//
	_40020("40020", "商户充值赠送/积分规则设置错误"),
	//
	_40021("40021", "商户总账没有开通"),
	//
	_40022("40022", "基础信息没有指定数据类型（BSS）"),
	//
	_40023("40023", "");

	String code;
	String name;

	CTErrorCode(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> reply() {
		Map<String, String> ret = new HashMap<String, String>();
		ret.put(CTConstant.RC, this.code);
		ret.put(CTConstant.RI, this.name);
		return ret;
	}

	public Map<String, String> reply(String msg) {
		Map<String, String> ret = new HashMap<String, String>();
		ret.put(CTConstant.RC, this.code);
		ret.put(CTConstant.RI, this.name + ":" + msg);
		return ret;
	}

	public String getReturn() {
		return JSONObject.fromObject(reply()).toString();
	}

	public String getReturnObj(String msg) {
		return JSONObject.fromObject(reply(msg)).toString();
	}
}