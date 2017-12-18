package com.longan.biz.core.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.cached.MemcachedService;
import com.longan.biz.core.AcctService;
import com.longan.biz.core.AreaInfoService;
import com.longan.biz.core.BaseService;
import com.longan.biz.core.BizOrderService;
import com.longan.biz.core.ChargingLimitService;
import com.longan.biz.core.ItemService;
import com.longan.biz.core.PayService;
import com.longan.biz.core.StockService;
import com.longan.biz.core.UserService;
import com.longan.biz.dao.BizOrderDAO;
import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.BizOrderExample;
import com.longan.biz.dataobject.BizOrderQuery;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.MobileBelong;
import com.longan.biz.dataobject.RefundOrder;
import com.longan.biz.dataobject.TraderInfo;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.OperationVO;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;
import com.longan.biz.utils.SwitchConstant;
import com.longan.biz.utils.Utils;

public class BizOrderServiceImpl extends BaseService implements BizOrderService {

	@Resource
	private BizOrderDAO bizOrderDAO;

	@Resource
	private ItemService itemService;

	@Resource
	private LocalCachedService localCachedService;

	@Resource
	private UserService userService;

	@Resource
	private AcctService acctService;

	@Resource
	private MemcachedClient memcachedClient;

	@Resource
	private AreaInfoService areaInfoService;

	@Resource
	private StockService stockService;

	@Resource
	private PayService payService;

	@Resource
	private ChargingLimitService chargingLimitService;

	@Resource
	private MemcachedService memcachedService;

