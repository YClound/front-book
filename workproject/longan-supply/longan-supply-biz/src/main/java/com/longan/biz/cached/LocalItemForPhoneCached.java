package com.longan.biz.cached;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import com.longan.biz.dao.ItemDAO;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.ItemExample;
import com.longan.biz.utils.Constants;

public class LocalItemForPhoneCached extends LocalCached<String, Item> {
	private static final String SPLIT = "_";
	private static final String NATIONWIDE = "ALL";

	@Resource
	private ItemDAO itemDAO;

	@Override
	public void init() {
		super.init(60l, 60l);
	}

	@SuppressWarnings("unchecked")
	@Override
	void reloadFromDb(ConcurrentMap<String, Item> cached) {
		ItemExample itemExample = new ItemExample();
		List<Integer> list = new ArrayList<Integer>();
		list.add(Constants.BizInfo.CODE_PHONE_MOBILE);
		list.add(Constants.BizInfo.CODE_PHONE_TELECOM);
		list.add(Constants.BizInfo.CODE_PHONE_UNICOM);

		itemExample.createCriteria().andStatusEqualTo(Constants.Item.STATUS_UP).andBizIdIn(list);

		try {
			List<Item> itemList = (List<Item>) itemDAO.selectByExample(itemExample);
			for (Item item : itemList) {
				if (item.isNationwide()) {
					cached.put(
							getNationwideCachedKey(item.getItemFacePrice(), item.getCarrierType()),
							item);
				} else {
					List<String> salesAreaList = item.getSalesAreaList();
					if (salesAreaList != null) {
						for (String provinceCode : salesAreaList) {
							cached.put(
									getCachedKey(provinceCode, item.getItemFacePrice(),
											item.getCarrierType()), item);
						}
					}
				}
			}
		} catch (SQLException e) {
			logger.error("reload Item For PhoneCached error", e);
		}
	}

	public static String getCachedKey(String provinceCode, Integer itemFacePrice,
			Integer carrierType) {
		return provinceCode + SPLIT + itemFacePrice + SPLIT + carrierType;
	}

	public static String getNationwideCachedKey(Integer itemFacePrice, Integer carrierType) {
		return NATIONWIDE + SPLIT + itemFacePrice + SPLIT + carrierType;
	}
}
