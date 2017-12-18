package com.longan.getway.core.impl;

import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.getway.core.SupplyService;

public class CardChargeSupplyServiceImpl implements SupplyService {

	@Override
	public Result<SupplyOrder> supply(SupplyOrder supplyOrder, BizOrder bizOrder) {
		Result<SupplyOrder> result = new Result<SupplyOrder>();
		result.setModule(supplyOrder);

		if (bizOrder == null || bizOrder.getItemId() == null) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD);
			result.setResultMsg("供货参数不对");
			return result;
		}

		result.setResultMsg("暂未开放该业务");
		return result;
	}

	@Override
	public Result<SupplyOrder> asyncSupply(SupplyOrder supplyOrder, BizOrder bizOrder) {
		Result<SupplyOrder> result = new Result<SupplyOrder>();
		result.setModule(supplyOrder);

		if (bizOrder == null || bizOrder.getItemId() == null) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD);
			result.setResultMsg("供货参数不对");
			return result;
		}

		result.setResultMsg("暂未开放该业务");
		return result;
	}
}
