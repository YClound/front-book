package com.longan.biz.core;

import com.longan.biz.dataobject.ShortMsg;
import com.longan.biz.domain.Result;

public interface ShortMsgService {
	public Result<ShortMsg> createShortMsg(ShortMsg shortMsg);

	public Result<Boolean> updateShortMsg(ShortMsg shortMsg);
}
