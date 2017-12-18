package com.longan.getway.core.impl;

import javax.annotation.Resource;

import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.service.CallBackService;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.SupplyService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamSupplyDispatcherService;

public class DirectChargeSupplyServiceImpl extends BaseService implements SupplyService {
	@Resource
	private UpstreamSupplyDispatcherService upstreamSupplyDispatcherService;

	@Resource
	private CallBackService callBackService;

	@Override
	public Result<SupplyOrder> supply(SupplyOrder supplyOrder, final BizOrder bizOrder) {
		Result<SupplyOrder> result = new Result<SupplyOrder>();
		result.setModule(supplyOrder);

		if (supplyOrder == null || bizOrder == null) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD);
			result.setResultMsg("供货参数不对");
			return result;
		}

		// 对外供货
		SupplyResult<SupplyOrder> chargeResult = upstreamSupplyDispatcherService
				.charge(supplyOrder);

		return bizDealService.dealSyncSupplyResult(chargeResult, supplyOrder, bizOrder);
	}

	@Override
	public Result<SupplyOrder> asyncSupply(final SupplyOrder supplyOrder, final BizOrder bizOrder) {
		Result<SupplyOrder> result = new Result<SupplyOrder>();
		result.setModule(supplyOrder);

		if (supplyOrder == null || bizOrder == null) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD);
			result.setResultMsg("供货参数不对");
			return result;
		}

		es.execute(new Runnable() {
			@Override
			public void run() {
				// 对外供货
				SupplyResult<SupplyOrder> chargeResult = upstreamSupplyDispatcherService
						.charge(supplyOrder);

				bizDealService.dealAsyncSupplyResult(chargeResult, supplyOrder, bizOrder);
			}

		});

		result.setSuccess(true);
		return result;
	}

}