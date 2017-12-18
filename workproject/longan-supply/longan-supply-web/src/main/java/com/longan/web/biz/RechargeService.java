package com.longan.web.biz;

import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.domain.Result;
import com.longan.client.remote.domain.web.BuyItem;
import com.longan.client.remote.domain.web.PhoneQuery;
import com.longan.web.biz.domain.FlowQueryForWeb;

public interface RechargeService {
	Result<PhoneQuery> getItemList(String uid, AcctInfo acctInfo);

	Result<BizOrder> payment(BuyItem buyItem);

	Result<FlowQueryForWeb> getFlowItemList(String uid, AcctInfo acctInfo);
}
