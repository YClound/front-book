package com.longan.getway.biz.common.request;

import org.apache.commons.lang.StringUtils;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

public class QueryBizOrderRequest extends Request {
	@NotBlank(message = "合作方商户的流水号不能为空")
	@NotNull(message = "合作方商户的流水号不能为空")
	private String serialno;

	//新参数名，替代serialno参数
	private String order_no;
	/**
	 * 新参转旧参
	 */
	public void convertToOld(){
		if(!StringUtils.isEmpty(order_no)){
			this.serialno=this.order_no;
			this.order_no=null;
		}
	}
	/**
	 * 旧参转新参
	 */
	public void convertToNew(){
		if(!StringUtils.isEmpty(serialno)){
			this.order_no=this.serialno;
			this.serialno=null;
		}
	}
	
	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	
	
}
