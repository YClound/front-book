package com.longan.mng.action;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.druid.util.StringUtils;
import com.longan.biz.core.UserService;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.mng.action.common.BaseController;
import com.longan.mng.form.LoginForm;
import com.longan.mng.utils.CheckCodeContext;
import com.longan.mng.utils.Constants;

@Controller
@RequestMapping("login")
public class Login extends BaseController {
	@Resource
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@ModelAttribute("loginForm") LoginForm loginForm, BindingResult result,
			Model model, HttpSession session, HttpServletRequest request) {
		if (CheckCodeContext.map.get(getRemoteIp(request)) == null) {
			alertErrorNoneBack(model, "验证码已失效!");
			return "index";
		}
		if (StringUtils.isEmpty(loginForm.getCheckCode())
				|| !loginForm.getCheckCode().equalsIgnoreCase(
						CheckCodeContext.map.get(getRemoteIp(request)))) {
			alertErrorNoneBack(model, "验证码不正确!");
			return "index";
		}

		Result<UserInfo> loginResult = userService.login(loginForm.getName(), loginForm.getPwd());
		if (loginResult.isSuccess() && loginResult.getModule() != null) {
			UserInfo updateUserInfo = new UserInfo();
			updateUserInfo.setId(loginResult.getModule().getId());
			updateUserInfo.setLastLoginIp(super.getRemoteIp(request));
			updateUserInfo.setLastLoginTime(new Date());
			userService.updateUserInfo(updateUserInfo);
			session.setAttribute(Constants.SESSION_USER, loginResult.getModule());
			return "redirect:main.do";
		}
		alertErrorNoneBack(model, loginResult.getResultMsg());
		return "index";

	}
}
