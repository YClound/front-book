package com.longan.getway.core.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.core.BizOrderService;
import com.longan.biz.core.ItemService;
import com.longan.biz.core.SupplyOrderService;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.getway.core.SupplyDispatcherService;
import com.longan.getway.core.SupplyService;

public class SupplyDispatcherServiceImpl implements SupplyDispatcherService {
	public Logger logger = LoggerFactory.getLogger(SupplyDispatcherServiceImpl.class);
	@Resource
	private LocalCachedService localCachedService;

	@Resource
	private SupplyService cardForwardChargeSupplyService;

	@Resource
	private SupplyService directChargeSupplyService;

	@Resource
	private SupplyService manChargeSupplyService;

	@Resource
	private SupplyService cardSupplyService;

	@Resource
	private SupplyService cardChargeSupplyService;

	@Resource
	private ItemService itemService;

	@Resource
	private SupplyOrderService supplyOrderService;

	@Resource
	private BizOrderService bizOrderService;

	private void setItemSupplyToBizOrder(BizOrder bizOrder, ItemSupply itemSupply) {
		// 只设置成本。以后加上成本累计
		// bizOrder.setUpstreamId(itemSupply.getSupplyTraderId() + "");
		// bizOrder.setItemSupplyId(itemSupply.getId());
		// bizOrder.setSupplyType(itemSupply.getItemSupplyType());

		// 设置订单筛选标志
		bizOrder.setSupplyFilterIndex(itemSupply.getSupplyFilterIndex());

		if (itemSupply.getItemCostPrice() != null && itemSupply.getItemCostPrice() != 0) {
			bizOrder.setItemCostPrice(itemSupply.getItemCostPrice());
		}
	}

	private SupplyOrder newSupplyOrder(BizOrder bizOrder, ItemSupply itemSupply) {
		SupplyOrder supplyOrder = new SupplyOrder();
		supplyOrder.setAmount(bizOrder.getAmount());
		supplyOrder.setBizId(bizOrder.getBizId());
		supplyOrder.setBizOrderId(bizOrder.getId());
		supplyOrder.setCombineType(bizOrder.getCombineType());
		supplyOrder.setFinalType(Constants.SupplyOrder.FINAL_TYPE_NO);
		supplyOrder.setItemFacePrice(bizOrder.getItemFacePrice());
		supplyOrder.setItemId(bizOrder.getItemId());
		supplyOrder.setItemName(bizOrder.getItemName());
		supplyOrder.setItemSupplyId(itemSupply.getId());
		supplyOrder
				.setManualType(bizOrder.getManualType() == null ? Constants.SupplyOrder.MANUAL_TYPE_NO
						: bizOrder.getManualType());
		supplyOrder
				.setRepeatType(bizOrder.getIsRepeat() != null && bizOrder.getIsRepeat() ? Constants.SupplyOrder.REPEAT_TYPE_YES
						: Constants.SupplyOrder.REPEAT_TYPE_NO);
		supplyOrder.setSupplyCostPrice(itemSupply.getItemCostPrice());
		supplyOrder.setSupplyFacePrice(bizOrder.getItemFacePrice());
		supplyOrder.setSupplyStatus(Constants.SupplyOrder.STATUS_INIT);
		supplyOrder.setSupplyTraderId(itemSupply.getSupplyTraderId());
		supplyOrder.setSupplyTraderName(itemSupply.getSupplyTraderName());
		supplyOrder.setSupplyType(itemSupply.getItemSupplyType());
		supplyOrder.setUserId(bizOrder.getUserId());
		supplyOrder.setItemUid(bizOrder.getItemUid());
		supplyOrder.setAmt(bizOrder.getAmt());
		supplyOrder.setItemExt1(bizOrder.getItemExt1());
		supplyOrder.setItemExt2(bizOrder.getItemExt2());
		supplyOrder.setItemExt3(bizOrder.getItemExt3());
		
		return supplyOrder;
	}

