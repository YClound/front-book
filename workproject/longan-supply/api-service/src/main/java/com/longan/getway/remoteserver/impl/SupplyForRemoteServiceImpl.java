package com.longan.getway.remoteserver.impl;

import java.util.List;

import javax.annotation.Resource;

import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.client.remote.domain.web.BuyItem;
import com.longan.client.remote.domain.web.BuyFacePrice;
import com.longan.client.remote.domain.web.FlowQuery;
import com.longan.client.remote.domain.web.PhoneQuery;
import com.longan.client.remote.service.SupplyForRemoteService;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.biz.service.CommonDealService;
import com.longan.getway.core.SupplyDispatcherService;

public class SupplyForRemoteServiceImpl extends BaseService implements SupplyForRemoteService {
	@Resource
	private SupplyDispatcherService supplyDispatcherService;

	@Resource
	private CommonDealService commonDealService;

	@Override
	public Result<SupplyOrder> supply(BizOrder bizOrder) {

		return supplyDispatcherService.supply(bizOrder);
	}

	@Override
	public Result<BizOrder> buy(BuyItem buyItem) {
		return commonDealService.buyFromWeb(buyItem);
	}

	@Override
	public Result<BizOrder> buyByFacePrice(BuyFacePrice buyFacePrice) {

		return commonDealService.buyPhoneFromWeb(buyFacePrice);
	}

	@Override
	public Result<PhoneQuery> queryPhoneItem(String uid, AcctInfo acctInfo) {
		return commonDealService.queryPhoneItem(uid, acctInfo);
	}

	@Override
	public Result<FlowQuery> queryFlowItem(String uid, Integer type, AcctInfo acctInfo) {
		return commonDealService.queryFlowItem(uid, type, acctInfo);
	}

	@Override
	public Result<Integer> bantchBuyFlowByFacePrice(List<BuyFacePrice> list) {
		Result<Integer> result = new Result<Integer>();
		if (list == null || list.isEmpty()) {
			result.setResultMsg("入参异常");
			return result;
		}

		logger.warn("开始流量批充..");
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			Result<BizOrder> buyResult = commonDealService.buyFlowFromWeb(list.get(i));
			if (!buyResult.isSuccess()) {
				logger.warn(String.format("充值序列： %d 充值失败,原因: %s", i, buyResult.getResultMsg()));
			}
			if (buyResult.getModule() != null) {
				logger.warn(String.format("充值序列： %d 充值处理中,订单号: %d", i, buyResult.getModule().getId()));
				count = count + 1;
			}
		}
		result.setSuccess(count > 0);
		result.setModule(count);
		if (count == 0) {
			result.setResultMsg("充值失败，请检查上传文件 或商品未上架。");
		}
		return result;
	}

	@Override
	public Result<Integer> bantchBuyPhoneByFacePrice(List<BuyFacePrice> list) {
		Result<Integer> result = new Result<Integer>();
		if (list == null || list.isEmpty()) {
			result.setResultMsg("入参异常");
			return result;
		}

		logger.warn("开始话费批充..");
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			Result<BizOrder> buyResult = commonDealService.buyPhoneFromWeb(list.get(i));
			if (!buyResult.isSuccess()) {
				logger.warn(String.format("充值序列： %d 充值失败,原因: %s", i, buyResult.getResultMsg()));
			}
			if (buyResult.getModule() != null) {
				logger.warn(String.format("充值序列： %d 充值处理中,订单号: %d", i, buyResult.getModule().getId()));
				count = count + 1;
			}
		}
		result.setSuccess(count > 0);
		result.setModule(count);
		if (count == 0) {
			result.setResultMsg("充值失败，请检查上传文件 或商品未上架。");
		}
		return result;
	}
}
