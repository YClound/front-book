package com.longan.biz.core;

import java.util.List;

import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.ItemQuery;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.ItemSupplyQuery;
import com.longan.biz.domain.Result;

public interface ItemService {
	public Result<Item> createNewItem(Item item);

	public Result<Item> getItem(Integer itemId);

	public Result<List<Item>> queryItemList(Integer bizId);

	public Result<Boolean> upItem(Integer itemId);

	public Result<Boolean> downItem(Integer itemId);

	public Result<Boolean> updateItem(Item item);

	public Result<Boolean> upItem(List<Integer> ids);

	public Result<Boolean> downItem(List<Integer> ids);
	
	public Result<Boolean> upItemSupply(List<Integer> ids);

	public Result<Boolean> downItemSupply(List<Integer> ids);

	public Result<Integer> getPriceByAcct(Item item, AcctInfo acctInfo);

	public Result<Boolean> checkItem(BizOrder bizOrder);

	public Result<Boolean> checkPrice(BizOrder bizOrder,Item item);

	public Result<Boolean> checkItemStatus(Integer itemId);

	public Result<List<Item>> queryItemList(ItemQuery itemQuery);

	public Result<List<ItemSupply>> queryItemSupplyOnSale(Integer itemId);

	public Result<List<ItemSupply>> queryItemSupplyByBizId(Integer bizId);

	public Result<List<ItemSupply>> queryStockItemSupplyByBiz(Integer bizId);

	public Result<List<ItemSupply>> queryItemSuppyOrderByItemSetting(Item item);
	
	public Result<ItemSupply> filterItemSupply(Item item,BizOrder bizOrder);

	public Result<Boolean> createItem(Item item);

	public Result<Boolean> createItemSupply(ItemSupply itemSupply);

	public Result<ItemSupply> getItemSupply(Long itemSupplyId);

	public Result<Boolean> updateItemSupply(ItemSupply itemSupply);

	public Result<List<ItemSupply>> queryItemSupplyPage(ItemSupplyQuery itemSupplyQuery);

	public Result<Boolean> upItemSupply(Long itemSupplyId);

	public Result<Boolean> downItemSupply(Long itemSupplyId);

	public Result<List<Item>> queryItemList();
}
