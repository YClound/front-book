package com.longan.mng.action.user;

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

import com.longan.biz.core.OperationLogService;
import com.longan.biz.core.TraderInfoService;
import com.longan.biz.core.UserService;
import com.longan.biz.dataobject.OperationLog;
import com.longan.biz.dataobject.TraderInfo;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.OperLogUtils;
import com.longan.mng.action.common.BaseController;
import com.longan.mng.form.UserInfoForm;

@Controller
public class UserInfoDeal extends BaseController {

	@Resource
	private UserService userService;

	@Resource
	private Validator validator;

	@Resource
	private OperationLogService operationLogService;

	@Resource
	private TraderInfoService traderInfoService;

	@RequestMapping(value = "user/userInfoAdd", method = RequestMethod.GET)
	public void onAddIndex(Model model) {
		setSelectValue(model);
	}

	@RequestMapping(value = "user/userInfoAdd", method = RequestMethod.POST)
	public void onPostAdd(@ModelAttribute("userInfoForm") UserInfoForm userInfoForm,
			BindingResult bindingResult, Model model, HttpSession session) {
		UserInfo userInfom = super.getUserInfo(session);
		setSelectValue(model);
		validator.validate(userInfoForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return;
		}
		if ((Constants.UserInfo.TYPE_UPSTREAM + "").equals(userInfoForm.getType())) {// 上游商户信息验证
			if (StringUtils.isEmpty(userInfoForm.getSupplyType())
					|| userInfoForm.getIsAsyncSupply() == null) {
				List<ObjectError> allErrors = new ArrayList<ObjectError>();
				if (StringUtils.isEmpty(userInfoForm.getSupplyType())) {
					allErrors.add(new FieldError("userInfoForm", "supplyType", "供货类型不能为空"));
				}
				if (userInfoForm.getIsAsyncSupply() == null) {
					allErrors.add(new FieldError("userInfoForm", "isAsyncSupply", "是否异步类型不能为空"));
				}
				model.addAttribute("errorList", allErrors);
				return;
			}
		}
		UserInfo userInfo = formToUserInfo(userInfoForm);
		TraderInfo traderInfo = formToTraderInfo(userInfoForm);
		Result<Boolean> result = null;
		try {
			result = userService.createUserInfo(userInfo, traderInfo);
			if (result.isSuccess() && result.getModule()) {
				@SuppressWarnings("unchecked")
				Map<String, String> map = (HashMap<String, String>) session
						.getAttribute("requestInfoMap");
				OperationLog operationLog = OperLogUtils.operationLogDeal(null, userInfo,
						userInfom, map.get("moduleName"), null, map.get("loginIp"));
				operationLogService.createOperationLog(operationLog);
			}
		} catch (Exception e) {
			logger.error("userInfoAdd error", e);
		}
		if (result != null && result.isSuccess()) {
			super.alertSuccess(model, "queryUserInfo.do");
		} else {
			super.alertError(model, result == null ? "操作失败" : result.getResultMsg());
		}
	}

	@RequestMapping(value = "user/userInfoEdit", method = RequestMethod.GET)
	public void onEditIndex(@RequestParam("id") Long id, Model model) {
		setSelectValue(model);
		Result<UserInfo> result = userService.getUserInfo(id);
		if (result.isSuccess()) {
			UserInfo userInfo = result.getModule();
			if (userInfo != null) {
				model.addAttribute("userInfo", userInfo);
			} else {
				super.alertError(model, "没有该用户");
			}
			if (userInfo.isDownStreamUser() || userInfo.isUpStreamUser()) {
				Result<TraderInfo> traderInfoResult = traderInfoService.getTraderInfoByUserId(id);
				if (traderInfoResult.isSuccess()) {
					TraderInfo traderInfo = traderInfoResult.getModule();
					if (traderInfo != null) {
						model.addAttribute("traderInfo", traderInfo);
					} else {
						super.alertError(model, "没有该商户信息");
					}
				} else {
					super.alertError(model, traderInfoResult.getResultMsg());
				}
			}
		} else {
			super.alertError(model, result.getResultMsg());
		}
	}

