package com.longan.web.core.action.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.longan.biz.core.UserService;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.web.biz.LoginService;
import com.longan.web.biz.RegService;
import com.longan.web.biz.domain.LoginToken;
import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.common.controller.BaseController;
import com.longan.web.core.form.LoginForm;
import com.longan.web.core.form.UserRegisterForm;

@Controller
public class Register extends BaseController {
	@Resource
	private UserService userService;

	@Resource
	private Validator validator;

	@Resource
	private LoginService loginService;

	@Resource
	private RegService regService;

	@RequestMapping("user/register")
	void index() {
	}

	@RequestMapping("user/registerCommit")
	private String registerCommit(
			@ModelAttribute("userRegisterForm") UserRegisterForm userRegisterForm,
			BindingResult bindingResult,
			HttpServletRequest request,
			Model model,
			@CookieValue(value = com.longan.web.utils.Constants.Cookie.KEY_TT_UID, defaultValue = "") String ttUid,
			HttpServletResponse response) {

		validator.validate(userRegisterForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorList", bindingResult.getAllErrors());
			return "user/register";
		}
		LoginToken loginToken = loginService.getToken(ttUid, response);
		if (loginToken == null) {
			logger.error("注册失败 Token不存在 loginId : " + userRegisterForm.getLoginId());
			model.addAttribute("errorMsg", "注册失败密钥验证失败,请重新获取短信验证码");
			return "user/register";
		}

		Result<String> pwdDecryptResult = loginService.checkTokenAndDecryptPwd(userRegisterForm,
				loginToken);
		if (!pwdDecryptResult.isSuccess()) {
			logger.error("注册失败 Token,密钥验证失败 loginId : " + userRegisterForm.getLoginId());
			model.addAttribute("errorMsg", "注册失败密钥验证失败，请重新获取短信验证码");
			return "user/register";
		}

		Result<Boolean> checkAuthCodeResult = regService.checkAuthCode(
				userRegisterForm.getLoginId(), userRegisterForm.getAuthCode());
		if (!checkAuthCodeResult.isSuccess()) {
			logger.warn("注册短信验证码错误 loginId : " + userRegisterForm.getLoginId());
			model.addAttribute("errorMsg", "短信验证码错误");
			userRegisterForm.setToken(loginToken.getToken());
			userRegisterForm.setTs(loginToken.getTs() + "");
			model.addAttribute("flag", true);

			// 去掉短信发送限制
			regService.removeAuthCodeMutex(userRegisterForm.getLoginId());
			return "user/register";
		}

		String unDecryptPwd = userRegisterForm.getPwd();

		// 设置解密的密码
		userRegisterForm.setPwd(pwdDecryptResult.getModule());

		UserInfo userInfo = formToUserInfo(userRegisterForm);
		Result<Boolean> result = new Result<Boolean>();
		try {
			result = userService.createUserInfo(userInfo, null);
		} catch (Exception e) {
			logger.error("userInfoAdd error", e);
			model.addAttribute("errorMsg", "注册失败数据库异常");
			return "user/register";
		}

		if (!result.isSuccess()) {
			model.addAttribute("errorMsg", result.getResultMsg());
			return "user/register";
		}

		model.addAttribute("result", result);

		// 模拟登录
		LoginForm loginForm = new LoginForm();
		loginForm.setLoginId(userRegisterForm.getLoginId());
		loginForm.setTs(userRegisterForm.getTs());
		loginForm.setPwd(unDecryptPwd);
		Result<UserInfo> loginResult = loginService.login(loginForm, loginToken);
		if (loginResult != null && loginResult.getModule() != null) {
			LoginUserInfo loginUserInfo = new LoginUserInfo();
			loginUserInfo.setUserInfo(loginResult.getModule());
			loginUserInfo.setSessionId(loginService.createSessionId());
			loginService.setUserLogin(loginUserInfo, response, request);
		}

		return "user/registerResult";

	}

	private UserInfo formToUserInfo(UserRegisterForm userRegisterForm) {
		UserInfo userInfo = new UserInfo();
		userInfo.setLoginId(userRegisterForm.getLoginId());
		userInfo.setEmail(userRegisterForm.getEmail());
		userInfo.setUserName(userRegisterForm.getUserName());
		userInfo.setPwd(userRegisterForm.getPwd());
		userInfo.setCompayInfo(userRegisterForm.getCompayInfo());
		userInfo.setAddr(userRegisterForm.getAddr().trim());
		userInfo.setMobile(userRegisterForm.getLoginId());// 绑定手机号为登录名
		userInfo.setType(Constants.UserInfo.TYPE_CUSTOM);// 门店用户才能建立账户信息
		return userInfo;
	}
}