	public Result<BizOrder> bizOrderPreCheck(BizOrder bizOrder) {
		Result<BizOrder> result = new Result<BizOrder>();

		Result<Boolean> checkBizOrderResult = checkBizOrder(bizOrder);

		if (!checkBizOrderResult.isSuccess() || !checkBizOrderResult.getModule()) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_BIZORDER);
			result.setResultMsg(checkBizOrderResult.getResultMsg());
			return result;
		}
		result.setModule(bizOrder);

		// 用户和账户相关查询和校验

		UserInfo userInfo = localCachedService.getUserInfo(bizOrder.getUserId());
		if (userInfo == null || !userInfo.canUserTrade()) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_USER);
			result.setResultMsg(Constants.ErrorCode.DESC_ERROR_USER);
			return result;
		}

		Result<Boolean> checkUserInfo = userService.checkUserInfo(userInfo);
		if (!checkUserInfo.isSuccess()) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_USER);
			result.setResultMsg(checkUserInfo.getResultMsg());
			return result;
		}

		Result<AcctInfo> acctInfoResult = acctService.getAcctInfo(userInfo.getAcctId());
		if (!acctInfoResult.isSuccess()) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_ACCT);
			result.setResultMsg(acctInfoResult.getResultMsg());
			return result;
		}

		AcctInfo acctInfo = acctInfoResult.getModule();
		Result<Boolean> acctCheckResult = acctService.checkAcct(acctInfo);
		if (!acctCheckResult.isSuccess()) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_ACCT);
			result.setResultMsg(acctCheckResult.getResultMsg());
			return result;
		}

		// 获取商品
		Result<Item> itemResult = itemService.getItem(bizOrder.getItemId());
		if (!itemResult.isSuccess()) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_ITEM);
			result.setResultMsg(itemResult.getResultMsg());
			return result;
		}

		Item item = itemResult.getModule();

		if (item.getBizId() == null) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_ITEM);
			result.setResultMsg("商品业务编号是空");
			return result;
		}

		// 权限判断
		if (userInfo.isDownStreamUser()) { // 非接口用户不做权限判断
			Set<Integer> bizSet = localCachedService.getAuthBizByUserId(userInfo.getId());
			if (bizSet == null || !bizSet.contains(item.getBizId())) {
				result.setResultCode(Constants.ErrorCode.CODE_ERROR_AUTH);
				result.setResultMsg(Constants.ErrorCode.DESC_ERROR_AUTH);
				return result;
			}
		}

		bizOrder.setBizId(item.getBizId());

		// 重复流水号校验
		Result<Boolean> checkDownStreamSerialno = checkDownstreamSerialno(bizOrder);
		if (!checkDownStreamSerialno.isSuccess() || !checkDownStreamSerialno.getModule()) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_SERIALNO);
			result.setResultMsg(checkDownStreamSerialno.getResultMsg());
			return result;
		}

		// 充值手机号码查询
		Result<Boolean> checkUidResult = checkUid(bizOrder);
		if (!checkUidResult.isSuccess()) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_PHONE_AREA);
			result.setResultMsg(checkUidResult.getResultMsg());
			return result;
		}

		// 余额校验
		Result<Boolean> checkBalanceAcct = acctService.checkBalanceAcct(acctInfo, item,
				bizOrder.getAmt());
		if (!checkBalanceAcct.isSuccess() || !checkBalanceAcct.getModule()) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_BALANCE);
			result.setResultMsg(checkBalanceAcct.getResultMsg());
			return result;
		}

		// 传入如果有itemPrice的时候，直接设置价格，之后会校验,没有则获取商品价格。
		if (bizOrder.getItemPrice() == null) {
			Result<Integer> priceResult = itemService.getPriceByAcct(item, acctInfo);
			if (!priceResult.isSuccess()) {
				result.setResultCode(Constants.ErrorCode.CODE_ERROR_ITEM_PRICE);
				result.setResultMsg(priceResult.getResultMsg());
				return result;
			}
			bizOrder.setItemPrice(priceResult.getModule());
		}

		// 设置bizOrder
		bizOrder.setItemFacePrice(item.getItemFacePrice());
		bizOrder.setItemName(item.getItemName());
		bizOrder.setAmount(Long.parseLong(bizOrder.getItemPrice().toString()) * bizOrder.getAmt());
		bizOrder.setStatus(Constants.BizOrder.STATUS_INIT);

		// 检查商品以及供货商品
		Result<Boolean> checkItemResult = itemService.checkItem(bizOrder);
		if (!checkItemResult.isSuccess() || !checkItemResult.getModule()) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_ITEM);
			result.setResultMsg(checkItemResult.getResultMsg());
			return result;
		}

		// 充值用户最大限额校验
		Result<Boolean> checkUidAmountResult = checkUidCount(bizOrder);
		if (!checkUidAmountResult.isSuccess() || !checkUidAmountResult.getModule()) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_UID_AMOUNT);
			result.setResultMsg(checkUidAmountResult.getResultMsg());
			return result;
		}

		result.setSuccess(true);
		return result;
	}

	@Override
	public Result<BizOrder> createBizOrder(BizOrder bizOrder) {
		Result<BizOrder> result = new Result<BizOrder>();
		result.setModule(bizOrder);
		if (bizOrder == null) {
			result.setResultMsg("入参错误");
			return result;
		}
		// 创建订单
		try {
			// 这里设置时间，是给后续的补充判断用。
			bizOrder.setGmtCreate(new Date());
			Long id = bizOrderDAO.insert(bizOrder);
			if (id != null) {
				bizOrder.setId(id);
				result.setSuccess(true);
				result.setModule(bizOrder);
			} else {
				result.setResultMsg("创建订单失败");
			}
		} catch (SQLException e) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_BIZORDER);
			result.setResultMsg("创建订单失败  msg: " + e.getMessage());
			logger.error("createBizOrder error", e);
		}
		return result;
	}

	private Result<Boolean> checkBizOrder(BizOrder bizOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (bizOrder == null) {
			result.setResultMsg("bizOrder is null");
			return result;
		}

		if (bizOrder.getUserId() == null) {
			result.setResultMsg("用户编号缺失");
			return result;
		}

		if (bizOrder.getItemId() == null) {
			result.setResultMsg("商品编号缺失");
			return result;
		}

		if (bizOrder.getAmt() == null || bizOrder.getAmt() == 0) {
			result.setResultMsg("购买数量缺失");
			return result;
		}

		if (bizOrder.getChannel() == null) {
			result.setResultMsg("渠道编号缺失");
			return result;
		}

		// 如果是接口供货
		if (bizOrder.isFromSupply()) {
			if (bizOrder.getDownstreamSerialno() == null) {
				result.setResultMsg("商户交易流水号缺失");
				return result;
			}
		}

		result.setSuccess(true);
		result.setModule(true);
		return result;
	}

	private Result<Boolean> checkUid(BizOrder bizOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (bizOrder == null || bizOrder.getItemId() == null || bizOrder.getItemUid() == null) {
			result.setResultMsg("入参错误");
			return result;
		}
		Item item = localCachedService.getItem(bizOrder.getItemId());

		if (item == null) {
			result.setResultMsg("不存在该商品");
			return result;
		}
		if (!item.isUidNumber()) {
			// 商品不是供货给手机，不做下面判断
			result.setSuccess(true);
			result.setModule(true);
			return result;
		}
		Result<MobileBelong> mobileBelongResult = null;
		MobileBelong mobileBelong = null;
		mobileBelongResult = areaInfoService.queryProvinceCodeByMobile(bizOrder.getItemUid());
		if (!mobileBelongResult.isSuccess()) {
			logger.error("checkCarrierType error " + mobileBelongResult.getResultMsg());
			result.setResultMsg(mobileBelongResult.getResultMsg());
			return result;
		}

		if (SwitchConstant.CHECK_CARRIER_TYPE_SWITCH) {
			mobileBelong = mobileBelongResult.getModule();
			if (mobileBelong != null) {
				if (!mobileBelong.getCarrierType().equals(item.getCarrierType())) {
					logger.warn("手机号和商品运营商不匹配 uid: " + bizOrder.getItemUid());
					result.setResultMsg("手机号和商品运营商不匹配");
					return result;
				}
			} else {
				// 查询不到结果，不影响主流程，只记录一下
				logger.warn("checkCarrierType mobileBelong is null uid : " + bizOrder.getItemUid());
			}
		}

		if (SwitchConstant.CHECK_AREA_SWITCH && !item.isNationwide()) {
			mobileBelong = mobileBelongResult.getModule();
			if (mobileBelong != null) {
				if (item.getSalesAreaList() != null
						&& !item.getSalesAreaList().contains(mobileBelong.getProvinceCode())) {
					logger.warn("手机号和商品省域不匹配 手机号: " + bizOrder.getItemUid() + " 手机归属地 ："
							+ mobileBelong.getProvinceName() + " 商品编号 : " + item.getId());
					result.setResultMsg("手机号和商品省域不匹配");
					return result;
				}
			} else {
				// 查询不到结果，不影响主流程，只记录一下
				logger.warn("checkArea mobileBelong is null uid : " + bizOrder.getItemUid());
			}
		}

		if (mobileBelong != null) {
			bizOrder.setCarrierType(mobileBelong.getCarrierType());
			bizOrder.setProvinceCode(mobileBelong.getProvinceCode());
		}

		result.setSuccess(true);
		result.setModule(true);
		return result;
	}

	@Override
	public Result<Boolean> updateBizOrder(BizOrder bizOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (bizOrder == null || bizOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}
		try {
			if (bizOrder.getStatus() != null
					&& bizOrder.getStatus() == Constants.BizOrder.STATUS_SUCCESS) {
				// 记录充值时间。
				Result<BizOrder> queryResult = getBizOrderById(bizOrder.getId());
				if (queryResult.getModule() != null) {
					bizOrder.setCostTime(bizOrder.computCostTime(queryResult.getModule()
							.getGmtCreate()));
				}
			}
			int row = bizOrderDAO.updateByPrimaryKeySelective(bizOrder);
			if (row > 0) {
				result.setSuccess(true);
				result.setModule(true);
			} else {
				result.setResultMsg("更新订单失败  ，没有该订单。订单id : " + bizOrder.getId());
			}
		} catch (SQLException e) {
			result.setResultMsg("更新订单失败  bizorderId : " + bizOrder.getId() + " msg: "
					+ e.getMessage());
			logger.error("updateBizOrder error", e);
		}
		return result;
	}

	@Override
	public Result<Boolean> updateBizOrder(BizOrder bizOrder, BizOrderExample bizOrderExample) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (bizOrder == null || bizOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}
		try {
			int row = bizOrderDAO.updateByExampleSelective(bizOrder, bizOrderExample);
			if (row > 0) {
				result.setSuccess(true);
				result.setModule(true);
			} else {
				result.setResultMsg("更新订单失败 。订单id : " + bizOrder.getId());
			}
		} catch (SQLException e) {
			result.setResultMsg("更新订单失败  bizorderId : " + bizOrder.getId());
			logger.error("updateBizOrder error", e);
		}
		return result;
	}

	public Result<Boolean> comfirmBizOrder(BizOrder bizOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);

		if (bizOrder == null || bizOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		// if (bizOrder.isSupplyFromStock() && bizOrder.getStockId() != null) {
		// // 这个逻辑主要是给，手动退款订单用的。
		// Result<Stock> getStockResult =
		// stockService.getStockById(bizOrder.getStockId());
		// // 不影响主流程。
		// if (!getStockResult.isSuccess()) {
		// logger.error("comfirmBizOrder getStockById error stockId:" +
		// bizOrder.getStockId()
		// + " msg : " + getStockResult.getResultMsg());
		// } else {
		// Stock stock = getStockResult.getModule();
		// if (stock != null) {
		// stockService.deliveryFromStorage(stock);
		// }
		// }
		// }

		try {
			// 记录充值时间。
			if (bizOrder.getGmtCreate() == null) {
				Result<BizOrder> queryResult = getBizOrderById(bizOrder.getId());
				if (queryResult.getModule() == null) {
					result.setResultMsg("找不到该订单");
					return result;
				}
				bizOrder.setCostTime(bizOrder
						.computCostTime(queryResult.getModule().getGmtCreate()));
			} else {
				bizOrder.setCostTime(bizOrder.computCostTime(bizOrder.getGmtCreate()));
			}

			// 更新订单状态
			bizOrder.setStatus(Constants.BizOrder.STATUS_SUCCESS);
			bizOrder.setUpstreamMemo(" ");

			int row = bizOrderDAO.updateByPrimaryKeySelective(bizOrder);
			if (row <= 0) {
				result.setResultMsg("订单确认失败  ，没有该订单。订单id : " + bizOrder.getId());
				return result;
			}
		} catch (SQLException e) {
			result.setResultMsg("订单确认失败  bizorderId : " + bizOrder.getId());
			logger.error("comfirmBizOrder error bizOrderId " + bizOrder.getId(), e);
			return result;
		}

		// // 处理中限制 减减
		// if (StringUtils.isNumeric(bizOrder.getUpstreamId())) {
		// chargingLimitService.decCount(Long.parseLong(bizOrder.getUpstreamId()));
		// }

		// 用户充值金额计数
		String key = getUidAmountKey(bizOrder.getUserId(), bizOrder.getItemUid());
		Integer amount = bizOrder.getItemFacePrice();
		memcachedService.inc(key, Constants.MemcachedKey.UID_AMOUNT_EXPTIME, amount, amount);

		result.setSuccess(true);
		result.setModule(true);
		return result;
	}

	public Result<Boolean> cancelBizOrder(BizOrder bizOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (bizOrder == null || bizOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		// if (bizOrder.isSupplyFromStock() && bizOrder.getStockId() != null) {
		// Result<Stock> getStockResult =
		// stockService.getStockById(bizOrder.getStockId());
		// // 不影响主流程。
		// if (!getStockResult.isSuccess()) {
		// logger.error("cancelBizOrder getStockById error stockId:" +
		// bizOrder.getStockId()
		// + " msg : " + getStockResult.getResultMsg());
		// } else {
		// Stock stock = getStockResult.getModule();
		// if (stock != null && bizOrder.isFlagCardValid()) {
		// stock.setErrorInfo(bizOrder.getUpstreamMemo());
		// stockService.sequestrationStorage(stock);
		// } else if (stock != null && !bizOrder.isFlagCardValid()) {
		// stockService.returnToStorage(stock);
		// }
		//
		// }
		// }

		if (bizOrder.getPayOrderId() == null) {
			Result<BizOrder> getBizOrderByIdResult = getBizOrderById(bizOrder.getId());
			if (!getBizOrderByIdResult.isSuccess()) {
				result.setResultMsg(getBizOrderByIdResult.getResultMsg());
				return result;
			}

			bizOrder.setPayOrderId(getBizOrderByIdResult.getModule().getPayOrderId());
		}

		// 如果订单中有支付单号,则走退款流程。
		if (bizOrder.getPayOrderId() != null) {
			OperationVO operationVO = null;
			if (bizOrder.getDealOperId() != null) {
				operationVO = new OperationVO();
				UserInfo userInfo = new UserInfo();
				userInfo.setId(bizOrder.getDealOperId());
				userInfo.setUserName(bizOrder.getDealOperName());
				operationVO.setUserInfo(userInfo);
				operationVO.setOperationMemo("供货失败");
			}
			Result<RefundOrder> commitRefundResult = payService.commitRefund(
					bizOrder.getPayOrderId(), operationVO);
			if (!commitRefundResult.isSuccess()) {
				if (!commitRefundResult.getResultCode().equals(
						Constants.PayOrder.CODE_NO_NEED_REFUND)) {
					// 非正常退款失败。
					result.setResultMsg(commitRefundResult.getResultMsg());
					return result;
				}
				// 根本没有支付单，或者支付状态非正常(比如已退款,或者未支付等) ，继续做取消操作
			}

			// 退款成功
		}
		bizOrder.setStatus(Constants.BizOrder.STATUS_FAILED);

		int row = 0;
		try {
			row = bizOrderDAO.updateByPrimaryKeySelective(bizOrder);
		} catch (SQLException e) {
			result.setResultMsg("订单取消失败: bizOrderId : " + bizOrder.getId() + " 数据库异常");
			logger.error("cancelBizOrder error bizOrderId " + bizOrder.getId(), e);
			return result;
		}

		if (row <= 0) {
			result.setResultMsg("取消订单失败  ，没有该订单");
			logger.error("cancelBizOrder error bizOrderId " + bizOrder.getId() + " 更新数 0");
			return result;
		}

		// // 处理中限制 减减
		// if (StringUtils.isNumeric(bizOrder.getUpstreamId())) {
		// chargingLimitService.decCount(Long.parseLong(bizOrder.getUpstreamId()));
		// }

		result.setSuccess(true);
		result.setModule(true);
		return result;
	}

	public Result<Boolean> unComfirmBizOrder(BizOrder bizOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);

		if (bizOrder == null || bizOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			bizOrder.setStatus(Constants.BizOrder.STATUS_UNCONFIRMED);
			int row = bizOrderDAO.updateByPrimaryKeySelective(bizOrder);
			if (row <= 0) {
				result.setResultMsg("订单至未确认状态失败，没有该订单,订单id : " + bizOrder.getId());
				logger.error("unComfirmBizOrder error bizOrderId " + bizOrder.getId() + " 更新数 0");
				return result;
			}
		} catch (SQLException e) {
			result.setResultMsg("订单至未确认状态失败  bizorderId : " + bizOrder.getId());
			logger.error("unComfirmBizOrder error bizOrderId " + bizOrder.getId(), e);
			return result;
		}

		// 未确认不做计数处理

		result.setSuccess(true);
		result.setModule(true);
		return result;
	}

	public Result<Boolean> chargingBizOrderAndCounting(BizOrder bizOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);

		if (bizOrder == null || bizOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			bizOrder.setStatus(Constants.BizOrder.STATUS_CHARGING);
			int row = bizOrderDAO.updateByPrimaryKeySelective(bizOrder);
			if (row <= 0) {
				result.setResultCode(Constants.ErrorCode.CODE_ERROR_BIZORDER);
				result.setResultMsg("订单至未确认状态失败，没有该订单,订单id : " + bizOrder.getId());
				logger.error("chargingBizOrder error bizOrderId " + bizOrder.getId() + " 更新数 0");
				return result;
			}
		} catch (SQLException e) {
			result.setResultCode(Constants.ErrorCode.CODE_ERROR_BIZORDER);
			result.setResultMsg("订单至未确认状态失败  bizorderId : " + bizOrder.getId());
			logger.error("unComfirmBizOrder error bizOrderId " + bizOrder.getId(), e);
			return result;
		}

		// // 处理中限制 加加
		// if (StringUtils.isNumeric(bizOrder.getUpstreamId())) {
		// chargingLimitService.incCount(Long.parseLong(bizOrder.getUpstreamId()));
		// }

		result.setSuccess(true);
		result.setModule(true);
		return result;
	}

	@Override
	public Result<BizOrder> getBizOrderById(Long bizOrderId) {
		Result<BizOrder> result = new Result<BizOrder>();
		if (bizOrderId == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			BizOrder bizOrder = bizOrderDAO.selectByPrimaryKey(bizOrderId);
			if (bizOrder != null) {
				result.setSuccess(true);
				result.setModule(bizOrder);
			} else {
				result.setResultMsg("没有该订单。订单id : " + bizOrderId);
			}
		} catch (SQLException e) {
			result.setResultMsg("订单查询失败  id : " + bizOrderId + " msg: " + e.getMessage());
			logger.error("queryBizOrderById error", e);
		}
		return result;
	}

	@Override
	public Result<BizOrder> getBizOrderByPayOrder(Long payOrderId) {
		Result<BizOrder> result = new Result<BizOrder>();
		if (payOrderId == null) {
			result.setResultMsg("入参错误");
			return result;
		}
		try {
			BizOrderExample bizOrderExample = new BizOrderExample();
			bizOrderExample.createCriteria().andPayOrderIdEqualTo(payOrderId);
			@SuppressWarnings("unchecked")
			List<BizOrder> list = (List<BizOrder>) bizOrderDAO.selectByExample(bizOrderExample);
			if (list != null && list.size() > 0) {
				result.setSuccess(true);
				result.setModule(list.get(0));
			} else {
				result.setResultMsg("没有该订单信息  支付单id ：" + payOrderId);
			}
		} catch (SQLException e) {
			result.setResultMsg("订单查询失败  支付单id  : " + payOrderId + " msg: " + e.getMessage());
			logger.error("queryBizOrderByPayOrder error", e);
		}
		return result;
	}

	@Override
	public Result<List<BizOrder>> queryBizOrderPage(BizOrderQuery bizOrderQuery) {
		Result<List<BizOrder>> result = new Result<List<BizOrder>>();
		if (bizOrderQuery == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			List<BizOrder> queryResult = bizOrderDAO.queryByPage(bizOrderQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("订单查询失败    msg: " + e.getMessage());
			logger.error("queryBizOrderPage error ", e);
		}

		return result;
	}

	@Override
	public Result<List<BizOrder>> queryBizOrderExport(BizOrderQuery bizOrderQuery) {
		Result<List<BizOrder>> result = new Result<List<BizOrder>>();
		if (bizOrderQuery == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			List<BizOrder> queryResult = bizOrderDAO.queryByExport(bizOrderQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("订单查询失败    msg: " + e.getMessage());
			logger.error("queryBizOrderPage error ", e);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result<Boolean> checkDownstreamSerialno(BizOrder bizOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (bizOrder == null) {
			result.setResultMsg("bizOrder is null");
			return result;
		}

		if (!bizOrder.isFromSupply()) {
			result.setSuccess(true);
			result.setModule(true);
			return result;
		}

		String serialno = bizOrder.getDownstreamSerialno();
		Long userId = bizOrder.getUserId();
		if (serialno == null || userId == null || bizOrder.getBizId() == null) {
			result.setResultMsg("serialno or userId or bizId is null");
			return result;
		}

		BizOrderExample bizOrderExample = new BizOrderExample();
		bizOrderExample.createCriteria().andDownstreamSerialnoEqualTo(serialno)
				.andUserIdEqualTo(userId);
		try {
			List<BizOrder> list = (List<BizOrder>) bizOrderDAO.selectByExample(bizOrderExample);
			if (list.size() == 0) {
				result.setSuccess(true);
				result.setModule(true);
			} else {
				result.setResultMsg("重复流水号");
			}
		} catch (SQLException e) {
			result.setResultMsg("商户流水号重复校验失败    msg: " + e.getMessage());
			logger.error("checkDownStreamSerialno error userId : " + userId, e);
		}

		return result;
	}

	@Override
	public Result<BizOrder> getBizOrderDownstreamSerialno(String downstreamSerialno, Long userId) {
		Result<BizOrder> result = new Result<BizOrder>();

		if (downstreamSerialno == null || userId == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			BizOrderExample bizOrderExample = new BizOrderExample();
			bizOrderExample.createCriteria().andDownstreamSerialnoEqualTo(downstreamSerialno)
					.andUserIdEqualTo(userId);
			@SuppressWarnings("unchecked")
			List<BizOrder> list = (List<BizOrder>) bizOrderDAO.selectByExample(bizOrderExample);
			result.setSuccess(true);
			if (list != null && list.size() > 0) {
				result.setModule(list.get(0));
			} else {
				result.setResultMsg("没有该订单信息 下游流水号 ：" + downstreamSerialno);
			}
		} catch (SQLException e) {
			result.setResultMsg("订单查询失败   下游流水号 : " + downstreamSerialno);
			logger.error("getBizOrderDownstreamSerialno error", e);
		}
		return result;
	}

	@Override
	public Result<BizOrder> getBizOrderUpstreamSerialno(String upstreamSerialno, Long userId) {
		Result<BizOrder> result = new Result<BizOrder>();
		if (upstreamSerialno == null || userId == null) {
			result.setResultMsg("入参错误");
			return result;
		}
		try {
			BizOrderExample bizOrderExample = new BizOrderExample();
			bizOrderExample.createCriteria().andUpstreamSerialnoEqualTo(upstreamSerialno)
					.andUpstreamIdEqualTo(userId + "");
			@SuppressWarnings("unchecked")
			List<BizOrder> list = (List<BizOrder>) bizOrderDAO.selectByExample(bizOrderExample);
			if (list != null && list.size() > 0) {
				result.setSuccess(true);
				result.setModule(list.get(0));
			} else {
				result.setResultMsg("没有该订单信息 上游流水号 ：" + upstreamSerialno);
			}
		} catch (SQLException e) {
			result.setResultMsg("订单查询失败   上游流水号 : " + upstreamSerialno);
			logger.error("getBizOrderUpstreamSerialno error", e);
		}
		return result;
	}

	@Override
	public Result<Boolean> manualLockBizOrder(BizOrder bizOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (bizOrder == null || bizOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			int row = bizOrderDAO.lockBizOrder(bizOrder);
			if (row > 0) {
				result.setSuccess(true);
				result.setModule(true);
			} else {
				result.setResultMsg("人工锁定订单失败 ,可能被其他操作员抢先啦。订单id : " + bizOrder.getId());
			}
		} catch (SQLException e) {
			result.setResultMsg("人工锁定订单失败  bizorderId : " + bizOrder.getId() + " msg: "
					+ e.getMessage());
			logger.error("lockBizOrder error", e);
		}
		return result;
	}

	@Override
	public Result<Boolean> manualUnLockBizOrder(BizOrder bizOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (bizOrder == null || bizOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			int row = bizOrderDAO.unLockBizOrder(bizOrder);
			if (row > 0) {
				result.setSuccess(true);
				result.setModule(true);
			} else {
				result.setResultMsg("解锁订单失败 ,可能被其他操作员抢先啦。订单id : " + bizOrder.getId());
			}
		} catch (SQLException e) {
			result.setResultMsg("解锁订单失败  bizorderId : " + bizOrder.getId() + " msg: "
					+ e.getMessage());
			logger.error("unLockBizOrder error", e);
		}
		return result;
	}

	@Override
	public Result<Boolean> manualConfirmBizOrder(BizOrder bizOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);

		if (bizOrder == null || bizOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			if (bizOrder.getGmtCreate() != null) {
				bizOrder.setCostTime(bizOrder.computCostTime(bizOrder.getGmtCreate()));
			}
			int row = bizOrderDAO.confirmBizOrder(bizOrder);
			if (row > 0) {
				result.setSuccess(true);
				result.setModule(true);

				// 处理中限制 减减
				if (StringUtils.isNumeric(bizOrder.getUpstreamId())) {
					chargingLimitService.decCount(Long.parseLong(bizOrder.getUpstreamId()));
				}

				String key = getUidAmountKey(bizOrder.getUserId(), bizOrder.getItemUid());
				Integer amount = bizOrder.getItemFacePrice();
				memcachedService
						.inc(key, Constants.MemcachedKey.UID_AMOUNT_EXPTIME, amount, amount);
			} else {
				result.setResultMsg("人工确认订单失败,订单已经被确认,或者订单不是你锁定的订单。订单id : " + bizOrder.getId());
			}
		} catch (SQLException e) {
			result.setResultMsg("人工确认订单失败  bizorderId : " + bizOrder.getId() + " msg: "
					+ e.getMessage());
			logger.error("confirmBizOrder error", e);
		}
		return result;
	}

	@Override
	public Result<Boolean> manualCancelBizOrder(BizOrder bizOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);

		if (bizOrder == null || bizOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		// 如果订单中有支付单号,则走退款流程。
		if (bizOrder.getPayOrderId() != null) {
			OperationVO operationVO = null;
			if (bizOrder.getDealOperId() != null) {
				operationVO = new OperationVO();
				UserInfo userInfo = new UserInfo();
				userInfo.setId(bizOrder.getDealOperId());
				userInfo.setUserName(bizOrder.getDealOperName());
				operationVO.setUserInfo(userInfo);
				operationVO.setOperationMemo("人工充值失败退款");
			}

			Result<RefundOrder> commitRefundResult = payService.commitRefund(
					bizOrder.getPayOrderId(), operationVO);
			if (!commitRefundResult.isSuccess()) {
				if (commitRefundResult.getResultCode() != Constants.PayOrder.CODE_NO_NEED_REFUND) {
					// 非正常退款失败。
					result.setResultMsg(commitRefundResult.getResultMsg());
					return result;
				}
				// 根本没有支付单，或者支付状态非正常(比如已退款,或者未支付等) ，继续做取消操作
			}

			// 退款成功
		}

		try {
			int row = bizOrderDAO.cancelBizOrder(bizOrder);
			if (row > 0) {
				result.setSuccess(true);
				result.setModule(true);

				// 处理中限制 减减
				if (StringUtils.isNumeric(bizOrder.getUpstreamId())) {
					chargingLimitService.decCount(Long.parseLong(bizOrder.getUpstreamId()));
				}
			} else {
				result.setResultMsg("人工取消订单失败 ,可能订单已经被取消。订单id : " + bizOrder.getId());
			}
		} catch (SQLException e) {
			result.setResultMsg("人工取消订单失败  bizorderId : " + bizOrder.getId() + " msg: "
					+ e.getMessage());
			logger.error("cancelBizOrder error", e);
		}
		return result;
	}

	@Override
	public Result<Boolean> manualUnConfirmBizOrder(BizOrder bizOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);

		if (bizOrder == null || bizOrder.getId() == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			int row = bizOrderDAO.unConfirmBizOrder(bizOrder);
			if (row > 0) {
				result.setSuccess(true);
				result.setModule(true);
			} else {
				result.setResultMsg("人工未确认订单失败 。订单id : " + bizOrder.getId());
			}
		} catch (SQLException e) {
			result.setResultMsg("人工未确认订单失败  bizorderId : " + bizOrder.getId() + " msg: "
					+ e.getMessage());
			logger.error("cancelBizOrder error", e);
		}
		return result;
	}

	@Override
	public Result<Integer> getCountInCharging(Integer bizId) {
		// 包括充值中 和 处理中，未确认
		Result<Integer> result = new Result<Integer>();
		if (bizId == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			BizOrderExample bizOrderExample = new BizOrderExample();

			List<Integer> values = new ArrayList<Integer>();
			values.add(Constants.BizOrder.STATUS_CHARGING);
			values.add(Constants.BizOrder.STATUS_LOCK);
			values.add(Constants.BizOrder.STATUS_UNCONFIRMED);
			bizOrderExample.createCriteria().andBizIdEqualTo(bizId).andStatusIn(values);
			Integer count = bizOrderDAO.countByExample(bizOrderExample);
			if (count != null) {
				result.setSuccess(true);
				result.setModule(count);
			} else {
				result.setResultMsg("查询充值中的订单数量异常  bizId : " + bizId);
			}
		} catch (SQLException e) {
			result.setResultMsg("查询充值中的订单数量异常  bizId : " + bizId + " msg : " + e.getMessage());
			logger.error("getCountInCharging error biz : " + bizId, e);
		}

		return result;
	}

	@Override
	public Result<Integer> getCountInChargingByTraderId(Long traderId) {
		// 包括充值中 和 处理中和未确认
		Result<Integer> result = new Result<Integer>();
		if (traderId == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			BizOrderExample bizOrderExample = new BizOrderExample();

			List<Integer> values = new ArrayList<Integer>();
			values.add(Constants.BizOrder.STATUS_CHARGING);
			values.add(Constants.BizOrder.STATUS_LOCK);
			values.add(Constants.BizOrder.STATUS_UNCONFIRMED);
			bizOrderExample.createCriteria().andUpstreamIdEqualTo(traderId + "")
					.andStatusIn(values);
			Integer count = bizOrderDAO.countByExample(bizOrderExample);
			if (count != null) {
				result.setSuccess(true);
				result.setModule(count);
			} else {
				result.setResultMsg("查询充值中的订单数量异常  traderId : " + traderId);
			}
		} catch (SQLException e) {
			result.setResultMsg("查询充值中的订单数量异常  traderId : " + traderId + " msg : " + e.getMessage());
			logger.error("getCountInCharging error traderId : " + traderId, e);
		}

		return result;
	}

	@Override
	public Result<Boolean> checkUidCount(BizOrder bizOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (bizOrder == null) {
			result.setResultMsg("bizOrder is null");
			return result;
		}
		if (bizOrder.getUserId() == null || bizOrder.getItemUid() == null
				|| bizOrder.getItemFacePrice() == null) {
			result.setResultMsg("uid ,userId or itemFacePrice  is null ");
			return result;
		}
		Integer amount = bizOrder.getItemFacePrice();
		String key = getUidAmountKey(bizOrder.getUserId(), bizOrder.getItemUid());
		try {
			Long value = memcachedService.getLongValue(key);
			if (value == null || value == 0L) {
				result.setSuccess(true);
				result.setModule(true);
				return result;
			}

			logger.warn("userId : " + bizOrder.getUserId() + " uid : " + bizOrder.getItemUid()
					+ " amount : " + value);

			if (StringUtils.isNumeric(Utils.getProperty("maxUidAmount"))) {
				if (value + amount >= Long.parseLong(Utils.getProperty("maxUidAmount"))) {
					result.setResultMsg(bizOrder.getItemUid() + "用户今日已经达到最大充值金额");
					logger.warn("userId : " + bizOrder.getUserId() + " uid : "
							+ bizOrder.getItemUid() + " 今日已经达到最大充值金额");
					return result;
				}
			}
		} catch (Exception e) {
			// 出错了,就降级了,也返回true, 防止memcached挂了后，导致业务走不下去
			logger.error("check Uid Count异常 ", e);
		}
		result.setSuccess(true);
		result.setModule(true);
		return result;
	}

	private String getUidAmountKey(Long userId, String uid) {
		return DateTool.getToday() + Constants.MemcachedKey.UID_AMOUNT + userId + "_" + uid;
	}

	@Override
	public Result<BizOrder> robotLockBizOrder(Long upstreamId, String posId, String pcId,
			Long lockOperId) {
		Result<BizOrder> result = new Result<BizOrder>();
		if (upstreamId == null || lockOperId == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		TraderInfo traderInfo = localCachedService.getTraderInfo(upstreamId);
		if (!traderInfo.isManualSupply()) {
			result.setResultMsg("供货商供货方式不对");
			return result;
		}

		BizOrderQuery bizOrderQuery = new BizOrderQuery();
		bizOrderQuery.setUpstreamId(upstreamId);
		bizOrderQuery.setStatus(Constants.BizOrder.STATUS_CHARGING);

		Long startTime = System.currentTimeMillis();
		BizOrder lockedBizOrder = null;
		result.setSuccess(true);
		while ((System.currentTimeMillis() - 5000) <= startTime) {
			Result<List<BizOrder>> queryResult = this.queryBizOrderPage(bizOrderQuery);
			if (!queryResult.isSuccess()) {
				result.setResultMsg(queryResult.getResultMsg());
				return result;
			}

			List<BizOrder> bizOrderList = queryResult.getModule();
			if (bizOrderList == null || bizOrderList.isEmpty()) {
				return result;
			}
			// 打乱减少竞争
			Collections.shuffle(bizOrderList);
			for (BizOrder bizOrder : bizOrderList) {
				bizOrder.setItemExt1(posId);
				bizOrder.setItemExt2(pcId);
				bizOrder.setLockOperId(lockOperId);
				Result<Boolean> lockResult = this.manualLockBizOrder(bizOrder);
				if (!lockResult.isSuccess()) {
					continue;
				}
				lockedBizOrder = bizOrder;
				break;
			}
			if (lockedBizOrder == null) {
				continue;
			}
			// 锁定成功
			break;
		}
		result.setModule(lockedBizOrder);
		return result;
	}

}
