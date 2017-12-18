package com.longan.biz.core.impl;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.longan.biz.core.AcctService;
import com.longan.biz.core.BaseService;
import com.longan.biz.core.TraderInfoService;
import com.longan.biz.core.UserService;
import com.longan.biz.dao.UserInfoDAO;
import com.longan.biz.dataobject.AcctInfo;
import com.longan.biz.dataobject.TraderInfo;
import com.longan.biz.dataobject.UserInfo;
import com.longan.biz.dataobject.UserInfoExample;
import com.longan.biz.dataobject.UserInfoQuery;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.biz.utils.Md5Encrypt;

public class UserServiceImpl extends BaseService implements UserService {

	@Resource
	private UserInfoDAO userInfoDAO;

	@Resource
	private AcctService acctService;

	@Resource
	private TraderInfoService traderInfoService;

	private final static String PWD_KEY = "longan_login";
	private final static String DEFAUT_PWD = "longan123456";

	@Override
	public Result<Boolean> checkUserInfo(UserInfo userInfo) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		result.setSuccess(true);
		boolean flag = userInfo.getStatus() == Constants.UserInfo.STATUS_NORMAL;
		result.setModule(flag);
		if (!flag) {
			result.setResultMsg("用户状态异常");
		}
		return result;
	}

	@Override
	public Result<UserInfo> getUserInfo(Long userId) {
		Result<UserInfo> result = new Result<UserInfo>();
		try {
			UserInfo userInfo = userInfoDAO.selectByPrimaryKey(userId);
			if (userInfo != null) {
				result.setSuccess(true);
				result.setModule(userInfo);
			} else {
				result.setResultMsg("不存在该用户 ");
			}
		} catch (SQLException e) {
			result.setResultMsg("用户查询异常  msg : " + e.getMessage());
			logger.error("getUserInfo error  userId = " + userId, e);
		}
		return result;
	}

	@Override
	public Result<List<UserInfo>> queryUserInfoList(UserInfoQuery userInfoQuery) {
		Result<List<UserInfo>> result = new Result<List<UserInfo>>();
		try {
			List<UserInfo> queryResult = userInfoDAO.queryByPage(userInfoQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("用户列表查询失败    msg: " + e.getMessage());
			logger.error("queryUserInfoList error ", e);
		}

		return result;
	}

	@Override
	public Result<List<UserInfo>> queryDownStreamInfoList(UserInfoQuery userInfoQuery) {
		Result<List<UserInfo>> result = new Result<List<UserInfo>>();
		try {
			List<UserInfo> queryResult = userInfoDAO.queryDownStreamByPage(userInfoQuery);
			result.setSuccess(true);
			result.setModule(queryResult);
		} catch (SQLException e) {
			result.setResultMsg("下游用户列表查询失败    msg: " + e.getMessage());
			logger.error("queryDownStreamInfoList error ", e);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Result<Boolean> createUserInfo(UserInfo userInfo, TraderInfo traderInfo)
			throws Exception {
		Result<Boolean> result = new Result<Boolean>();
		try {
			UserInfoExample example = new UserInfoExample();
			userInfo.setLoginId(userInfo.getLoginId().toLowerCase());
			example.createCriteria().andLoginIdEqualTo(userInfo.getLoginId());
			List<UserInfo> list = (List<UserInfo>) userInfoDAO.selectByExample(example);
			if (list.size() > 0) {
				result.setResultMsg("登录名已存在");
				return result;
			}

			userInfo.setStatus(Constants.UserInfo.STATUS_NORMAL);
			if (StringUtils.isNotEmpty(userInfo.getPwd())) {
				// 前台注册
				userInfo.setPwd(Md5Encrypt.md5(userInfo.getPwd()));
			} else {
				// 管理端注册
				userInfo.setPwd(getDefaultPwd());
			}
			Long userId = userInfoDAO.insert(userInfo);
			result.setSuccess(true);
			result.setModule(true);
			userInfo.setId(userId);
			if (userInfo.isDownStreamUser() || userInfo.isCustomUser() || userInfo.isBusinessUser()) {
				// 下游代理商建立账户
				AcctInfo acctInfo = new AcctInfo();
				acctInfo.setLoginId(userInfo.getLoginId());
				if (userInfo.isDownStreamUser()) {
					acctInfo.setSalesPrice(Constants.AcctInfo.SALES_PRICE_1); // 默认价格1
				} else if (userInfo.isCustomUser()) {
					acctInfo.setSalesPrice(Constants.AcctInfo.SALES_PRICE_1); // 默认价格1
				} else if (userInfo.isBusinessUser()) {
					acctInfo.setSalesPrice(Constants.AcctInfo.SALES_PRICE_1); // 默认价格1
				}
				acctInfo.setUserId(userId);
				acctInfo.setStatus(Constants.AcctInfo.STATUS_NORMAL);
				Result<Long> acctResult = acctService.addAcct(acctInfo);
				if (acctResult.isSuccess()) {
					userInfo.setAcctId(acctResult.getModule());
					userInfoDAO.updateByPrimaryKeySelective(userInfo);
				} else {
					result.setResultMsg(acctResult.getResultMsg());
					return result;
				}
				// 下游代理商建立商户
				if (userInfo.isDownStreamUser()) {
					traderInfo.setDownstreamKey(createPrivateKey());
					traderInfo.setUserId(userId);
					traderInfo.setStatus(Constants.TraderInfo.STATUS_NORMAL);
					traderInfoService.createTraderInfo(traderInfo);
				}
			}
			if (userInfo.isUpStreamUser()) {
				// 上游代理商建立商户
				traderInfo.setUserId(userId);
				traderInfo.setStatus(Constants.TraderInfo.STATUS_NORMAL);
				traderInfoService.createTraderInfo(traderInfo);
			}
		} catch (Exception e) {
			logger.error("createUserInfo error ", e);
			throw new RuntimeException(e.getMessage()); // 抛出异常，以回滚
		}

		return result;
	}

	@Override
	public Result<Boolean> updateUserInfo(UserInfo userInfo) {
		Result<Boolean> result = new Result<Boolean>();
		try {
			int row = userInfoDAO.updateByPrimaryKeySelective(userInfo);
			result.setSuccess(row > 0);
			result.setModule(row > 0);
		} catch (SQLException e) {
			result.setResultMsg("更新用户失败    msg: " + e.getMessage());
			logger.error("updateUserInfo error ", e);
		}

		return result;
	}

	@SuppressWarnings("unused")
	private String setPwd(String source) {
		String result = Md5Encrypt.md5(Md5Encrypt.md5(source + "longan_login"));
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result<UserInfo> login(String userName, String pwd) {
		Result<UserInfo> result = new Result<UserInfo>();
		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(pwd)) {
			result.setResultMsg("入参错误");
			return result;
		}

		UserInfoExample userInfoExample = new UserInfoExample();
		userInfoExample.createCriteria().andLoginIdEqualTo(userName);
		try {
			List<UserInfo> userList = (List<UserInfo>) userInfoDAO.selectByExample(userInfoExample);
			result.setSuccess(true);
			if (userList != null && userList.size() > 0) {
				boolean flag = userList.get(0).getPwd().equals(Md5Encrypt.md5(pwd));
				if (flag) {
					if (userList.get(0).getStatus() == Constants.UserInfo.STATUS_NORMAL) {
						result.setModule(userList.get(0));
					} else {
						result.setResultMsg("用户状态异常");
					}
				} else {
					result.setResultCode(Constants.UserInfo.CODE_PWD_ERROR);
					result.setResultMsg("密码错误");
				}
			} else {
				result.setResultMsg("无此用户");
			}
		} catch (SQLException e) {
			result.setResultMsg("用户登录异常  msg : " + e.getMessage());
			logger.error("login error  userName = " + userName, e);
		}

		return result;
	}

	/*
	 * 查询下游用户，即下游代理商
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Result<List<UserInfo>> queryUserInfoDownStream() {
		Result<List<UserInfo>> result = new Result<List<UserInfo>>();
		UserInfoExample example = new UserInfoExample();
		example.createCriteria().andStatusEqualTo(Constants.UserInfo.STATUS_NORMAL)
				.andTypeEqualTo(Constants.UserInfo.TYPE_DOWNSTREAM);
		try {
			List<UserInfo> list = (List<UserInfo>) userInfoDAO.selectByExample(example);
			result.setSuccess(true);
			result.setModule(list);
		} catch (SQLException e) {
			result.setResultMsg("查询下游代理商异常  msg : " + e.getMessage());
			logger.error("queryUserInfoDownStream error ", e);
		}
		return result;
	}

	@Override
	public Result<UserInfo> getDownStreamById(Long userId) {
		Result<UserInfo> result = new Result<UserInfo>();
		try {
			UserInfo userInfo = userInfoDAO.selectDownStreamByPrimaryKey(userId);
			if (userInfo != null) {
				result.setSuccess(true);
				result.setModule(userInfo);
			} else {
				result.setResultMsg("不存在该用户 ");
			}
		} catch (SQLException e) {
			result.setResultMsg("用户查询异常  msg : " + e.getMessage());
			logger.error("getUserInfo error  userId = " + userId, e);
		}
		return result;
	}

	/*
	 * 查询下游用户，即下游代理商
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Result<List<UserInfo>> queryUserInfoAdmin() {
		Result<List<UserInfo>> result = new Result<List<UserInfo>>();
		UserInfoExample example = new UserInfoExample();
		example.createCriteria().andStatusEqualTo(Constants.UserInfo.STATUS_NORMAL)
				.andTypeEqualTo(Constants.UserInfo.TYPE_ADMIN);
		try {
			List<UserInfo> list = (List<UserInfo>) userInfoDAO.selectByExample(example);
			result.setSuccess(true);
			result.setModule(list);
		} catch (SQLException e) {
			result.setResultMsg("查询内部管理员异常  msg : " + e.getMessage());
			logger.error("queryUserInfoAdmin error ", e);
		}
		return result;
	}

	/*
	 * 查询上游用户，即上游供应商
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Result<List<UserInfo>> queryUserInfoUpStream() {
		Result<List<UserInfo>> result = new Result<List<UserInfo>>();
		UserInfoExample example = new UserInfoExample();
		example.createCriteria().andStatusEqualTo(Constants.UserInfo.STATUS_NORMAL)
				.andTypeEqualTo(Constants.UserInfo.TYPE_UPSTREAM);
		try {
			List<UserInfo> list = (List<UserInfo>) userInfoDAO.selectByExample(example);
			result.setSuccess(true);
			result.setModule(list);
		} catch (SQLException e) {
			result.setResultMsg("查询上游供货代理商异常  msg : " + e.getMessage());
			logger.error("queryUserInfoUpStream error ", e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result<UserInfo> getUserInfo(String loginId) {
		Result<UserInfo> result = new Result<UserInfo>();
		try {
			UserInfoExample example = new UserInfoExample();
			example.createCriteria().andLoginIdEqualTo(loginId);
			List<UserInfo> userInfoList = (List<UserInfo>) userInfoDAO.selectByExample(example);

			if (userInfoList != null && userInfoList.size() > 0) {
				result.setSuccess(true);
				result.setModule(userInfoList.get(0));
			} else {
				result.setResultMsg("不存在该用户 ");
			}
		} catch (SQLException e) {
			result.setResultMsg("用户查询异常 查询异常");
			logger.error("getUserInfo error  loginId = " + loginId, e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result<UserInfo> getUserInfoById(Long id) {
		Result<UserInfo> result = new Result<UserInfo>();
		try {
			UserInfoExample example = new UserInfoExample();
			example.createCriteria().andIdEqualTo(id);
			List<UserInfo> userInfoList = (List<UserInfo>) userInfoDAO.selectByExample(example);

			if (userInfoList != null && userInfoList.size() > 0) {
				result.setSuccess(true);
				result.setModule(userInfoList.get(0));
			} else {
				result.setResultMsg("不存在该用户 ");
			}
		} catch (SQLException e) {
			result.setResultMsg("用户查询异常  msg : " + e.getMessage());
			logger.error("getUserInfo error  id = " + id, e);
		}
		return result;
	}

	@Override
	public Result<Boolean> modifyPwd(Long userId, String oldPwd, String newPwd) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);

		if (StringUtils.isEmpty(newPwd)) {
			result.setResultMsg("新密码不能为空");
			return result;
		}
		Result<UserInfo> userInfoResult = this.getUserInfo(userId);
		if (!userInfoResult.isSuccess()) {
			result.setResultMsg(userInfoResult.getResultMsg());
			return result;
		}

		UserInfo userInfo = userInfoResult.getModule();
		if (StringUtils.isEmpty(oldPwd) || !userInfo.getPwd().equals(Md5Encrypt.md5(oldPwd))) {
			result.setResultMsg("旧密码验证错误");
			return result;
		}

		userInfo.setPwd(Md5Encrypt.md5(newPwd));
		if (userInfo.getPwd().equals(userInfo.getPayPwd())) {
			result.setResultMsg("登录密码不能和支付密码相同");
			return result;
		}
		Result<Boolean> updateResult = updateUserInfo(userInfo);

		if (!updateResult.isSuccess()) {
			result.setResultMsg(updateResult.getResultMsg());
			return result;
		}
		result.setSuccess(true);
		result.setModule(true);
		return result;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Result<Boolean> updateUser(UserInfo userInfo, TraderInfo traderInfo) {
		Result<Boolean> result = new Result<Boolean>();
		try {
			int row = userInfoDAO.updateByPrimaryKeySelective(userInfo);
			result.setSuccess(row > 0);
			result.setModule(row > 0);
			if (userInfo.isDownStreamUser() || userInfo.isUpStreamUser()) {
				traderInfoService.updateTraderInfo(traderInfo);
			}
		} catch (SQLException e) {
			result.setResultMsg("更新用户失败    msg: " + e.getMessage());
			logger.error("updateUserInfo error ", e);
		}

		return result;
	}

	private String getPwd(String pwd) {
		String result = Md5Encrypt.md5(pwd + PWD_KEY);
		result = Md5Encrypt.md5(result);
		return result;
	}

	private String getDefaultPwd() {
		return getPwd(DEFAUT_PWD);
	}

	public static void main(String[] args) {
		String a = "123456";
		System.out.println(Md5Encrypt.md5(Md5Encrypt.md5(a + PWD_KEY)));
		;
		System.out.println(Md5Encrypt.md5(Md5Encrypt.md5(DEFAUT_PWD + PWD_KEY)));
		;
	}

	protected String createPrivateKey() {// 创建密钥
		String show = null;
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(512);
			// 生成公钥和私钥对
			KeyPair key = keyGen.generateKeyPair();
			// 实例化Signature，用于产生数字签名，指定用RSA和SHA算法
			// 得到私钥
			PrivateKey privateKey = key.getPrivate();
			RSAPrivateCrtKey RSAPrivateKey = (RSAPrivateCrtKey) privateKey;
			BigInteger enter = RSAPrivateKey.getPrimeP();
			show = enter.toString(16);
		} catch (NoSuchAlgorithmException e) {
			logger.error("createPrivateKey error", e);
		}
		return show;
	}

	@Override
	public Result<Boolean> modifyPayPwd(Long userId, String oldPayPwd, String newPayPwd) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);

		if (StringUtils.isEmpty(newPayPwd)) {
			result.setResultMsg("新支付密码不能为空");
			return result;
		}
		Result<UserInfo> userInfoResult = this.getUserInfo(userId);
		if (!userInfoResult.isSuccess()) {
			result.setResultMsg(userInfoResult.getResultMsg());
			return result;
		}

		UserInfo userInfo = userInfoResult.getModule();
		if (StringUtils.isEmpty(oldPayPwd)
				|| !userInfo.getPayPwd().equals(Md5Encrypt.md5(oldPayPwd))) {
			result.setResultMsg("旧支付密码验证错误");
			return result;
		}

		userInfo.setPayPwd(Md5Encrypt.md5(newPayPwd));
		if (userInfo.getPayPwd().equals(userInfo.getPwd())) {
			result.setResultMsg("支付密码不能和登录密码相同");
			return result;
		}
		Result<Boolean> updateResult = updateUserInfo(userInfo);

		if (!updateResult.isSuccess()) {
			result.setResultMsg(updateResult.getResultMsg());
			return result;
		}
		result.setSuccess(true);
		result.setModule(true);
		return result;
	}

	@Override
	public Result<Boolean> setPayPwd(Long userId, String payPwd) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);

		if (StringUtils.isEmpty(payPwd)) {
			result.setResultMsg("支付密码不能为空");
			return result;
		}
		Result<UserInfo> userInfoResult = this.getUserInfo(userId);
		if (!userInfoResult.isSuccess()) {
			result.setResultMsg(userInfoResult.getResultMsg());
			return result;
		}

		UserInfo userInfo = userInfoResult.getModule();

		userInfo.setPayPwd(Md5Encrypt.md5(payPwd));
		if (userInfo.getPayPwd().equals(userInfo.getPwd())) {
			result.setResultMsg("支付密码不能和登录密码相同");
			return result;
		}
		Result<Boolean> updateResult = updateUserInfo(userInfo);

		if (!updateResult.isSuccess()) {
			result.setResultMsg(updateResult.getResultMsg());
			return result;
		}
		result.setSuccess(true);
		result.setModule(true);
		return result;
	}

	@Override
	public Result<Boolean> checkPayPwd(Long userId, String payPwd) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);

		if (userId == null || StringUtils.isEmpty(payPwd)) {
			result.setResultMsg("入参错误");
			return result;
		}

		try {
			UserInfo userInfo = (UserInfo) userInfoDAO.selectByPrimaryKey(userId);

			if (userInfo == null) {
				result.setResultMsg("无此用户");
				return result;
			}

			if (userInfo.getStatus() != Constants.UserInfo.STATUS_NORMAL) {
				result.setResultMsg("用户状态异常");
				return result;
			}

			if (StringUtils.isEmpty(userInfo.getPayPwd())) {
				result.setResultMsg("用户支付密码为空");
				return result;
			}

			result.setSuccess(true);
			result.setModule(userInfo.getPayPwd().equals(Md5Encrypt.md5(payPwd)));
		} catch (SQLException e) {
			result.setResultMsg("用户支付校验异常,数据库异常");
			logger.error("checkPayPwd error  userId = " + userId, e);
		}

		return result;
	}
}
