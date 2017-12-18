package com.longan.mng.action.biz;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.core.ItemService;
import com.longan.biz.core.OperationLogService;
import com.longan.biz.core.PayService;
import com.longan.biz.core.TaskService;
import com.longan.biz.dataobject.AreaInfo;
import com.longan.biz.dataobject.Item;
import com.longan.biz.dataobject.ItemQuery;
import com.longan.biz.dataobject.OperationLog;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.BigDecimalUtils;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.OperLogUtils;
import com.longan.mng.action.common.BaseController;
import com.longan.mng.form.ItemForm;
import com.longan.mng.form.UpDownForm;

@Controller
public class ItemDeal extends BaseController {
	@Resource
	private ItemService itemService;

	@Resource
	private PayService payService;

	@Resource
	private Validator validator;

	@Resource
	private LocalCachedService localCachedService;

	@Resource
	private OperationLogService operationLogService;

	@Resource
	private TaskService taskService;

	@RequestMapping(value = "biz/itemDeal", params = "requestType=up")
	public String onRequestUp(@RequestParam("itemId") Integer itemId,
			@RequestParam("bizId") Integer bizId, HttpSession session, Model model,
			HttpServletRequest request) {
		String refererUrl = getRefererUrlUnPath(request);
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(bizId, userInfo)) {
			return "error/autherror";
		}

