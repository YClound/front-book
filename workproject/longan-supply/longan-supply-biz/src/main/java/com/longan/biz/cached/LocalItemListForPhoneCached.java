package com.longan.biz.cached;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import com.longan.biz.core.AreaInfoService;
import com.longan.biz.dao.ItemDAO;
import com.longan.biz.dataobject.AreaInfo;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.ItemExample;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;

public class LocalItemListForPhoneCached extends LocalCached<String, List<Item>> {
	private static final String SPLIT = "_";

	@Resource
	private ItemDAO itemDAO;

	@Override
	public void init() {
		super.init(60l, 60l);
	}

	@Resource
	private AreaInfoService areaInfoService;

	@SuppressWarnings("unchecked")
	@Override
	void reloadFromDb(ConcurrentMap<String, List<Item>> cached) {
		try {
			Result<List<AreaInfo>> queryAllProvinceReuslt = areaInfoService.queryAllProvince();
			if (!queryAllProvinceReuslt.isSuccess()) {
				logger.error("reloadFromDb error : " + queryAllProvinceReuslt.getResultMsg());
			}
			List<AreaInfo> arealist = queryAllProvinceReuslt.getModule();
			ItemExample itemExample = new ItemExample();
			List<Integer> list = new ArrayList<Integer>();
			list.add(Constants.BizInfo.CODE_PHONE_MOBILE);
			list.add(Constants.BizInfo.CODE_PHONE_TELECOM);
			list.add(Constants.BizInfo.CODE_PHONE_UNICOM);

			itemExample.createCriteria().andStatusEqualTo(Constants.Item.STATUS_UP)
					.andBizIdIn(list);

			List<Item> itemList = (List<Item>) itemDAO.selectByExample(itemExample);
			for (Item item : itemList) {
				if (item.isNationwide() && arealist != null) {
					for (AreaInfo areaInfo : arealist) {
						List<Item> items = null;
						String key = getCachedKey(areaInfo.getProvinceCode(), item.getCarrierType());
						if (cached.containsKey(key)) {
							cached.get(key).add(item);
						} else {
							items = new ArrayList<Item>();
							items.add(item);
							cached.put(key, items);
						}
					}
				} else { // 省域商品会覆盖全国商品。
					List<String> salesAreaList = item.getSalesAreaList();
					if (salesAreaList != null) {
						for (String provinceCode : salesAreaList) {
							List<Item> items = null;
							String key = getCachedKey(provinceCode, item.getCarrierType());
							if (cached.containsKey(key)) {
								cached.get(key).add(item);
							} else {
								items = new ArrayList<Item>();
								items.add(item);
								cached.put(key, items);
							}
						}
					}
				}

			}
		} catch (SQLException e) {
			logger.error("reload ItemList For PhoneCached error", e);
		}
	}

	public static String getCachedKey(String provinceCode, Integer carrierType) {
		return provinceCode + SPLIT + carrierType;
	}

}
