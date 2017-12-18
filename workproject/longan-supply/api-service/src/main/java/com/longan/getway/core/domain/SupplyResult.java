package com.longan.getway.core.domain;

import java.io.Serializable;

import com.longan.biz.domain.Result;

public class SupplyResult<T> implements Serializable {
	public static int STATUS_FAILED = 0; // 失败
	public static int STATUS_SUCCESS = 1; // 成功
	public static int STATUS_UNCONFIRM = 2; // 超时或者未确认状态
	public static int STATUS_CARD_INVALID = 3; // 卡失效,过期，不存在等

	private static final long serialVersionUID = 1L;

	private Integer status = STATUS_FAILED;

	private boolean isAsync = false; // 是否异步供货， 如果是异步供货 返回状态是 STATUS_SUCCESS
										// 则表示提交成功

	private String resultCode;

	private String resultMsg;

	private T module;

	public synchronized static <T> Result<T> createResult() {
		return new Result<T>();
	}

	public boolean isSuccess() {
		return status == STATUS_SUCCESS;
	}

	public boolean isFailed() {
		return status == STATUS_FAILED;
	}

	public boolean isUnConfirm() {
		return status == STATUS_UNCONFIRM;
	}

	public boolean isCardInvalid() {
		return status == STATUS_CARD_INVALID;
	}

	public boolean isUndifend() {
		return status != STATUS_FAILED && status != STATUS_SUCCESS && status != STATUS_UNCONFIRM
				&& status != STATUS_CARD_INVALID;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public T getModule() {
		return module;
	}

	public void setModule(T module) {
		this.module = module;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public boolean isAsync() {
		return isAsync;
	}

	public void setAsync(boolean isAsync) {
		this.isAsync = isAsync;
	}
	
	
}
