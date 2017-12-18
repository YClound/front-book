package com.longan.web.core.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.web.biz.LoginService;
import com.longan.web.biz.domain.LoginToken;
import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.common.controller.BaseController;
import com.longan.web.core.form.LoginForm;
import com.longan.web.utils.Constants;

@Controller
public class Login extends BaseController {
	@Resource
	private LoginService loginService;

	@RequestMapping(value = "login")
	public String loginReqeust(
			@CookieValue(value = Constants.Cookie.KEY_TT_UID, defaultValue = "") String ttUid,
			@ModelAttribute("loginForm") LoginForm loginForm, BindingResult result,
			HttpServletResponse response, HttpServletRequest request, Model model) {
		if (request.getMethod().endsWith("GET")) {
			return "redirect:index.htm";
		}
		LoginToken loginToken = loginService.getToken(ttUid, response);
		if (loginToken == null) {
			model.addAttribute("message", "密钥校验失败");
			return "index";
		}

		Result<UserInfo> loginResult = loginService.login(loginForm, loginToken);
		if (!loginResult.isSuccess() || loginResult.getModule() == null) {
			model.addAttribute("message", "登录失败! " + loginResult.getResultMsg());
			if (Constants.Login.CODE_NEED_CHECKCODE.equals(loginResult.getResultCode())) {
				model.addAttribute("checkCodeFlag", true);
			}
			return "index";
		}

		LoginUserInfo loginUserInfo = new LoginUserInfo();
		loginUserInfo.setUserInfo(loginResult.getModule());
		loginUserInfo.setSessionId(loginService.createSessionId());
		loginService.setUserLogin(loginUserInfo, response, request);

		return "redirect:main.htm";
	}

	@RequestMapping("loginOut")
	public String loginOutReqeust(HttpServletResponse response, HttpServletRequest request) {
		LoginUserInfo loginUserInfo = getUserInfo(request);
		loginService.loginOut(loginUserInfo, response, request);
		return "redirect:index.htm";
	}

}
