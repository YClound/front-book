package com.longan.getway.biz.common.request;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

public class QueryItemListRequest extends Request {
	@NotBlank(message = "业务编号不能为空")
	@NotNull(message = "业务编号不能为空")
	@RegExp(value = "^\\d{1,8}$", message = "业务编号必须是1到8位数字")
	private String bizId;

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	
}
