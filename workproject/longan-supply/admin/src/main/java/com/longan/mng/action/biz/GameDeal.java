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

import com.longan.biz.cached.LocalCachedService;
import com.longan.biz.core.GameCarrierService;
import com.longan.biz.core.GameService;
import com.longan.biz.dataobject.Game;
import com.longan.biz.dataobject.GameCarrier;
import com.longan.biz.dataobject.GameCarrierQuery;
import com.longan.biz.dataobject.GameQuery;
import com.longan.biz.dataobject.OperationLog;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.OperLogUtils;
import com.longan.mng.action.common.BaseController;
import com.longan.mng.form.GameForm;
import com.longan.mng.utils.ChineseInitial;

@Controller
public class GameDeal extends BaseController {
	@Resource
	private GameService gameService;

	@Resource
	private GameCarrierService gameCarrierService;

	@Resource
	private LocalCachedService localCachedService;

	@Resource
	private Validator validator;

	@RequestMapping("biz/queryGame")
	public String queryGameCarrier(@ModelAttribute("gameQuery") GameQuery gameQuery,
			HttpSession session, Model model) {
		// 业务 权限判断
		UserInfo userInfo = super.getUserInfo(session);
		if (!checkBizAuth(gameQuery.getBizId(), userInfo)) {
			return "error/autherror";
		}
		Result<List<Game>> result = gameService.queryGameList(gameQuery);
		if (!result.isSuccess()) {
			super.setError(model, result.getResultMsg());
			return "biz/queryGame";
		}
		List<Game> gameList = result.getModule();
		if (gameList != null) {
			for (Game game : gameList) {
				GameCarrier gameCarrier = localCachedService.getGameCarrier(game.getCarrierId());
				if (gameCarrier != null) {
					game.setCarrierName(gameCarrier.getCarrierName());
				}
			}
		}
		model.addAttribute("gameList", gameList);
		return "biz/queryGame";
	}

