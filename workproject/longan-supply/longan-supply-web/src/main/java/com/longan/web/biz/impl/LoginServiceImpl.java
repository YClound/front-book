package com.longan.web.biz.impl;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.cached.MemcachedService;
import com.longan.biz.core.UserService;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.AESUtils;
import com.longan.web.biz.BaseService;
import com.longan.web.biz.LoginService;
import com.longan.web.biz.domain.LoginToken;
import com.longan.web.biz.domain.LoginUserInfo;
import com.longan.web.core.form.BaseUserForm;
import com.longan.web.core.form.LoginForm;
import com.longan.web.utils.Constants;

public class LoginServiceImpl extends BaseService implements LoginService {

	@Resource
	private MemcachedService memcachedService;

	@Resource
	private UserService userService;

	@Override
	public Result<UserInfo> login(LoginForm loginForm, LoginToken loginToken) {
		Result<UserInfo> result = new Result<UserInfo>();

		Result<String> tokenResult = checkTokenAndDecryptPwd(loginForm, loginToken);
		if (!tokenResult.isSuccess()) {
			result.setResultMsg(tokenResult.getResultMsg());
			return result;
		}

		String pwd = tokenResult.getModule();
		loginForm.setPwd(pwd);

		// check Code
		Result<Long> checkCodeResult = this.checkCode(loginForm);
		if (!checkCodeResult.isSuccess()) {
			result.setResultMsg(checkCodeResult.getResultMsg());
			result.setResultCode(checkCodeResult.getResultCode());
			return result;
		}

		// check phoneCode

		result = checkPwd(loginForm);

		// check userType
		if (result.isSuccess() && result.getModule() != null) {
			UserInfo userInfo = result.getModule();
			if (!(userInfo.canUserTrade())) {
				result.setSuccess(false);
				result.setResultMsg("该用户不允许登录");
			}
		}

		return result;
	}

	@Override
	public Result<Long> checkCode(LoginForm loginForm) {
		Result<Long> result = new Result<Long>();
		// isNeedCheck
		Object object = memcachedService.get(getPwdErrorCountKey(loginForm.getLoginId()));
		Long count = null;
		if (object != null) {
			count = memcachedService.getLongValue(getPwdErrorCountKey(loginForm.getLoginId()));
		}
		if (count == null || count < Constants.Login.PWD_ERROR_COUNT_MAX) {
			// 不需要做checkCode 并返回当前错误次数。
			result.setSuccess(true);
			result.setModule(count);
			return result;
		}

		// check
		Object checkCodeobject = memcachedService.get(getCheckCodeKey(loginForm.getLoginId()));
		String checkCode = null;
		if (checkCodeobject != null && checkCodeobject instanceof String) {
			checkCode = (String) checkCodeobject;
		}
		if (StringUtils.isEmpty(loginForm.getCheckCode())) {
			result.setResultMsg("请输入验证码");
			result.setResultCode(Constants.Login.CODE_NEED_CHECKCODE);
			return result;
		}
		if (!loginForm.getCheckCode().equalsIgnoreCase(checkCode)) {
			result.setResultCode(Constants.Login.CODE_NEED_CHECKCODE);
			result.setResultMsg("验证码错误");
			return result;
		}
		
		memcachedService.delete(getCheckCodeKey(loginForm.getLoginId()));
		result.setSuccess(true);
		return result;
	}

	private String getPwdErrorCountKey(String loginId) {
		return Constants.Memcached.KEY_LOGIN_PWD_ERROR_COUNT + loginId;
	}

	private String getCheckCodeKey(String loginId) {
		return Constants.Memcached.KEY_LOGIN_CHECKCODE + loginId;
	}

