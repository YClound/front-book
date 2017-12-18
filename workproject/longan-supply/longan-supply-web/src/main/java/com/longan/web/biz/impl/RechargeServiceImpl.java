package com.longan.web.biz.impl;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.web.BuyItem;
import com.longan.client.remote.domain.web.FlowQuery;
import com.longan.client.remote.domain.web.ItemForQuery;
import com.longan.client.remote.domain.web.PhoneQuery;
import com.longan.client.remote.service.SupplyForRemoteService;
import com.longan.web.biz.BaseService;
import com.longan.web.biz.RechargeService;
import com.longan.web.biz.domain.FlowQueryForWeb;

public class RechargeServiceImpl extends BaseService implements RechargeService {
	@Resource
	private SupplyForRemoteService supplyForRemoteService;

	@Override
	public Result<PhoneQuery> getItemList(String uid, AcctInfo acctInfo) {
		Result<PhoneQuery> result = new Result<PhoneQuery>();
		Result<PhoneQuery> phoneQueryResult = null;
		try {
			phoneQueryResult = supplyForRemoteService.queryPhoneItem(uid, acctInfo);
		} catch (Exception e) {
			result.setResultMsg("查询失败,网络连接超时");
			logger.error("queryPhoneItem error", e);
			return result;
		}
		if (!phoneQueryResult.isSuccess()) {
			logger.error("getItemList error " + phoneQueryResult.getResultMsg());
			result.setResultMsg("查询失败,请确认输入的是正确手机号");
			return result;
		}

		result.setSuccess(true);
		result.setModule(phoneQueryResult.getModule());

		return result;
	}

	@Override
	public Result<BizOrder> payment(BuyItem buyItem) {
		Result<BizOrder> result = new Result<BizOrder>();
		if (buyItem == null) {
			result.setResultMsg("入参错误");
			return result;
		}
		try {
			result = supplyForRemoteService.buy(buyItem);
		} catch (Exception e) {
			logger.error("buy error", e);
			if (e instanceof SocketTimeoutException) {
				result.setSuccess(true);
				result.setResultMsg("交易处理中，结果未确认，等稍候查询订单状态");
			} else {
				result.setResultMsg("网络连接失败");
			}
			return result;
		}
		return result;
	}

	@Override
	public Result<FlowQueryForWeb> getFlowItemList(String uid, AcctInfo acctInfo) {
		Result<FlowQueryForWeb> result = new Result<FlowQueryForWeb>();
		FlowQueryForWeb flowQueryForWeb = new FlowQueryForWeb();
		try {

			Map<Integer, Map<String, ItemForQuery>> map = new HashMap<Integer, Map<String, ItemForQuery>>();
			Result<FlowQuery> flowQueryWideResult = supplyForRemoteService.queryFlowItem(uid,
					Constants.BizFlow.ITEM_USABLE_NATIONWIDE, acctInfo);
			if (!flowQueryWideResult.isSuccess()) {
				logger.error("getItemList error " + flowQueryWideResult.getResultMsg());
				result.setResultMsg("查询失败,请确认输入的是正确手机号");
				return result;
			}

			if (flowQueryWideResult.getModule() != null) {
				flowQueryForWeb.setMobileBelong(flowQueryWideResult.getModule().getMobileBelong());
				map.put(Constants.BizFlow.ITEM_USABLE_NATIONWIDE, flowQueryWideResult.getModule()
						.getItemMap());
			}

			Result<FlowQuery> flowQueryAreaResult = supplyForRemoteService.queryFlowItem(uid,
					Constants.BizFlow.ITEM_USABLE_AREA, acctInfo);
			if (!flowQueryAreaResult.isSuccess()) {
				logger.error("getItemList error " + flowQueryAreaResult.getResultMsg());
				result.setResultMsg("查询失败,请确认输入的是正确手机号");
				return result;
			}

			if (flowQueryAreaResult.getModule() != null) {
				flowQueryForWeb.setMobileBelong(flowQueryAreaResult.getModule().getMobileBelong());
				map.put(Constants.BizFlow.ITEM_USABLE_AREA, flowQueryAreaResult.getModule()
						.getItemMap());
			}

			flowQueryForWeb.setItemMap(map);
		} catch (Exception e) {
			result.setResultMsg("查询失败,网络连接超时");
			logger.error("queryFlowItem error", e);
			return result;
		}

		result.setSuccess(true);
		result.setModule(flowQueryForWeb);
		return result;
	}
}
