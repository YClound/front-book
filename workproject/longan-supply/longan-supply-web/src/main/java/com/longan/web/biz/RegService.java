package com.longan.web.biz;

import com.longan.biz.domain.Result;
import com.longan.web.biz.domain.LoginToken;

public interface RegService {

	Result<Boolean> isUserExist(String loginId);

	Result<Boolean> sendRegMsg(String loginId, LoginToken loginToken);

	Result<Boolean> checkAuthCode(String loginId, String code);
	
	void removeAuthCodeMutex(String loginId);

}
