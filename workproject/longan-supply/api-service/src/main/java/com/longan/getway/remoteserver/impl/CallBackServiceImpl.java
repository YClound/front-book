package com.longan.getway.remoteserver.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.dataobject.BizOrder;
import com.longan.biz.domain.Result;
import com.longan.biz.utils.Md5Encrypt;
import com.longan.client.remote.service.CallBackService;
import com.longan.getway.biz.common.BaseService;
import com.longan.getway.core.domain.SupplyResult;
import com.longan.getway.utils.MultiThreadedHttpConnection;

public class CallBackServiceImpl extends BaseService implements CallBackService {
	final Logger logger = LoggerFactory.getLogger(CallBackServiceImpl.class);

	private ExecutorService es = Executors.newFixedThreadPool(50);

	@Override
	public Result<Boolean> callBackAsync(final BizOrder bizOrder) {
		Result<Boolean> result = new Result<Boolean>();
		result.setModule(false);
		if (bizOrder == null || bizOrder.getUserId() == null || bizOrder.getBizId() == null
				|| bizOrder.getId() == null || bizOrder.getDownstreamSerialno() == null
				|| bizOrder.getStatus() == null) {
			result.setResultMsg("bizOrder is null ");
			return result;
		}
		final String callBackUrl = getDownStreamCallBackUrl(bizOrder.getUserId() + "");
		final String key = getDownStreamKey(bizOrder.getUserId() + "");
		if (StringUtils.isEmpty(callBackUrl) || StringUtils.isEmpty(key)) {
			result.setResultMsg("回调地址或代理商密钥是空");
			return result;
		}
		result.setModule(true);
		result.setSuccess(true);
		es.execute(new Runnable() {
			@Override
			public void run() {
				/**
				 * 修改回调接口
				 * 
				 */
//				String sign = Md5Encrypt.md5(bizOrder.getBizId() + bizOrder.getDownstreamSerialno()
//						+ bizOrder.getId() + +bizOrder.getStatus() + bizOrder.getUserId() + key);
//
//				String url = callBackUrl + "userId=" + bizOrder.getUserId() + "&bizId="
//						+ bizOrder.getBizId() + "&id=" + bizOrder.getId() + "&downstreamSerialno="
//						+ bizOrder.getDownstreamSerialno() + "&status=" + bizOrder.getStatus()
//						+ "&sign=" + sign;
				/**
				 * 新的回调
				 */
				String url=NewCallBackHelper.createNewCallBackUrl(callBackUrl, bizOrder, key);
				try {
					// 等候2秒再发送通知
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}

				SupplyResult<Boolean> sr = MultiThreadedHttpConnection.getInstance()
						.sendDataByGetForCallBack(url);
				if (!sr.isSuccess()) {
					logger.error("sendDataByGetForCallBack bizOrderId " + bizOrder.getId()
							+ " msg : " + sr.getResultMsg());
					try {
						Thread.sleep(120000);
					} catch (InterruptedException e) {
					}
					MultiThreadedHttpConnection.getInstance().sendDataByGetForCallBack(url);
				}

			}
		});

		return result;

	}
}
