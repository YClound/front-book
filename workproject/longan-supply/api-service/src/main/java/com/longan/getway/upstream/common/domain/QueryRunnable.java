package com.longan.getway.upstream.common.domain;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.core.BizOrderService;
import com.longan.biz.core.SupplyOrderService;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Utils;
import com.longan.getway.core.BizDealService;
import com.longan.getway.core.domain.SupplyResult;

public abstract class QueryRunnable implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(QueryRunnable.class);
	private static final int defaultMaxCount = Integer.parseInt(Utils.getProperty("supllyQueryCount"));
	
	private String queryName;
	protected Long supplyOrderId;
	protected Long bizOrderId;

	private BizOrderService bizOrderService;
	private SupplyOrderService supplyOrderService;
	private BizDealService bizDealService;

	private Future<?> future;
	private AtomicInteger count = new AtomicInteger(0);
	private int maxCount = defaultMaxCount;

	public QueryRunnable(String queryName, Long supplyOrderId, Long bizOrderId,
			BizOrderService bizOrderService, BizDealService bizDealService,
			SupplyOrderService supplyOrderService) {
		this.queryName = queryName;
		this.supplyOrderId = supplyOrderId;
		this.bizOrderService = bizOrderService;
		this.bizDealService = bizDealService;
		this.supplyOrderService = supplyOrderService;
		this.bizOrderId = bizOrderId;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	/**
	 * 查看任务是否达到调用次数限制，达到则停止该定时任务。
	 * 
	 * @return
	 */
	public boolean checkLimit() {
		boolean flag = count.addAndGet(1) > maxCount;
		if (flag) {
			if (future != null) {
				// 这里要用false ，不然会中断线程中的其他操作。
				future.cancel(false);
			}
		}
		return flag;
	}

	public int getCount() {
		return count.get();
	}

	public void stopTimer() {
		if (future != null) {
			logger.warn(queryName + "查询定时任务停止");
			// 这里要用false ，不然会中断线程中的其他操作。
			future.cancel(false);
		}
	}

	public Future<?> getFuture() {
		return future;
	}

	public void setFuture(Future<?> future) {
		this.future = future;
	}

	protected abstract CallBack chargeResultQuery();

	@Override
	public void run() {
		if (bizOrderId == null || supplyOrderId == null) {
			logger.warn(queryName + "查询失败,订单是空");
			stopTimer();
			return;
		}

		if (bizOrderService == null || supplyOrderService == null || bizDealService == null) {
			logger.warn(queryName + "查询失败，service设置错误");
			stopTimer();
			return;
		}

		// checkLimit 必须要做 ， 不做这个任务就停不下来了。 并将订单改为未处理
		if (checkLimit()) {

			Result<SupplyOrder> result = supplyOrderService.getSupplyOrder(supplyOrderId);
			if (!result.isSuccess() || result.getModule() == null) {
				logger.warn("供货单查询失败  bizOrderId : " + bizOrderId + " supplyOrderId : "
						+ supplyOrderId);
				stopTimer();
				return;
			}
			SupplyOrder supplyOrder = result.getModule();

			CallBack callBack = new CallBack();
			callBack.setSupplyOrderId(supplyOrderId);
			callBack.setStatus(SupplyResult.STATUS_UNCONFIRM);
			bizDealService.dealBizOrder(supplyOrder, callBack);
			return;
		}

		logger.warn("开始" + queryName + "核单查询  bizOrderId : " + bizOrderId + " supplyOrderId : "
				+ supplyOrderId + " 次数 : " + getCount());

		// 从chargeQuery()中获取结果
		CallBack callBack = chargeResultQuery();

		// 如果确定成功失败， 即生命周期结束，则结束定时任务， 并处理订单
		if (callBack != null && callBack.isOver() && callBack.getSupplyOrderId() != null) {
			logger.warn(queryName + "查询得到明确结果 : bizOrderId : " + bizOrderId + " supplyOrderId : "
					+ supplyOrderId);

			stopTimer();

			Result<SupplyOrder> result = supplyOrderService.getSupplyOrder(supplyOrderId);
			if (!result.isSuccess() || result.getModule() == null) {
				logger.warn("供货单查询失败  bizOrderId : " + bizOrderId + " supplyOrderId : "
						+ supplyOrderId);
				return;
			}

			SupplyOrder supplyOrder = result.getModule();
			if (supplyOrder != null) {
				bizDealService.dealBizOrder(supplyOrder, callBack);
			}
		}

	}

}
