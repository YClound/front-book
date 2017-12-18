package com.longan.getway.biz.common.request;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

public class QueryGameRequest extends Request {
	@NotBlank(message = "网游编号不能为空")
	@NotNull(message = "网游编号不能为空")
	@RegExp(value = "^\\d{1,8}$", message = "网游编号必须是1到8位数字")
	private String gameId;

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

}
