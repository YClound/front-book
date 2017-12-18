package com.longan.mng.action.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.longan.biz.core.ItemService;
import com.longan.biz.core.OperationLogService;
import com.longan.biz.core.TraderInfoService;
import com.longan.biz.core.UserService;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.ItemSupply;
import com.longan.biz.dataobject.OperationLog;
import com.longan.biz.dataobject.TraderInfo;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.BigDecimalUtils;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.OperLogUtils;
import com.longan.mng.action.common.BaseController;
import com.longan.mng.form.GameItemSupplyForm;
import com.longan.mng.form.ItemSupplyForm;

@Controller
public class GameItemSupplyDeal extends BaseController {
	@Resource
	private ItemService itemService;

	@Resource
	private Validator validator;

	@Resource
	private UserService userService;

	@Resource
	private OperationLogService operationLogService;

	@Resource
	private TraderInfoService traderInfoService;

	@RequestMapping(value = "biz/gameItemSupplyAdd", method = RequestMethod.GET)
	public String onAddIndex(@RequestParam("bizId") Integer bizId,
			@RequestParam("itemId") Integer itemId, HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
		model.addAttribute("bizId", bizId);

		// 业务权限判断
		if (!checkBizAuth(bizId, userInfo)) {
			return "error/autherror";
		}

		ItemSupplyForm itemSupplyForm = new ItemSupplyForm();
		model.addAttribute("itemSupplyForm", itemSupplyForm);
		Result<Item> result = itemService.getItem(itemId);
		if (!result.isSuccess()) {
			super.alertError(model, result.getResultMsg());
			return "biz/gameItemSupplyAdd";
		}
		Item item = result.getModule();
		if (item == null) {
			super.alertError(model, "没有该商品");
			return "biz/gameItemSupplyAdd";
		}
		model.addAttribute("item", item);
		return "biz/gameItemSupplyAdd";
	}