	@Override
	public Result<String> checkTokenAndDecryptPwd(BaseUserForm form, LoginToken loginToken) {
		Result<String> result = new Result<String>();
		if (loginToken == null || form == null) {
			logger.error("checkPwd error 入参错误");
			result.setResultMsg("入参错误");
			return result;
		}

		String str;
		try {
			str = AESUtils.decrypt(form.getPwd(), loginToken.getToken());
		} catch (Exception e) {
			if (e instanceof BadPaddingException) {
				logger.error("decrypt warn key error msg: " + e.getMessage());
				result.setResultMsg("密钥校验失败.");
			} else if (e instanceof IllegalBlockSizeException) {
				logger.error("decrypt warn msg: " + e.getMessage());
				result.setResultMsg("密钥校验失败.");
			} else {
				logger.error("decrypt error", e);
				result.setResultMsg("密钥校验失败..");
			}

			return result;
		}

		if (StringUtils.isEmpty(str)) {
			result.setResultMsg("密钥校验失败..");
			return result;
		}

		String[] strs = str.split("_");
		if (strs.length <= 1) {
			result.setResultMsg("密钥校验失败..");
			return result;
		}
		String ts = strs[strs.length - 1];
		if (!ts.equals(String.valueOf(loginToken.getTs()))) {
			result.setResultMsg("密钥校验失败...");
			return result;
		}

		String pwd = strs[0];
		result.setSuccess(true);
		result.setModule(pwd);
		return result;
	}

	private Result<UserInfo> checkPwd(LoginForm loginForm) {
		Result<UserInfo> result = new Result<UserInfo>();

		result = userService.login(loginForm.getLoginId(), loginForm.getPwd());

		if (result.isSuccess()
				&& com.longan.biz.utils.Constants.UserInfo.CODE_PWD_ERROR.equals(result
						.getResultCode())) {
			// 密码错误 计数+1
			long count = memcachedService.inc(getPwdErrorCountKey(loginForm.getLoginId()),
					Constants.Memcached.EXP_LOGIN_PWD_ERROR_COUNT, 1, 1l);
			if (count >= Constants.Login.PWD_ERROR_COUNT_MAX) {
				result.setResultCode(Constants.Login.CODE_NEED_CHECKCODE);

				// init checkCode
				memcachedService.set(getCheckCodeKey(loginForm.getLoginId()),
						Constants.Memcached.EXP_LOGIN_CHECKCODE, "init");
			}
		}

		return result;
	}

	@Override
	public LoginToken getToken(String ttUid, HttpServletResponse response) {
		LoginToken loginToken = null;
		if (StringUtils.isBlank(ttUid)) {
			return null;
		}
		loginToken = getTokenFromCached(ttUid);
		return loginToken;
	}

	@Override
	public LoginToken getTokenAndUpdate(String ttUid, HttpServletResponse response) {
		LoginToken loginToken = null;
		if (StringUtils.isBlank(ttUid)) {
			loginToken = new LoginToken();
			loginToken.createTtUid();
			setTokenToCached(loginToken);
			ttUid = loginToken.getTtUid();
			super.addCookie(Constants.Cookie.KEY_TT_UID, ttUid, Constants.Cookie.EXP_TT_UID,
					response);
			return loginToken;
		}

		loginToken = getTokenFromCached(ttUid);

		if (loginToken == null) {
			loginToken = new LoginToken();
			loginToken.setTtUid(ttUid);
			setTokenToCached(loginToken);
			super.addCookie(Constants.Cookie.KEY_TT_UID, ttUid, Constants.Cookie.EXP_TT_UID,
					response);
			return loginToken;
		}
		// 重置 ts
		loginToken.resetTs();
		// 更新至缓存
		setTokenToCached(loginToken);

		return loginToken;
	}

	private LoginToken getTokenFromCached(String ttUid) {
		LoginToken result = null;
		if (ttUid == null) {
			return result;
		}
		Object object = memcachedService.get(getTokenKey(ttUid));

		if (object != null && object instanceof LoginToken) {
			result = (LoginToken) object;
		}
		return result;
	}

	private void setTokenToCached(LoginToken loginToken) {
		if (loginToken == null) {
			logger.error("setTokenToCached error loginToken is null");
			return;
		}
		memcachedService.set(getTokenKey(loginToken.getTtUid()),
				Constants.Memcached.EXP_LOGIN_TOKEN, loginToken);
	}

	private String getTokenKey(String ttUid) {
		return Constants.Memcached.KEY_LOGIN_TOKEN + ttUid;
	}

