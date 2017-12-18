package com.longan.getway.biz.common.request;

import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

public class QueryGameCarrierRequest extends Request {
	
	@RegExp(value = "(^\\d{1,8}$)?", message = "游戏厂商必须是1到8位数字")
	private String gameCarrierId;

	public String getGameCarrierId() {
		return gameCarrierId;
	}

	public void setGameCarrierId(String gameCarrierId) {
		this.gameCarrierId = gameCarrierId;
	}
	
	
}
