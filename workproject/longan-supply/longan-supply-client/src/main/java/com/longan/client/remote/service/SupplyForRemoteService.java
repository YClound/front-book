package com.longan.client.remote.service;

import java.util.List;

import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.domain.Result;
import com.longan.client.remote.domain.web.BuyItem;
import com.longan.client.remote.domain.web.BuyFacePrice;
import com.longan.client.remote.domain.web.FlowQuery;
import com.longan.client.remote.domain.web.PhoneQuery;

public interface SupplyForRemoteService {
	/**
	 * 人工补充的供货操作。
	 * 
	 * @param bizOrder
	 * @return
	 */
	Result<SupplyOrder> supply(BizOrder bizOrder);

	/**
	 * 用户通过已知商品下单
	 * 
	 * @param userId
	 * @param item
	 * @return
	 */
	Result<BizOrder> buy(BuyItem buyItem);

	/**
	 * 用户根据话费充值金额下单
	 * 
	 * @param buyPhone
	 * @return
	 */
	Result<BizOrder> buyByFacePrice(BuyFacePrice buyFacePrice);

	/**
	 * 手机号查询商品信息
	 * 
	 * @param uid
	 * @return
	 */
	Result<PhoneQuery> queryPhoneItem(String uid, AcctInfo acctInfo);

	/**
	 * 手机号查询流量商品信息
	 * 
	 * @param uid
	 * @return
	 */
	Result<FlowQuery> queryFlowItem(String uid, Integer type, AcctInfo acctInfo);

	Result<Integer> bantchBuyFlowByFacePrice(List<BuyFacePrice> list);

	Result<Integer> bantchBuyPhoneByFacePrice(List<BuyFacePrice> list);

}