		Result<Item> itemResult = itemService.getItem(itemId);
		Item older;
		if (!itemResult.isSuccess()) {
			super.alertError(model, itemResult.getResultMsg());
			return refererUrl;
		} else {
			if (itemResult.getModule() == null) {
				super.alertError(model, "商品为空");
				return refererUrl;
			}
			older = itemResult.getModule();
		}
		logger.warn(userInfo.getUserName() + "执行上架操作 商品 id : " + itemId);
		Result<Boolean> result = itemService.upItem(itemId);
		if (result.isSuccess() && result.getModule()) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (HashMap<String, String>) session
					.getAttribute("requestInfoMap");
			OperationLog operationLog = OperLogUtils.operationLogDeal(older,
					itemService.getItem(itemId).getModule(), userInfo, map.get("moduleName")
							+ "(上架)", bizId, map.get("loginIp"));
			operationLogService.createOperationLog(operationLog);
			alertSuccess(model, refererUrl.replace("biz/", "") + ".do?bizId=" + bizId + "&id="
					+ itemId);
		} else {
			alertError(model, result.getResultMsg());
		}

		return refererUrl;
	}

	@RequestMapping(value = "biz/itemDeal", params = "requestType=down")
	public String onRequestDown(@RequestParam("itemId") Integer itemId,
			@RequestParam("bizId") Integer bizId, HttpSession session, Model model,
			HttpServletRequest request) {
		String refererUrl = getRefererUrlUnPath(request);
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(bizId, userInfo)) {
			return "error/autherror";
		}

		Result<Item> itemResult = itemService.getItem(itemId);
		Item older;
		if (!itemResult.isSuccess()) {
			super.alertError(model, itemResult.getResultMsg());
			return refererUrl;
		} else {
			if (itemResult.getModule() == null) {
				super.alertError(model, "商品为空");
				return refererUrl;
			}
			older = itemResult.getModule();
		}
		logger.warn(userInfo.getUserName() + "执行下架操作 商品 id : " + itemId);
		Result<Boolean> result = itemService.downItem(itemId);
		if (result.isSuccess() && result.getModule()) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (HashMap<String, String>) session
					.getAttribute("requestInfoMap");
			OperationLog operationLog = OperLogUtils.operationLogDeal(older,
					itemService.getItem(itemId).getModule(), userInfo, map.get("moduleName")
							+ "(下架)", bizId, map.get("loginIp"));
			operationLogService.createOperationLog(operationLog);
			alertSuccess(model, refererUrl.replace("biz/", "") + ".do?bizId=" + bizId + "&id="
					+ itemId);
		} else {
			alertError(model, result.getResultMsg());
		}
		return refererUrl;
	}

	@RequestMapping(value = "biz/itemDeal", params = "requestType=batchDownIndex")
	public String onRequestBatchDownIndex(@RequestParam("ids") String ids,
			@RequestParam("bizId") Integer bizId, HttpSession session, Model model,
			HttpServletRequest request) {
		String refererUrl = getRefererUrlUnPath(request);
		model.addAttribute("refererUrl", refererUrl);
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(bizId, userInfo)) {
			return "error/autherror";
		}

		model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
		model.addAttribute("bizId", bizId);

		UpDownForm upDownForm = new UpDownForm();
		upDownForm.setIds(ids);
		upDownForm.setUrl("itemDeal.do");
		upDownForm.setRequestType("batchDown");
		upDownForm.setTypeDown();
		upDownForm.setModuleName("商品");
		upDownForm.setBizId(bizId + "");

		model.addAttribute("upDownForm", upDownForm);
		return "biz/batchUpDown";
	}

	@RequestMapping(value = "biz/itemDeal", params = "requestType=conditionDownIndex", method = RequestMethod.POST)
	public String onRequestConditionDownIndex(@ModelAttribute("itemQuery") ItemQuery itemQuery,
			@RequestParam("bizId") Integer bizId, @RequestParam("requestType") String requestType,
			HttpSession session, Model model,HttpServletRequest request) {
		String refererUrl = getRefererUrlUnPath(request);
		model.addAttribute("refererUrl", refererUrl);
		UserInfo userInfo = super.getUserInfo(session);

		model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
		model.addAttribute("bizId", bizId);

		// 业务权限判断
		if (!checkBizAuth(bizId, userInfo)) {
			return "error/autherror";
		}

		itemQuery.setPageSize(10000);
		itemQuery.setCurrentPage(1);
		Result<List<Item>> result = itemService.queryItemList(itemQuery);

		if (!result.isSuccess()) {
			super.alertError(model, result.getResultMsg());
			return refererUrl;
		}

		StringBuffer ids = new StringBuffer("");
		StringBuffer names = new StringBuffer("");
		List<Item> list = result.getModule();
		if (list == null || list.isEmpty()) {
			super.alertError(model, "商品列表不能为空");
			return refererUrl;
		}
		for (int i = 0; i < list.size(); i++) {
			ids.append(list.get(i).getId());
			names.append(list.get(i).getItemName());
			if (i != (list.size() - 1)) {
				ids.append(",");
				names.append(" ，");
			}
		}
		UpDownForm upDownForm = new UpDownForm();
		upDownForm.setIds(ids.toString().trim());
		upDownForm.setNames(names.toString().trim());
		upDownForm.setUrl("itemDeal.do");
		upDownForm.setRequestType("batchDown");
		upDownForm.setTypeDown();
		upDownForm.setBizId(bizId + "");
		upDownForm.setModuleName("商品");
		model.addAttribute("upDownForm", upDownForm);

		return "biz/batchUpDown";
	}

	@RequestMapping(value = "biz/itemDeal", params = "requestType=batchDown", method = RequestMethod.POST)
	public String onRequestBatchDown(@ModelAttribute("upDownForm") UpDownForm upDownForm,
			@RequestParam("refererUrl") String refererUrl, BindingResult bindingResult,
			HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		final String returnUrl = "biz/batchUpDown";

		validator.validate(upDownForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			model.addAttribute("bizName", Constants.BIZ_MAP.get(upDownForm.getBizId()));
			model.addAttribute("bizId", upDownForm.getBizId());
			return returnUrl;
		}

		logger.warn(userInfo.getUserName() + "执行批量下架操作 商品 id : " + upDownForm.getIds());

		model.addAttribute("bizName", Constants.BIZ_MAP.get(upDownForm.getBizId()));
		model.addAttribute("bizId", upDownForm.getBizId());

		// 业务权限判断
		if (!checkBizAuth(Integer.parseInt(upDownForm.getBizId()), userInfo)) {
			return "error/autherror";
		}

		String[] strs = upDownForm.getIds().split(",");
		if (strs == null || strs.length == 0) {
			super.alertError(model, "商品列表不能为空");
			return returnUrl;
		}

		List<Integer> list = new ArrayList<Integer>();
		for (String str : strs) {
			if (StringUtils.isNumeric(str)) {
				list.add(Integer.parseInt(str));
			}
		}
		if (list.isEmpty()) {
			super.alertError(model, "商品列表不能为空");
			return returnUrl;
		}

		String description = upDownForm.toString();
		OperationLog operationLog = OperLogUtils.operationLogDeal(description, userInfo,
				getModuleNameFromSession(session), null, getIpFromSession(session),
				Constants.OperationLog.TYPE_UPDATE);

		Result<Long> result = super.submitTaskForm(upDownForm, "itemService", "downItem",
				(Serializable) list, "java.util.List", operationLog, userInfo,
				Integer.parseInt(upDownForm.getBizId()));

		if (!result.isSuccess()) {
			super.alertError(model, result.getResultMsg());
			return returnUrl;
		}

		if (upDownForm.isRealTimeCommit()) {
			alertSuccess(model,
					refererUrl.replace("biz/", "") + ".do?bizId=" + upDownForm.getBizId());
		} else {
			if (result.getModule() == null) {
				alertSuccess(model, "../system/queryTask.do");
			} else {
				alertSuccess(model, "../system/queryTask.do?id=" + result.getModule());
			}
		}

		return returnUrl;
	}

	@RequestMapping(value = "biz/itemDeal", params = "requestType=batchUpIndex")
	public String onRequestBatchUpIndex(@RequestParam("ids") String ids,
			@RequestParam("bizId") Integer bizId, HttpSession session, Model model,
			HttpServletRequest request) {
		String refererUrl = getRefererUrlUnPath(request);
		model.addAttribute("refererUrl", refererUrl);
		UserInfo userInfo = super.getUserInfo(session);

		model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
		model.addAttribute("bizId", bizId);

		// 业务权限判断
		if (!checkBizAuth(bizId, userInfo)) {
			return "error/autherror";
		}

		UpDownForm upDownForm = new UpDownForm();
		upDownForm.setIds(ids);
		upDownForm.setUrl("itemDeal.do");
		upDownForm.setRequestType("batchUp");
		upDownForm.setTypeOn();
		upDownForm.setBizId(bizId + "");
		upDownForm.setModuleName("商品");
		model.addAttribute("upDownForm", upDownForm);

		return "biz/batchUpDown";
	}

	@RequestMapping(value = "biz/itemDeal", params = "requestType=conditionUpIndex", method = RequestMethod.POST)
	public String onRequestConditionUpIndex(@ModelAttribute("itemQuery") ItemQuery itemQuery,
			@RequestParam("bizId") Integer bizId, @RequestParam("requestType") String requestType,
			HttpSession session, Model model, HttpServletRequest request) {
		String refererUrl = getRefererUrlUnPath(request);
		model.addAttribute("refererUrl", refererUrl);
		UserInfo userInfo = super.getUserInfo(session);

		model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
		model.addAttribute("bizId", bizId);

		// 业务权限判断
		if (!checkBizAuth(bizId, userInfo)) {
			return "error/autherror";
		}

		itemQuery.setPageSize(10000);
		itemQuery.setCurrentPage(1);
		Result<List<Item>> result = itemService.queryItemList(itemQuery);

		if (!result.isSuccess()) {
			super.alertError(model, result.getResultMsg());
			return refererUrl;
		}

		StringBuffer ids = new StringBuffer("");
		StringBuffer names = new StringBuffer("");
		List<Item> list = result.getModule();
		if (list == null || list.isEmpty()) {
			super.alertError(model, "商品列表不能为空");
			return refererUrl;
		}
		for (int i = 0; i < list.size(); i++) {
			ids.append(list.get(i).getId());
			names.append(list.get(i).getItemName()).append(" ，");
			if (i != (list.size() - 1)) {
				ids.append(",");
			}
		}
		UpDownForm upDownForm = new UpDownForm();
		upDownForm.setIds(ids.toString().trim());
		upDownForm.setNames(names.toString().trim());
		upDownForm.setUrl("itemDeal.do");
		upDownForm.setRequestType("batchUp");
		upDownForm.setTypeOn();
		upDownForm.setBizId(bizId + "");
		upDownForm.setModuleName("商品");
		model.addAttribute("upDownForm", upDownForm);

		return "biz/batchUpDown";
	}

	@RequestMapping(value = "biz/itemDeal", params = "requestType=batchUp", method = RequestMethod.POST)
	public String onRequestBatchUp(@ModelAttribute("upDownForm") UpDownForm upDownForm,
			@RequestParam("refererUrl") String refererUrl, BindingResult bindingResult,
			HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		final String returnUrl = "biz/batchUpDown";

		validator.validate(upDownForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			model.addAttribute("bizName", Constants.BIZ_MAP.get(upDownForm.getBizId()));
			model.addAttribute("bizId", upDownForm.getBizId());
			return returnUrl;
		}

		logger.warn(userInfo.getUserName() + "执行批量上架操作 商品 id : " + upDownForm.getIds());

		model.addAttribute("bizName", Constants.BIZ_MAP.get(upDownForm.getBizId()));
		model.addAttribute("bizId", upDownForm.getBizId());

		// 业务权限判断
		if (!checkBizAuth(Integer.parseInt(upDownForm.getBizId()), userInfo)) {
			return "error/autherror";
		}

		String[] strs = upDownForm.getIds().split(",");
		if (strs == null || strs.length == 0) {
			super.alertError(model, "商品列表不能为空");
			return returnUrl;
		}

		List<Integer> list = new ArrayList<Integer>();
		for (String str : strs) {
			if (StringUtils.isNumeric(str)) {
				list.add(Integer.parseInt(str));
			}
		}
		if (list.isEmpty()) {
			super.alertError(model, "商品列表不能为空");
			return returnUrl;
		}

		String description = upDownForm.toString();
		OperationLog operationLog = OperLogUtils.operationLogDeal(description, userInfo,
				getModuleNameFromSession(session), null, getIpFromSession(session),
				Constants.OperationLog.TYPE_UPDATE);

		Result<Long> result = super.submitTaskForm(upDownForm, "itemService", "upItem",
				(Serializable) list, "java.util.List", operationLog, userInfo,
				Integer.parseInt(upDownForm.getBizId()));

		if (!result.isSuccess()) {
			super.alertError(model, result.getResultMsg());
			return returnUrl;
		}

		if (upDownForm.isRealTimeCommit()) {
			alertSuccess(model,
					refererUrl.replace("biz/", "") + ".do?bizId=" + upDownForm.getBizId());
		} else {
			if (result.getModule() == null) {
				alertSuccess(model, "../system/queryTask.do");
			} else {
				alertSuccess(model, "../system/queryTask.do?id=" + result.getModule());
			}
		}

		return returnUrl;
	}

	@RequestMapping(value = "biz/itemAdd", method = RequestMethod.GET)
	public String onAddIndex(@RequestParam("bizId") Integer bizId, HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(bizId, userInfo)) {
			return "error/autherror";
		}
		model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
		model.addAttribute("bizId", bizId);
		model.addAttribute("isFlow", super.isFlow(bizId));
		initForm(model, bizId);
		return "biz/itemAdd";
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

	@RequestMapping(value = "biz/itemAdd", method = RequestMethod.POST)
	public String onPostAdd(@ModelAttribute("itemForm") ItemForm itemForm,
			BindingResult bindingResult, HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(Integer.parseInt(itemForm.getBizId()), userInfo)) {
			return "error/autherror";
		}
		model.addAttribute("bizName", Constants.BIZ_MAP.get(Integer.parseInt(itemForm.getBizId())));
		model.addAttribute("bizId", itemForm.getBizId());
		model.addAttribute("isFlow", super.isFlow(Integer.parseInt(itemForm.getBizId())));
		initForm(model, Integer.parseInt(itemForm.getBizId()));

		validator.validate(itemForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return "biz/itemAdd";
		}

		// 对salesAreaList做特殊验证
		if (String.valueOf(Constants.Item.SALE_TYPE_AREA).equals(itemForm.getSalesAreaType())
				&& ((itemForm.getSalesAreaList() == null || itemForm.getSalesAreaList().isEmpty()))) {
			FieldError error = new FieldError("itemForm", "salesAreaList", "至少选择一个销售省域");
			bindingResult.addError(error);
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return "biz/itemAdd";
		}

		if (super.isFlow(Integer.parseInt(itemForm.getBizId()))) {
			if (StringUtils.isBlank(itemForm.getItemExt1())) {
				FieldError error = new FieldError("itemForm", "itemExt1", "必须填写流量大小");
				bindingResult.addError(error);
				model.addAttribute("errorList", bindingResult.getAllErrors());
				return "biz/itemAdd";
			}

			if (StringUtils.isBlank(itemForm.getItemExt2())) {
				FieldError error = new FieldError("itemForm", "itemExt2", "必须选择流量包使用范围");
				bindingResult.addError(error);
				model.addAttribute("errorList", bindingResult.getAllErrors());
				return "biz/itemAdd";
			}
		}

		Item item = formToItem(itemForm);
		Result<Boolean> result = null;
		result = itemService.createItem(item);
		if (result != null && result.isSuccess()) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (HashMap<String, String>) session
					.getAttribute("requestInfoMap");
			OperationLog operationLog = OperLogUtils.operationLogDeal(null, item, userInfo,
					map.get("moduleName"), item.getBizId(), map.get("loginIp"));
			operationLogService.createOperationLog(operationLog);
			super.alertSuccess(model,
					"queryItem.do?bizId=" + itemForm.getBizId() + "&id=" + item.getId());
		} else {
			super.alertError(model, result == null ? "操作失败" : result.getResultMsg());// ???
		}
		return "biz/itemAdd";
	}

	@RequestMapping(value = "biz/itemEdit", method = RequestMethod.GET)
	public String onEditIndex(@RequestParam("id") Integer id, @RequestParam("bizId") Integer bizId,
			HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(bizId, userInfo)) {
			return "error/autherror";
		}
		model.addAttribute("bizName", Constants.BIZ_MAP.get(bizId));
		model.addAttribute("bizId", bizId);
		model.addAttribute("isFlow", super.isFlow(bizId));
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
		return "biz/itemEdit";
	}

	@RequestMapping(value = "biz/itemEdit", method = RequestMethod.POST)
	public String onPostEdit(@ModelAttribute("itemForm") ItemForm itemForm,
			BindingResult bindingResult, HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(Integer.parseInt(itemForm.getBizId()), userInfo)) {
			return "error/autherror";
		}
		model.addAttribute("bizName", Constants.BIZ_MAP.get(Integer.parseInt(itemForm.getBizId())));
		model.addAttribute("bizId", itemForm.getBizId());
		model.addAttribute("isFlow", super.isFlow(Integer.parseInt(itemForm.getBizId())));
		initForm(model, Integer.parseInt(itemForm.getBizId()));

		if (itemForm.getId() == null) {
			super.alertError(model, "商品编号不能为空");
			return "biz/itemEdit";
		}
		Result<Item> itemResult = itemService.getItem(Integer.parseInt(itemForm.getId()));
		if (!itemResult.isSuccess()) {
			super.alertError(model, itemResult.getResultMsg());
			return "biz/itemEdit";
		}
		if (itemResult.getModule() == null) {
			super.alertError(model, "没有该商品");
			return "biz/itemEdit";
		}
		Item older = itemResult.getModule();
		model.addAttribute("item", itemResult.getModule());

		validator.validate(itemForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return "biz/itemEdit";
		}

		// 对salesAreaList做特殊验证
		if (String.valueOf(Constants.Item.SALE_TYPE_AREA).equals(itemForm.getSalesAreaType())
				&& ((itemForm.getSalesAreaList() == null || itemForm.getSalesAreaList().isEmpty()))) {
			FieldError error = new FieldError("itemForm", "salesAreaList", "至少选择一个销售省域");
			bindingResult.addError(error);
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return "biz/itemEdit";
		}

		// 流量包特殊判断
		if (super.isFlow(Integer.parseInt(itemForm.getBizId()))) {
			if (StringUtils.isBlank(itemForm.getItemExt1())) {
				FieldError error = new FieldError("itemForm", "itemExt1", "必须填写流量大小");
				bindingResult.addError(error);
				model.addAttribute("errorList", bindingResult.getAllErrors());
				return "biz/itemEdit";
			}

			if (StringUtils.isBlank(itemForm.getItemExt2())) {
				FieldError error = new FieldError("itemForm", "itemExt2", "必须选择流量包使用范围");
				bindingResult.addError(error);
				model.addAttribute("errorList", bindingResult.getAllErrors());
				return "biz/itemEdit";
			}
		}

		Item itemUpdate = formToItem(itemForm);
		Result<Boolean> result = itemService.updateItem(itemUpdate);
		if (result.isSuccess()) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (HashMap<String, String>) session
					.getAttribute("requestInfoMap");
			OperationLog operationLog = OperLogUtils.operationLogDeal(older, itemUpdate, userInfo,
					map.get("moduleName"), itemUpdate.getBizId(), map.get("loginIp"));
			operationLogService.createOperationLog(operationLog);
			super.alertSuccess(model, "queryItem.do?bizId=" + itemForm.getBizId() + "&id="
					+ itemUpdate.getId());
			return "biz/itemEdit";
		} else {
			super.alertError(model, result.getResultMsg());
			return "biz/itemEdit";
		}
	}

	private Item formToItem(ItemForm itemForm) {
		Item result = new Item();
		if (StringUtils.isNotEmpty(itemForm.getId())) {
			result.setId(Integer.parseInt(itemForm.getId()));
		}
		result.setItemName(itemForm.getItemName());
		result.setItemType(Integer.parseInt(itemForm.getItemType()));
		result.setBizId(Integer.parseInt(itemForm.getBizId()));
		result.setCarrierType(Integer.parseInt(itemForm.getCarrierType()));
		if (String.valueOf(Constants.Item.SALE_TYPE_AREA).equals(itemForm.getSalesAreaType())) {
			StringBuffer sb = new StringBuffer("");
			for (String areaCode : itemForm.getSalesAreaList()) {
				sb.append(areaCode).append(Constants.Item.SALES_AREA_SPLIT);
			}
			result.setSalesArea(sb.toString().substring(0, sb.toString().length() - 1));
		} else {
			result.setSalesArea(""); // 空表示全国
		}

		if (StringUtils.isNotEmpty(itemForm.getCanSync()) && "1".equals(itemForm.getCanSync())) {
			result.setCanSync(true);
		} else {
			result.setCanSync(false);
		}

		// 判断价格是否为空,不为空则*1000
		if (StringUtils.isNotBlank(itemForm.getItemFacePrice())) {
			Integer price = BigDecimalUtils.multInteger(itemForm.getItemFacePrice());
			if (price > 0) {
				result.setItemFacePrice(price);
			}
		}
		if (StringUtils.isNotBlank(itemForm.getItemSalesPrice())) {
			Integer price = BigDecimalUtils.multInteger(itemForm.getItemSalesPrice());
			if (price > 0) {
				result.setItemSalesPrice(price);
			}
		}
		if (StringUtils.isNotBlank(itemForm.getItemSalesPrice2())) {
			Integer price = BigDecimalUtils.multInteger(itemForm.getItemSalesPrice2());
			if (price > 0) {
				result.setItemSalesPrice2(price);
			}
		}
		if (StringUtils.isNotBlank(itemForm.getItemSalesPrice3())) {
			Integer price = BigDecimalUtils.multInteger(itemForm.getItemSalesPrice3());
			if (price > 0) {
				result.setItemSalesPrice3(price);
			}
		}
		if (StringUtils.isNotBlank(itemForm.getItemSalesPrice4())) {
			Integer price = BigDecimalUtils.multInteger(itemForm.getItemSalesPrice4());
			if (price > 0) {
				result.setItemSalesPrice4(price);
			}
		}
		if (StringUtils.isNotBlank(itemForm.getItemExt1())) {
			result.setItemExt1(itemForm.getItemExt1().trim());
		}
		if (StringUtils.isNotBlank(itemForm.getItemExt2())) {
			result.setItemExt2(itemForm.getItemExt2().trim());
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

	@ModelAttribute("itemTypeList")
	public Map<String, String> itemTypeList() {
		Map<String, String> mapType = new HashMap<String, String>();
		mapType.put(Constants.Item.TYPE_ITEM_CARD + "", Constants.Item.TYPE_ITEM_CARD_DESC);
		mapType.put(Constants.Item.TYPE_ITEM_CHARGE + "", Constants.Item.TYPE_ITEM_CHARGE_DESC);
		return mapType;
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

	@ModelAttribute("carrierTypeList")
	public Map<String, String> carrierTypeList() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.Item.CARRIER_TYPE_UNICOM + "", Constants.Item.CARRIER_TYPE_UNICOM_DESC);
		map.put(Constants.Item.CARRIER_TYPE_MOBILE + "", Constants.Item.CARRIER_TYPE_MOBILE_DESC);
		map.put(Constants.Item.CARRIER_TYPE_TELECOM + "", Constants.Item.CARRIER_TYPE_TELECOM_DESC);
		map.put(Constants.Item.CARRIER_TYPE_OTHER + "", Constants.Item.CARRIER_TYPE_OTHER_DESC);
		return map;
	}

	@ModelAttribute("commitTypeList")
	public Map<String, String> commitTypeList() {
		Map<String, String> map = new HashMap<String, String>(2);
		map.put(Constants.Task.COMMIT_TYPE_REAL_TIME + "",
				Constants.Task.COMMIT_TYPE_REAL_TIME_DESC);
		map.put(Constants.Task.COMMIT_TYPE_TASK + "", Constants.Task.COMMIT_TYPE_TASK_DESC);
		return map;
	}

	@InitBinder
	public void formInitBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
