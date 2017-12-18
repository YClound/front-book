package com.longan.web.biz;

import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.domain.Result;
import com.longan.web.biz.domain.PayToken;
import com.longan.web.core.form.BasePayForm;

public interface PayPwdService {
	Result<Boolean> checkPayPwd(BasePayForm basePayForm, UserInfo userInfo, PayToken payToken);

	Result<PayToken> getPayToken(Integer bizId, UserInfo userInfo);
}
