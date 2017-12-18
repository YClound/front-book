package com.longan.client.remote.service;

import com.longan.biz.domain.Result;
import com.longan.client.remote.domain.BalanceQueryInfo;

public interface ShortMsgSendService {
	Result<Boolean> shortMsgSend(String mobile, String content, Integer type);
	
	public Result<BalanceQueryInfo> balanceQuery();
}
