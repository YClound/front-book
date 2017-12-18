package com.longan.web.biz.impl;

import java.util.Random;

import javax.annotation.Resource;

import com.alibaba.druid.util.StringUtils;
import com.longan.biz.cached.MemcachedService;
import com.longan.biz.core.UserService;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.client.remote.service.ShortMsgSendService;
import com.longan.web.biz.BaseService;
import com.longan.web.biz.RegService;
import com.longan.web.biz.domain.LoginToken;
import com.longan.web.utils.Constants;

public class RegServiceImpl extends BaseService implements RegService {

	@Resource
	private ShortMsgSendService shortMsgSendService;

	@Resource
	private UserService userService;

	@Resource
	private MemcachedService memcachedService;

	@Override
	public Result<Boolean> isUserExist(String loginId) {
		Result<Boolean> result = new Result<Boolean>();
		Result<UserInfo> userInFoResult = userService.getUserInfo(loginId);
		if (!userInFoResult.isSuccess()) {
			result.setResultMsg(userInFoResult.getResultMsg());
			return result;
		}
		result.setSuccess(true);
		result.setModule(true);
		return result;
	}

	@Override
	public Result<Boolean> sendRegMsg(String loginId, LoginToken loginToken) {
		Result<Boolean> result = new Result<Boolean>();

		// 能否发送
		boolean flag = memcachedService.isMutex(getAuthCodeKey(loginId),
				Constants.Memcached.EXP_REG_AUTHCODE_MUTEX);
		if (flag) {
			result.setResultMsg("不能频繁发送短信");
			return result;
		}

		String code = createAuthCode();
		// 发送
		try {
			Result<Boolean> shortMsgSendResult = shortMsgSendService.shortMsgSend(loginId,
					"您的注册验证码是:" + code + ",10分钟后失效请尽快使用",
					com.longan.biz.utils.Constants.ShortMsg.TYPE_REG);
			if (!shortMsgSendResult.isSuccess()) {
				result.setResultMsg(shortMsgSendResult.getResultMsg());
				return result;
			}
			logger.warn("新注册用户 " + loginId + " 短信验证码 :" + code);
		} catch (Exception e) {
			logger.error("shortMsgSend error", e);
			result.setResultMsg("注册短信发送失败，请稍候再试");
			return result;
		}

		// 设置memcached
		memcachedService.set(getAuthCodeKey(loginId), Constants.Memcached.EXP_REG_AUTHCODE, code);

		result.setSuccess(true);
		result.setModule(true);
		return result;
	}

	@Override
	public Result<Boolean> checkAuthCode(String loginId, String code) {
		Result<Boolean> result = new Result<Boolean>();
		Object object = memcachedService.get(getAuthCodeKey(loginId));
		if (object == null || !((String) object).equals(code) || StringUtils.isEmpty(code)) {
			result.setResultMsg("验证码错误");
			return result;
		}

		result.setSuccess(true);
		result.setModule(true);
		return result;
	}

	private String createAuthCode() {
		StringBuffer sb = new StringBuffer("");
		Random r = new Random();
		for (int i = 0; i < 6; i++) {
			sb.append(r.nextInt(10));
		}
		return sb.toString();
	}

	private String getAuthCodeKey(String loginId) {
		return Constants.Memcached.KEY_REG_AUTHCODE + loginId;
	}

	@Override
	public void removeAuthCodeMutex(String loginId) {
		memcachedService.deleteMutex(getAuthCodeKey(loginId));
	}

}
