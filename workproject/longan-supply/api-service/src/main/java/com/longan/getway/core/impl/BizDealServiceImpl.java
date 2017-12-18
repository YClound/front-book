package com.longan.getway.core.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.core.BizOrderService;
import com.longan.biz.core.PayService;
import com.longan.biz.core.StockService;
import com.longan.biz.core.SupplyOrderService;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.Utils;
import com.longan.client.remote.service.CallBackService;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.NotifyManager;
import com.longan.getway.core.BizDealService;
import com.longan.getway.core.SupplyDispatcherService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.common.domain.QueryRunnable;

public class BizDealServiceImpl extends BaseService implements BizDealService {
	public Logger logger = LoggerFactory.getLogger(this.getClass());
	private static Long chargeQueryDelay = Long.parseLong(Utils.getProperty("chargeQueryDelay"));

	private static Long qbQueryDelay = Long.parseLong(Utils.getProperty("qbQueryDelay"));

	@Resource
	public BizOrderService bizOrderService;

	@Resource
	public PayService payService;

	@Resource
	public LocalCachedService localCachedService;

	@Resource
	public StockService stockService;

	@Resource
	private NotifyManager notifyManager;

	@Resource
	private CallBackService callBackService;

	@Resource
	private SupplyOrderService supplyOrderService;

	@Resource
	private SupplyDispatcherService supplyDispatcherService;

	protected final static ScheduledExecutorService querySes = new ScheduledThreadPoolExecutor(32);

	protected final static ScheduledExecutorService qbQuerySes = new ScheduledThreadPoolExecutor(20);

	protected final static ScheduledExecutorService repeatSupplySes = new ScheduledThreadPoolExecutor(
			16);

	@Override
	public void submitQueryRunnable(QueryRunnable runnable) {
		// 用scheduleWithFixedDelay 不用 scheduleAtFixedRate 因为不希望出现多线程查询。
		Future<?> future = querySes.scheduleWithFixedDelay(runnable, 30, chargeQueryDelay,
				TimeUnit.SECONDS);
		QueryRunnable queryRunnable = (QueryRunnable) runnable;
		queryRunnable.setFuture(future);
	}

	/*
	 * 因为声讯QB 是查询自己通道的，所以单独一个线程池
	 */
	@Override
	public void submitQbQueryRunnable(QueryRunnable runnable) {
		Future<?> future = qbQuerySes.scheduleWithFixedDelay(runnable, qbQueryDelay, qbQueryDelay,
				TimeUnit.SECONDS);
		QueryRunnable queryRunnable = (QueryRunnable) runnable;
		queryRunnable.setFuture(future);
	}

