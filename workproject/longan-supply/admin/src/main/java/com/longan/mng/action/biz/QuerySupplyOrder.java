package com.longan.mng.action.biz;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.core.ItemService;
import com.longan.biz.core.QueryService;
import com.longan.biz.core.UserService;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.dataobject.SupplyOrderQuery;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;
import com.longan.mng.action.common.BaseController;

@Controller
public class QuerySupplyOrder extends BaseController {

	@Resource
	private QueryService queryService;

	@Resource
	private UserService userService;

	@Resource
	private ItemService itemService;

	@Resource
	private LocalCachedService localCachedService;

	@RequestMapping("biz/querySupplyOrderIndex")
	public String index(@ModelAttribute("supplyOrderQuery") SupplyOrderQuery supplyOrderQuery,
			HttpSession session, Model model) {
		// 业务权限判断
		UserInfo userInfo = super.getUserInfo(session);

		if (!checkBizAuth(supplyOrderQuery.getBizId(), userInfo)) {
			return "error/autherror";
		}

		model.addAttribute("userInfo", userInfo);

		this.setSelectValue(userInfo, model, supplyOrderQuery);
		supplyOrderQuery.setStartGmtCreate(new Date());
		supplyOrderQuery.setEndGmtCreate(new Date());
		return "biz/querySupplyOrder";
	}

	@RequestMapping("biz/querySupplyOrder")
	public String doQuery(@ModelAttribute("supplyOrderQuery") SupplyOrderQuery supplyOrderQuery,
			Model model, HttpSession session) {
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(supplyOrderQuery.getBizId(), userInfo)) {
			return "error/autherror";
		}

		model.addAttribute("userInfo", userInfo);

		if (supplyOrderQuery.getId() != null || supplyOrderQuery.getBizOrderId() != null) {
			supplyOrderQuery.setEndGmtCreate(null);
			supplyOrderQuery.setStartGmtCreate(null);
		} else {
			Date date = null;
			try {
				date = DateTool.strintToDate(DateTool.parseDate(new Date()));
			} catch (ParseException e) {
			}
			if (supplyOrderQuery.getEndGmtCreate() == null) {
				supplyOrderQuery.setEndGmtCreate(date);
			}
			if (supplyOrderQuery.getStartGmtCreate() == null) {
				supplyOrderQuery.setStartGmtCreate(date);
			}
			// if (!check2Date(supplyOrderQuery.getStartGmtCreate(),
			// supplyOrderQuery.getEndGmtCreate())) {
			// super.alertErrorNoneBack(model, "订单时间区间不能超过2个月");
			// return "biz/querySupplyOrder";
			// }
		}

		setSelectValue(userInfo, model, supplyOrderQuery);

		Result<List<SupplyOrder>> result = queryService.querySupplyOrderPage(supplyOrderQuery);
		if (result.isSuccess()) {
			List<SupplyOrder> list = result.getModule();
			model.addAttribute("supplyOrderList", list);

			if (list != null) {
				for (SupplyOrder supplyOrder : list) {
					if (supplyOrder.getSupplyTraderId() != null) {
						UserInfo traderUserInfo = localCachedService.getUserInfo(supplyOrder
								.getSupplyTraderId());
						if (traderUserInfo != null) {
							supplyOrder.setSupplyTraderName(traderUserInfo.getUserName());
						}
					}
					if (supplyOrder.getSupplyType() == null
							&& supplyOrder.getItemSupplyId() != null) {
						ItemSupply itemSupply = localCachedService.getItemSupply(supplyOrder
								.getItemSupplyId());
						if (itemSupply != null) {
							supplyOrder.setSupplyType(itemSupply.getItemSupplyType());
						}
					}
				}
			}

		} else {
			super.setError(model, result.getResultMsg());
		}
		return "biz/querySupplyOrder";
	}

	void setSelectValue(UserInfo userInfo, Model model, SupplyOrderQuery supplyOrderQuery) {

		model.addAttribute("bizName", Constants.BIZ_MAP.get(supplyOrderQuery.getBizId()));
		model.addAttribute("bizId", supplyOrderQuery.getBizId());

		if (userInfo.isDownStreamUser()) { // 下游不能查看
			return;
		} else {
			Result<List<UserInfo>> dsUserResult = userService.queryUserInfoDownStream();
			if (dsUserResult.isSuccess() && dsUserResult.getModule() != null) {
				Map<Long, String> map = new HashMap<Long, String>();
				for (UserInfo user : dsUserResult.getModule()) {
					map.put(user.getId(), user.getUserName());
				}
				model.addAttribute("downStreamUser", map);
			}
		}

		Result<List<UserInfo>> usUserResult = userService.queryUserInfoUpStream();
		if (usUserResult.isSuccess() && usUserResult.getModule() != null) {
			Map<Long, String> map = new HashMap<Long, String>();
			for (UserInfo user : usUserResult.getModule()) {
				map.put(user.getId(), user.getUserName());
			}
			model.addAttribute("upStreamUser", map);
		}

		Result<List<Item>> itemResult = itemService.queryItemList(supplyOrderQuery.getBizId());

		if (itemResult.isSuccess() && itemResult.getModule() != null) {
			Map<Integer, String> map = new HashMap<Integer, String>();
			for (Item item : itemResult.getModule()) {
				map.put(item.getId(), item.getItemName());
			}
			model.addAttribute("itemList", map);
		}

	}

	@ModelAttribute("supplyStatusList")
	public Map<String, String> supplyStatusList() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.SupplyOrder.STATUS_INIT + "", Constants.SupplyOrder.STATUS_INIT_DESC);
		map.put(Constants.SupplyOrder.STATUS_CHARGING + "",
				Constants.SupplyOrder.STATUS_CHARGING_DESC);
		map.put(Constants.SupplyOrder.STATUS_FAILED + "", Constants.SupplyOrder.STATUS_FAILED_DESC);
		map.put(Constants.SupplyOrder.STATUS_SUCCESS + "",
				Constants.SupplyOrder.STATUS_SUCCESS_DESC);
		map.put(Constants.SupplyOrder.STATUS_LOCK + "", Constants.SupplyOrder.STATUS_LOCK_DESC);
		map.put(Constants.SupplyOrder.STATUS_UNCONFIRMED + "",
				Constants.SupplyOrder.STATUS_UNCONFIRMED_DESC);
		map.put(Constants.SupplyOrder.STATUS_EXCEPTION + "",
				Constants.SupplyOrder.STATUS_EXCEPTION_DESC);
		return map;
	}
}
