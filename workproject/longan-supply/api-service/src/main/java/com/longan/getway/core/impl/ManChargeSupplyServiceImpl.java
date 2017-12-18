package com.longan.getway.core.impl;

import javax.annotation.Resource;

import com.longan.biz.core.ItemService;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.SupplyService;

public class ManChargeSupplyServiceImpl extends BaseService implements SupplyService {

	@Resource
	private ItemService itemService;

	@Override
	public Result<SupplyOrder> supply(SupplyOrder supplyOrder, BizOrder bizOrder) {
		Result<SupplyOrder> result = new Result<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD);
		result.setResultMsg("人工充值不支持同步供货");
		return result;
	}

	@Override
	public Result<SupplyOrder> asyncSupply(SupplyOrder supplyOrder, BizOrder bizOrder) {
		Result<SupplyOrder> result = new Result<SupplyOrder>();
		result.setModule(supplyOrder);

		if (bizOrder == null || supplyOrder == null) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD);
			result.setResultMsg("供货参数不对");
			return result;
		}
		// 异步的交易，订单置于处理中状态。

		result.setSuccess(true);
		return result;
	}

}