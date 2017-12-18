package com.longan.getway.upstream.shortmsg.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.longan.biz.core.ShortMsgService;
import com.longan.biz.dataobject.ShortMsg;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Constants;
import com.longan.client.remote.domain.BalanceQueryInfo;
import com.longan.client.remote.service.ShortMsgSendService;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.upstream.shortmsg.vo.BaiWuShortMsgRequestVO;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class BaiWuShortMsgImpl extends BaseService implements ShortMsgSendService {
	@Resource
	private ShortMsgService shortMsgService;
	private static final ExecutorService es = Executors.newFixedThreadPool(3);

	@Override
	public Result<Boolean> shortMsgSend(final String mobile, final String content,
			final Integer type) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (StringUtils.isEmpty(mobile)) {
			result.setResultMsg("发送手机号为空");
			return result;
		}
		if (StringUtils.isEmpty(content)) {
			result.setResultMsg("发送内容为空");
			return result;
		}
		if (type == null) {
			result.setResultMsg("发送类型为空");
			return result;
		}
		result.setModule(true);
		result.setSuccess(true);
		es.execute(new Runnable() {
			@Override
			public void run() {
				ShortMsg shortMsg = new ShortMsg();
				shortMsg.setContent(content);
				shortMsg.setMobile(mobile);
				shortMsg.setStatus(Constants.ShortMsg.STATUS_INIT);
				shortMsg.setType(type);
				Result<ShortMsg> rs = shortMsgService.createShortMsg(shortMsg);

				BaiWuShortMsgRequestVO vo = new BaiWuShortMsgRequestVO();
				vo.setMobile(mobile);
				vo.setMsg_content(content);
				vo.setMsg_id(rs.getModule().getId() + "");
				vo.setExt("");
				SupplyResult<String> resultStr = MultiThreadedHttpConnection.getInstance()
						.sendPostByMap(vo.createSendAction(), vo.createSendParam());
				String str = resultStr.getModule();
				if (StringUtils.isNotEmpty(str)) {
					if ("0#1".equals(str)) {
						shortMsg.setStatus(Constants.ShortMsg.STATUS_INIT);// 提交成功
						shortMsgService.updateShortMsg(shortMsg);
					} else {
						shortMsg.setStatus(Constants.ShortMsg.STATUS_FAILED);// 提交失败,发送失败
						shortMsgService.updateShortMsg(shortMsg);
					}
				} else {
					shortMsg.setStatus(Constants.ShortMsg.STATUS_UNKNOWN);
					shortMsgService.updateShortMsg(shortMsg);
				}
			}
		});
		return result;
	}

	public static final Map<String, String> codeResultMap = new HashMap<String, String>();
	static {
		// 查询
		codeResultMap.put("101", "用户关闭");
		codeResultMap.put("104", "访问频率低于2秒");
		codeResultMap.put("106", "用户名不存在");
		codeResultMap.put("107", "密码错误");
		codeResultMap.put("108", "指定ip绑定错误");
	}

	@Override
	public Result<BalanceQueryInfo> balanceQuery() {
		Result<BalanceQueryInfo> result = new Result<BalanceQueryInfo>();
		BaiWuShortMsgRequestVO vo = new BaiWuShortMsgRequestVO();
		SupplyResult<String> supplyResult = MultiThreadedHttpConnection.getInstance()
				.sendPostByMap(vo.createQueryBalanceAction(), vo.createBalanceParam());
		BalanceQueryInfo balanceQueryInfo = new BalanceQueryInfo();
		result.setModule(balanceQueryInfo);
		if (supplyResult.isFailed()) {
			result.setResultMsg(supplyResult.getResultMsg());
			return result;
		}

		if (supplyResult.isUnConfirm()) {
			result.setResultMsg("查询超时");
			return result;
		}

		if (supplyResult.isUndifend()) {
			result.setResultMsg(Constants.ErrorCode.DESC_ERROR_SYSTEM);
			return result;
		}

		if (supplyResult.isSuccess()) {
			if (StringUtils.isEmpty(supplyResult.getModule())) {
				result.setResultMsg("上游返回信息为空");
				return result;
			}
			String resultStr = supplyResult.getModule();
			String[] strs = resultStr.split("#");
			if (!"ok".equals(strs[0])) {
				result.setSuccess(true);
				balanceQueryInfo.setMsg(resultStr + ":" + codeResultMap.get(resultStr));
				return result;
			}
			result.setSuccess(true);
			balanceQueryInfo.setMsg("查询余额成功");
			balanceQueryInfo.setBalance(strs[1]);
		}
		return result;
	}

}
