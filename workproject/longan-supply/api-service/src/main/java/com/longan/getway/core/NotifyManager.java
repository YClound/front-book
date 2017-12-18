package com.longan.getway.core;

import com.longan.getway.upstream.common.domain.CallBack;

public interface NotifyManager {

	Object addLock(Long supplyOrderId);

	Object getLock(Long supplyOrderId);

	CallBack waitForResult(Long supplyOrderId);

	void notify(CallBack upstreamCallBack);

}
