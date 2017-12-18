package com.longan.biz.core;

import java.util.List;

import com.longan.biz.dataobject.ItemPrice;
import com.longan.biz.dataobject.ItemPriceQuery;
import com.longan.biz.domain.Result;

public interface ItemPriceService {

	Result<List<ItemPrice>> queryItemPricePage(ItemPriceQuery itemPriceQuery);
	
	Result<Boolean> adjustPrice(ItemPrice itemPrice);
	
	Result<Boolean> batchAdjustPrice(List<ItemPrice> itemPriceList);
	
	public Result<List<ItemPrice>> queryItemPriceExport(ItemPriceQuery itemPriceQuery);

}
