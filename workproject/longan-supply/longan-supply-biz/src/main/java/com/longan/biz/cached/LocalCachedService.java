package com.longan.biz.cached;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.AreaInfo;
import com.longan.biz.dataobject.Game;
import com.longan.biz.dataobject.GameCarrier;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.OperationInfo;
import com.longan.biz.dataobject.TraderInfo;
import com.longan.biz.dataobject.UserInfo;

public interface LocalCachedService {

	Item getItem(Integer id);

	ItemSupply getItemSupply(Long id);

	AcctInfo getAcctInfoNot4Trade(Long id);

	UserInfo getUserInfo(Long id);

	Set<String> getAuthUrlByUserId(Long id);

	Set<Integer> getAuthBizByUserId(Long id);

	Set<String> getAuthCatalogByUserId(Long id);

	Map<String, AreaInfo> getProvinceMap();

	AreaInfo getProvinceByCode(String code);

	TraderInfo getTraderInfo(Long userId);

	Item getItemForPhone(String key);

	OperationInfo getOperationInfo(String operationUrl);

	List<TraderInfo> getTraderInfoList();

	List<Item> getItemListForPhone(String key);

	List<Item> getItemListForFlow(String key);

	GameCarrier getGameCarrier(Long id);

	Game getGame(Long id);
}
