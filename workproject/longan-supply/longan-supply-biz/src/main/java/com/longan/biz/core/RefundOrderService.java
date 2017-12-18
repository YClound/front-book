package com.longan.biz.core;

import java.util.List;

import com.longan.biz.dataobject.PayOrder;
import com.longan.biz.dataobject.RefundOrder;
import com.longan.biz.dataobject.RefundOrderQuery;
import com.longan.biz.domain.OperationVO;
import com.longan.biz.domain.Result;

public interface RefundOrderService {
	public Result<RefundOrder> createRefundOrder(PayOrder payOrder, OperationVO operationVO);

	public Result<RefundOrder> getRefundOrder(Long refundOrderId);

	public Result<List<RefundOrder>> queryRefundOrder(RefundOrderQuery refundOrderQuery);
	
	public Result<List<RefundOrder>> queryRefundOrderExport(RefundOrderQuery refundOrderQuery);

	public Result<Boolean> updateRefundOrderWhenException(Long refundOrderId, String errorMsg);
}
