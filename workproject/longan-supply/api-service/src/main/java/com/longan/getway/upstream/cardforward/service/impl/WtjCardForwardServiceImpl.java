package com.longan.getway.upstream.cardforward.service.impl;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.core.StockService;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.Stock;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.CardChargeInfo;
import com.longan.client.remote.domain.CardCheck;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.cardforward.vo.WtjChargeRequestVO;
import com.longan.getway.upstream.cardforward.vo.WtjHeartBeatRequestVO;
import com.longan.getway.upstream.cardforward.vo.WtjQueryRequestVO;
import com.longan.getway.upstream.common.UpstreamCardSupplyService;
import com.longan.getway.upstream.common.domain.CallBack;
import com.longan.getway.upstream.common.domain.QueryRunnable;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class WtjCardForwardServiceImpl extends BaseService implements UpstreamCardSupplyService {
	private static final Logger logger = LoggerFactory.getLogger(WtjCardForwardServiceImpl.class);
	private boolean isAsync = true;
	private static final String supplyName = "南京威塔捷收卡通道";

	private final static ScheduledExecutorService ses = Executors
			.newSingleThreadScheduledExecutor();

	@Resource
	private StockService stockService;

	void init() {
		ses.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				WtjHeartBeatRequestVO wtjHeartBeatRequestVO = new WtjHeartBeatRequestVO();
				wtjHeartBeatRequestVO.heartBeat();
			}
		}, 1, 30, TimeUnit.SECONDS);
	}

	@Override
	public SupplyResult<CardCheck> cardCheck(Stock stock) {
		SupplyResult<CardCheck> result = new SupplyResult<CardCheck>();
		result.setResultCode(Constants.ErrorCode.CODE_ERROR_SUPPLY_FAILD);
		result.setResultMsg("供货商不支持该查询");
		return result;
	}

	@Override
	public SupplyResult<List<CardChargeInfo>> cardInfoQuery(Stock stock) {
		SupplyResult<List<CardChargeInfo>> result = new SupplyResult<List<CardChargeInfo>>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		WtjQueryRequestVO wtjQueryRequestVO = new WtjQueryRequestVO();
		wtjQueryRequestVO.setBillId(stock.getBizOrderId() + "");
		if (stock.getOutTime() != null) {
			wtjQueryRequestVO.setBillDate(sdf.format(stock.getOutTime()));
		}

		SupplyResult<String> httpRequest = MultiThreadedHttpConnection
				.getInstance()
				.sendPostByMapLogin(wtjQueryRequestVO.createUrl(), wtjQueryRequestVO.createParams());
		result.setStatus(httpRequest.getStatus());
		result.setResultMsg(httpRequest.getResultMsg());

		if (httpRequest.isSuccess()) {
			if (httpRequest.getModule() == null) {
				logger.error("result is null");
				result.setResultMsg("result is null");
				return result;
			}
			SAXReader reader = new SAXReader();
			Document document = null;
			try {
				document = reader.read(new StringReader(httpRequest.getModule()));
			} catch (DocumentException e) {
				logger.error("parse error : ", e);
				result.setResultMsg("parse error");
				return result;
			}
			Element root = document.getRootElement();
			String code = root.elementText("error_code");
			String errorDetail = root.elementText("error_detail");
			if ("0".equals(code)) {
				// 查询成功
				result.setStatus(SupplyResult.STATUS_SUCCESS);

				String billCount = root.elementText("bill_count");
				if (!StringUtils.isNumeric(billCount)) {
					logger.error("billCount is null");
					result.setResultMsg("billCount is null");
					return result;
				}

				List<CardChargeInfo> cardChargeInfoList = new ArrayList<CardChargeInfo>();

				int count = Integer.parseInt(billCount);
				if (count <= 0) {
					// 查询成功，但是没有订单
					// 成功，但是没有信息，表示未受理。
					return result;
				}

				Element billList = root.element("bill_list");
				if (billList == null) {
					logger.error("billList is null");
					result.setResultMsg("billList is null");
					return result;
				}

				@SuppressWarnings("unchecked")
				Iterator<Element> i = billList.elementIterator();
				while (i.hasNext()) {
					Element e = (Element) i.next();
					CardChargeInfo info = new CardChargeInfo();
					info.setStatus(e.attributeValue("charge_state"));
					info.setStatusDesc(e.attributeValue("charge_note"));
					info.setServiceNum(e.attributeValue("bill_usrid"));
					info.setMoney(e.attributeValue("bill_amount"));
					String chargeAmountDifference = e.attributeValue("charge_amount_difference");
					if (StringUtils.isNumeric(chargeAmountDifference)) {
						// 如果充值金额 和 amount 不一样， 要做 未确认处理
						if (Integer.parseInt(chargeAmountDifference) > 0) {
							// result.setStatus(SupplyResult.STATUS_UNCONFIRM);
							// result.setModule(cardChargeInfoList);
							// return result;
						}
					}
					cardChargeInfoList.add(info);
				}

				result.setStatus(SupplyResult.STATUS_SUCCESS);
				result.setModule(cardChargeInfoList);
			} else if ("100".equals(code) || "111".equals(code) || "120".equals(code)) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultCode(code);
				result.setResultMsg(errorDetail);
				return result;
			} else {
				result.setResultCode(code);
				result.setResultMsg(errorDetail);
				return result;
			}

		}

		return result;
	}

	class WtjCardForwardQueryRunnable extends QueryRunnable {

		public WtjCardForwardQueryRunnable(String queryName, Long supplyOrderId, Long bizOrderId) {
			super(queryName, supplyOrderId, bizOrderId, bizOrderService, bizDealService,
					supplyOrderService);
		}

		@Override
		protected CallBack chargeResultQuery() {
			Result<BizOrder> bizOrderResult = bizOrderService.getBizOrderById(bizOrderId);
			if (!bizOrderResult.isSuccess()) {
				logger.error("WtjCardForwardQueryRunnable getBizOrderById error msg : "
						+ bizOrderResult.getResultMsg());
				return null;
			}
			BizOrder bizOrder = bizOrderResult.getModule();
			if (bizOrder == null) {
				return null;
			}

			Result<Stock> stockResult = stockService.getStockById(bizOrder.getStockId());

			if (!stockResult.isSuccess()) {
				logger.error("WtjCardForwardQueryRunnable getStockById error msg : "
						+ bizOrderResult.getResultMsg());
				return null;
			}
			Stock stock = stockResult.getModule();
			if (stock == null) {
				return null;
			}

			SupplyResult<List<CardChargeInfo>> chargeInfoResult = cardInfoQuery(stock);
			List<CardChargeInfo> cardChargeInfoList = chargeInfoResult.getModule();
			if (cardChargeInfoList == null || cardChargeInfoList.isEmpty()) {
				return null;
			}
			CardChargeInfo cardChargeInfo = cardChargeInfoList.get(0);
			CallBack callBack = new CallBack();
			callBack.setSupplyOrderId(supplyOrderId);

			if ("0".equals(cardChargeInfo.getStatus())) {
				// success
				callBack.setStatus(SupplyResult.STATUS_SUCCESS);

			} else if ("900".equals(cardChargeInfo.getStatus())
					|| "-1".equals(cardChargeInfo.getStatus())) {
				// charging
				callBack.setStatus(SupplyResult.STATUS_UNCONFIRM);
				callBack.setFailedCode(cardChargeInfo.getStatus());
				callBack.setFailedMsg(cardChargeInfo.getStatusDesc());
			} else if ("901".equals(cardChargeInfo.getStatus())
					|| "902".equals(cardChargeInfo.getStatus())) {
				// card valid
				callBack.setStatus(SupplyResult.STATUS_CARD_INVALID);
				callBack.setFailedCode(cardChargeInfo.getStatus());
				callBack.setFailedMsg(cardChargeInfo.getStatusDesc());
			} else if ("906".equals(cardChargeInfo.getStatus())) {
				// uncomfirm
				callBack.setStatus(SupplyResult.STATUS_UNCONFIRM);
				callBack.setFailedCode(cardChargeInfo.getStatus());
				callBack.setFailedMsg(cardChargeInfo.getStatusDesc());
			} else if ("903".equals(cardChargeInfo.getStatus())
					|| "904".equals(cardChargeInfo.getStatus())
					|| "905".equals(cardChargeInfo.getStatus())
					|| "907".equals(cardChargeInfo.getStatus())
					|| "908".equals(cardChargeInfo.getStatus())) {
				// failed
				callBack.setStatus(SupplyResult.STATUS_FAILED);
				callBack.setFailedCode(cardChargeInfo.getStatus());
				callBack.setFailedMsg(cardChargeInfo.getStatusDesc());
			} else {
				// uncomfirm
				callBack.setStatus(SupplyResult.STATUS_UNCONFIRM);
				callBack.setFailedCode(cardChargeInfo.getStatus());
				callBack.setFailedMsg(cardChargeInfo.getStatusDesc());
			}

			return callBack;
		}
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder, Stock stock) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);
		WtjChargeRequestVO wtjChargeRequestVO = new WtjChargeRequestVO();
		wtjChargeRequestVO.setBillAmount(stock.getItemFacePriceDesc());
		wtjChargeRequestVO.setBillCrdno(stock.getCardId());
		wtjChargeRequestVO.setBillCrdpwd(stock.getCardPwd());
		wtjChargeRequestVO.setBillUsrid(supplyOrder.getItemUid());
		wtjChargeRequestVO.setClientBillId(supplyOrder.getId() + "");

		SupplyResult<String> chargeResult = MultiThreadedHttpConnection.getInstance()
				.sendPostByMapLogin(wtjChargeRequestVO.createUrl(),
						wtjChargeRequestVO.createParams());
		result.setStatus(chargeResult.getStatus());
		result.setResultMsg(chargeResult.getResultMsg());

		if (chargeResult.isSuccess()) {
			if (chargeResult.getModule() == null) {
				logger.error("result is null");
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("result is null");
				return result;
			}

			SAXReader reader = new SAXReader();
			Document document = null;
			try {
				document = reader.read(new StringReader(chargeResult.getModule()));
				Element root = document.getRootElement();
				String code = root.elementText("error_code");
				String errorDetail = root.elementText("error_detail");
				String billId = root.elementText("bill_id");
				supplyOrder.setUpstreamSerialno(billId);
				if ("0".equals(code)) {
					// 受理成功
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					bizDealService.submitQueryRunnable(new WtjCardForwardQueryRunnable(supplyName,
							supplyOrder.getId(), supplyOrder.getBizOrderId()));
				} else {
					// 受理失败
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultCode(code);
					result.setResultMsg(errorDetail);
					return result;
				}
			} catch (DocumentException e) {
				logger.error("parse error : ", e);
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("parse error");
				return result;
			}
		}

		return result;
	}
}
