package com.longan.biz.core;

import java.util.List;

import com.longan.biz.domain.MiniInfo;

public interface MimiMonitorService {
	void  heartbeat(String pcId, String posId);

	void countByPosId(String posId, Long amount);

	boolean checkLimit(String posId);

	List<MiniInfo>  queryAllMini();

	List<MiniInfo> queryAliveMini();
}
