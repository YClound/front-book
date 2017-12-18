package com.longan.getway.biz.service.impl;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.cached.LocalConfigCached;
import com.longan.biz.cached.LocalItemForPhoneCached;
import com.longan.biz.cached.LocalItemListForFlowCached;
import com.longan.biz.cached.LocalItemListForPhoneCached;
import com.longan.biz.core.AcctService;
import com.longan.biz.core.AreaInfoService;
import com.longan.biz.core.BizOrderService;
import com.longan.biz.core.GameCarrierService;
import com.longan.biz.core.GameService;
import com.longan.biz.core.ItemService;
import com.longan.biz.core.PayService;
import com.longan.biz.core.StockService;
import com.longan.biz.core.SupplyOrderService;
import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.Config;
import com.longan.biz.dataobject.Game;
import com.longan.biz.dataobject.GameCarrier;
import com.longan.biz.dataobject.GameCarrierQuery;
import com.longan.biz.dataobject.GameQuery;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.ItemQuery;
import com.longan.biz.dataobject.MobileBelong;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;
import com.longan.biz.utils.Md5Encrypt;
import com.longan.client.remote.domain.web.BuyFacePrice;
import com.longan.client.remote.domain.web.BuyItem;
import com.longan.client.remote.domain.web.FlowQuery;
import com.longan.client.remote.domain.web.ItemForQuery;
import com.longan.client.remote.domain.web.PhoneQuery;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.biz.common.request.BuyRequest;
import com.longan.getway.biz.common.request.FlowBuyRequest;
import com.longan.getway.biz.common.request.GameBuyRequest;
import com.longan.getway.biz.common.request.QueryBalanceRequest;
import com.longan.getway.biz.common.request.QueryBizOrderRequest;
import com.longan.getway.biz.common.request.QueryGameCarrierRequest;
import com.longan.getway.biz.common.request.QueryGameItemListRequest;
import com.longan.getway.biz.common.request.QueryGameItemRequest;
import com.longan.getway.biz.common.request.QueryGameListRequest;
import com.longan.getway.biz.common.request.QueryGameRequest;
import com.longan.getway.biz.common.request.QueryItemListRequest;
import com.longan.getway.biz.common.request.QueryItemRequest;
import com.longan.getway.biz.common.request.Request;
import com.longan.getway.biz.common.request.TelephoneBuyRequest;
import com.longan.getway.biz.common.response.BizOrderData;
import com.longan.getway.biz.common.response.BuyResponse;
import com.longan.getway.biz.common.response.GameCarrierData;
import com.longan.getway.biz.common.response.GameData;
import com.longan.getway.biz.common.response.GameItemData;
import com.longan.getway.biz.common.response.ItemData;
import com.longan.getway.biz.common.response.ListData;
import com.longan.getway.biz.common.response.QueryBalanceResponse;
import com.longan.getway.biz.common.response.QueryBizOrderResponse;
import com.longan.getway.biz.common.response.QueryGameCarrierResponse;
import com.longan.getway.biz.common.response.QueryGameItemListResponse;
import com.longan.getway.biz.common.response.QueryGameItemResponse;
import com.longan.getway.biz.common.response.QueryGameListResponse;
import com.longan.getway.biz.common.response.QueryGameResponse;
import com.longan.getway.biz.common.response.QueryItemListResponse;
import com.longan.getway.biz.common.response.QueryItemResponse;
import com.longan.getway.biz.common.response.Response;
import com.longan.getway.biz.service.CommonDealService;
import com.longan.getway.core.SupplyDispatcherService;

public class CommonDealServiceImpl extends BaseService implements CommonDealService {
	@Resource
	private BizOrderService bizOrderService;

	@Resource
	private PayService payService;

	@Resource
	private StockService stockService;

	@Resource
	private LocalCachedService localCachedService;

	@Resource
	private SupplyDispatcherService supplyDispatcherService;

	@Resource
	private AcctService acctService;

	@Resource
	private ItemService itemService;

	@Resource
	private AreaInfoService areaInfoService;

	@Resource
	private SupplyOrderService supplyOrderService;

	@Resource
	private GameCarrierService gameCarrierService;

	@Resource
	private GameService gameService;

	@Resource
	private LocalConfigCached localConfigCached;

