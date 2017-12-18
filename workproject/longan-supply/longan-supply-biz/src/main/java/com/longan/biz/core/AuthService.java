package com.longan.biz.core;

import java.util.Set;

import com.longan.biz.domain.Result;

public interface AuthService {
	Result<Set<String>> getAuthCatalogByUserId(Long userId);

	Result<Set<String>> getAuthUrlByUserId(Long userId);

	Result<Set<Integer>> getAuthBizIdByUserId(Long userId);

	Result<Set<Integer>> getAuthIdByRoleId(Integer roleId);

	Result<Set<Integer>> getAuthIdByUserId(Long userId);
}
