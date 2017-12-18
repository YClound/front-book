package com.longan.mng.action.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.longan.biz.core.GameService;
import com.longan.biz.core.ItemService;
import com.longan.biz.core.UserService;
import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.AreaInfo;
import com.longan.biz.dataobject.Game;
import com.longan.biz.dataobject.GameQuery;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.ItemQuery;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.mng.action.common.BaseController;

@Controller
public class QueryGameItem extends BaseController {
	@Resource
	private UserService userService;

	@Resource
	private ItemService itemService;

	@Resource
	private GameService gameService;

	@RequestMapping("biz/queryGameItem")
	public String doQuery(@ModelAttribute("itemQuery") ItemQuery itemQuery, Model model,
			HttpSession session) {
		// 业务 权限判断
		UserInfo userInfo = super.getUserInfo(session);
		if (!checkBizAuth(itemQuery.getBizId(), userInfo)) {
			return "error/autherror";
		}

		model.addAttribute("bizName", Constants.BIZ_MAP.get(itemQuery.getBizId()));
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("isFlow", super.isFlow(itemQuery.getBizId()));

		Result<List<Item>> result = itemService.queryItemList(itemQuery);
		if (result.isSuccess()) {
			List<Item> itemList = result.getModule();
			// 代理商显示价格特殊处理
			if (userInfo.isDownStreamUser()) {
				AcctInfo acctInfo = localCachedService.getAcctInfoNot4Trade(userInfo.getAcctId());
				if (itemList != null) {
					for (Item item : itemList) {
						Result<Integer> priceResult = itemService.getPriceByAcct(item, acctInfo);
						if (priceResult.isSuccess()) {
							item.setItemSalesPrice(priceResult.getModule());
						}
					}
				}
			}
			if (itemList != null) {
				for (Item item : itemList) {
					setItemArea(item);
					Game game = localCachedService
							.getGame(new Long((long) item.getItemCategoryId()));
					if (game != null) {
						item.setGameName(game.getGameName());
					}
				}
			}

			model.addAttribute("itemList", result.getModule());
		} else {
			super.setError(model, result.getResultMsg());
		}
		return "biz/queryGameItem";
	}

	@ModelAttribute("statusList")
	public Map<Integer, String> statusList() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(Constants.Item.STATUS_DEL, Constants.Item.STATUS_DEL_DESC);
		map.put(Constants.Item.STATUS_DOWN, Constants.Item.STATUS_DOWN_DESC);
		map.put(Constants.Item.STATUS_INIT, Constants.Item.STATUS_INIT_DESC);
		map.put(Constants.Item.STATUS_UP, Constants.Item.STATUS_UP_DESC);
		return map;
	}

	@ModelAttribute("provinceList")
	public Map<String, AreaInfo> provinceList() {
		return localCachedService.getProvinceMap();
	}

	@ModelAttribute("gameNameList")
	public Map<Long, String> downStreamUser() {
		Result<List<Game>> gameResult = gameService.queryAllGame(new GameQuery());
		if (gameResult.isSuccess() && gameResult.getModule() != null) {
			Map<Long, String> map = new HashMap<Long, String>();
			for (Game game : gameResult.getModule()) {
				map.put(game.getId(), game.getGameName());
			}
			return map;
		} else {
			return null;
		}
	}
}