	@Override
	public Result<SupplyOrder> supply(BizOrder bizOrder) {
		Result<SupplyOrder> result = new Result<SupplyOrder>();

		if (bizOrder == null || bizOrder.getItemId() == null) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD);
			result.setResultMsg("供货参数不对");
			return result;
		}

		if (bizOrder.getIsRepeat() != null && bizOrder.getIsRepeat()) {
			logger.warn("开始补充供货: bizOrderId : " + bizOrder.getId() + " count : "
					+ bizOrder.getSupplyCount());
		}

		Item item = localCachedService.getItem(bizOrder.getItemId());

		if (item == null) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_ITEM);
			result.setResultMsg(Constants.ErrorCode.DESC_ERROR_ITEM);
			return result;
		}

		String serialno = bizOrder.getDownstreamSerialno();

		logger.warn("serialno: " + serialno + " 开始供货筛选");

		Result<ItemSupply> filterItemSupplyResult = itemService.filterItemSupply(item, bizOrder);
		if (!filterItemSupplyResult.isSuccess() || filterItemSupplyResult.getModule() == null) {
			result.setResultCode(filterItemSupplyResult.getResultCode());
			result.setResultMsg(filterItemSupplyResult.getResultMsg());
			return result;
		}

		ItemSupply itemSupply = filterItemSupplyResult.getModule();
		logger.warn("serialno: " + serialno + " 供货筛选成功，供货编号 : " + itemSupply.getId() + " 供货商编号："
				+ itemSupply.getSupplyTraderId());

		setItemSupplyToBizOrder(bizOrder, itemSupply);

		SupplyOrder supplyOrder = newSupplyOrder(bizOrder, itemSupply);

		Result<SupplyOrder> createSupplyOrderResult = supplyOrderService
				.createSupplyOrder(supplyOrder);
		if (!createSupplyOrderResult.isSuccess() || createSupplyOrderResult.getModule() == null) {
			result.setResultCode(createSupplyOrderResult.getResultCode());
			result.setResultMsg(createSupplyOrderResult.getResultMsg());
			return result;
		}

		// 设置供货次数
		Integer supplyCount = bizOrder.getSupplyCount() == null ? 1 : bizOrder.getSupplyCount() + 1;
		bizOrder.setSupplyCount(supplyCount);

		// 更新订单信息
		Result<Boolean> updateBizOrderResult = bizOrderService.updateBizOrder(bizOrder);
		if (!updateBizOrderResult.isSuccess()) {
			result.setResultCode(updateBizOrderResult.getResultCode());
			result.setResultMsg(updateBizOrderResult.getResultMsg());
			return result;
		}

		// 这里以后可能会做上排队机制。

		Result<Boolean> chargingSupplyOrderResult = supplyOrderService
				.chargingSupplyOrderAndCounting(supplyOrder);
		if (!chargingSupplyOrderResult.isSuccess()) {
			result.setResultCode(chargingSupplyOrderResult.getResultCode());
			result.setResultMsg(chargingSupplyOrderResult.getResultMsg());
			return result;
		}

		boolean isSyncSupply = bizOrder.isSyncSupply();

		if (bizOrder.getIsRepeat() != null) {
			// 补充供货的话，统一用异步供货方式。
			isSyncSupply = !bizOrder.getIsRepeat();
		}

		// 根据商品类型进行供货
		if (itemSupply.isTypeCardForwardCharge()) {
			// 商品是卡密转直冲形式
			if (isSyncSupply) {
				result = cardForwardChargeSupplyService.supply(supplyOrder, bizOrder);
			} else {
				result = cardForwardChargeSupplyService.asyncSupply(supplyOrder, bizOrder);
			}
		} else if (itemSupply.isTypeDirectCharge()) {
			if (isSyncSupply) {
				result = directChargeSupplyService.supply(supplyOrder, bizOrder);
			} else {
				result = directChargeSupplyService.asyncSupply(supplyOrder, bizOrder);
			}
		} else if (itemSupply.isTypeMan()) {
			// 人工处理，无需对外充值
			if (isSyncSupply) {
				result = manChargeSupplyService.supply(supplyOrder, bizOrder);
			} else {
				result = manChargeSupplyService.asyncSupply(supplyOrder, bizOrder);
			}
		} else if (itemSupply.isTypeCard()) {
			// 直接返回卡密 无需对外供货
			if (isSyncSupply) {
				result = cardSupplyService.supply(supplyOrder, bizOrder);
			} else {
				result = cardSupplyService.asyncSupply(supplyOrder, bizOrder);
			}
		} else if (itemSupply.isTypeCardCharge()) {
			if (isSyncSupply) {
				result = cardChargeSupplyService.supply(supplyOrder, bizOrder);
			} else {
				result = cardChargeSupplyService.asyncSupply(supplyOrder, bizOrder);
			}
		}

		return result;
	}

}
