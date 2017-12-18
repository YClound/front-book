package com.longan.web.biz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.web.biz.domain.LoginToken;
import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.core.form.BaseUserForm;
import com.longan.web.core.form.LoginForm;

public interface LoginService {

	Result<UserInfo> login(LoginForm loginForm, LoginToken loginToken);

	String createSessionId();

	Result<Long> checkCode(LoginForm loginForm);

	LoginToken getToken(String ttUid, HttpServletResponse response);

	LoginToken getTokenAndUpdate(String ttUid, HttpServletResponse response);

	void setUserLogin(LoginUserInfo loginUserInfo, HttpServletResponse response,
			HttpServletRequest request);

	void keepUserLogin(LoginUserInfo loginUserInfo, HttpServletRequest request);

	Result<LoginUserInfo> checkLogin(HttpServletResponse response, HttpServletRequest request);

	void loginOut(LoginUserInfo loginUserInfo, HttpServletResponse response,
			HttpServletRequest request);

	void setCheckCodeToMem(String loginId, String checkCode);

	Result<String> checkTokenAndDecryptPwd(BaseUserForm form, LoginToken loginToken);

}
