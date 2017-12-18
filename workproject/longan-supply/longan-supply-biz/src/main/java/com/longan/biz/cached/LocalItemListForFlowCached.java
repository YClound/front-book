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

public class LocalItemListForFlowCached extends LocalCached<String, List<Item>> {
	private static final String SPLIT = "_";
	private static final String USE_NATIONWIDE = "USE_ALL_";
	private static final String USE_AREA = "USE_AREA_";

	@Resource
	private ItemDAO itemDAO;

	@Resource
	private AreaInfoService areaInfoService;

	@Override
	public void init() {
		super.init(60l, 60l);
	}

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
			list.add(Constants.BizInfo.CODE_FLOW_MOBILE);
			list.add(Constants.BizInfo.CODE_FLOW_TELECOM);
			list.add(Constants.BizInfo.CODE_FLOW_UNICOM);
			itemExample.createCriteria().andStatusEqualTo(Constants.Item.STATUS_UP)
					.andBizIdIn(list);

			List<Item> itemList = (List<Item>) itemDAO.selectByExample(itemExample);
			for (Item item : itemList) {
				if (item.isNationwide() && arealist != null) { // 归属全国的
					if (item.isFlowItemUsableArea()) { // 使用范围本省
						for (AreaInfo areaInfo : arealist) {
							String provinceCode = areaInfo.getProvinceCode();
							String key = getCachedUseAreaKey(provinceCode, item.getCarrierType());
							if (cached.containsKey(key)) {
								cached.get(key).add(item);
							} else {
								List<Item> items = new ArrayList<Item>();
								items.add(item);
								cached.put(key, items);
							}
						}
					} else {
						for (AreaInfo areaInfo : arealist) {
							List<Item> items = null;
							String provinceCode = areaInfo.getProvinceCode();
							String key = getCachedUseNationwideKey(provinceCode,
									item.getCarrierType());
							if (cached.containsKey(key)) {
								cached.get(key).add(item);
							} else {
								items = new ArrayList<Item>();
								items.add(item);
								cached.put(key, items);
							}
						}

					}
				} else { // 归于省域,省域商品会覆盖全国商品。
					if (item.isFlowItemUsableArea()) { // 使用范围本省
						List<String> salesAreaList = item.getSalesAreaList();
						if (salesAreaList != null) {
							for (String provinceCode : salesAreaList) {
								String key = getCachedUseAreaKey(provinceCode,
										item.getCarrierType());
								List<Item> items = null;
								if (cached.containsKey(key)) {
									cached.get(key).add(item);
								} else {
									items = new ArrayList<Item>();
									items.add(item);
									cached.put(key, items);
								}
							}
						}
					} else {
						List<String> salesAreaList = item.getSalesAreaList();
						if (salesAreaList != null) {
							for (String provinceCode : salesAreaList) {
								List<Item> items = null;
								String key = getCachedUseNationwideKey(provinceCode,
										item.getCarrierType());
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
			}
		} catch (SQLException e) {
			logger.error("reload Item For Flow Cached error", e);
		}
	}

	public static String getCachedUseAreaKey(String provinceCode, Integer carrierType) {
		return USE_AREA + provinceCode + SPLIT + carrierType;
	}

	public static String getCachedUseNationwideKey(String provinceCode, Integer carrierType) {
		return USE_NATIONWIDE + provinceCode + SPLIT + carrierType;
	}
}
