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

import com.longan.biz.core.ItemService;
import com.longan.biz.core.QueryService;
import com.longan.biz.core.UserService;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.SupplyOrder;
import com.longan.biz.dataobject.SupplyOrderQuery;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.domain.SupplyOrderStatisic;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.DateTool;
import com.longan.mng.action.common.BaseController;

@Controller
public class QueryManualSupplyOrder extends BaseController {
	@Resource
	private QueryService queryService;

	@Resource
	private UserService userService;

	@Resource
	private ItemService itemService;

	@RequestMapping("biz/queryManualSupplyOrder")
	public void doQuery(@ModelAttribute("supplyOrderQuery") SupplyOrderQuery supplyOrderQuery,
			Model model, HttpSession session) {
		UserInfo userInfo = super.getUserInfo(session);
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
			if (!check2Date(supplyOrderQuery.getStartGmtCreate(),
					supplyOrderQuery.getEndGmtCreate())) {
				super.alertErrorNoneBack(model, "供货单时间区间不能超过2个月");
			}
		}
		setSelectValue(userInfo, model, supplyOrderQuery);

		Result<List<SupplyOrder>> result = queryService.querySupplyOrderPage(supplyOrderQuery);
		if (result.isSuccess()) {
			List<SupplyOrder> list = result.getModule();
			if (list != null) {
				for (SupplyOrder supplyOrder : list) {
					if (supplyOrder.getLockOperId() != null) {
						UserInfo oper = localCachedService.getUserInfo(supplyOrder.getLockOperId());
						supplyOrder.setLockOperName(oper == null ? "" : oper.getUserName());
					}
					if (supplyOrder.getDealOperId() != null) {
						UserInfo oper = localCachedService.getUserInfo(supplyOrder.getDealOperId());
						supplyOrder.setDealOperName(oper == null ? "" : oper.getUserName());
					}
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
			model.addAttribute("supplyOrderList", list);
		} else {
			super.setError(model, result.getResultMsg());
		}

		// 订单统计。
		Result<SupplyOrderStatisic> countSupplyOrderResult = queryService
				.countSupplyOrder(supplyOrderQuery);
		if (countSupplyOrderResult.isSuccess()) {
			model.addAttribute("supplyOrderStatisic", countSupplyOrderResult.getModule());
		}
	}

	void setSelectValue(UserInfo userInfo, Model model, SupplyOrderQuery supplyOrderQuery) {

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
	}

	@ModelAttribute("operList")
	public Map<Long, String> operList() {
		Result<List<UserInfo>> adminUserResult = userService.queryUserInfoAdmin();
		Map<Long, String> map = new HashMap<Long, String>();
		if (adminUserResult.isSuccess() && adminUserResult.getModule() != null) {
			for (UserInfo user : adminUserResult.getModule()) {
				map.put(user.getId(), user.getUserName());
			}
		}
		return map;
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

	@ModelAttribute("bizList")
	public Map<Integer, String> bizList() {
		return Constants.getBizMap();
	}

}
