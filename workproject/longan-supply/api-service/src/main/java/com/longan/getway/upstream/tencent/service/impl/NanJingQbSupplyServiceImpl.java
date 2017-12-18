package com.longan.getway.upstream.tencent.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.core.QbOrderService;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.QbOrder;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Utils;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.domain.ChargeInfo;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamDirectSupplyService;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.common.domain.QueryRunnable;

public class NanJingQbSupplyServiceImpl extends BaseService implements UpstreamDirectSupplyService {
	private boolean isAsync = true;
	private static final String supplyName = "南京QB充值";
	private static final int qbMaxCount = Integer.parseInt(Utils.getProperty("qbSupllyQueryCount"));

	public static final Map<String, String> codeResultMap = new HashMap<String, String>();
	@Resource
	private QbOrderService qbOrderService;
	
	static {
		codeResultMap.put("LimErr111", "对不起，该用户充值金额已经达到平台限制");
		codeResultMap.put("LimErr112 NOQB", "对不起，该面额Q币已用完");
		codeResultMap.put("LimErr113 NOQB", "Q币没有了");
		codeResultMap.put("LimErr114 QQNumErr", "您输入的QQ号码不正确");
		codeResultMap.put("LimErr115 QBCodeErr", "您使用的Q币不正确（已使用）");
		codeResultMap.put("LimErr116 AccountLock2", "您的账户已经被冻结");
		codeResultMap.put("LimErr7", "您的QQ号码充值限额已到");
		codeResultMap.put("LimErr30", "您的QQ号码本日充值限额已到");
		codeResultMap.put("LimErr31", "您的QQ号码本月充值限额已到");
		codeResultMap.put("LimErr32", "您的QQ号码本日充值所用电话数目已到");
		codeResultMap.put("LimErr33", "您的QQ号码本月充值所用电话数目已到");
		codeResultMap.put("LimErr8", "您使用的电话充值限额已到");
		codeResultMap.put("LimErr60", "您使用的电话本日充值限额已到");
		codeResultMap.put("LimErr61", "您使用的电话本月充值限额已到");
		codeResultMap.put("LimErr62", "您使用的电话本日充值QQ号数目已到限额");
		codeResultMap.put("LimErr63", "您使用的电话本月充值QQ号数目已到限额");
		codeResultMap.put("LimErr117 Forbid", "您未被授权拨打此热线");
		codeResultMap.put("LimErr87", "您的此次充值存在风险");
		codeResultMap.put("LimErr118 WaitToRetry", "对不起，网络忙，系统会在24小时内为您进行充值");
		codeResultMap.put("000 ok", "充值成功");
		codeResultMap.put("LimErr119 ErrorQQNum", "对不起，您输入的QQ号码错误");
		codeResultMap.put("LimErr120 DayLimit", "到达每日最高限额，请明天再冲值");
		codeResultMap.put("LimErr121 MonthLimit", "到达每月最高限额，请下月再冲值");
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);
		QbOrder qbOrder = new QbOrder();
		ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder.getItemSupplyId());
		if (itemSupply == null) {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("供货商品为空");
			return result;
		}
		qbOrder.setSerialNum(supplyOrder.getId() + "");
		SimpleDateFormat formdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		qbOrder.setTime(formdate.format(new Date()));
		qbOrder.setQqNum(supplyOrder.getItemUid());
		qbOrder.setQbValue(supplyOrder.getItemFacePrice() / 1000 + "");
		qbOrder.setQbType("QB" + supplyOrder.getItemFacePrice() / 1000);
		qbOrder.setStatus("0");
		qbOrder.setReStatus("0");
		qbOrderService.createQbOrder(qbOrder);

		supplyOrder.setUpstreamSerialno(supplyOrder.getId() + "");
		bizDealService.submitQbQueryRunnable(new NanJingQbQueryRunnable(supplyName, supplyOrder
				.getId(), supplyOrder.getBizOrderId()));

		result.setStatus(SupplyResult.STATUS_SUCCESS);
		return result;
	}

	@Override
	public SupplyResult<ChargeInfo> chargeQuery(SupplyOrder supplyOrder) {
		SupplyResult<ChargeInfo> result = new SupplyResult<ChargeInfo>();
		if (supplyOrder == null) {
			result.setResultMsg("参数错误");
			return result;
		}

		if (StringUtils.isEmpty(supplyOrder.getUpstreamSerialno())) {
			result.setResultMsg("查询失败，上游流水号为空");
			return result;
		}
		ChargeInfo chargeInfo = new ChargeInfo();
		Result<QbOrder> qbOrderResult = qbOrderService.getQbOrderBySerialNum(supplyOrder.getId()
				+ "");
		QbOrder qbOrder;
		if (qbOrderResult.isSuccess()) {
			qbOrder = qbOrderResult.getModule();
			if (qbOrder == null) {
				result.setResultMsg("上游没有该供货单信息");
				return result;
			}
		} else {
			result.setResultMsg("查询上游失败");
			return result;
		}
		if (!"1".equals(qbOrder.getStatus())) {
			chargeInfo.setStatus(qbOrder.getStatus());
			chargeInfo.setStatusDesc("未同步状态(等待充值中)");
			result.setStatus(SupplyResult.STATUS_SUCCESS);
			result.setModule(chargeInfo);
			return result;
		} else {
			if (!"1".equals(qbOrder.getReStatus())) {
				chargeInfo.setStatus(qbOrder.getReStatus());
				chargeInfo.setStatusDesc("未写入结果(正在充值中)");
				result.setStatus(SupplyResult.STATUS_SUCCESS);
				result.setModule(chargeInfo);
				return result;
			} else {
				if (!StringUtils.isEmpty(qbOrder.getResult())) {
					chargeInfo.setStatus(qbOrder.getResult());
					chargeInfo.setStatusDesc(codeResultMap.get(qbOrder.getResult()));
					result.setStatus(SupplyResult.STATUS_SUCCESS);
				} else {
					result.setStatus(SupplyResult.STATUS_UNCONFIRM);
					result.setResultMsg("写入的结果为空");
				}
			}
		}
		result.setModule(chargeInfo);
		return result;
	}

	class NanJingQbQueryRunnable extends QueryRunnable {

		public NanJingQbQueryRunnable(String queryName, Long supplyOrderId, Long bizOrderId) {
			super(queryName, supplyOrderId, bizOrderId, bizOrderService, bizDealService,
					supplyOrderService);
			super.setMaxCount(qbMaxCount);
		}

		@Override
		protected CallBack chargeResultQuery() {
			Result<SupplyOrder> getSupplyOrderByIdResult = supplyOrderService
					.getSupplyOrderById(supplyOrderId);
			SupplyOrder supplyOrder = getSupplyOrderByIdResult.getModule();

			if (supplyOrder == null) {
				logger.error("getSupplyOrderById error supplyOrder is null id : " + supplyOrderId);
				return null;
			}

			SupplyResult<ChargeInfo> chargeInfoResult = chargeQuery(supplyOrder);
			if (!chargeInfoResult.isSuccess()) {
				logger.error("chargeQuery error : " + chargeInfoResult.getResultMsg());
				return null;
			}

			ChargeInfo chargeInfo = chargeInfoResult.getModule();
			if (chargeInfo == null) {
				logger.error("chargeQuery error  chargeInfo is null supplyOrderId : "
						+ supplyOrderId);
				return null;
			}

			if ("0".equals(chargeInfo.getStatus())) {
				return null;
			}

			CallBack callBack = new CallBack();
			callBack.setSupplyOrderId(supplyOrderId);
			if (!("000 ok".equals(chargeInfo.getStatus()) || "LimErr118 WaitToRetry"
					.equals(chargeInfo.getStatus()))) {
				callBack.setStatus(SupplyResult.STATUS_FAILED);
				return callBack;
			}
			callBack.setStatus(SupplyResult.STATUS_SUCCESS);
			return callBack;
		}
	}

	@Override
	public SupplyResult<BalanceQueryInfo> balanceQuery() {
		SupplyResult<BalanceQueryInfo> result = new SupplyResult<BalanceQueryInfo>();
		BalanceQueryInfo balanceQueryInfo = new BalanceQueryInfo();
		result.setModule(balanceQueryInfo);
		result.setStatus(SupplyResult.STATUS_SUCCESS);
		balanceQueryInfo.setMsg("该上游不支持余额查询");
		return result;
	}

}