	@Override
	public BuyResponse buy(BuyRequest buyRequest, Integer supllyType) {
		boolean isSync = (supllyType == Constants.BizOrder.DOWNSTREAM_SUPPLY_WAY_SYNC); // 判断同步或异步
		BuyResponse response = new BuyResponse();
		Long userId = Long.parseLong(buyRequest.getUserId());
		Integer itemId = Integer.parseInt(buyRequest.getItemId());
		String uid = buyRequest.getUid().trim();
		String serialno = buyRequest.getSerialno().trim();
		Integer itemPrice = null;

		if (StringUtils.isNotEmpty(buyRequest.getItemPrice())) {
			itemPrice = Integer.parseInt(buyRequest.getItemPrice());
		}

		Date dtCreate = new Date();
		try {
			if (StringUtils.isNotEmpty(buyRequest.getDtCreate())) {
				dtCreate = DateTool.strintToDatetime2(buyRequest.getDtCreate());
			}
		} catch (ParseException e) {
			logger.error("parse dtCreate error", e);
		}

		Item item = localCachedService.getItem(itemId); // 校验商品是否存在
		if (item == null) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_ITEM);
			response.setDesc("商品不存在");
			return response;
		}

		if (isSync && (item.getCanSync() == null || !item.getCanSync())) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_ITEM);
			response.setDesc("该商品不支持同步供货");
			return response;
		}

		BizOrder bizOrder = new BizOrder(); // 交易订单信息
		bizOrder.setBizId(item.getBizId());
		bizOrder.setItemPrice(itemPrice);
		bizOrder.setUserId(userId);
		bizOrder.setDownstreamDate(dtCreate);
		bizOrder.setDownstreamSerialno(serialno);
		bizOrder.setAmt(1);
		bizOrder.setChannel(Constants.BizOrder.CHANNEL_SUPPLY);
		bizOrder.setItemId(itemId);
		bizOrder.setItemUid(uid);
		if (isSync) {
			bizOrder.setDownstreamSupplyWay(Constants.BizOrder.DOWNSTREAM_SUPPLY_WAY_SYNC);
		} else {
			bizOrder.setDownstreamSupplyWay(Constants.BizOrder.DOWNSTREAM_SUPPLY_WAY_ASYNC);
		}

		response = buyItem(item, bizOrder, isSync);
		return response;
	}

	@Override
	public QueryBizOrderResponse queryResponse(QueryBizOrderRequest queryBizOrderRequest) {
		QueryBizOrderResponse response = new QueryBizOrderResponse();

		Result<BizOrder> result = bizOrderService.getBizOrderDownstreamSerialno(queryBizOrderRequest.getSerialno(), Long.parseLong(queryBizOrderRequest.getUserId()));
		if (!result.isSuccess()) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_SYSTEM);
			response.setDesc(result.getResultMsg());
			return response;
		}
		BizOrder bizOrder = result.getModule();

		if (bizOrder == null) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_BIZORDER);
			response.setDesc(result.getResultMsg());
			return response;
		}

		response.setSuccess();
		BizOrderData data = new BizOrderData();
		data.setAmount(bizOrder.getAmount());
		data.setAmt(bizOrder.getAmt());
		data.setGmtCreate(DateTool.parseDates(bizOrder.getGmtCreate()));
		data.setGmtModify(DateTool.parseDates(bizOrder.getGmtModify()));
		data.setId(bizOrder.getId());
		data.setItemId(bizOrder.getItemId());
		data.setSerialno(bizOrder.getDownstreamSerialno());
		data.setStatus(bizOrder.getStatus());
		data.setStatusDesc(bizOrder.getStatusDesc());
		response.setData(data);
		return response;
	}

	@Override
	public QueryBalanceResponse queryBalance(QueryBalanceRequest queryBalanceRequest) {
		QueryBalanceResponse queryBalanceResponse = new QueryBalanceResponse();
		UserInfo userInfo = localCachedService.getUserInfo(Long.parseLong(queryBalanceRequest.getUserId()));
		if (userInfo == null) {
			queryBalanceResponse.setCode(Constants.ErrorCode.CODE_ERROR_USER);
			queryBalanceResponse.setDesc(Constants.ErrorCode.DESC_ERROR_USER);
			return queryBalanceResponse;
		}
		Result<AcctInfo> result = acctService.getAcctInfo(userInfo.getAcctId());

		if (!result.isSuccess()) {
			queryBalanceResponse.setCode(Constants.ErrorCode.CODE_ERROR_USER);
			queryBalanceResponse.setDesc(result.getResultMsg());
			return queryBalanceResponse;
		}

		queryBalanceResponse.setSuccess();
		queryBalanceResponse.setBalance(result.getModule().getBalance());
		return queryBalanceResponse;
	}

	@Override
	public Response checkSign(Request request) {
		Response response = new Response();

		TreeMap<String, String> map = new TreeMap<String, String>();
		Field[] fields = request.getClass().getDeclaredFields(); // 获取所有属性

		for (Field field : fields) {
			field.setAccessible(true); // 设置对私有的访问权限
			try {
				if (field.get(request) != null) {
					map.put(field.getName(), field.get(request).toString());
				}
			} catch (IllegalArgumentException e1) {
				logger.error("checkSign error", e1);
			} catch (IllegalAccessException e1) {
				logger.error("checkSign error", e1);
			}
		}
		map.put("userId", request.getUserId());

		StringBuffer sb = new StringBuffer("");
		for (Map.Entry<String, String> e : map.entrySet()) {
			if (e.getValue() != null) {
				sb.append(e.getValue());
			}
		}
		String signKey = getDownStreamKey(request.getUserId());
		if (StringUtils.isEmpty(signKey)) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_SIGN);
			response.setDesc(Constants.ErrorCode.DESC_ERROR_SIGN_NONE);
			return response;
		}
		sb.append(signKey);
		String sign = request.getSign();
		boolean flag = sign.equals(Md5Encrypt.md5(sb.toString()));
		if (flag) {
			response.setStatus(Constants.ErrorCode.SUCCESS);
		} else {
			response.setCode(Constants.ErrorCode.CODE_ERROR_SIGN);
			response.setDesc(Constants.ErrorCode.DESC_ERROR_SIGN);
		}

		return response;
	}

	@Override
	public QueryItemResponse queryItem(QueryItemRequest queryItemRequest) {
		QueryItemResponse response = new QueryItemResponse();
		Integer itemId = Integer.parseInt(queryItemRequest.getItemId());

		Long userId = Long.parseLong(queryItemRequest.getUserId());
		Item item = localCachedService.getItem(itemId);

		if (item == null) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_ITEM);
			response.setDesc("没有该商品,itemId : " + itemId);
			return response;
		}

		ItemData itemData = new ItemData();
		itemData.setId(item.getId());
		itemData.setBizId(item.getBizId());
		itemData.setItemName(item.getItemName());
		itemData.setItemFacePrice(item.getItemFacePrice());
		itemData.setStatus(item.getStatus());
		itemData.setStatusDesc(item.getStatusDesc());

		UserInfo userInfo = localCachedService.getUserInfo(userId);
		if (userInfo == null || userInfo.getAcctId() == null) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_USER);
			response.setDesc(Constants.ErrorCode.DESC_ERROR_USER);
			return response;
		}

		Result<AcctInfo> acctInfoResult = acctService.getAcctInfo(userInfo.getAcctId());
		if (!acctInfoResult.isSuccess()) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_ACCT);
			response.setDesc(acctInfoResult.getResultMsg());
			return response;
		}

		AcctInfo acctInfo = acctInfoResult.getModule();
		Result<Integer> priceResult = itemService.getPriceByAcct(item, acctInfo);
		if (!priceResult.isSuccess()) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_ITEM_PRICE);
			response.setCode(priceResult.getResultMsg());
			return response;
		}
		// 设置供货价格
		itemData.setItemSalesPrice(priceResult.getModule());

		Result<Boolean> itemCheckResult = itemService.checkItemStatus(itemId);
		if (!itemCheckResult.isSuccess()) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_ITEM);
			response.setDesc(itemCheckResult.getResultMsg());
			return response;
		}

		if (!itemCheckResult.getModule()) {
			itemData.setStatus(Constants.Item.STATUS_DOWN);
			itemData.setStatusDesc(itemCheckResult.getResultMsg());
		}
		response.setData(itemData);
		response.setSuccess();
		return response;
	}

	@Override
	public QueryItemListResponse queryItemList(QueryItemListRequest queryItemListRequest) {

		return null;
	}

	@Override
	public BuyResponse telephoneBuy(TelephoneBuyRequest buyRequest) {
		BuyResponse response = new BuyResponse();
		Long userId = Long.parseLong(buyRequest.getUserId());
		Integer itemId = null;
		if (StringUtils.isNotEmpty(buyRequest.getItemId())) {
			itemId = Integer.parseInt(buyRequest.getItemId());
		}
		String uid = buyRequest.getUid().trim();
		String serialno = buyRequest.getSerialno().trim();
		Item item = null;
		if (itemId != null) {
			item = localCachedService.getItem(itemId);
		} else {
			if (StringUtils.isEmpty(buyRequest.getItemFacePrice())) {
				response.setCode(Constants.ErrorCode.CODE_ERROR_VALIDATE);
				response.setDesc("商品编号为空时， 需要带上商品面额");
				return response;
			}
			Integer itemFacePrice = Integer.parseInt(buyRequest.getItemFacePrice());
			Result<MobileBelong> mobileResult = areaInfoService.queryProvinceCodeByMobile(uid);
			if (!mobileResult.isSuccess()) {
				response.setCode(Constants.ErrorCode.CODE_ERROR_PHONE_AREA);
				response.setDesc("手机号归属查询失败。");
				return response;
			}

			MobileBelong mobileBelong = mobileResult.getModule();
			if (mobileBelong == null) {
				response.setCode(Constants.ErrorCode.CODE_ERROR_PHONE_AREA);
				response.setDesc("手机号归属查询失败，未找到该手机号归属地。");
				return response;
			}

			item = localCachedService.getItemForPhone(LocalItemForPhoneCached.getCachedKey(mobileBelong.getProvinceCode(), itemFacePrice, mobileBelong.getCarrierType()));
			if (item == null) {
				// 找不到省域商品找全国商品
				item = localCachedService.getItemForPhone(LocalItemForPhoneCached.getNationwideCachedKey(itemFacePrice, mobileBelong.getCarrierType()));
			}
			if (item == null) {
				logger.warn("serialno: " + serialno + " 查询不到手机号 : " + uid + " 面值 ： " + itemFacePrice + " 对应的商品");
			}
		}

		if (item == null) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_ITEM);
			response.setDesc("商品不存在");
			return response;
		}
		Integer itemPrice = null;

		if (StringUtils.isNotEmpty(buyRequest.getItemPrice())) {
			itemPrice = Integer.parseInt(buyRequest.getItemPrice());
		}

		Date dtCreate = new Date();
		try {
			if (StringUtils.isNotEmpty(buyRequest.getDtCreate())) {
				dtCreate = DateTool.strintToDatetime2(buyRequest.getDtCreate());
			}
		} catch (ParseException e) {
			logger.error("parse dtCreate error", e);
		}

		BizOrder bizOrder = new BizOrder();
		bizOrder.setBizId(item.getBizId());
		bizOrder.setItemPrice(itemPrice);
		bizOrder.setUserId(userId);
		bizOrder.setDownstreamDate(dtCreate);
		bizOrder.setDownstreamSerialno(serialno);
		bizOrder.setAmt(1);
		bizOrder.setChannel(Constants.BizOrder.CHANNEL_SUPPLY);
		bizOrder.setItemId(item.getId());
		bizOrder.setItemUid(uid);
		bizOrder.setDownstreamSupplyWay(Constants.BizOrder.DOWNSTREAM_SUPPLY_WAY_ASYNC);

		response = buyItem(item, bizOrder, false);
		return response;
	}

	/**
	 * 进行商品下单
	 * 
	 * @param item
	 *            商品信息
	 * @param bizOrder
	 *            交易订单信息
	 * @param isSync
	 *            是否同步
	 * @return
	 */
	private BuyResponse buyItem(Item item, BizOrder bizOrder, boolean isSync) {
		BuyResponse response = new BuyResponse();
		String serialno = bizOrder.getDownstreamSerialno();
		// 前置校验
		logger.warn("serialno: " + serialno + " 开始订单前置校验");
		Result<BizOrder> bizOrderPreCheckResult = bizOrderService.bizOrderPreCheck(bizOrder);
		if (!bizOrderPreCheckResult.isSuccess()) {
			response.setCode(bizOrderPreCheckResult.getResultCode());
			response.setDesc(bizOrderPreCheckResult.getResultMsg());
			return response;
		}

		bizOrder = bizOrderPreCheckResult.getModule();

		// 创建订单
		logger.warn("serialno: " + serialno + " 开始创建订单");
		Result<BizOrder> createBizResult = bizOrderService.createBizOrder(bizOrder);

		if (!createBizResult.isSuccess()) {
			response.setCode(createBizResult.getResultCode());
			response.setDesc(createBizResult.getResultMsg());
			return response;
		}

		// 得到返回的订单信息
		bizOrder = createBizResult.getModule();
		logger.warn("serialno: " + serialno + " 创建订单成功 bizOrderId : " + bizOrder.getId());

		// 支付
		logger.warn("serialno: " + serialno + " 开始支付 bizOrderId : " + bizOrder.getId());
		Result<BizOrder> commitPayResult = payService.commitPay(bizOrder);
		if (!commitPayResult.isSuccess()) {
			// 支付失败 将订单改成 失败状态.
			bizOrder.setStatus(Constants.BizOrder.STATUS_FAILED);
			Result<Boolean> updateBizOrderResult = bizOrderService.updateBizOrder(bizOrder);
			if (!updateBizOrderResult.isSuccess()) {
				logger.error("updateBizOrder bizOrderId = " + bizOrder.getId());
			}
			response.setCode(commitPayResult.getResultCode());
			response.setDesc(commitPayResult.getResultMsg());
			return response;
		}

		bizOrder = commitPayResult.getModule();
		logger.warn("serialno: " + serialno + " 支付成功 bizOrderId : " + bizOrder.getId() + " payOrderId : " + bizOrder.getPayOrderId());

		// 支付完成后统一设置成处理中。
		Result<Boolean> chargingBizOrderAndCountingResult = bizOrderService.chargingBizOrderAndCounting(bizOrder);
		if (!chargingBizOrderAndCountingResult.isSuccess()) {
			response.setCode(chargingBizOrderAndCountingResult.getResultCode());
			response.setDesc(chargingBizOrderAndCountingResult.getResultMsg());
			return response;
		}

		// 供货
		if (isSync) {
			logger.warn("serialno: " + serialno + " 开始同步供货  bizOrderId : " + bizOrder.getId());
		} else {
			logger.warn("serialno: " + serialno + " 开始异步供货  bizOrderId : " + bizOrder.getId());
		}
		Result<SupplyOrder> supplyResult = supplyDispatcherService.supply(bizOrder);

		// 根据供货情况，设置订单状态，并更新订单。 如果有供货失败，做退款操作。

		logger.warn("serialno: " + serialno + " 开始处理订单结果 bizOrderId : " + bizOrder.getId());
		SupplyOrder supplyOrder = supplyResult.getModule();
		Result<Boolean> dealResult = new Result<Boolean>();
		if (supplyResult.isSuccess()) {
			if (isSync) {
				logger.warn("serialno: " + serialno + " 开始处理成功供货单 supplyOrderId : " + supplyOrder.getId());
				dealResult = supplyOrderService.comfirmSupplyOrder(supplyOrder);
				if (!dealResult.isSuccess()) {
					// 处理失败，就返回异常。
					logger.warn("serialno: " + serialno + " 处理供货单失败 bizOrderId : " + bizOrder.getId() + " msg : " + dealResult.getResultMsg());
					response.setCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_UNCONFIRM);
					response.setDesc(Constants.ErrorCode.DESC_ERROR_SUPPLY_UNCONFIRM);
					response.setCarrierType(item.getCarrierType() + "");
					response.setItemFacePrice(item.getItemFacePrice() + "");
					response.setItemId(item.getId() + "");
					response.setItemName(item.getItemName());
					response.setPrice(bizOrder.getItemPrice() + "");
					response.setAmount(bizOrder.getAmount() + "");
					response.setAreaCode(bizOrder.getProvinceCode());
					return response;
				}

				logger.warn("serialno: " + serialno + " 开始处理成功订单 bizOrderId : " + bizOrder.getId());
				dealResult = bizOrderService.comfirmBizOrder(bizOrder);
				if (!dealResult.isSuccess()) {

					// 处理失败，就返回异常。
					logger.warn("serialno: " + serialno + " 处理订单失败 bizOrderId : " + bizOrder.getId() + " msg : " + dealResult.getResultMsg());
					response.setCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_UNCONFIRM);
					response.setDesc(Constants.ErrorCode.DESC_ERROR_SUPPLY_UNCONFIRM);
					response.setCarrierType(item.getCarrierType() + "");
					response.setItemFacePrice(item.getItemFacePrice() + "");
					response.setItemId(item.getId() + "");
					response.setItemName(item.getItemName());
					response.setPrice(bizOrder.getItemPrice() + "");
					response.setAmount(bizOrder.getAmount() + "");
					response.setAreaCode(bizOrder.getProvinceCode());
					return response;
				}
			} else {
				logger.warn("serialno: " + serialno + " 供货受理成功  bizOrderId : " + bizOrder.getId());
			}

			response.setSuccess();
			response.setSerialno(serialno);
			response.setBizOrderId(bizOrder.getId() + "");
			response.setCarrierType(item.getCarrierType() + "");
			response.setItemFacePrice(item.getItemFacePrice() + "");
			response.setItemId(item.getId() + "");
			response.setItemName(item.getItemName());
			response.setPrice(bizOrder.getItemPrice() + "");
			response.setAmount(bizOrder.getAmount() + "");
			response.setAreaCode(bizOrder.getProvinceCode());

		} else {
			response.setCode(supplyResult.getResultCode());
			response.setDesc(supplyResult.getResultMsg());
			if (supplyResult.getResultCode() != null && Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD.equals(supplyResult.getResultCode())) {
				response.setDesc("上游供货失败");
			}

			bizOrder.setUpstreamMemo((supplyResult.getResultCode() + "_" + supplyResult.getResultMsg()));

			if (supplyOrder != null) {
				// 设置供货异常的信息
				supplyOrder.setUpstreamMemo(supplyResult.getResultCode() + "_" + supplyResult.getResultMsg());
			}

			if (Constants.ErrorCode.CODE_ERROR_SUPPLY_UNCONFIRM.equals(supplyResult.getResultCode())) {
				// 供货未确认情况下不退款
				// 这里处理结果成功还是失败都统一返回
				logger.warn("serialno: " + serialno + " 开始处理未确认供货单 bizOrderId : " + bizOrder.getId());
				dealResult = supplyOrderService.unComfirmSupplyOrder(supplyOrder);

				if (!dealResult.isSuccess()) {
					logger.warn("serialno: " + serialno + " 处理供货单失败 bizOrderId : " + bizOrder.getId() + " msg : " + dealResult.getResultMsg());
				} else {
					logger.warn("serialno: " + serialno + " 处理供货单成功 bizOrderId : " + bizOrder.getId());
				}

				logger.warn("serialno: " + serialno + " 开始处理未确认订单 bizOrderId : " + bizOrder.getId());
				dealResult = bizOrderService.unComfirmBizOrder(bizOrder);
				if (!dealResult.isSuccess()) {
					logger.warn("serialno: " + serialno + " 处理订单失败 bizOrderId : " + bizOrder.getId() + " msg : " + dealResult.getResultMsg());
				} else {
					logger.warn("serialno: " + serialno + " 处理订单成功 bizOrderId : " + bizOrder.getId());
				}

				bizOrder.setStatus(Constants.BizOrder.STATUS_UNCONFIRMED);
				response.setCarrierType(item.getCarrierType() + "");
				response.setItemFacePrice(item.getItemFacePrice() + "");
				response.setItemId(item.getId() + "");
				response.setItemName(item.getItemName());
				response.setPrice(bizOrder.getItemPrice() + "");
				response.setAmount(bizOrder.getAmount() + "");
				response.setAreaCode(bizOrder.getProvinceCode());

				return response;
			} else {
				if (supplyOrder != null) {
					logger.warn("serialno: " + serialno + "  开始处理失败供货单 bizOrderId : " + bizOrder.getId());
					supplyOrder.setFinalType(Constants.SupplyOrder.FINAL_TYPE_YES);
					dealResult = supplyOrderService.cancelSupplyOrder(supplyOrder);
				}

				logger.warn("serialno: " + serialno + "  开始处理失败退款订单 bizOrderId : " + bizOrder.getId());

				// 这里不做补充供货了，这里失败要么就是同步，就直接取消订单。
				dealResult = bizOrderService.cancelBizOrder(bizOrder);
			}

		}
		logger.warn("serialno: " + serialno + " 处理订单成功 bizOrderId : " + bizOrder.getId());

		return response;
	}

	@Override
	public Result<BizOrder> buyFromWeb(BuyItem buyItem) {
		Result<BizOrder> result = new Result<BizOrder>();
		boolean isSync = (buyItem.getSupplyWay() == Constants.BizOrder.DOWNSTREAM_SUPPLY_WAY_SYNC);

		Long userId = buyItem.getUserId();
		Integer itemId = buyItem.getItemId();
		String uid = buyItem.getUid();
		Integer amt = buyItem.getAmt();

		// 是否做itemPrice Check

		Item item = localCachedService.getItem(itemId);
		if (item == null) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_ITEM);
			result.setResultMsg("商品不存在");
			return result;
		}

		if (isSync && (item.getCanSync() == null || !item.getCanSync())) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_ITEM);
			result.setResultMsg("该商品不支持同步供货");
			return result;
		}

		BizOrder bizOrder = new BizOrder();
		bizOrder.setBizId(item.getBizId());
		bizOrder.setUserId(userId);
		bizOrder.setDownstreamDate(new Date());
		bizOrder.setAmt(amt);
		bizOrder.setChannel(Constants.BizOrder.CHANNEL_WEB);
		bizOrder.setItemId(itemId);
		bizOrder.setItemUid(uid);
		bizOrder.setDownstreamSupplyWay(buyItem.getSupplyWay());

		BuyResponse response = buyItem(item, bizOrder, isSync);
		result = pasreResult(response);
		if (bizOrder.getId() != null) {
			result.setModule(bizOrder);
		}
		return result;
	}

	private Result<BizOrder> pasreResult(BuyResponse buyResponse) {
		Result<BizOrder> result = new Result<BizOrder>();
		if (!buyResponse.isSuccess()) {
			if (Constants.ErrorCode.CODE_ERROR_SUPPLY_UNCONFIRM.equals(result.getResultCode())) {
				result.setSuccess(true);
				result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_UNCONFIRM);
				return result;
			}
			result.setResultMsg(buyResponse.getDesc());
			result.setResultCode(buyResponse.getCode());
			return result;
		}

		result.setSuccess(true);
		return result;
	}

	@Override
	public Result<BizOrder> buyPhoneFromWeb(BuyFacePrice buyFacePrice) {
		Result<BizOrder> result = new Result<BizOrder>();
		try {
			boolean isSync = false;

			Long userId = buyFacePrice.getUserId();
			String uid = buyFacePrice.getUid();
			Integer amt = 1;
			if (!StringUtils.isNumeric(buyFacePrice.getItemFacePrice())) {
				result.setResultCode(Constants.ErrorCode.CODE_ERROR_VALIDATE);
				result.setResultMsg("商品面额必须数字");
				return result;
			}
			Integer itemFacePrice = Integer.parseInt(buyFacePrice.getItemFacePrice());
			Result<MobileBelong> mobileResult = areaInfoService.queryProvinceCodeByMobile(uid);
			if (!mobileResult.isSuccess()) {
				result.setResultCode(Constants.ErrorCode.CODE_ERROR_PHONE_AREA);
				result.setResultMsg("手机号归属查询失败。");
				return result;
			}

			MobileBelong mobileBelong = mobileResult.getModule();
			if (mobileBelong == null) {
				result.setResultCode(Constants.ErrorCode.CODE_ERROR_PHONE_AREA);
				result.setResultMsg("手机号归属查询失败，未找到该手机号归属地。");
				return result;
			}

			Item item = localCachedService.getItemForPhone(LocalItemForPhoneCached.getCachedKey(mobileBelong.getProvinceCode(), itemFacePrice, mobileBelong.getCarrierType()));
			if (item == null) {
				// 找不到省域商品找全国商品
				item = localCachedService.getItemForPhone(LocalItemForPhoneCached.getNationwideCachedKey(itemFacePrice, mobileBelong.getCarrierType()));
			}

			if (item == null) {
				logger.warn(" 查询不到手机号 : " + uid + " 面值 ： " + itemFacePrice + " 对应的商品");
				result.setResultCode(Constants.ErrorCode.CODE_ERROR_ITEM);
				result.setResultMsg("找不到对应商品");
				return result;
			}

			BizOrder bizOrder = new BizOrder();
			bizOrder.setBizId(item.getBizId());
			bizOrder.setUserId(userId);
			bizOrder.setDownstreamDate(new Date());
			bizOrder.setAmt(amt);
			bizOrder.setChannel(Constants.BizOrder.CHANNEL_WEB);
			bizOrder.setItemId(item.getId());
			bizOrder.setItemUid(uid);
			bizOrder.setDownstreamSupplyWay(buyFacePrice.getSupplyWay());

			bizOrder.setItemExt5(buyFacePrice.getOtherSerialNO());

			BuyResponse response = buyItem(item, bizOrder, isSync);
			result = pasreResult(response);
			if (bizOrder.getId() != null) {
				result.setModule(bizOrder);
			}
		} catch (Exception e) {
			logger.error(buyFacePrice.toString(), e);
		}
		return result;
	}

	@Override
	public Result<BizOrder> buyFlowFromWeb(BuyFacePrice buyFacePrice) {
		Result<BizOrder> result = new Result<BizOrder>();
		try {
			Long userId = buyFacePrice.getUserId();
			String uid = buyFacePrice.getUid();
			Integer amt = 1;
			if (!StringUtils.isNumeric(buyFacePrice.getItemFacePrice())) {
				result.setResultCode(Constants.ErrorCode.CODE_ERROR_VALIDATE);
				result.setResultMsg("商品面额必须数字");
				return result;
			}
			Result<MobileBelong> mobileResult = areaInfoService.queryProvinceCodeByMobile(uid);
			if (!mobileResult.isSuccess()) {
				result.setResultCode(Constants.ErrorCode.CODE_ERROR_PHONE_AREA);
				result.setResultMsg("手机号归属查询失败。");
				return result;
			}

			MobileBelong mobileBelong = mobileResult.getModule();
			if (mobileBelong == null) {
				result.setResultCode(Constants.ErrorCode.CODE_ERROR_PHONE_AREA);
				result.setResultMsg("手机号归属查询失败，未找到该手机号归属地。");
				return result;
			}

			Item item = null;
			int flowType = buyFacePrice.getType();
			List<Item> itemList = null;
			if (flowType == Constants.BizFlow.ITEM_USABLE_AREA) {
				itemList = localCachedService.getItemListForFlow(LocalItemListForFlowCached.getCachedUseAreaKey(mobileBelong.getProvinceCode(), mobileBelong.getCarrierType()));
			} else if (flowType == Constants.BizFlow.ITEM_USABLE_NATIONWIDE) {
				itemList = localCachedService.getItemListForFlow(LocalItemListForFlowCached.getCachedUseNationwideKey(mobileBelong.getProvinceCode(), mobileBelong.getCarrierType()));
			}

			UserInfo userInfo = localCachedService.getUserInfo(userId);
			if (userInfo == null) {
				result.setResultCode(Constants.ErrorCode.CODE_ERROR_USER);
				result.setResultMsg(Constants.ErrorCode.DESC_ERROR_USER);
				return result;
			}

			AcctInfo acctInfo = localCachedService.getAcctInfoNot4Trade(userInfo.getAcctId());
			if (acctInfo == null) {
				result.setResultCode(Constants.ErrorCode.CODE_ERROR_ACCT);
				result.setResultMsg(Constants.ErrorCode.DESC_ERROR_ACCT);
				return result;
			}

			String itemFace = "";

			if (Integer.parseInt(buyFacePrice.getItemFacePrice()) < 1024) {
				itemFace = buyFacePrice.getItemFacePrice().trim() + "m";
			} else {
				itemFace = Integer.parseInt(buyFacePrice.getItemFacePrice()) / 1024 + "g";
			}

			if (itemList != null && itemList.size() > 0) {
				item = filterItemFromListByItemFace(itemList, itemFace, acctInfo);
			}

			if (item == null) {
				logger.warn(String.format("查询不到手机号 : %s 流量 ： %s 类型： %s 对应的商品", uid, buyFacePrice.getItemFacePrice(), buyFacePrice.getType()));
				result.setResultCode(Constants.ErrorCode.CODE_ERROR_ITEM);
				result.setResultMsg("找不到对应商品");
				return result;
			}

			BizOrder bizOrder = new BizOrder();
			bizOrder.setBizId(item.getBizId());
			bizOrder.setUserId(userId);
			bizOrder.setDownstreamDate(new Date());
			bizOrder.setAmt(amt);
			bizOrder.setChannel(Constants.BizOrder.CHANNEL_WEB);
			bizOrder.setItemId(item.getId());
			bizOrder.setItemUid(uid);
			bizOrder.setDownstreamSupplyWay(buyFacePrice.getSupplyWay());

			bizOrder.setItemExt5(buyFacePrice.getOtherSerialNO());

			BuyResponse response = buyItem(item, bizOrder, false);
			result = pasreResult(response);
			if (bizOrder.getId() != null) {
				result.setModule(bizOrder);
			}
		} catch (Exception e) {
			logger.error(buyFacePrice.toString(), e);
		}
		return result;
	}

	@Override
	public Result<PhoneQuery> queryPhoneItem(String uid, AcctInfo acctInfo) {
		Result<PhoneQuery> result = new Result<PhoneQuery>();
		Result<MobileBelong> mobileResult = areaInfoService.queryProvinceCodeByMobile(uid);
		System.out.println(mobileResult+"========");
		logger.error("--->" + mobileResult);
		if (!mobileResult.isSuccess()) {
			result.setResultMsg("手机号归属查询失败。");
			return result;
		}

		MobileBelong mobileBelong = mobileResult.getModule();
		if (mobileBelong == null) {
			result.setResultMsg("未找到该手机号归属地。");
			return result;
		}

		PhoneQuery phoneQuery = new PhoneQuery();
		phoneQuery.setMobileBelong(mobileBelong);

		List<Item> itemList = localCachedService.getItemListForPhone(LocalItemListForPhoneCached.getCachedKey(mobileBelong.getProvinceCode(), mobileBelong.getCarrierType()));

		Map<Integer, ItemForQuery> map = new HashMap<Integer, ItemForQuery>();
		if (itemList != null) {
			for (Item item : itemList) {
				if (item.getItemFacePrice() != null) {
					ItemForQuery itemForQuery = parseItemToItemForQuery(item, acctInfo);
					if (itemForQuery != null) {
						map.put(item.getItemFacePrice(), itemForQuery);
					}
				}
			}
		}
		phoneQuery.setItemMap(map);
		result.setSuccess(true);
		result.setModule(phoneQuery);

		return result;
	}

	private Item filterItemFromListByItemFace(List<Item> itemList, String itemFace, AcctInfo acctInfo) {
		if (itemList == null || itemFace == null) {
			return null;
		}
		Item item = null;
		for (Item i : itemList) {
			if (StringUtils.isNotEmpty(i.getItemExt1()) && i.getItemExt1().trim().equalsIgnoreCase(itemFace)) {
				Result<Integer> priceResult = itemService.getPriceByAcct(i, acctInfo);
				Integer salesPrice = priceResult.getModule();
				if (salesPrice == null) {
					// 价格为空
					continue;
				}
				if (item == null) {
					item = i;
					continue;
				}

				Config config = localConfigCached.get(Constants.Config.KEY_SUPPLY_FLOW_CHOOSE);
				if (config == null || config.getConfigValue().equals(Constants.Config.VALUE_SUPPLY_FLOW_CHOOSE_AREA)) {
					if (!i.isNationwide()) {
						// 覆盖
						Result<Integer> oldPriceResult = itemService.getPriceByAcct(item, acctInfo);
						Integer oldPrice = oldPriceResult.getModule();
						if (item.isNationwide() || oldPrice > salesPrice) {
							item = i;
						}
					} else {
						Result<Integer> oldPriceResult = itemService.getPriceByAcct(item, acctInfo);
						Integer oldPrice = oldPriceResult.getModule();
						if (item.isNationwide() && oldPrice > salesPrice) {
							item = i;
						}
					}
				} else {
					if (i.isNationwide()) {
						// 覆盖
						Result<Integer> oldPriceResult = itemService.getPriceByAcct(item, acctInfo);
						Integer oldPrice = oldPriceResult.getModule();
						if (!item.isNationwide() || oldPrice > salesPrice) {
							item = i;
						}
					} else {
						Result<Integer> oldPriceResult = itemService.getPriceByAcct(item, acctInfo);
						Integer oldPrice = oldPriceResult.getModule();
						if (!item.isNationwide() && oldPrice > salesPrice) {
							item = i;
						}
					}
				}
			}
		}
		return item;
	}

	private ItemForQuery parseItemToItemForQuery(Item item, AcctInfo acctInfo) {
		if (item == null) {
			return null;
		}
		ItemForQuery itemForQuery = new ItemForQuery();

		itemForQuery.setBizId(item.getBizId());
		itemForQuery.setCarrierType(item.getCarrierType());
		itemForQuery.setId(item.getId());
		itemForQuery.setItemFacePrice(item.getItemFacePrice());
		itemForQuery.setItemName(item.getItemName());
		Result<Integer> priceResult = itemService.getPriceByAcct(item, acctInfo);
		Integer salesPrice = priceResult.getModule();
		if (salesPrice == null) {
			return null;
		}
		itemForQuery.setItemSalesPrice(salesPrice);
		itemForQuery.setItemType(item.getItemType());
		itemForQuery.setItemUnit(item.getItemUnit());
		itemForQuery.setStatus(item.getStatus());
		itemForQuery.initPriceDesc();
		return itemForQuery;
	}

	@Override
	public Result<FlowQuery> queryFlowItem(String uid, Integer useableType, AcctInfo acctInfo) {
		Result<FlowQuery> result = new Result<FlowQuery>();
		Result<MobileBelong> mobileResult = areaInfoService.queryProvinceCodeByMobile(uid);
		if (!mobileResult.isSuccess()) {
			result.setResultMsg("手机号归属查询失败。");
			return result;
		}

		MobileBelong mobileBelong = mobileResult.getModule();
		if (mobileBelong == null) {
			result.setResultMsg("未找到该手机号归属地。");
			return result;
		}

		FlowQuery flowQuery = new FlowQuery();
		flowQuery.setMobileBelong(mobileBelong);

		List<Item> itemList = new ArrayList<Item>();
		if (useableType == Constants.BizFlow.ITEM_USABLE_NATIONWIDE) {
			itemList = localCachedService.getItemListForFlow(LocalItemListForFlowCached.getCachedUseNationwideKey(mobileBelong.getProvinceCode(), mobileBelong.getCarrierType()));
		} else {
			itemList = localCachedService.getItemListForFlow(LocalItemListForFlowCached.getCachedUseAreaKey(mobileBelong.getProvinceCode(), mobileBelong.getCarrierType()));
		}

		Map<String, ItemForQuery> map = new HashMap<String, ItemForQuery>();
		if (itemList != null) {
			for (Item item : itemList) {
				if (StringUtils.isNotBlank(item.getItemExt1())) {
					Result<Integer> priceResult = itemService.getPriceByAcct(item, acctInfo);
					Integer salesPrice = priceResult.getModule();
					if (salesPrice == null) {
						continue;
					}
					ItemForQuery itemForQuery = parseItemToItemForQuery(item, acctInfo);
					if (itemForQuery == null) {
						continue;
					}
					itemForQuery.setItemSalesPrice(salesPrice);
					if (map.containsKey(item.getItemExt1())) {
						ItemForQuery exsitsItemForQuery = map.get(item.getItemExt1());
						if (exsitsItemForQuery.getItemSalesPrice() > itemForQuery.getItemSalesPrice()) {
							map.put(item.getItemExt1(), itemForQuery);
						}
					} else {
						map.put(item.getItemExt1(), itemForQuery);
					}
				}
			}
		}
		flowQuery.setItemMap(map);
		result.setSuccess(true);
		result.setModule(flowQuery);

		return result;
	}

	@Override
	public BuyResponse buyGame(GameBuyRequest buyRequest, Integer supplyType) {
		boolean isSync = (supplyType == Constants.BizOrder.DOWNSTREAM_SUPPLY_WAY_SYNC);
		BuyResponse response = new BuyResponse();
		Long userId = Long.parseLong(buyRequest.getUserId());
		Integer itemId = Integer.parseInt(buyRequest.getItemId());
		String uid = buyRequest.getUid().trim();
		String serialno = buyRequest.getSerialno().trim();
		Integer itemPrice = null;

		if (StringUtils.isNotEmpty(buyRequest.getItemPrice())) {
			itemPrice = Integer.parseInt(buyRequest.getItemPrice());
		}

		Date dtCreate = new Date();
		try {
			if (StringUtils.isNotEmpty(buyRequest.getDtCreate())) {
				dtCreate = DateTool.strintToDatetime2(buyRequest.getDtCreate());
			}
		} catch (ParseException e) {
			logger.error("parse dtCreate error", e);
		}

		Item item = localCachedService.getItem(itemId);
		if (item == null) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_ITEM);
			response.setDesc("商品不存在");
			return response;
		}

		if (isSync && (item.getCanSync() == null || !item.getCanSync())) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_ITEM);
			response.setDesc("该商品不支持同步供货");
			return response;
		}

		BizOrder bizOrder = new BizOrder();
		bizOrder.setBizId(item.getBizId());
		bizOrder.setItemPrice(itemPrice);
		bizOrder.setUserId(userId);
		bizOrder.setDownstreamDate(dtCreate);
		bizOrder.setDownstreamSerialno(serialno);
		bizOrder.setAmt(Integer.parseInt(buyRequest.getAmt()));
		bizOrder.setChannel(Constants.BizOrder.CHANNEL_SUPPLY);
		bizOrder.setItemId(itemId);
		bizOrder.setItemUid(uid);
		bizOrder.setItemExt1(buyRequest.getExt1());
		bizOrder.setItemExt2(buyRequest.getExt2());
		bizOrder.setItemExt3(buyRequest.getExt3());

		if (isSync) {
			bizOrder.setDownstreamSupplyWay(Constants.BizOrder.DOWNSTREAM_SUPPLY_WAY_SYNC);
		} else {
			bizOrder.setDownstreamSupplyWay(Constants.BizOrder.DOWNSTREAM_SUPPLY_WAY_ASYNC);
		}

		response = buyItem(item, bizOrder, isSync);
		return response;
	}

	@Override
	public QueryGameCarrierResponse queryGameCarrier(QueryGameCarrierRequest queryGameCarrierRequest) {
		QueryGameCarrierResponse response = new QueryGameCarrierResponse();
		GameCarrierQuery gameCarrierQuery = new GameCarrierQuery();
		if (queryGameCarrierRequest.getGameCarrierId() != null) {
			gameCarrierQuery.setId(Long.parseLong(queryGameCarrierRequest.getGameCarrierId()));
		}
		Result<List<GameCarrier>> result = gameCarrierService.queryGameCarrierList(gameCarrierQuery);
		if (!result.isSuccess()) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_GAME_QUERY);
			response.setDesc(Constants.ErrorCode.DESC_ERROR_GAME_QUERY);
			return response;
		}
		List<GameCarrier> list = result.getModule();

		List<Object> gameCarrierList = new ArrayList<Object>();
		for (GameCarrier gameCarrier : list) {
			GameCarrierData data = new GameCarrierData();
			data.setCarrierDesc(gameCarrier.getCarrierDesc());
			data.setCarrierName(gameCarrier.getCarrierName());
			data.setId(gameCarrier.getId());
			data.setStatus(gameCarrier.getStatus());
			gameCarrierList.add(data);
		}

		ListData listData = new ListData();
		listData.setData(gameCarrierList);

		response.setDatas(listData);
		response.setSuccess();
		return response;
	}

	@Override
	public QueryGameListResponse queryGameList(QueryGameListRequest queryGameListRequest) {
		QueryGameListResponse response = new QueryGameListResponse();
		GameQuery gameQuery = new GameQuery();
		gameQuery.setPageSize(2000);
		gameQuery.setCarrierId(Long.parseLong(queryGameListRequest.getGameCarrierId()));
		Result<List<Game>> result = gameService.queryGameList(gameQuery);
		if (!result.isSuccess()) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_GAME_QUERY);
			response.setDesc(Constants.ErrorCode.DESC_ERROR_GAME_QUERY);
			return response;
		}

		List<Game> list = result.getModule();

		List<Object> gameList = new ArrayList<Object>();
		for (Game game : list) {
			GameData gameData = new GameData();
			gameData.setCarrierId(game.getCarrierId());
			gameData.setCarrierName(game.getCarrierName());
			gameData.setGameDesc(game.getGameDesc());
			gameData.setGameName(game.getGameName());
			gameData.setOfficialUrl(game.getOfficialUrl());
			gameData.setId(game.getId());
			gameData.setKeyIndex(game.getKeyIndex());
			gameData.setStatus(game.getStatus());

			gameList.add(gameData);
		}

		ListData datas = new ListData();
		datas.setData(gameList);

		response.setSuccess();
		response.setDatas(datas);

		return response;
	}

	@Override
	public QueryGameResponse queryGame(QueryGameRequest queryGameRequest) {
		QueryGameResponse response = new QueryGameResponse();

		Result<Game> result = gameService.getGame(Long.parseLong(queryGameRequest.getGameId()));
		if (!result.isSuccess()) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_GAME_QUERY);
			response.setDesc(Constants.ErrorCode.DESC_ERROR_GAME_QUERY);
			return response;
		}

		Game game = result.getModule();
		GameData gameData = new GameData();
		if (game != null) {
			gameData.setCarrierId(game.getCarrierId());
			gameData.setCarrierName(game.getCarrierName());
			gameData.setGameDesc(game.getGameDesc());
			gameData.setGameName(game.getGameName());
			gameData.setId(game.getId());
			gameData.setKeyIndex(game.getKeyIndex());
			gameData.setOfficialUrl(game.getOfficialUrl());
			gameData.setStatus(game.getStatus());
		}

		response.setSuccess();
		response.setData(gameData);
		return response;
	}

	@Override
	public QueryGameItemListResponse queryGameItemList(QueryGameItemListRequest queryGameItemListRequest) {
		QueryGameItemListResponse response = new QueryGameItemListResponse();
		ItemQuery itemQuery = new ItemQuery();
		itemQuery.setItemCategoryId(Integer.parseInt(queryGameItemListRequest.getGameId()));
		itemQuery.setPageSize(2000);
		itemQuery.setBizId(Constants.BizInfo.CODE_GAME);
		Result<List<Item>> result = itemService.queryItemList(itemQuery);
		if (!result.isSuccess()) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_GAME_QUERY);
			response.setDesc(Constants.ErrorCode.DESC_ERROR_GAME_QUERY);
			return response;
		}

		UserInfo userInfo = localCachedService.getUserInfo(Long.parseLong(queryGameItemListRequest.getUserId()));
		if (userInfo == null) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_USER);
			response.setDesc(Constants.ErrorCode.DESC_ERROR_USER);
			return response;
		}

		AcctInfo acctInfo = localCachedService.getAcctInfoNot4Trade(userInfo.getAcctId());
		if (acctInfo == null) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_ACCT);
			response.setDesc(Constants.ErrorCode.DESC_ERROR_ACCT);
			return response;
		}

		List<Item> list = result.getModule();
		List<Object> itemList = new ArrayList<Object>();
		for (Item item : list) {
			GameItemData gameItemData = new GameItemData();
			gameItemData.setBizId(item.getBizId());
			gameItemData.setExchargeRatio(item.getExchargeRatio() + "");
			gameItemData.setGameId(item.getItemCategoryId());
			gameItemData.setId(item.getId());
			gameItemData.setItemFacePrice(item.getItemFacePrice());
			gameItemData.setItemName(item.getItemName());
			Result<Integer> getPriceByAcctResult = itemService.getPriceByAcct(item, acctInfo);
			if (getPriceByAcctResult != null) {
				gameItemData.setItemSalesPrice(getPriceByAcctResult.getModule());
			}
			itemList.add(gameItemData);
		}

		ListData datas = new ListData();
		datas.setData(itemList);

		response.setSuccess();
		response.setDatas(datas);
		return response;
	}

	@Override
	public QueryGameItemResponse queryGameItem(QueryGameItemRequest queryGameItemRequest) {
		QueryGameItemResponse response = new QueryGameItemResponse();
		Result<Item> result = itemService.getItem(Integer.parseInt(queryGameItemRequest.getItemId()));
		if (!result.isSuccess()) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_GAME_QUERY);
			response.setDesc(Constants.ErrorCode.DESC_ERROR_GAME_QUERY);
			return response;
		}

		UserInfo userInfo = localCachedService.getUserInfo(Long.parseLong(queryGameItemRequest.getUserId()));
		if (userInfo == null) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_USER);
			response.setDesc(Constants.ErrorCode.DESC_ERROR_USER);
			return response;
		}

		AcctInfo acctInfo = localCachedService.getAcctInfoNot4Trade(userInfo.getAcctId());
		if (acctInfo == null) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_ACCT);
			response.setDesc(Constants.ErrorCode.DESC_ERROR_ACCT);
			return response;
		}

		Item item = result.getModule();
		GameItemData gameItemData = new GameItemData();

		if (item != null && item.getBizId().equals(Constants.BizInfo.CODE_GAME)) {
			gameItemData.setBizId(item.getBizId());
			gameItemData.setExchargeRatio(item.getExchargeRatio() + "");
			gameItemData.setGameId(item.getItemCategoryId());
			gameItemData.setId(item.getId());
			gameItemData.setItemFacePrice(item.getItemFacePrice());
			gameItemData.setItemName(item.getItemName());
			Result<Integer> getPriceByAcctResult = itemService.getPriceByAcct(item, acctInfo);
			if (getPriceByAcctResult != null) {
				gameItemData.setItemSalesPrice(getPriceByAcctResult.getModule());
			}
			gameItemData.setItemUnit(item.getItemUnit());
			gameItemData.setNumberList(item.getNumberList());
			gameItemData.setStatus(item.getStatus());
		}

		response.setSuccess();
		response.setData(gameItemData);
		return response;
	}

	@Override
	public BuyResponse flowBuy(FlowBuyRequest buyRequest) {
		BuyResponse response = new BuyResponse();
		Long userId = Long.parseLong(buyRequest.getUserId());
		String uid = buyRequest.getUid().trim();
		String serialno = buyRequest.getSerialno().trim();
		Item item = null;

		Result<MobileBelong> mobileResult = areaInfoService.queryProvinceCodeByMobile(uid);
		if (!mobileResult.isSuccess()) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_PHONE_AREA);
			response.setDesc("手机号归属查询失败。");
			return response;
		}

		MobileBelong mobileBelong = mobileResult.getModule();
		if (mobileBelong == null) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_PHONE_AREA);
			response.setDesc("手机号归属查询失败，未找到该手机号归属地。");
			return response;
		}
		int flowType = Integer.parseInt(buyRequest.getFlowType());
		List<Item> itemList = null;
		if (flowType == Constants.BizFlow.ITEM_USABLE_AREA) {
			itemList = localCachedService.getItemListForFlow(LocalItemListForFlowCached.getCachedUseAreaKey(mobileBelong.getProvinceCode(), mobileBelong.getCarrierType()));
		} else if (flowType == Constants.BizFlow.ITEM_USABLE_NATIONWIDE) {
			itemList = localCachedService.getItemListForFlow(LocalItemListForFlowCached.getCachedUseNationwideKey(mobileBelong.getProvinceCode(), mobileBelong.getCarrierType()));
		}

		UserInfo userInfo = localCachedService.getUserInfo(userId);
		if (userInfo == null) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_USER);
			response.setDesc(Constants.ErrorCode.DESC_ERROR_USER);
			return response;
		}

		AcctInfo acctInfo = localCachedService.getAcctInfoNot4Trade(userInfo.getAcctId());
		if (acctInfo == null) {
			response.setCode(Constants.ErrorCode.CODE_ERROR_ACCT);
			response.setDesc(Constants.ErrorCode.DESC_ERROR_ACCT);
			return response;
		}

		if (itemList != null && itemList.size() > 0) {
			item = filterItemFromListByItemFace(itemList, buyRequest.getFlow().trim(), acctInfo);
		}

		if (item == null) {
			logger.warn(String.format("serialno: %s 查询不到手机号 : %s 流量 ： %s 类型： %s 对应的商品", serialno, uid, buyRequest.getFlow(), buyRequest.getFlowType()));
			response.setCode(Constants.ErrorCode.CODE_ERROR_ITEM);
			response.setDesc("商品不存在");
			return response;
		}

		Date dtCreate = new Date();
		try {
			if (StringUtils.isNotEmpty(buyRequest.getDtCreate())) {
				dtCreate = DateTool.strintToDatetime2(buyRequest.getDtCreate());
			}
		} catch (ParseException e) {
			// 不影响主流程
			logger.error("parse dtCreate error", e);
		}

		BizOrder bizOrder = new BizOrder();
		bizOrder.setBizId(item.getBizId());
		bizOrder.setUserId(userId);
		bizOrder.setDownstreamDate(dtCreate);
		bizOrder.setDownstreamSerialno(serialno);
		bizOrder.setAmt(1);
		bizOrder.setChannel(Constants.BizOrder.CHANNEL_SUPPLY);
		bizOrder.setItemId(item.getId());
		bizOrder.setItemUid(uid);
		bizOrder.setDownstreamSupplyWay(Constants.BizOrder.DOWNSTREAM_SUPPLY_WAY_ASYNC);
		response = buyItem(item, bizOrder, false);
		return response;
	}

}
