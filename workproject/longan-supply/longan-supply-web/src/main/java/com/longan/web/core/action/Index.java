package com.longan.web.core.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.longan.web.biz.LoginService;
import com.longan.web.common.controller.BaseController;
import com.longan.web.core.form.LoginForm;
import com.longan.web.utils.Constants;

@Controller
public class Index extends BaseController {
	@Resource
	private LoginService loginService;

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public ModelAndView setupForm(
			@CookieValue(value = Constants.Cookie.KEY_TT_UID, defaultValue = "") String ttUid,
			HttpServletRequest request, HttpServletResponse response) {
		if (isEnvTest() && isFromOtherDomain(request)) {
			// 防止其他人，用域名连接至我们的测试服务器，并收入到搜索引擎(百度)中。
			return new ModelAndView(new RedirectView("http://www.91tangtang.com/"));
		}

		// LoginToken loginToken = loginService.getToken(ttUid, response);
		// ttUid = loginToken.getTtUid();
		LoginForm loginForm = new LoginForm();
		// loginForm.setToken(loginToken.getToken());
		// loginForm.setTs(String.valueOf(loginToken.getTs()));
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.getModelMap().addAttribute("loginForm", loginForm);
		return modelAndView;
	}

	boolean isEnvTest() {
		return Constants.ENV.equals("test");
	}

	boolean isFromOtherDomain(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		url = url.delete(url.length() - request.getRequestURI().length(), url.length());
		url = url.delete(0, 7);
		String domain = url.toString().split(":")[0];
		return !isIp(domain);
	}

	boolean isIp(String addr) {
		if (addr == null || addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
			return false;
		}
		String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern pat = Pattern.compile(rexp);
		Matcher mat = pat.matcher(addr);
		boolean ipAddress = mat.find();
		return ipAddress;
	}

}
