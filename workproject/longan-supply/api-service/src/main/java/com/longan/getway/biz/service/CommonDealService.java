package com.longan.getway.biz.service;

import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.domain.Result;
import com.longan.client.remote.domain.web.BuyItem;
import com.longan.client.remote.domain.web.BuyFacePrice;
import com.longan.client.remote.domain.web.FlowQuery;
import com.longan.client.remote.domain.web.PhoneQuery;
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
import com.longan.getway.biz.common.response.BuyResponse;
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

public interface CommonDealService {
	public BuyResponse buy(BuyRequest buyRequest, Integer supplyType);

	public QueryBizOrderResponse queryResponse(QueryBizOrderRequest queryBizOrderRequest);

	public QueryBalanceResponse queryBalance(QueryBalanceRequest queryBalanceRequest);

	public Response checkSign(Request request);

	public QueryItemResponse queryItem(QueryItemRequest queryItemRequest);

	public QueryItemListResponse queryItemList(QueryItemListRequest queryItemListRequest);

	public BuyResponse telephoneBuy(TelephoneBuyRequest buyRequest);
	
	public Result<BizOrder> buyFromWeb(BuyItem buyItem);

	public Result<BizOrder> buyPhoneFromWeb(BuyFacePrice buyPhone);

	/**
	 * 通过手机号查询话费商品
	 * 
	 * @param uid
	 * @return
	 */
	public Result<PhoneQuery> queryPhoneItem(String uid, AcctInfo acctInfo);

	/**
	 * 通过手机号查询流量商品
	 * 
	 * @param uid
	 * @return
	 */
	public Result<FlowQuery> queryFlowItem(String uid, Integer useableType, AcctInfo acctInfo);

	public BuyResponse buyGame(GameBuyRequest buyRequest, Integer supplyType);

	public QueryGameCarrierResponse queryGameCarrier(QueryGameCarrierRequest queryGameCarrierRequest);

	public QueryGameListResponse queryGameList(QueryGameListRequest queryGameListRequest);

	public QueryGameResponse queryGame(QueryGameRequest queryGameRequest);

	public QueryGameItemListResponse queryGameItemList(
			QueryGameItemListRequest queryGameItemListRequest);

	public QueryGameItemResponse queryGameItem(QueryGameItemRequest queryGameItemRequest);
	
	public Result<BizOrder> buyFlowFromWeb(BuyFacePrice buyFacePrice);
	
	public BuyResponse flowBuy(FlowBuyRequest buyRequest);
}
