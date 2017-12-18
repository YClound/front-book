package com.longan.getway.biz.common.request;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

public class QueryGameItemRequest extends Request {
	@NotBlank(message = "商品编号不能为空")
	@NotNull(message = "商品编号不能为空")
	@RegExp(value = "^\\d{1,8}$", message = "商品编号必须是1到8位数字")
	private String itemId;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

}