	@Override
	public void keepUserLogin(LoginUserInfo loginUserInfo, HttpServletRequest request) {
		if (loginUserInfo == null) {
			logger.error("keepUserLogin error parms is null");
			return;
		}

		loginUserInfo.setRemoteIp(getRemoteIp(request));
		memcachedService.set(getSessionKey(loginUserInfo.getUserInfo().getId() + ""),
				Constants.Memcached.EXP_LOGIN_SESSION, loginUserInfo);
	}

	@Override
	public void setUserLogin(LoginUserInfo loginUserInfo, HttpServletResponse response,
			HttpServletRequest request) {
		if (loginUserInfo == null || StringUtils.isEmpty(loginUserInfo.getSessionId())
				|| loginUserInfo.getUserInfo() == null) {
			logger.error("setUserLogin error parms is null");
			return;
		}
		super.addCookieLikeSession(Constants.Cookie.KEY_USER_ID, loginUserInfo.getUserInfo()
				.getId() + "", response);
		super.addCookieLikeSession(Constants.Cookie.KEY_SESSION_ID, loginUserInfo.getSessionId(),
				response);

		keepUserLogin(loginUserInfo, request);

		// 更新用户最后登录时间和IP
		UserInfo updateUserInfo = new UserInfo();
		updateUserInfo.setId(loginUserInfo.getUserInfo().getId());
		updateUserInfo.setLastLoginIp(super.getRemoteIp(request));
		updateUserInfo.setLastLoginTime(new Date());
		userService.updateUserInfo(updateUserInfo);

		// 去掉验证码验证
		memcachedService.delete(getPwdErrorCountKey(loginUserInfo.getUserInfo().getLoginId()));

		logger.warn("用户登录成功 userId : " + loginUserInfo.getUserInfo().getId() + " sessionId : "
				+ loginUserInfo.getSessionId() + " ip : " + loginUserInfo.getRemoteIp());
	}

	private String getSessionKey(String userId) {
		if (StringUtils.isEmpty(userId)) {
			return null;
		}
		return Constants.Memcached.KEY_LOGIN_SESSION + userId.trim();
	}

	@Override
	public Result<LoginUserInfo> checkLogin(HttpServletResponse response, HttpServletRequest request) {
		Result<LoginUserInfo> result = new Result<LoginUserInfo>();
		String userIdStr = getCookieValue(Constants.Cookie.KEY_USER_ID, request);
		String sessionId = getCookieValue(Constants.Cookie.KEY_SESSION_ID, request);

		if (!StringUtils.isNumeric(userIdStr) || StringUtils.isEmpty(sessionId)) {
			return result;
		}

		Object object = memcachedService.get(getSessionKey(userIdStr));

		if (object == null) {
			return result;
		}

		if (!(object instanceof LoginUserInfo)) {
			result.setResultMsg("userInfo params error");
			return result;
		}

		LoginUserInfo loginUserInfo = (LoginUserInfo) object;

		if (loginUserInfo.getUserInfo() == null || !loginUserInfo.getSessionId().equals(sessionId)) {
			// logger.warn("用户重复登陆,userId" + userIdStr + " 已被IP为" +
			// loginUserInfo.getRemoteIp() + "置换");
			return result;
		}

		result.setSuccess(true);
		result.setModule(loginUserInfo);
		return result;
	}

	@Override
	public String createSessionId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	@Override
	public void loginOut(LoginUserInfo loginUserInfo, HttpServletResponse response,
			HttpServletRequest request) {
		super.removeCookie(Constants.Cookie.KEY_USER_ID, request, response);
		super.removeCookie(Constants.Cookie.KEY_SESSION_ID, request, response);

		if (loginUserInfo != null) {
			memcachedService.delete(getSessionKey(loginUserInfo.getUserInfo().getId() + ""));
		}
	}

	@Override
	public void setCheckCodeToMem(String loginId, String checkCode) {
		Object object = memcachedService.get(getCheckCodeKey(loginId));
		if (object != null) {
			memcachedService.set(getCheckCodeKey(loginId), Constants.Memcached.EXP_LOGIN_CHECKCODE,
					checkCode);
		}
	}

}