	@RequestMapping(value = "user/userInfoEdit", method = RequestMethod.POST)
	public void onPostEdit(@ModelAttribute("userInfoForm") UserInfoForm userInfoForm,
			BindingResult bindingResult, Model model, HttpSession session) {
		UserInfo userInfom = super.getUserInfo(session);
		if (userInfoForm.getId() == null) {
			super.alertError(model, "用户编号不能为空");
			return;
		}
		Result<UserInfo> userInfoResult = userService.getUserInfo(Long.parseLong(userInfoForm
				.getId()));
		UserInfo older = null;
		TraderInfo TraderInfoOlder = null;
		if (!userInfoResult.isSuccess()) {
			super.alertError(model, userInfoResult.getResultMsg());
			return;
		} else {
			UserInfo userInfo = userInfoResult.getModule();
			if (userInfo == null) {
				super.alertError(model, "用户为空");
				return;
			}
			older = userInfo;
			model.addAttribute("userInfo", userInfo);
			if (userInfo.isDownStreamUser() || userInfo.isUpStreamUser()) {
				Result<TraderInfo> TraderInfoResult = traderInfoService
						.getTraderInfoByUserId(userInfo.getId());
				if (!TraderInfoResult.isSuccess()) {
					super.alertError(model, TraderInfoResult.getResultMsg());
					return;
				} else {
					TraderInfo oldTraderInfo = TraderInfoResult.getModule();
					if (oldTraderInfo == null) {
						super.alertError(model, "商户信息为空");
						return;
					}
					TraderInfoOlder = oldTraderInfo;
					model.addAttribute("traderInfo", oldTraderInfo);
				}
			}
		}

		setSelectValue(model);
		validator.validate(userInfoForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return;
		}
		if (userInfoForm.getStatus() == null) {
			List<ObjectError> allErrors = new ArrayList<ObjectError>();
			allErrors.add(new FieldError("userInfoForm", "status", "用户状态不能为空"));
			model.addAttribute("errorList", allErrors);
			return;
		}
		if ((Constants.UserInfo.TYPE_UPSTREAM + "").equals(userInfoForm.getType())) {// 上游商户信息验证
			if (StringUtils.isEmpty(userInfoForm.getSupplyType())
					|| userInfoForm.getTraderInoStatus() == null
					|| userInfoForm.getIsAsyncSupply() == null) {
				List<ObjectError> allErrors = new ArrayList<ObjectError>();
				if (StringUtils.isEmpty(userInfoForm.getSupplyType())) {
					allErrors.add(new FieldError("userInfoForm", "supplyType", "供货类型不能为空"));
				}
				if (userInfoForm.getTraderInoStatus() == null) {
					allErrors.add(new FieldError("userInfoForm", "traderInoStatus", "商户状态不能为空"));
				}
				if (userInfoForm.getIsAsyncSupply() == null) {
					allErrors.add(new FieldError("userInfoForm", "isAsyncSupply", "是否异步类型不能为空"));
				}
				model.addAttribute("errorList", allErrors);
				return;
			}
		}
		if ((Constants.UserInfo.TYPE_DOWNSTREAM + "").equals(userInfoForm.getType())) {// 下游商户信息验证
			if (userInfoForm.getTraderInoStatus() == null) {
				List<ObjectError> allErrors = new ArrayList<ObjectError>();
				if (userInfoForm.getTraderInoStatus() == null) {
					allErrors.add(new FieldError("userInfoForm", "traderInoStatus", "商户状态不能为空"));
				}
				model.addAttribute("errorList", allErrors);
				return;
			}
		}

		UserInfo userInfo = formToUserInfo(userInfoForm);
		TraderInfo traderInfo = null;
		if (userInfo.isDownStreamUser() || userInfo.isUpStreamUser()) {
			traderInfo = formToTraderInfo(userInfoForm);
		}
		Result<Boolean> result = userService.updateUser(userInfo, traderInfo);
		if (result.isSuccess()) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (HashMap<String, String>) session
					.getAttribute("requestInfoMap");
			OperationLog operationLog = OperLogUtils.operationLogDeal(older, userInfo, userInfom,
					map.get("moduleName"), null, map.get("loginIp"));
			operationLogService.createOperationLog(operationLog);
			if (userInfo.isDownStreamUser() || userInfo.isUpStreamUser()) {
				OperationLog operLog = OperLogUtils.operationLogDeal(TraderInfoOlder, traderInfo,
						userInfom, "商户信息修改", null, map.get("loginIp"));
				operationLogService.createOperationLog(operLog);
			}
			super.alertSuccess(model, -2);
			return;
		} else {
			super.alertError(model, result.getResultMsg());
			return;
		}
	}

	@RequestMapping(value = "user/userPwdReset", method = RequestMethod.GET)
	public String onPwdResetIndex(@RequestParam("id") Long id, Model model, HttpSession session) {
		UserInfo userInfom = super.getUserInfo(session);
		Result<UserInfo> userInfoResult = userService.getUserInfo(id);
		UserInfo older;
		if (!userInfoResult.isSuccess()) {
			super.alertError(model, userInfoResult.getResultMsg());
			return "user/queryUserInfo";
		} else {
			UserInfo userInfo = userInfoResult.getModule();
			if (userInfo == null) {
				super.alertError(model, "用户为空");
				return "user/queryUserInfo";
			}
			older = userInfo;
		}
		UserInfo newer = older;
		newer.setPwd(getDefaultPwd());
		Result<Boolean> result = userService.updateUserInfo(newer);
		if (result.isSuccess()) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (HashMap<String, String>) session
					.getAttribute("requestInfoMap");
			OperationLog operationLog = OperLogUtils.operationLogDeal(older, newer, userInfom,
					map.get("moduleName"), null, map.get("loginIp"));
			operationLogService.createOperationLog(operationLog);
			super.alertSuccess(model, "queryUserInfo.do");
			return "user/queryUserInfo";
		} else {
			super.alertError(model, result.getResultMsg());
			return "user/queryUserInfo";
		}
	}

	private UserInfo formToUserInfo(UserInfoForm userInfoForm) {
		UserInfo result = new UserInfo();
		if (StringUtils.isNotEmpty(userInfoForm.getId())) {
			result.setId(Long.parseLong(userInfoForm.getId()));
		}
		result.setAddr(userInfoForm.getAddr());
		result.setArea(userInfoForm.getArea());
		result.setCompayInfo(userInfoForm.getCompayInfo());
		result.setEmail(userInfoForm.getEmail());
		result.setLoginId(userInfoForm.getLoginId());
		result.setMobile(userInfoForm.getMobile());
		result.setType(Integer.parseInt(userInfoForm.getType()));
		result.setUserName(userInfoForm.getUserName());
		result.setStatus(userInfoForm.getStatus());
		return result;
	}

	private TraderInfo formToTraderInfo(UserInfoForm userInfoForm) {
		TraderInfo result = new TraderInfo();
		if (StringUtils.isNotEmpty(userInfoForm.getId())) {
			Result<TraderInfo> traderInfoResult = traderInfoService.getTraderInfoByUserId(Long
					.parseLong(userInfoForm.getId()));
			Long traderInfoId = traderInfoResult.getModule().getId();
			if (StringUtils.isNotEmpty(traderInfoId + "")) {
				result.setId(traderInfoId);
			}
		}
		if ((Constants.UserInfo.TYPE_DOWNSTREAM + "").equals(userInfoForm.getType())) {// 下游
			result.setTraderType(Constants.TraderInfo.TRADER_TYPE_DOWNSTREAM);
			result.setCallbackUrl(userInfoForm.getCallbackUrl());
		}
		if ((Constants.UserInfo.TYPE_UPSTREAM + "").equals(userInfoForm.getType())) {// 上游
			result.setTraderType(Constants.TraderInfo.TRADER_TYPE_UPSTREAM);
			result.setSupplyType(Integer.parseInt(userInfoForm.getSupplyType()));
			result.setIsAsyncSupply(userInfoForm.getIsAsyncSupply());
		}
		result.setStatus(userInfoForm.getTraderInoStatus());
		if (StringUtils.isNotEmpty(userInfoForm.getChargingLimit())) {
			result.setChargingLimit(Integer.parseInt(userInfoForm.getChargingLimit()));
		}
		return result;
	}

	private void setSelectValue(Model model) {
		Map<String, String> mapType = new HashMap<String, String>();
		mapType.put(Constants.UserInfo.TYPE_ADMIN + "", Constants.UserInfo.TYPE_ADMIN_DESC);
		mapType.put(Constants.UserInfo.TYPE_DOWNSTREAM + "",
				Constants.UserInfo.TYPE_DOWNSTREAM_DESC);
		mapType.put(Constants.UserInfo.TYPE_UPSTREAM + "", Constants.UserInfo.TYPE_UPSTREAM_DESC);
		mapType.put(Constants.UserInfo.TYPE_BUSINESS + "", Constants.UserInfo.TYPE_BUSINESS_DESC);
		mapType.put(Constants.UserInfo.TYPE_CUSTOM + "", Constants.UserInfo.TYPE_CUSTOM_DESC);
		model.addAttribute("typeList", mapType);

		Map<String, String> mapStatus = new LinkedHashMap<String, String>();
		mapStatus.put(Constants.UserInfo.STATUS_NORMAL + "", Constants.UserInfo.STATUS_NORMAL_DESC);
		mapStatus.put(Constants.UserInfo.STATUS_CANCEL + "", Constants.UserInfo.STATUS_CANCEL_DESC);
		model.addAttribute("statusList", mapStatus);
		// 商户信息
		Map<String, String> mapSupplyType = new LinkedHashMap<String, String>();
		mapSupplyType.put(Constants.TraderInfo.TYPE_CARD_FORWARD_CHARGE + "",
				Constants.TraderInfo.TYPE_CARD_FORWARD_CHARGE_DESC);
		mapSupplyType.put(Constants.TraderInfo.TYPE_DIRECT_CHARGE + "",
				Constants.TraderInfo.TYPE_DIRECT_CHARGE_DESC);
		mapSupplyType.put(Constants.TraderInfo.TYPE_MAN + "", Constants.TraderInfo.TYPE_MAN_DESC);
		mapSupplyType.put(Constants.TraderInfo.TYPE_CARD + "", Constants.TraderInfo.TYPE_CARD_DESC);
		mapSupplyType.put(Constants.TraderInfo.TYPE_CARD_CHARGE + "",
				Constants.TraderInfo.TYPE_CARD_CHARGE_DESC);
		model.addAttribute("supplyTypeList", mapSupplyType);

		Map<String, String> mapIsAsyncSupply = new LinkedHashMap<String, String>();
		mapIsAsyncSupply.put("false", Constants.TraderInfo.SUPPLY_WAY_SYNC_DESC);
		mapIsAsyncSupply.put("true", Constants.TraderInfo.SUPPLY_WAY_ASYNC_DESC);
		model.addAttribute("isAsyncSupplyList", mapIsAsyncSupply);

		Map<String, String> mapTraderInfoStatus = new LinkedHashMap<String, String>();
		mapTraderInfoStatus.put(Constants.TraderInfo.STATUS_NORMAL + "",
				Constants.TraderInfo.STATUS_NORMAL_DESC);
		mapTraderInfoStatus.put(Constants.TraderInfo.STATUS_CANCEL + "",
				Constants.TraderInfo.STATUS_CANCEL_DESC);
		model.addAttribute("traderInfoStatusList", mapTraderInfoStatus);
	}
}
