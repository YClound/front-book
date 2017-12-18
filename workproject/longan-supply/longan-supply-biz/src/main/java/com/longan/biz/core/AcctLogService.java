package com.longan.biz.core;

import java.util.List;

import com.longan.biz.dataobject.AcctLog;
import com.longan.biz.dataobject.AcctLogQuery;
import com.longan.biz.domain.Result;

public interface AcctLogService {
	Result<List<AcctLog>> queryAcctLog(AcctLogQuery acctLogQuery);
	
	public Result<List<AcctLog>> queryAcctLogExport(AcctLogQuery acctLogQuery);
}
