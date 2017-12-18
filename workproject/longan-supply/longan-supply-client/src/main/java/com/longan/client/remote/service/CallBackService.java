package com.longan.client.remote.service;

import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.domain.Result;

public interface CallBackService {
	Result<Boolean> callBackAsync(BizOrder bizOrder);
}