	@Override
	public void dealBizOrder(SupplyOrder supplyOrder, CallBack callBack) {
		if (supplyOrder == null || callBack == null || supplyOrder.getBizOrderId() == null) {
			logger.error("dealBizOrder 入参错误");
			return;
		}

		Result<BizOrder> bizOrderResult = bizOrderService.getBizOrderById(supplyOrder
				.getBizOrderId());
		if (!bizOrderResult.isSuccess()) {
			logger.error("dealBizOrder getBizOrderById error : " + bizOrderResult.getResultMsg());
			return;
		}
		BizOrder bizOrder = bizOrderResult.getModule();
		if (bizOrder == null) {
			logger.error("dealBizOrder 没有该订单 bizOrderId : " + supplyOrder.getBizOrderId());
			return;
		}

		logger.warn("开始异步处理订单 ： bizOrderId : " + bizOrder.getId());
		if (bizOrder.isSyncSupply() && bizOrder.getStatus() == Constants.BizOrder.STATUS_CHARGING) {
			logger.warn("异步转同步订单,在处理中的时候不做处理。 bizOrderId : " + bizOrder.getId());

			// 释放等待
			notifyManager.notify(callBack);
			return;
		}

		boolean flag = callBack.getActualCost() != null && bizOrder.getActualCost() == null;
		if (flag) {
			logger.warn("记录上游实际扣款金额  bizOrderId : " + bizOrder.getId() + " cost : "
					+ callBack.getActualCost());
			// 如果订单中没有上游实际扣款金额，而且核单中能获取到，则做更新操作。
			supplyOrder.setSupplyActualCost(callBack.getActualCost());
			bizOrder.setActualCost(callBack.getActualCost());
		}

		// 判断是否是已经终结订单
		if (supplyOrder.isOver()) {
			if (flag) {
				SupplyOrder supplyOrderUpdate = new SupplyOrder();
				supplyOrderUpdate.setId(supplyOrder.getId());
				supplyOrderUpdate.setSupplyActualCost(callBack.getActualCost());
				supplyOrderService.updateSupplyOrder(supplyOrderUpdate);

				BizOrder bizOrderUpdate = new BizOrder();
				bizOrderUpdate.setId(bizOrder.getId());
				bizOrderUpdate.setActualCost(callBack.getActualCost());
				// 如果订单中没有上游实际扣款金额，而且核单中能获取到，则做更新操作。
				bizOrderService.updateBizOrder(bizOrderUpdate);
			}
			logger.warn("生命周期已结束的供货单不做后续处理。bizOrderId : " + bizOrder.getId());
			return;
		}

		if (StringUtils.isNotEmpty(callBack.getFailedCode())
				|| StringUtils.isNotEmpty(callBack.getFailedMsg())) {
			bizOrder.setUpstreamMemo(callBack.getFailedCode() + "_" + callBack.getFailedMsg());
			supplyOrder.setUpstreamMemo(callBack.getFailedCode() + "_" + callBack.getFailedMsg());
		}

		if (callBack.isFaild()) {
			logger.warn("开始处理失败订单 bizOrderId : " + bizOrder.getId());
			this.cancelSupplyOrderAndRepeatSupply(supplyOrder, bizOrder);

		} else if ((supplyOrder.isTypeCardCharge() || supplyOrder.isTypeCardForwardCharge())
				&& callBack.isCardValied()) {
			supplyOrder.setFlagCardValid(true);
			supplyOrder.setUpstreamMemo(supplyOrder.getUpstreamMemo());
			logger.warn("开始处理失败订单(非法卡) bizOrderId : " + bizOrder.getId());

			this.cancelSupplyOrderAndRepeatSupply(supplyOrder, bizOrder);

		} else if (callBack.isUnconfirm()) {
			logger.warn("开始处理供货超时或未确认订单 bizOrderId : " + bizOrder.getId());

			supplyOrderService.unComfirmSupplyOrder(supplyOrder);
			bizOrderService.unComfirmBizOrder(bizOrder);
		} else if (callBack.isSuccess()) {
			logger.warn("开始处理成功订单  bizOrderId : " + bizOrder.getId());

			supplyOrderService.comfirmSupplyOrder(supplyOrder);
			bizOrderService.comfirmBizOrder(bizOrder);
		}

		// 下游如果不是同步请求 ，而且订单是成功的，则通知下游
		if (!bizOrder.isSyncSupply() && callBack.isSuccess()) {
			callBackService.callBackAsync(bizOrder);
		}

	}

