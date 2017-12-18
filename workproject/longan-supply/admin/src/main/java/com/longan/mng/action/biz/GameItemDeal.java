package com.longan.mng.action.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.core.GameService;
import com.longan.biz.core.ItemService;
import com.longan.biz.core.OperationLogService;
import com.longan.biz.dataobject.AreaInfo;
import com.longan.biz.dataobject.Game;
import com.longan.biz.dataobject.GameQuery;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.OperationLog;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.BigDecimalUtils;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.OperLogUtils;
import com.longan.mng.action.common.BaseController;
import com.longan.mng.form.GameItemForm;

@Controller
public class GameItemDeal extends BaseController {
	@Resource
	private ItemService itemService;

	@Resource
	private Validator validator;

	@Resource
	private LocalCachedService localCachedService;

	@Resource
	private OperationLogService operationLogService;

	@Resource
	private GameService gameService;

	@RequestMapping(value = "biz/gameItemAdd", method = RequestMethod.GET)
	public String onAddIndex(@RequestParam("bizId") Integer bizId, HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(bizId, userInfo)) {
			return "error/autherror";
		}
		model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
		model.addAttribute("bizId", bizId);
		initForm(model, bizId);
		return "biz/gameItemAdd";
	}

	private void initForm(Model model, Integer bizId) {
		if (isFlow(bizId)) {
			Map<String, String> map = new HashMap<String, String>(2);
			map.put(Constants.BizFlow.ITEM_USABLE_NATIONWIDE + "",
					Constants.BizFlow.ITEM_USABLE_NATIONWIDE_DESC);
			map.put(Constants.BizFlow.ITEM_USABLE_AREA + "",
					Constants.BizFlow.ITEM_USABLE_AREA_DESC);
			model.addAttribute("usableAreaList", map);
		}
	}

	@RequestMapping(value = "biz/gameItemAdd", method = RequestMethod.POST)
	public String onPostAdd(@ModelAttribute("gameItemForm") GameItemForm gameItemForm,
			BindingResult bindingResult, HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(Integer.parseInt(gameItemForm.getBizId()), userInfo)) {
			return "error/autherror";
		}
		model.addAttribute("bizName",
				Constants.BIZ_MAP.get(Integer.parseInt(gameItemForm.getBizId())));
		model.addAttribute("bizId", gameItemForm.getBizId());
		initForm(model, Integer.parseInt(gameItemForm.getBizId()));

		validator.validate(gameItemForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return "biz/gameItemAdd";
		}

		// 对salesAreaList做特殊验证
		if (String.valueOf(Constants.Item.SALE_TYPE_AREA).equals(gameItemForm.getSalesAreaType())
				&& ((gameItemForm.getSalesAreaList() == null || gameItemForm.getSalesAreaList()
						.isEmpty()))) {
			FieldError error = new FieldError("itemForm", "salesAreaList", "至少选择一个销售省域");
			bindingResult.addError(error);
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return "biz/gameItemAdd";
		}

		Item item = formToItem(gameItemForm);
		Result<Boolean> result = null;
		result = itemService.createItem(item);
		if (result != null && result.isSuccess()) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (HashMap<String, String>) session
					.getAttribute("requestInfoMap");
			OperationLog operationLog = OperLogUtils.operationLogDeal(null, item, userInfo,
					map.get("moduleName"), item.getBizId(), map.get("loginIp"));
			operationLogService.createOperationLog(operationLog);
			super.alertSuccess(model, "queryGameItem.do?bizId=" + gameItemForm.getBizId() + "&id="
					+ item.getId());
		} else {
			super.alertError(model, result == null ? "操作失败" : result.getResultMsg());// ???
		}
		return "biz/gameItemAdd";
	}

	@RequestMapping(value = "biz/gameItemEdit", method = RequestMethod.GET)
	public String onEditIndex(@RequestParam("id") Integer id, @RequestParam("bizId") Integer bizId,
			HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(bizId, userInfo)) {
			return "error/autherror";
		}
		model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
		model.addAttribute("bizId", bizId);
		initForm(model, bizId);
		Result<Item> result = itemService.getItem(id);
		if (result.isSuccess()) {
			Item item = result.getModule();
			if (item != null) {
				model.addAttribute("item", item);
			} else {
				super.alertError(model, "没有该商品");
			}
		} else {
			super.alertError(model, result.getResultMsg());
		}
		return "biz/gameItemEdit";
	}

	@RequestMapping(value = "biz/gameItemEdit", method = RequestMethod.POST)
	public String onPostEdit(@ModelAttribute("gameItemForm") GameItemForm gameItemForm,
			BindingResult bindingResult, HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(Integer.parseInt(gameItemForm.getBizId()), userInfo)) {
			return "error/autherror";
		}
		model.addAttribute("bizName",
				Constants.BIZ_MAP.get(Integer.parseInt(gameItemForm.getBizId())));
		model.addAttribute("bizId", gameItemForm.getBizId());
		model.addAttribute("isFlow", super.isFlow(Integer.parseInt(gameItemForm.getBizId())));
		initForm(model, Integer.parseInt(gameItemForm.getBizId()));

		if (gameItemForm.getId() == null) {
			super.alertError(model, "商品编号不能为空");
			return "biz/gameItemEdit";
		}
		Result<Item> itemResult = itemService.getItem(Integer.parseInt(gameItemForm.getId()));
		if (!itemResult.isSuccess()) {
			super.alertError(model, itemResult.getResultMsg());
			return "biz/gameItemEdit";
		}
		if (itemResult.getModule() == null) {
			super.alertError(model, "没有该商品");
			return "biz/gameItemEdit";
		}
		Item older = itemResult.getModule();
		model.addAttribute("item", itemResult.getModule());

		validator.validate(gameItemForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return "biz/gameItemEdit";
		}

		// 对salesAreaList做特殊验证
		if (String.valueOf(Constants.Item.SALE_TYPE_AREA).equals(gameItemForm.getSalesAreaType())
				&& ((gameItemForm.getSalesAreaList() == null || gameItemForm.getSalesAreaList()
						.isEmpty()))) {
			FieldError error = new FieldError("itemForm", "salesAreaList", "至少选择一个销售省域");
			bindingResult.addError(error);
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return "biz/gameItemEdit";
		}

		Item itemUpdate = formToItem(gameItemForm);
		Result<Boolean> result = itemService.updateItem(itemUpdate);
		if (result.isSuccess()) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (HashMap<String, String>) session
					.getAttribute("requestInfoMap");
			OperationLog operationLog = OperLogUtils.operationLogDeal(older, itemUpdate, userInfo,
					map.get("moduleName"), itemUpdate.getBizId(), map.get("loginIp"));
			operationLogService.createOperationLog(operationLog);
			super.alertSuccess(model, "queryGameItem.do?bizId=" + gameItemForm.getBizId() + "&id="
					+ itemUpdate.getId());
			return "biz/gameItemEdit";
		} else {
			super.alertError(model, result.getResultMsg());
			return "biz/gameItemEdit";
		}
	}

	private Item formToItem(GameItemForm gameItemForm) {
		Item result = new Item();
		if (StringUtils.isNotEmpty(gameItemForm.getId())) {
			result.setId(Integer.parseInt(gameItemForm.getId()));
		}
		result.setItemName(gameItemForm.getItemName());
		result.setItemType(Constants.Item.TYPE_ITEM_CHARGE);// 游戏只为直充
		result.setBizId(Integer.parseInt(gameItemForm.getBizId()));
		result.setCarrierType(Constants.Item.CARRIER_TYPE_OTHER);// 运营商类型为其他
		result.setItemCategoryId(Integer.parseInt(gameItemForm.getItemCategoryId()));
		result.setNumberList(gameItemForm.getNumberList());
		result.setExchargeRatio(Integer.parseInt(gameItemForm.getExchargeRatio()));
		result.setItemUnit(gameItemForm.getItemUnit());
		if (String.valueOf(Constants.Item.SALE_TYPE_AREA).equals(gameItemForm.getSalesAreaType())) {
			StringBuffer sb = new StringBuffer("");
			for (String areaCode : gameItemForm.getSalesAreaList()) {
				sb.append(areaCode).append(Constants.Item.SALES_AREA_SPLIT);
			}
			result.setSalesArea(sb.toString().substring(0, sb.toString().length() - 1));
		} else {
			result.setSalesArea(""); // 空表示全国
		}

		if (StringUtils.isNotEmpty(gameItemForm.getCanSync())
				&& "1".equals(gameItemForm.getCanSync())) {
			result.setCanSync(true);
		} else {
			result.setCanSync(false);
		}

		// 判断价格是否为空,不为空则*1000
		if (StringUtils.isNotBlank(gameItemForm.getItemFacePrice())) {
			Integer price = BigDecimalUtils.multInteger(gameItemForm.getItemFacePrice());
			if (price > 0) {
				result.setItemFacePrice(price);
			}
		}
		if (StringUtils.isNotBlank(gameItemForm.getItemSalesPrice())) {
			Integer price = BigDecimalUtils.multInteger(gameItemForm.getItemSalesPrice());
			if (price > 0) {
				result.setItemSalesPrice(price);
			}
		}
		if (StringUtils.isNotBlank(gameItemForm.getItemSalesPrice2())) {
			Integer price = BigDecimalUtils.multInteger(gameItemForm.getItemSalesPrice2());
			if (price > 0) {
				result.setItemSalesPrice2(price);
			}
		}
		if (StringUtils.isNotBlank(gameItemForm.getItemSalesPrice3())) {
			Integer price = BigDecimalUtils.multInteger(gameItemForm.getItemSalesPrice3());
			if (price > 0) {
				result.setItemSalesPrice3(price);
			}
		}
		if (StringUtils.isNotBlank(gameItemForm.getItemSalesPrice4())) {
			Integer price = BigDecimalUtils.multInteger(gameItemForm.getItemSalesPrice4());
			if (price > 0) {
				result.setItemSalesPrice4(price);
			}
		}
		if (StringUtils.isNotBlank(gameItemForm.getItemExt1())) {
			result.setItemExt1(gameItemForm.getItemExt1().trim());
		}
		if (StringUtils.isNotBlank(gameItemForm.getItemExt2())) {
			result.setItemExt2(gameItemForm.getItemExt2().trim());
		}
		return result;
	}

	@ModelAttribute("salesAreaTypeList")
	public Map<Integer, String> salesAreaTypeList() {
		Map<Integer, String> salesAreaTypeList = new HashMap<Integer, String>();
		salesAreaTypeList.put(Constants.Item.SALE_TYPE_NATIONWIDE,
				Constants.Item.SALE_TYPE_NATIONWIDE_DESC);
		salesAreaTypeList.put(Constants.Item.SALE_TYPE_AREA, Constants.Item.SALE_TYPE_AREA_DESC);
		return salesAreaTypeList;
	}

	@ModelAttribute("areaInfoList")
	public Map<String, String> areaInfoList() {
		Map<String, String> result = new HashMap<String, String>();
		Map<String, AreaInfo> map = localCachedService.getProvinceMap();
		if (map == null) {
			return result;
		}
		for (Entry<String, AreaInfo> e : map.entrySet()) {
			result.put(e.getKey(), e.getValue().getProvinceName());
		}
		return result;
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
