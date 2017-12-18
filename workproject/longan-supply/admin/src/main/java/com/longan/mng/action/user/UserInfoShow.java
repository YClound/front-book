package com.longan.mng.action.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.longan.biz.core.AcctService;
import com.longan.biz.core.AuthService;
import com.longan.biz.core.UserService;
import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.mng.action.common.BaseController;

@Controller
@RequestMapping("user/userInfoShow")
public class UserInfoShow extends BaseController {
	@Resource
	private UserService userService;

	@Resource
	private AcctService acctService;

	@Resource
	private AuthService authService;

	@RequestMapping(method = RequestMethod.GET)
	public void onRequest(@RequestParam("id") Long id, HttpSession session, Model model) {
		UserInfo user = getUserInfo(session);
		if (user.isDownStreamUser()) {
			// 下游只能看自己的信息
			id = user.getId();
		}
		
		model.addAttribute("user", user);
		
		Result<UserInfo> result = userService.getUserInfo(id);
		if (!result.isSuccess()) {
			super.alertError(model, result.getResultMsg());
			return;
		}
		UserInfo userInfo = result.getModule();
		if (userInfo != null) {
			model.addAttribute("userInfo", userInfo);
			if (userInfo.canUserTrade()) {
				// 能交易的用户，才有账户信息。
				Result<AcctInfo> acctResult = acctService.getAcctInfo(userInfo.getAcctId());
				if (acctResult.isSuccess()) {
					if (acctResult.getModule() != null) {
						model.addAttribute("acctInfo", acctResult.getModule());
					}
				} else {
					super.alertError(model, acctResult.getResultMsg());
				}

				Result<Set<Integer>> bizResult = authService.getAuthBizIdByUserId(id);
				if (bizResult.isSuccess()) {
					Set<Integer> bizSet = bizResult.getModule();
					model.addAttribute("bizList", getBizList(bizSet));
				}
			}
		}

	}

	private Map<Integer, String> getBizList(Set<Integer> set) {
		Map<Integer, String> result = new HashMap<Integer, String>();
		if (set == null) {
			return result;
		}
		for (Integer bizId : set) {
			result.put(bizId, Constants.BIZ_MAP.get(bizId));
		}
		return result;

	}
}