	@RequestMapping(value = "biz/gameItemSupplyAdd", method = RequestMethod.POST)
	public String onPostAdd(
			@ModelAttribute("gameItemSupplyForm") GameItemSupplyForm gameItemSupplyForm,
			BindingResult bindingResult, HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		model.addAttribute("bizName",
				Constants.BIZ_MAP.get(Integer.parseInt(gameItemSupplyForm.getBizId())));
		model.addAttribute("bizId", gameItemSupplyForm.getBizId());

		Result<Item> itemResult = itemService.getItem(Integer.parseInt(gameItemSupplyForm
				.getItemId()));
		if (!itemResult.isSuccess()) {
			super.alertError(model, itemResult.getResultMsg());
			return "biz/gameItemSupplyAdd";
		}
		Item item = itemResult.getModule();
		if (item == null) {
			super.alertError(model, "没有该商品");
			return "biz/gameItemSupplyAdd";
		}
		model.addAttribute("item", item);

		validator.validate(gameItemSupplyForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return "biz/gameItemSupplyAdd";
		}
		if ((Constants.ItemSupply.LOSSTYPE_CAN + "").equals(gameItemSupplyForm.getLossType())) {
			if (StringUtils.isEmpty(gameItemSupplyForm.getLossTime())) {
				List<ObjectError> allErrors = new ArrayList<ObjectError>();
				allErrors.add(new FieldError("itemSupplyForm", "lossTime", "损失笔数不能为空"));
				model.addAttribute("errorList", allErrors);
				return "biz/gameItemSupplyAdd";
			}
		}

		// 业务权限判断
		if (!checkBizAuth(Integer.parseInt(gameItemSupplyForm.getBizId()), userInfo)) {
			return "error/autherror";
		}

		ItemSupply itemSupply = formToItemSupply(gameItemSupplyForm);
		Result<Boolean> result = itemService.createItemSupply(itemSupply);
		if (result.isSuccess()) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (HashMap<String, String>) session
					.getAttribute("requestInfoMap");
			OperationLog operationLog = OperLogUtils.operationLogDeal(null, itemSupply, userInfo,
					map.get("moduleName"), itemSupply.getBizId(), map.get("loginIp"));
			operationLogService.createOperationLog(operationLog);
			super.alertSuccess(model,
					"queryGameItemSupply.do?bizId=" + gameItemSupplyForm.getBizId() + "&id="
							+ itemSupply.getId());
		} else {
			super.alertError(model, result.getResultMsg());
		}
		return "biz/gameItemSupplyAdd";
	}

	@RequestMapping(value = "biz/gameItemSupplyEdit", method = RequestMethod.GET)
	public String onEditIndex(@RequestParam("id") long id, @RequestParam("bizId") int bizId,
			HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(bizId, userInfo)) {
			return "error/autherror";
		}
		model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
		model.addAttribute("bizId", bizId);

		Result<ItemSupply> result = itemService.getItemSupply(id);
		if (!result.isSuccess()) {
			super.alertError(model, result.getResultMsg());
			return "biz/gameItemSupplyEdit";
		}
		ItemSupply itemSupply = result.getModule();
		if (itemSupply == null) {
			super.alertError(model, "没有该供货商品");
			return "biz/gameItemSupplyEdit";
		}
		model.addAttribute("itemSupply", itemSupply);

		Result<Item> itemResult = itemService.getItem(itemSupply.getItemId());
		if (!itemResult.isSuccess()) {
			super.alertError(model, itemResult.getResultMsg());
			return "biz/gameItemSupplyAdd";
		}
		Item item = itemResult.getModule();
		if (item == null) {
			super.alertError(model, "没有该商品");
			return "biz/gameItemSupplyAdd";
		}
		model.addAttribute("item", item);

		return "biz/gameItemSupplyEdit";
	}

	@RequestMapping(value = "biz/gameItemSupplyEdit", method = RequestMethod.POST)
	public String onPostEdit(
			@ModelAttribute("gameItemSupplyForm") GameItemSupplyForm gameItemSupplyForm,
			BindingResult bindingResult, HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		model.addAttribute("bizName",
				Constants.BIZ_MAP.get(Integer.parseInt(gameItemSupplyForm.getBizId())));
		model.addAttribute("bizId", gameItemSupplyForm.getBizId());

		if (gameItemSupplyForm.getId() == null) {
			super.alertError(model, "供货商品编号不能为空");
			return "biz/gameItemSupplyEdit";
		}
		Result<ItemSupply> itemSupplyResult = itemService.getItemSupply(Long
				.parseLong(gameItemSupplyForm.getId()));
		if (!itemSupplyResult.isSuccess()) {
			super.alertError(model, itemSupplyResult.getResultMsg());
			return "biz/gameItemSupplyEdit";
		}
		if (itemSupplyResult.getModule() == null) {
			super.alertError(model, "没有该供货商品");
			return "biz/gameItemSupplyEdit";
		}
		ItemSupply older = itemSupplyResult.getModule();
		model.addAttribute("itemSupply", itemSupplyResult.getModule());

		Result<Item> itemResult = itemService.getItem(itemSupplyResult.getModule().getItemId());
		if (!itemResult.isSuccess()) {
			super.alertError(model, itemResult.getResultMsg());
			return "biz/gameItemSupplyAdd";
		}
		Item item = itemResult.getModule();
		if (item == null) {
			super.alertError(model, "没有该商品");
			return "biz/gameItemSupplyAdd";
		}
		model.addAttribute("item", item);

		validator.validate(gameItemSupplyForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return "biz/gameItemSupplyEdit";
		}
		if ((Constants.ItemSupply.LOSSTYPE_CAN + "").equals(gameItemSupplyForm.getLossType())) {
			if (StringUtils.isEmpty(gameItemSupplyForm.getLossTime())) {
				List<ObjectError> allErrors = new ArrayList<ObjectError>();
				allErrors.add(new FieldError("itemSupplyForm", "lossTime", "损失笔数不能为空"));
				model.addAttribute("errorList", allErrors);
				return "biz/gameItemSupplyEdit";
			}
		}
		// 业务权限判断
		if (!checkBizAuth(Integer.parseInt(gameItemSupplyForm.getBizId()), userInfo)) {
			return "error/autherror";
		}

		ItemSupply itemSupply = formToItemSupply(gameItemSupplyForm);

		Result<Boolean> result = itemService.updateItemSupply(itemSupply);
		if (!result.isSuccess()) {
			super.alertError(model, result.getResultMsg());
			return "biz/itemSupplyEdit";
		}
		@SuppressWarnings("unchecked")
		Map<String, String> map = (HashMap<String, String>) session.getAttribute("requestInfoMap");
		OperationLog operationLog = OperLogUtils.operationLogDeal(older, itemSupply, userInfo,
				map.get("moduleName"), itemSupply.getBizId(), map.get("loginIp"));
		operationLogService.createOperationLog(operationLog);
		super.alertSuccess(model, "queryGameItemSupply.do?bizId=" + gameItemSupplyForm.getBizId()
				+ "&id=" + itemSupply.getId());
		return "biz/gameItemSupplyEdit";
	}

	private ItemSupply formToItemSupply(GameItemSupplyForm gameItemSupplyForm) {
		ItemSupply result = new ItemSupply();
		if (StringUtils.isNotEmpty(gameItemSupplyForm.getId())) {
			result.setId(Long.parseLong(gameItemSupplyForm.getId()));
		}

		result.setBizId(Integer.parseInt(gameItemSupplyForm.getBizId()));
		result.setItemId(Integer.parseInt(gameItemSupplyForm.getItemId()));
		result.setPriority(Integer.parseInt(gameItemSupplyForm.getPriority()));
		result.setSupplyTraderId(Long.parseLong(gameItemSupplyForm.getSupplyTraderId()));
		if (StringUtils.isNotEmpty(gameItemSupplyForm.getLossType())) {
			result.setLossType(Integer.parseInt(gameItemSupplyForm.getLossType()));
		} else {
			result.setLossType(Constants.ItemSupply.LOSSTYPE_CANNOT);
		}
		if (StringUtils.isNotEmpty(gameItemSupplyForm.getLossTime())) {
			result.setLossTime(Integer.parseInt(gameItemSupplyForm.getLossTime()));
		}
		if (StringUtils.isNotBlank(gameItemSupplyForm.getItemCostPrice())) {
			Integer price = BigDecimalUtils.multInteger(gameItemSupplyForm.getItemCostPrice());
			if (price > 0) {
				result.setItemCostPrice(price);
			}
		}

		if (StringUtils.isNotEmpty(gameItemSupplyForm.getSupplyProductCode())) {
			result.setSupplyProductCode(gameItemSupplyForm.getSupplyProductCode().trim());
		}
		result.setItemSupplyExt1(gameItemSupplyForm.getItemSupplyExt1());
		result.setItemSupplyExt2(gameItemSupplyForm.getItemSupplyExt2());
		result.setItemSupplyExt3(gameItemSupplyForm.getItemSupplyExt3());
		return result;
	}

	@ModelAttribute("traderInfoList")
	public Map<Long, TraderInfo> traderInfoList() {
		Map<Long, TraderInfo> result = new HashMap<Long, TraderInfo>();
		Result<List<TraderInfo>> queryResult = traderInfoService.queryTraderInfoList();
		if (queryResult.isSuccess() && queryResult.getModule() != null) {
			for (TraderInfo traderInfo : queryResult.getModule()) {
				result.put(traderInfo.getUserId(), traderInfo);
			}
		}
		return result;
	}

	@ModelAttribute("supplyTraderList")
	public Map<Long, String> supplyTraderList() {
		Map<Long, String> result = new HashMap<Long, String>();
		Result<List<UserInfo>> usUserResult = userService.queryUserInfoUpStream();
		if (usUserResult.isSuccess() && usUserResult.getModule() != null) {
			for (UserInfo user : usUserResult.getModule()) {
				result.put(user.getId(), user.getUserName());
			}
		}
		return result;
	}

	@ModelAttribute("lossTypeList")
	public Map<String, String> lossTypeList() {
		Map<String, String> mapLossType = new LinkedHashMap<String, String>();
		mapLossType.put(Constants.ItemSupply.LOSSTYPE_CANNOT + "",
				Constants.ItemSupply.LOSSTYPE_CANNOT_DESC);
		mapLossType.put(Constants.ItemSupply.LOSSTYPE_CAN + "",
				Constants.ItemSupply.LOSSTYPE_CAN_DESC);
		return mapLossType;
	}
}
