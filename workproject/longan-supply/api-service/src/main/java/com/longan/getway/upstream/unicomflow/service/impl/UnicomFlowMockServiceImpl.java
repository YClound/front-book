package com.longan.getway.upstream.unicomflow.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.longan.biz.dataobject.Stock;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.client.remote.domain.CardChargeInfo;
import com.longan.client.remote.domain.CardCheck;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamCardSupplyService;

public class UnicomFlowMockServiceImpl implements UpstreamCardSupplyService {
	private boolean isAsync = false;

	@Override
	public SupplyResult<CardCheck> cardCheck(Stock stock) {
		SupplyResult<CardCheck> result = new SupplyResult<CardCheck>();
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		CardCheck unicomFlowCardCheck = new CardCheck();
		unicomFlowCardCheck.setCode("00");
		unicomFlowCardCheck.setDesc("该卡为有效卡");
		result.setModule(unicomFlowCardCheck);
		return result;
	}

	@Override
	public SupplyResult<List<CardChargeInfo>> cardInfoQuery(Stock stock) {
		SupplyResult<List<CardChargeInfo>> result = new SupplyResult<List<CardChargeInfo>>();
		List<CardChargeInfo> list = new ArrayList<CardChargeInfo>();
		CardChargeInfo module = new CardChargeInfo();
		module.setServiceNum("13757146578");
		module.setMoney("20");
		module.setStatus("00");
		module.setStatusDesc("充值成功");
		module.setTime("2014-01-02 19:19:19");
		list.add(module);

		CardChargeInfo module2 = new CardChargeInfo();
		module2.setServiceNum("13757146578");
		module2.setMoney("20");
		module2.setStatus("00");
		module2.setStatusDesc("充值成功");
		module2.setTime("2014-01-02 19:19:19");
		list.add(module2);
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		result.setModule(list);
		return result;
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder, Stock stock) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);
		result.setResultMsg("test");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		result.setStatus(SupplyResult.STATUS_UNCONFIRM);
		return result;
	}

}
