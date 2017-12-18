package com.longan.mng.action.biz;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.longan.biz.core.GameCarrierService;
import com.longan.biz.dataobject.GameCarrier;
import com.longan.biz.dataobject.GameCarrierQuery;
import com.longan.biz.dataobject.OperationLog;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.OperLogUtils;
import com.longan.mng.action.common.BaseController;
import com.longan.mng.form.GameCarrierForm;

@Controller
public class GameCarrierDeal extends BaseController {
	@Resource
	private GameCarrierService gameCarrierService;

	@Resource
	private Validator validator;

	@RequestMapping("biz/queryGameCarrier")
	public String queryGameCarrier(
			@ModelAttribute("gameCarrierQuery") GameCarrierQuery gameCarrierQuery,
			HttpSession session, Model model) {
		// 业务 权限判断
		UserInfo userInfo = super.getUserInfo(session);
		if (!checkBizAuth(gameCarrierQuery.getBizId(), userInfo)) {
			return "error/autherror";
		}
		Result<List<GameCarrier>> result = gameCarrierService
				.queryGameCarrierList(gameCarrierQuery);
		if (result.isSuccess()) {
			model.addAttribute("gameCarrierList", result.getModule());
		} else {
			super.setError(model, result.getResultMsg());
		}
		return "biz/queryGameCarrier";
	}

	@RequestMapping(value = "biz/gameCarrierAdd", method = RequestMethod.GET)
	public String onAddIndex(@RequestParam("bizId") Integer bizId, HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(bizId, userInfo)) {
			return "error/autherror";
		}
		model.addAttribute("bizId", bizId);
		return "biz/gameCarrierAdd";
	}

	@RequestMapping(value = "biz/gameCarrierAdd", method = RequestMethod.POST)
	public String onPostAdd(@ModelAttribute("gameCarrierForm") GameCarrierForm gameCarrierForm,
			BindingResult bindingResult, HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(Integer.parseInt(gameCarrierForm.getBizId()), userInfo)) {
			return "error/autherror";
		}
		model.addAttribute("bizId", gameCarrierForm.getBizId());
		validator.validate(gameCarrierForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return "biz/gameCarrierAdd";
		}
		GameCarrier gameCarrier = formToGameCarrier(gameCarrierForm);
		Result<Boolean> result = gameCarrierService.createGameCarrier(gameCarrier);
		if (result != null && result.isSuccess()) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (HashMap<String, String>) session
					.getAttribute("requestInfoMap");
			OperationLog operationLog = OperLogUtils.operationLogDeal(null, gameCarrier, userInfo,
					map.get("moduleName"), gameCarrier.getBizId(), map.get("loginIp"));
			operationLogService.createOperationLog(operationLog);
			super.alertSuccess(model, "queryGameCarrier.do?bizId=" + gameCarrierForm.getBizId());
		} else {
			super.alertError(model, result == null ? "操作失败" : result.getResultMsg());
		}
		return "biz/gameCarrierAdd";
	}

	@RequestMapping(value = "biz/gameCarrierEdit", method = RequestMethod.GET)
	public void onEditIndex(@RequestParam("id") Long id, Model model) {
		Result<GameCarrier> result = gameCarrierService.getGameCarrier(id);
		if (result.isSuccess()) {
			GameCarrier gameCarrier = result.getModule();
			if (gameCarrier != null) {
				model.addAttribute("gameCarrier", gameCarrier);
			} else {
				super.alertError(model, "没有该厂商");
			}
		} else {
			super.alertError(model, result.getResultMsg());
		}
	}

	@RequestMapping(value = "biz/gameCarrierEdit", method = RequestMethod.POST)
	public String onPostEdit(@ModelAttribute("gameCarrierForm") GameCarrierForm gameCarrierForm,
			BindingResult bindingResult, Model model, HttpSession session) {
		UserInfo userInfom = super.getUserInfo(session);
		if (gameCarrierForm.getId() == null) {
			super.alertError(model, "厂商编号不能为空");
			return "biz/gameCarrierEdit";
		}
		Result<GameCarrier> gameCarrierResult = gameCarrierService.getGameCarrier(Long
				.parseLong(gameCarrierForm.getId()));
		GameCarrier older = null;
		if (!gameCarrierResult.isSuccess()) {
			super.alertError(model, gameCarrierResult.getResultMsg());
			return "biz/gameCarrierEdit";
		} else {
			GameCarrier result = gameCarrierResult.getModule();
			if (result == null) {
				super.alertError(model, "厂商为空");
				return "biz/gameCarrierEdit";
			}
			older = result;
			model.addAttribute("gameCarrier", result);

			validator.validate(gameCarrierForm, bindingResult);
			if (bindingResult.hasErrors()) {
				model.addAttribute("errorList", bindingResult.getAllErrors());
				return "biz/gameCarrierEdit";
			}

			if (gameCarrierForm.getStatus() == null) {
				List<ObjectError> allErrors = new ArrayList<ObjectError>();
				allErrors.add(new FieldError("userInfoForm", "status", "厂商状态不能为空"));
				model.addAttribute("errorList", allErrors);
				return "biz/gameCarrierEdit";
			}
			GameCarrier gameCarrier = formToGameCarrier(gameCarrierForm);
			Result<Boolean> rs = gameCarrierService.updateGameCarrier(gameCarrier);
			if (rs.isSuccess()) {
				@SuppressWarnings("unchecked")
				Map<String, String> map = (HashMap<String, String>) session
						.getAttribute("requestInfoMap");
				OperationLog operationLog = OperLogUtils.operationLogDeal(older, gameCarrier,
						userInfom, map.get("moduleName"), gameCarrier.getBizId(),
						map.get("loginIp"));
				operationLogService.createOperationLog(operationLog);
				super.alertSuccess(model, "queryGameCarrier.do?bizId=" + older.getBizId());
				return null;
			} else {
				super.alertError(model, rs.getResultMsg());
				return "biz/gameCarrierEdit";
			}
		}
	}

	private GameCarrier formToGameCarrier(GameCarrierForm gameCarrierForm) {
		GameCarrier gameCarrier = new GameCarrier();
		if (StringUtils.isNotEmpty(gameCarrierForm.getId())) {
			gameCarrier.setId(Long.parseLong(gameCarrierForm.getId()));
		}
		if (StringUtils.isNotEmpty(gameCarrierForm.getBizId())) {
			gameCarrier.setBizId(Integer.parseInt(gameCarrierForm.getBizId()));
		}
		if (StringUtils.isNotEmpty(gameCarrierForm.getCarrierName())) {
			gameCarrier.setCarrierName(gameCarrierForm.getCarrierName());
		}
		gameCarrier.setCarrierDesc(gameCarrierForm.getCarrierDesc());
		if (StringUtils.isNotEmpty(gameCarrierForm.getStatus() + "")) {
			gameCarrier.setStatus(gameCarrierForm.getStatus());
		}
		return gameCarrier;
	}

	@ModelAttribute("statusList")
	public Map<Integer, String> statusList() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(Constants.GameCarrier.STATUS_NORMAL, Constants.GameCarrier.STATUS_NORMAL_DESC);
		map.put(Constants.GameCarrier.STATUS_CANCEL, Constants.GameCarrier.STATUS_CANCEL_DESC);
		return map;
	}
}