	@RequestMapping(value = "biz/gameAdd", method = RequestMethod.GET)
	public String onAddIndex(@RequestParam("bizId") Integer bizId, HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(bizId, userInfo)) {
			return "error/autherror";
		}
		model.addAttribute("bizId", bizId);
		return "biz/gameAdd";
	}

	@RequestMapping(value = "biz/gameAdd", method = RequestMethod.POST)
	public String onPostAdd(@ModelAttribute("gameForm") GameForm gameForm,
			BindingResult bindingResult, HttpSession session, Model model) {
		UserInfo userInfo = super.getUserInfo(session);
		// 业务权限判断
		if (!checkBizAuth(Integer.parseInt(gameForm.getBizId()), userInfo)) {
			return "error/autherror";
		}
		model.addAttribute("bizId", gameForm.getBizId());
		validator.validate(gameForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return "biz/gameAdd";
		}
		Game game = formToGame(gameForm);
		Result<Boolean> result = gameService.createGame(game);
		if (result != null && result.isSuccess()) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (HashMap<String, String>) session
					.getAttribute("requestInfoMap");
			OperationLog operationLog = OperLogUtils.operationLogDeal(null, game, userInfo,
					map.get("moduleName"), game.getBizId(), map.get("loginIp"));
			operationLogService.createOperationLog(operationLog);
			super.alertSuccess(model, "queryGame.do?bizId=" + gameForm.getBizId());
		} else {
			super.alertError(model, result == null ? "操作失败" : result.getResultMsg());
		}
		return "biz/gameAdd";
	}

	@RequestMapping(value = "biz/gameEdit", method = RequestMethod.GET)
	public void onEditIndex(@RequestParam("id") Long id, Model model) {
		Result<Game> result = gameService.getGame(id);
		if (result.isSuccess()) {
			Game game = result.getModule();
			if (game != null) {
				model.addAttribute("game", game);
			} else {
				super.alertError(model, "没有该游戏");
			}
		} else {
			super.alertError(model, result.getResultMsg());
		}
	}

	@RequestMapping(value = "biz/gameEdit", method = RequestMethod.POST)
	public String onPostEdit(@ModelAttribute("gameForm") GameForm gameForm,
			BindingResult bindingResult, Model model, HttpSession session) {
		UserInfo userInfom = super.getUserInfo(session);
		if (gameForm.getId() == null) {
			super.alertError(model, "游戏编号不能为空");
			return "biz/gameEdit";
		}
		Result<Game> gameResult = gameService.getGame(Long.parseLong(gameForm.getId()));
		Game older = null;
		if (!gameResult.isSuccess()) {
			super.alertError(model, gameResult.getResultMsg());
			return "biz/gameEdit";
		} else {
			Game result = gameResult.getModule();
			if (result == null) {
				super.alertError(model, "游戏为空");
				return "biz/gameEdit";
			}
			older = result;
			model.addAttribute("game", result);

			validator.validate(gameForm, bindingResult);
			if (bindingResult.hasErrors()) {
				model.addAttribute("errorList", bindingResult.getAllErrors());
				return "biz/gameEdit";
			}

			if (gameForm.getStatus() == null) {
				List<ObjectError> allErrors = new ArrayList<ObjectError>();
				allErrors.add(new FieldError("userInfoForm", "status", "厂商状态不能为空"));
				model.addAttribute("errorList", allErrors);
				return "biz/gameEdit";
			}
			Game game = formToGame(gameForm);
			Result<Boolean> rs = gameService.updateGame(game);
			if (rs.isSuccess()) {
				@SuppressWarnings("unchecked")
				Map<String, String> map = (HashMap<String, String>) session
						.getAttribute("requestInfoMap");
				OperationLog operationLog = OperLogUtils.operationLogDeal(older, game, userInfom,
						map.get("moduleName"), game.getBizId(), map.get("loginIp"));
				operationLogService.createOperationLog(operationLog);
				super.alertSuccess(model, "queryGame.do?bizId=" + older.getBizId());
				return null;
			} else {
				super.alertError(model, rs.getResultMsg());
				return "biz/gameEdit";
			}
		}
	}

	private Game formToGame(GameForm gameForm) {
		Game game = new Game();
		if (StringUtils.isNotEmpty(gameForm.getId())) {
			game.setId(Long.parseLong(gameForm.getId()));
		}
		game.setGameName(gameForm.getGameName().trim());
		game.setKeyIndex(ChineseInitial.getInitialIndex(gameForm.getGameName()));
		if (StringUtils.isNotEmpty(gameForm.getBizId())) {
			game.setBizId(Integer.parseInt(gameForm.getBizId()));
		}
		if (gameForm.getCarrierId() != null) {
			game.setCarrierId(gameForm.getCarrierId());
		}
		game.setOfficialUrl(gameForm.getOfficialUrl());
		game.setLogo(gameForm.getLogo());
		game.setGameDesc(gameForm.getGameDesc());
		if (StringUtils.isNotEmpty(gameForm.getStatus() + "")) {
			game.setStatus(gameForm.getStatus());
		}
		return game;
	}

	@ModelAttribute("carrierNameList")
	public Map<Long, String> downStreamUser() {
		Result<List<GameCarrier>> gameCarrierResult = gameCarrierService
				.queryAllGameCarrier(new GameCarrierQuery());
		if (gameCarrierResult.isSuccess() && gameCarrierResult.getModule() != null) {
			Map<Long, String> map = new HashMap<Long, String>();
			for (GameCarrier gameCarrier : gameCarrierResult.getModule()) {
				map.put(gameCarrier.getId(), gameCarrier.getCarrierName());
			}
			return map;
		} else {
			return null;
		}
	}

	@ModelAttribute("statusList")
	public Map<Integer, String> statusList() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(Constants.Game.STATUS_NORMAL, Constants.Game.STATUS_NORMAL_DESC);
		map.put(Constants.Game.STATUS_CANCEL, Constants.Game.STATUS_CANCEL_DESC);
		return map;
	}
}
