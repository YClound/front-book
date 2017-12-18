package com.longan.getway.upstream.unicomflow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.dataobject.Stock;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.client.remote.domain.CardChargeInfo;
import com.longan.client.remote.domain.CardCheck;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.common.UpstreamCardSupplyService;
import com.longan.getway.upstream.unicomflow.vo.UnicomFlowRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class UnicomFlowServiceImpl implements UpstreamCardSupplyService {
	private static final Logger logger = LoggerFactory.getLogger(UnicomFlowServiceImpl.class);
	private boolean isAsync = false; // 同步供货 traderInfo 中也有保存该信息，供货的时候以这个为准。

	private static Map<String, String> queryResultMap = new HashMap<String, String>(4);

	static {
		queryResultMap.put("00", "成功");
		queryResultMap.put("01", "失败");
		queryResultMap.put("09", "充值中");
	}

	@Override
	public SupplyResult<CardCheck> cardCheck(Stock stock) {
		SupplyResult<CardCheck> result = new SupplyResult<CardCheck>();
		UnicomFlowRequestVO request = new UnicomFlowRequestVO();
		request.setCardNum(stock.getCardId());
		request.setMoney(1);
		request.setPassword(stock.getCardPwd());
		request.settId("111");

		SupplyResult<Document> httpResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByGet(request.getCardCheckURL());

		if (httpResult.isSuccess()) {
			Document document = httpResult.getModule();
			Element root = document.getRootElement();
			logger.warn("response: " + root.asXML());

			// 查询结果，不管卡是否有效，都是成功
			result.setStatus(SupplyResult.STATUS_SUCCESS);

			if ("SUCCESS".equals(root.elementText("status"))) {
				String code = root.elementText("code");
				String desc = root.elementText("desc");
				CardCheck unicomFlowCardCheck = new CardCheck();
				unicomFlowCardCheck.setCode(code);
				unicomFlowCardCheck.setDesc(desc);
				result.setModule(unicomFlowCardCheck);
			} else {
				String code = root.elementText("failCode");
				String desc = root.elementText("failDesc");
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultCode(code);
				result.setResultMsg(desc);
			}
		} else if (httpResult.isUnConfirm()) {
			result.setStatus(SupplyResult.STATUS_UNCONFIRM);
			result.setResultMsg("请求超时");
		} else {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg(httpResult.getResultMsg());
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SupplyResult<List<CardChargeInfo>> cardInfoQuery(Stock stock) {
		SupplyResult<List<CardChargeInfo>> result = new SupplyResult<List<CardChargeInfo>>();
		UnicomFlowRequestVO request = new UnicomFlowRequestVO();
		request.setCardNum(stock.getCardId());
		request.setMoney(1);
		request.setPassword(stock.getCardPwd());
		request.settId("222");

		SupplyResult<Document> httpResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByGet(request.getCardQueryURL());

		if (httpResult.isSuccess()) {
			Document document = httpResult.getModule();
			Element root = document.getRootElement();
			logger.warn("response: " + root.asXML());

			// 查询结果，不管卡是否有效，都是成功
			result.setStatus(SupplyResult.STATUS_SUCCESS);

			if ("SUCCESS".equals(root.elementText("status"))) {
				Element element = root.element("chargeInfos");

				List<CardChargeInfo> list = new ArrayList<CardChargeInfo>();

				if (element != null) {
					Iterator<Element> chargeInfos = (Iterator<Element>) element.elementIterator();
					if (chargeInfos != null) {
						while (chargeInfos.hasNext()) {
							Element chargeInfoElement = chargeInfos.next();
							CardChargeInfo cardChargeInfo = new CardChargeInfo();
							cardChargeInfo.setServiceNum(chargeInfoElement
									.elementText("serviceNum"));
							cardChargeInfo.setMoney(chargeInfoElement.elementText("money"));
							cardChargeInfo.setStatus(chargeInfoElement.elementText("result"));
							cardChargeInfo.setTime(chargeInfoElement.elementText("time"));
							cardChargeInfo.setStatusDesc(queryResultMap.get(cardChargeInfo
									.getStatus()));
							list.add(cardChargeInfo);
						}
					}
				}
				result.setModule(list);
			} else {
				String code = root.elementText("failCode");
				String desc = root.elementText("failDesc");
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultCode(code);
				result.setResultMsg(desc);
			}
		} else if (httpResult.isUnConfirm()) {
			result.setStatus(SupplyResult.STATUS_UNCONFIRM);
			result.setResultMsg("请求超时");
		} else {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg(httpResult.getResultMsg());
		}

		return result;
	}

	@Override
	public SupplyResult<SupplyOrder> chargeRequest(SupplyOrder supplyOrder, Stock stock) {
		SupplyResult<SupplyOrder> result = new SupplyResult<SupplyOrder>();
		result.setModule(supplyOrder);
		result.setAsync(isAsync);

		UnicomFlowRequestVO request = new UnicomFlowRequestVO();
		request.setCardNum(stock.getCardId());
		request.setMoney(1);
		request.setPassword(stock.getCardPwd());
		request.setServiceNum(Long.parseLong(supplyOrder.getItemUid()));
		request.settId(supplyOrder.getId() + "");

		SupplyResult<Document> httpResult = MultiThreadedHttpConnection.getInstance()
				.sendDataByGet(request.getChargeURL());
		result.setStatus(httpResult.getStatus());
		result.setResultMsg(httpResult.getResultMsg());

		if (httpResult.isSuccess()) {
			Document doc = httpResult.getModule();
			Element root = doc.getRootElement();
			logger.warn("response: " + root.asXML());
			if ("SUCCESS".equals(root.elementText("status"))) {
				String code = root.elementText("code");
				String desc = root.elementText("desc");
				if ("00".equals(code)) {
					// do nothing
				} else if ("11".equals(code) || "04".equals(code)) {
					result.setStatus(SupplyResult.STATUS_UNCONFIRM);
					result.setResultCode(code);
					result.setResultMsg("充值处理中");
				} else if ("07".equals(code) || "08".equals(code) || "09".equals(code)
						|| "10".equals(code) || "12".equals(code) || "13".equals(code)
						|| "14".equals(code)) {
					result.setStatus(SupplyResult.STATUS_CARD_INVALID);
					result.setResultCode(code);
					result.setResultMsg(desc);
				} else {
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultCode(code);
					result.setResultMsg(desc);
				}
			} else {
				String code = root.elementText("failCode");
				String desc = root.elementText("failDesc");
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultCode(code);
				result.setResultMsg(desc);
			}
		} else if (httpResult.isUnConfirm()) {
			result.setStatus(SupplyResult.STATUS_UNCONFIRM);
			result.setResultMsg("充值处理中");
		} else {
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("网络连接失败");
		}
		return result;
	}

}
