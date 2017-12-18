package com.longan.getway.biz.common.request;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

public class QueryGameListRequest extends Request {
	@NotBlank(message = "游戏厂商编号不能为空")
	@NotNull(message = "游戏厂商编号不能为空")
	@RegExp(value = "^\\d{1,8}$", message = "游戏厂商编号必须是1到8位数字")
	private String gameCarrierId;

	public String getGameCarrierId() {
		return gameCarrierId;
	}

	public void setGameCarrierId(String gameCarrierId) {
		this.gameCarrierId = gameCarrierId;
	}

}
