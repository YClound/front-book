package com.longan.client.remote.service;

import com.longan.biz.domain.Result;

public interface RequestLimitService {

	Result<Boolean> isMaxLimit(Integer bizId);

	Result<Boolean> putInQueue(Integer bizId);

	Result<Boolean> outQueue(Integer bizId);
	
	Result<Integer> getQueueCount(Integer bizId);

}
