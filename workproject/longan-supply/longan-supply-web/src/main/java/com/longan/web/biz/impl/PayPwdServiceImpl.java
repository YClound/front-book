package com.longan.web.biz.impl;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.cached.MemcachedService;
import com.longan.biz.core.UserService;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.AESUtils;
import com.longan.web.biz.BaseService;
import com.longan.web.biz.PayPwdService;
import com.longan.web.biz.domain.PayToken;
import com.longan.web.core.form.BasePayForm;
import com.longan.web.utils.Constants;

public class PayPwdServiceImpl extends BaseService implements PayPwdService {
	@Resource
	private MemcachedService memcachedService;

	@Resource
	private UserService userService;

	@Override
	public Result<Boolean> checkPayPwd(BasePayForm basePayForm, UserInfo userInfo, PayToken payToken) {
		Result<Boolean> result = new Result<Boolean>();
		if (basePayForm == null || userInfo == null || payToken == null) {
			result.setResultMsg("入参错误");
			return result;
		}
		// aes
		String str;
		try {
			str = AESUtils.decrypt(basePayForm.getPayPwd(), payToken.getToken());
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
		if (!ts.equals(String.valueOf(payToken.getOldTs()))) {
			result.setResultMsg("密钥校验失败...");
			return result;
		}

		Result<Boolean> checkResult = userService.checkPayPwd(userInfo.getId(), str = strs[0]);
		return checkResult;
	}

	@Override
	public Result<PayToken> getPayToken(Integer bizId, UserInfo userInfo) {
		Result<PayToken> result = new Result<PayToken>();
		if (userInfo == null || bizId == null) {
			result.setResultMsg("入参错误");
			return result;
		}

		String key = getPayTokenKey(bizId, userInfo.getId());

		PayToken payToken = getTokenFromCached(key);
		if (payToken == null) {
			payToken = createPayToken(bizId, userInfo);
		}
		payToken.resetTs();
		setTokenToCached(payToken);

		result.setSuccess(true);
		result.setModule(payToken);
		return result;
	}

	private PayToken getTokenFromCached(String key) {
		PayToken result = null;
		Object object = memcachedService.get(key);

		if (object != null && object instanceof PayToken) {
			result = (PayToken) object;
		}
		return result;
	}

	private PayToken createPayToken(Integer bizId, UserInfo userInfo) {
		PayToken payToken = new PayToken();
		payToken.setBizId(bizId);
		payToken.setUserId(userInfo.getId());

		return payToken;
	}

	private void setTokenToCached(PayToken payToken) {
		if (payToken == null) {
			logger.error("setTokenToCached error payToken is null");
			return;
		}
		memcachedService.set(getPayTokenKey(payToken.getBizId(), payToken.getUserId()),
				Constants.Memcached.EXP_PAY_TOKEN, payToken);
	}

	private String getPayTokenKey(Integer bizId, Long userId) {
		return Constants.Memcached.KEY_PAY_TOKEN + bizId + "_" + userId;

	}

}
