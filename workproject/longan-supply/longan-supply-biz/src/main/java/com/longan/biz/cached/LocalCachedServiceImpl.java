package com.longan.biz.cached;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.longan.biz.core.BaseService;
import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.AreaInfo;
import com.longan.biz.dataobject.Game;
import com.longan.biz.dataobject.GameCarrier;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.OperationInfo;
import com.longan.biz.dataobject.TraderInfo;
import com.longan.biz.dataobject.UserInfo;

public class LocalCachedServiceImpl extends BaseService implements LocalCachedService {

	@Resource
	private LocalAuthCached localAuthCached;

	@Resource
	private LocalAcctInfoCached localAcctInfoCached;

	@Resource
	private LocalBizAuthCached localBizAuthCached;

	@Resource
	private LocalCatalogAuthCached localCatalogAuthCached;

	@Resource
	private LocalItemCached localItemCached;

	@Resource
	private LocalItemSupplyCached localItemSupplyCached;

	@Resource
	private LocalUserInfoCached localUserInfoCached;

	@Resource
	private LocalAreaInfoCached localAreaInfoCached;

	@Resource
	private LocalTraderInfoCached localTraderInfoCached;

	@Resource
	private LocalItemForPhoneCached localItemForPhoneCached;

	@Resource
	private LocalOperationInfoCached localOperationInfoCached;

	@Resource
	private LocalItemListForPhoneCached localItemListForPhoneCached;

	@Resource
	private LocalItemListForFlowCached localItemListForFlowCached;

	@Resource
	private LocalGameCarrierCached localGameCarrierCached;

	@Resource
	private LocalGameCached localGameCached;

	@Override
	public Item getItem(Integer id) {
		if (id == null) {
			return null;
		}
		return localItemCached.get(id);
	}

	@Override
	public AcctInfo getAcctInfoNot4Trade(Long id) {
		if (id == null) {
			return null;
		}
		return localAcctInfoCached.get(id);
	}

	@Override
	public UserInfo getUserInfo(Long id) {
		if (id == null) {
			return null;
		}
		return localUserInfoCached.get(id);
	}

	@Override
	public Set<String> getAuthUrlByUserId(Long id) {
		if (id == null) {
			return null;
		}
		return localAuthCached.get(id);
	}

	@Override
	public Set<Integer> getAuthBizByUserId(Long id) {
		if (id == null) {
			return null;
		}
		return localBizAuthCached.get(id);
	}

	@Override
	public Set<String> getAuthCatalogByUserId(Long id) {
		if (id == null) {
			return null;
		}
		return localCatalogAuthCached.get(id);
	}

	@Override
	public ItemSupply getItemSupply(Long id) {
		if (id == null) {
			return null;
		}
		return localItemSupplyCached.get(id);
	}

	@Override
	public Map<String, AreaInfo> getProvinceMap() {
		return localAreaInfoCached.getMap();
	}

	@Override
	public AreaInfo getProvinceByCode(String code) {
		if (code == null) {
			return null;
		}
		return localAreaInfoCached.get(code);
	}

	@Override
	public TraderInfo getTraderInfo(Long userId) {
		if (userId == null) {
			return null;
		}
		return localTraderInfoCached.get(userId);
	}

	@Override
	public Item getItemForPhone(String key) {
		if (key == null) {
			return null;
		}
		return localItemForPhoneCached.get(key);
	}

	@Override
	public OperationInfo getOperationInfo(String operationUrl) {
		if (operationUrl == null) {
			return null;
		}
		return localOperationInfoCached.get(operationUrl);
	}

	@Override
	public List<TraderInfo> getTraderInfoList() {
		return localTraderInfoCached.getList();
	}

	@Override
	public List<Item> getItemListForPhone(String key) {
		return localItemListForPhoneCached.get(key);
	}

	@Override
	public List<Item> getItemListForFlow(String key) {
		return localItemListForFlowCached.get(key);
	}

	@Override
	public GameCarrier getGameCarrier(Long id) {
		if (id == null) {
			return null;
		}
		return localGameCarrierCached.get(id);
	}

	@Override
	public Game getGame(Long id) {
		if (id == null) {
			return null;
		}
		return localGameCached.get(id);
	}

}