	public Result<SupplyOrder> dealSyncSupplyResult(SupplyResult<SupplyOrder> chargeResult,
			SupplyOrder supplyOrder, BizOrder bizOrder) {
		Result<SupplyOrder> result = new Result<SupplyOrder>();
		result.setModule(supplyOrder);

		if (chargeResult == null || supplyOrder == null || bizOrder == null) {
			logger.warn("dealSyncSupplyResult error 入参错误 ");
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SYSTEM);
			result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SYSTEM);
			return result;
		}

		if (chargeResult.isUndifend()) {
			logger.warn("supply bizOrderId : " + bizOrder.getId() + " undifend : ");
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SYSTEM);
			result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SYSTEM);
			return result;
		}
		if (chargeResult.isFailed()) { // 供货失败
			logger.warn("supply bizOrderId : " + bizOrder.getId() + " failed : "
					+ chargeResult.getResultMsg());
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD);
			result.setResultMsg(chargeResult.getResultMsg());
			return result;
		}
		if (chargeResult.isCardInvalid()) {
			logger.warn("supply bizOrderId : " + bizOrder.getId() + " cardinvalid : "
					+ chargeResult.getResultMsg());
			supplyOrder.setFlagCardValid(true);

			if (supplyOrder.isSupplyFromStock()) {
				result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD);
				result.setResultMsg(chargeResult.getResultMsg());
			} else {
				// 非库存情况下作为未确认
				result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_UNCONFIRM);
				result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SUPPLY_UNCONFIRM);
			}
			return result;
		}
		if (chargeResult.isUnConfirm()) {
			logger.warn("supply bizOrderId : " + bizOrder.getId() + " unconfirm");
			// 未确认状态 不做 库存退还或者出库处理。
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_UNCONFIRM);
			result.setResultMsg(chargeResult.getResultMsg());
			return result;
		}
		if (chargeResult.isSuccess()) {
			// 成功状态下 设置成本
			supplyOrder = chargeResult.getModule();
			bizOrder.setActualCost(supplyOrder.getSupplyActualCost());

			// 根据上游供货类型 做不同的处理方式
			if (chargeResult.isAsync()) { // 如果上游是异步供货。

				// 设置同步的锁
				notifyManager.addLock(supplyOrder.getId());

				// 更新已有供货的信息
				supplyOrderService.updateSupplyOrder(supplyOrder);

				long bizOrderId = bizOrder.getId();
				final long supplyOrderId = supplyOrder.getId();
				Future<CallBack> future = null;
				try {
					future = notifyExecutor.submit(new Callable<CallBack>() {
						@Override
						public CallBack call() throws Exception {
							return notifyManager.waitForResult(supplyOrderId);
						}
					});
					CallBack upstreamCallBack = null;
					try {
						upstreamCallBack = future.get();
					} catch (InterruptedException e) {
						logger.error("同步等待通知线程异常 bizOrderId = " + bizOrderId + " supplyOrderId = "
								+ supplyOrderId, e);
					} catch (ExecutionException e) {
						logger.error("同步等待通知线程异常 bizOrderId = " + bizOrderId + " supplyOrderId = "
								+ supplyOrderId, e);
					}

					if (upstreamCallBack == null) {
						// 供货超时
						result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_UNCONFIRM);
						result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SUPPLY_UNCONFIRM);
						return result;
					}

					// 状态不确认
					if (upstreamCallBack.isUnconfirm()) {
						result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_UNCONFIRM);
						result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SUPPLY_UNCONFIRM);
						return result;
					}

					// 卡失效
					if (chargeResult.isCardInvalid()) {
						supplyOrder.setUpstreamMemo(chargeResult.getResultMsg());
						supplyOrder.setFlagCardValid(true);

						if (supplyOrder.isSupplyFromStock()) {
							result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD);
							result.setResultMsg("上游供货失败");
						} else {
							// 直冲等情况下作为未确认
							result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_UNCONFIRM);
							result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SUPPLY_UNCONFIRM);
						}

						return result;
					}

					// 失败
					if (upstreamCallBack.isFaild()) {
						if (StringUtils.isNotEmpty(upstreamCallBack.getFailedCode())
								|| StringUtils.isNotEmpty(upstreamCallBack.getFailedMsg())) {
							supplyOrder.setUpstreamMemo(upstreamCallBack.getFailedCode() + "_"
									+ upstreamCallBack.getFailedMsg());
						}
						result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD);
						result.setResultMsg("上游供货失败");
						return result;
					}

					// 供货成功
					if (upstreamCallBack.isSuccess()) {
						supplyOrder.setSupplyStatus(Constants.SupplyOrder.STATUS_SUCCESS);
						bizOrder.setStatus(Constants.BizOrder.STATUS_SUCCESS);
					}
				} catch (RejectedExecutionException e) {
					logger.warn("同步等待通知线程繁忙 bizOrderId = " + bizOrderId, e);
					// 直接被拒绝，所以不知道结果，供货未确认
					result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_UNCONFIRM);
					result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SUPPLY_UNCONFIRM);
					return result;
				}
			} else {
				// 供货成功
				supplyOrder.setSupplyStatus(Constants.SupplyOrder.STATUS_SUCCESS);
				bizOrder.setStatus(Constants.BizOrder.STATUS_SUCCESS);
			}
		}

		result.setSuccess(true);
		return result;

	}

	public void dealAsyncSupplyResult(SupplyResult<SupplyOrder> chargeResult,
			SupplyOrder supplyOrder, BizOrder bizOrder) {

		if (chargeResult == null || supplyOrder == null || bizOrder == null) {
			logger.warn("dealAsyncSupplyResult error 入参错误 ");
			return;
		}

		if (chargeResult.isFailed()) { // 供货失败
			logger.warn("supply bizOrderId : " + supplyOrder.getBizOrderId() + " failed : "
					+ chargeResult.getResultMsg());
			// 设置状态失败
			supplyOrder.setSupplyStatus(Constants.SupplyOrder.STATUS_FAILED);
			supplyOrder.setUpstreamMemo(chargeResult.getResultMsg());

		} else if (chargeResult.isUnConfirm()) {
			logger.warn("supply bizOrderId : " + supplyOrder.getBizOrderId() + " unconfirm ");
			// 设置状态未知
			supplyOrder.setSupplyStatus(Constants.SupplyOrder.STATUS_UNCONFIRMED);
			supplyOrder.setUpstreamMemo(chargeResult.getResultMsg());

			bizOrder.setStatus(Constants.BizOrder.STATUS_UNCONFIRMED);
		} else if (chargeResult.isCardInvalid()) {
			logger.warn("supply bizOrderId : " + bizOrder.getId() + " cardinvalid : "
					+ chargeResult.getResultMsg());
			// 设置状态失败
			supplyOrder.setSupplyStatus(Constants.SupplyOrder.STATUS_FAILED);
			supplyOrder.setFlagCardValid(true);

		} else if (chargeResult.isSuccess()) {
			SupplyOrder returnSupplyOrder = chargeResult.getModule();
			if (returnSupplyOrder != null) {
				supplyOrder.setUpstreamDate(returnSupplyOrder.getUpstreamDate());
				supplyOrder.setUpstreamMemo(returnSupplyOrder.getUpstreamMemo());
				supplyOrder.setUpstreamSerialno(returnSupplyOrder.getUpstreamSerialno());
			}

			// 根据上游供货类型 不同设置订单状态。
			if (chargeResult.isAsync()) {
				// 处理中
				supplyOrder.setSupplyStatus(Constants.SupplyOrder.STATUS_CHARGING);
				bizOrder.setStatus(Constants.BizOrder.STATUS_CHARGING);
			} else {
				// 如果上游是同步，设置状态为成功
				supplyOrder.setSupplyStatus(Constants.SupplyOrder.STATUS_SUCCESS);
				bizOrder.setStatus(Constants.BizOrder.STATUS_SUCCESS);
			}
		} else {
			logger.warn("供货异常,不能识别供货状态 开始更新订单状态 bizOrderId : " + supplyOrder.getBizOrderId());
			// 这里就不去关心更新结果了
			supplyOrder.setSupplyStatus(Constants.SupplyOrder.STATUS_EXCEPTION);
			supplyOrder.setUpstreamMemo("供货异常,不能识别供货状态");
			supplyOrderService.unComfirmSupplyOrder(supplyOrder);
			bizOrderService.unComfirmBizOrder(bizOrder);
			return;
		}

		// 更新订单
		if (supplyOrder.getSupplyStatus() == Constants.SupplyOrder.STATUS_SUCCESS) {
			logger.warn("供货成功，开始更新订单状态 bizOrderId : " + supplyOrder.getBizOrderId());
			supplyOrderService.comfirmSupplyOrder(supplyOrder);
			bizOrder.setActualCost(supplyOrder.getSupplyActualCost());
			bizOrderService.comfirmBizOrder(bizOrder);
		} else if (supplyOrder.getSupplyStatus() == Constants.SupplyOrder.STATUS_FAILED) {

			logger.warn("供货失败 开始处理失败订单 bizOrderId : " + supplyOrder.getBizOrderId());
			this.cancelSupplyOrderAndRepeatSupply(supplyOrder, bizOrder);

		} else if (supplyOrder.getSupplyStatus() == Constants.SupplyOrder.STATUS_CHARGING) {
			logger.warn("供货处理中，开始更新订单状态 bizOrderId : " + supplyOrder.getBizOrderId());
			supplyOrderService.updateSupplyOrder(supplyOrder);
			bizOrder.setActualCost(supplyOrder.getSupplyActualCost());
			bizOrderService.updateBizOrder(bizOrder);
		} else if (supplyOrder.getSupplyStatus() == Constants.SupplyOrder.STATUS_UNCONFIRMED) {
			logger.warn("供货未确认， 开始更新订单状态 bizOrderId : " + supplyOrder.getBizOrderId());
			supplyOrderService.unComfirmSupplyOrder(supplyOrder);
			bizOrderService.unComfirmBizOrder(bizOrder);
		}

		// 明确成功的情况下 通知下游， 这里供货失败就不通知下游了，因为要补充
		if (supplyOrder.getSupplyStatus() == Constants.SupplyOrder.STATUS_SUCCESS) {
			// 只有成功异步通知。
			callBackService.callBackAsync(bizOrder);
		}
	}

	private void cancelSupplyOrderAndRepeatSupply(final SupplyOrder supplyOrder,
			final BizOrder bizOrder) {
		if (supplyOrder.isManualRepeatType()) {
			// 手动补充，充值取消订单肯定是最终供货
			supplyOrder.setFinalType(Constants.SupplyOrder.FINAL_TYPE_YES);
		}

		Result<Boolean> cancelSupplyOrderResult = supplyOrderService.cancelSupplyOrder(supplyOrder);
		if (!cancelSupplyOrderResult.isSuccess()) {
			logger.error("处理失败，供货单已经被其他线程处理  bizOrderId : " + bizOrder.getId() + " msg : "
					+ cancelSupplyOrderResult.getResultMsg());
			return;
		} else {
			if (!supplyOrder.isManualRepeatType()) {
				// 非手动的情况下补充
				repeatSupplySes.execute(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							logger.error("error when sleep");
						}
						// 这里不取消订单， 做补充操作。
						bizOrder.setIsRepeat(true);
						Result<SupplyOrder> supplyResult = supplyDispatcherService.supply(bizOrder);
						if (!supplyResult.isSuccess()) {
							if (Constants.ErrorCode.CODE_ERROR_SUPPLY_UNCONFIRM.equals(supplyResult
									.getResultCode())) {
								// do nothing
							} else {
								logger.warn("补充供货停止： " + supplyResult.getResultMsg()
										+ " bizOrderId : " + bizOrder.getId());
								if (supplyResult.getModule() != null) {
									supplyResult.getModule().setFinalType(
											Constants.SupplyOrder.FINAL_TYPE_YES);
									supplyOrderService.cancelSupplyOrder(supplyResult.getModule());
								} else {
									supplyOrder.setFinalType(Constants.SupplyOrder.FINAL_TYPE_YES);
									supplyOrder
											.setSupplyStatus(Constants.SupplyOrder.STATUS_FAILED);
									supplyOrderService.updateSupplyOrder(supplyOrder);
								}
								bizOrderService.cancelBizOrder(bizOrder);

								// 通知下游
								bizOrder.setStatus(Constants.BizOrder.STATUS_FAILED);
								callBackService.callBackAsync(bizOrder);
							}
						}
					}
				});
			} else {
				// 手动补充就直接 ， 取消订单。
				bizOrderService.cancelBizOrder(bizOrder);

				// 通知下游
				bizOrder.setStatus(Constants.BizOrder.STATUS_FAILED);
				callBackService.callBackAsync(bizOrder);
			}
		}
	}
}
